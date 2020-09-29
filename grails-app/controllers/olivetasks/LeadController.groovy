package olivetasks

import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import grails.web.mapping.LinkGenerator

import static org.springframework.http.HttpStatus.*
import groovy.time.TimeCategory
import groovy.time.TimeDuration

@Secured('permitAll')
class LeadController {

    def mailedTaskService
    def mailService

    LinkGenerator grailsLinkGenerator
    String url

    List<Task> tasks
    List<Task> leadtasks
    List<Employee> employees
    def springSecurityService
    def user
    def dataSource

    static allowedMethods = [save: "POST", update: "PUT", delete: ["DELETE",'GET','POST','PUT']]

    @Transactional
    @Secured(['ROLE_LEAD'])
    def home(Integer max) {

        user = springSecurityService.getPrincipal().username
        def thisEmployee = Employee.findAllByUsername(user)

        def department = thisEmployee.department

        String dept = department.get(0)
        employees = null

        leadtasks = Task.all.findAll {
            it.employee.department.departmentName == "${dept}"
        }

        def version = thisEmployee.version


        params.max = Math.min(max ?: 10, 100)
        def count = (leadtasks==null) ? 0 : leadtasks.size()
        if(version[0]==0){
            flash.message="Security at Risk! Please change your password."
        }

        respond leadtasks, model:[taskCount: count]
    }

    @Secured(['ROLE_LEAD'])
    def alltasks(Integer max) {
        def user = springSecurityService.getPrincipal().username
        params.max = Math.min(max ?: 10, 100)
        tasks = Task.findAllByEmployee(Employee.findByUsername(user))
        def count = (tasks==null) ? 0 : tasks.size()
        respond tasks, model:[taskCount: count]
    }

    @Secured(['ROLE_LEAD'])
    def show(Long id) {
        if(id == null) {
            notFound()
            return
        }

        Task task = Task.get(id)
        if(task == null) {
            notFound()
            return
        }

        user = springSecurityService.getPrincipal().username
        def thisEmployee = Employee.findAllByUsername(user)

        def department = thisEmployee.department

        String dept = department.get(0)

        if(task.employee.department.departmentName != dept) {
            flash.message = "You are not authorized to view other departments' tasks!"
            redirect action: "home", controller: "lead"
            return
        }

        def emp = Employee.findByUsername(user)
        def nList = Notification.all.findAll {
            it?.receiver == emp && !it?.seen && it?.task?.id == id
        }

        def nCount = nList.size()
        makeSeen(nList,nCount)

        def mode = ""
        if(task.getStatus().equals("Not Started")) {
            mode = 'Start'
        }
        else if(task.getStatus().equals("In Progress")) {
            mode = 'End'
        }
        else {
            if(task.getStatus().equals("Completed")) {
                mode = 'Restart'
            }
        }
        respond Task.get(id), model: [mode: mode]
    }

    @Secured(['ROLE_LEAD'])
    def create() {
        user = springSecurityService.getPrincipal().username
        def thisEmployee = Employee.findAllByUsername(user)
        def department = thisEmployee.department
        String dept = department.get(0)

        List<Employee> employees1 = Employee.all.findAll {
            it.department.departmentName == "${dept}"
        }
        respond new Task(params), model: [employees1 : employees1]
    }

    @Secured(['ROLE_LEAD'])
    def save(Task task) {
        Employee worker
        Employee emp
        if (task == null) {
            notFound()
            return
        }

        try {
            worker = Employee.get(params.myemployee)
            task.employee = worker

            def user = springSecurityService.getPrincipal().username
            emp = Employee.findByUsername(user)

            if( user.equals(task.employee?.username)) {
                task.setStatus("In Progress")
                task.setStartDate(new Date())
            }
            else
                task.setStatus("Not Started")

            task.setCreationDate(new Date())
            task.assignee = emp.id

            Change change = new Change('Created', new Date())
            task.addToChanges(change)

            DailyUpdate dailyUpdate = new DailyUpdate(new Date(),"Created")
            task.addToUpdates(dailyUpdate)

            task.save()

            if(!worker.equals(emp)) {
                // Notify by Email
                def html = new MyMailService().newTaskHtml(task)
                def cc = mailedTaskService.getManagers(task.employee)
                String recipient = task.employee.email
                MyMailService.sendEmail(recipient,task.taskName,html, cc)

                Notification not = new Notification("New Task Assigned", new Date(), task.employee)
                addAlert(task.taskName, not)
            }


        } catch (ValidationException e) {
            respond task.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'task.label', default: 'Task'), task.id])
                redirect task
            }
            '*' { respond task, [status: CREATED] }
        }
    }

    @Secured(['ROLE_LEAD'])
    def edit(Long id) {
        respond Task.get(id)
    }

    @Transactional
    @Secured(['ROLE_LEAD'])
    def update(Task task) {
        if (task == null) {
            notFound()
            return
        }

        try {
            Change change = new Change('Updated', new Date())
            task.addToChanges(change)

            DailyUpdate dailyUpdate = new DailyUpdate(new Date(),"Updated")
            task.addToUpdates(dailyUpdate)

            task.save()
        } catch (ValidationException e) {
            respond task.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'task.label', default: 'Task'), task.id])
                redirect task
            }
            '*'{ respond task, [status: OK] }
        }
    }

    @Transactional
    @Secured(['ROLE_LEAD'])
    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }
        Task task = Task.get(id)

        if (task == null) {
            notFound()
            return
        }

        (Task.get(id)).delete()

        flash.message = "Task Deleted"
        redirect action: "home", controller: "lead"

    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'task.label', default: 'Task'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }

    // render view: "home", model: [data: data]
    @Secured(['ROLE_LEAD'])
    @Transactional
    def start(Long id) {
        if (id == null) {
            notFound()
            return
        }

        Task task1 = Task.get(id)

        if (task1 == null) {
            notFound()
            return
        }

        user = springSecurityService.getPrincipal().username
        def emp = Employee.findByUsername(user)
        if(task1.employee!=emp) {
            flash.message = "You are not authorized to start others task!"
            redirect action:"show", id:id, controller:"lead", model: [id: id]
            return
        }

        else if(task1.getStatus().equals("In Progress")) {
            flash.message = "Task is already started!"
            redirect action:"show", id:id, controller:"lead", model: [id: id]
            return
        }

        else if(task1.getStatus().equals("Completed")) {
            task1.setStatus("In Progress")
            //task1.setStartDate(new Date())
            //task1.save()
            Notification not = new Notification(user + " restarted a task", new Date(), Employee.get(task1.assignee))
            not.iconClass = "fas fa-play text-white"
            url = grailsLinkGenerator.link(controller: 'task', action: 'show', id: task1.id)
            not.url = url
            //not.save()
            task1.addToAlerts(not)

            Change change = new Change('Restarted', new Date())
            task1.addToChanges(change)

            DailyUpdate dailyUpdate = new DailyUpdate(new Date(),"Restarted")
            task1.addToUpdates(dailyUpdate)
            task1.save()

            flash.message = "Task Restarted"
            redirect action: "show", id: id, controller: "lead", model: [id: id]
        }
        else {

            task1.setStatus("In Progress")
            task1.setStartDate(new Date())
            //task1.save()
            Notification not = new Notification(user + " started a task", new Date(), Employee.get(task1.assignee))
            not.iconClass = "fas fa-play text-white"
            url = grailsLinkGenerator.link(controller: 'task', action: 'show', id: task1.id)
            not.url = url
            //not.save()
            task1.addToAlerts(not)

            Change change = new Change('Started', new Date())
            task1.addToChanges(change)

            DailyUpdate dailyUpdate = new DailyUpdate(new Date(),"Started")
            task1.addToUpdates(dailyUpdate)
            task1.save()

            flash.message = "Task Started"
            redirect action: "show", id: id, controller: "lead", model: [id: id]
        }
    }

    @Secured(['ROLE_ADMIN', 'ROLE_LEAD'])
    @Transactional
    def end(Long id) {
        if (id == null) {
            notFound()
            return
        }

        Task task1 = Task.get(id)

        if (task1 == null) {
            notFound()
            return
        }

        user = springSecurityService.getPrincipal().username
        def emp = Employee.findByUsername(user)
        if(task1.employee!=emp) {
            flash.message = "You are not authorized to end others task!"
            redirect action:"show", id:id, controller:"lead", model: [id: id]
            return
        }

        if(task1.getStatus().equals("Completed")) {
            flash.message = "Task is already accomplished!"
            redirect action:"show", id:id, controller:"lead", model: [id: id]
            return
        }

        task1.setStatus("Completed")
        Date startDate = task1.getStartDate()
        Date endDate = new Date()
        task1.setEndDate(endDate)

        TimeDuration timeSpent = TimeCategory.minus( endDate, startDate )
        task1.setTimeSpent(timeSpent.toString())

        //task1.save()
        Notification not = new Notification(user + " completed a task", new Date(), Employee.get(task1.assignee))
        not.iconClass = "fas fa-check-circle text-white"
        url = grailsLinkGenerator.link(controller: 'task', action: 'show', id: task1.id)
        not.url = url
        //not.save()
        task1.addToAlerts(not)

        Change change = new Change('Completed', new Date())
        task1.addToChanges(change)

        DailyUpdate dailyUpdate = new DailyUpdate(new Date(),"Completed")
        task1.addToUpdates(dailyUpdate)

        task1.save()

        def html = new MyMailService().taskDoneHtml(task1)
        def cc = mailedTaskService.getManagers(task1.employee)
        String recipient = Employee.get(task1.assignee)?.email
        MyMailService.sendEmail(recipient,task1.taskName,html, cc)

        flash.message = "Congrats! Task Completed"
        redirect action:"show", id:id, controller:"lead", model: [id: id]
    }

    @Secured(['ROLE_LEAD'])
    def createUpdate(Long id) {
        if(id == null) {
            notFound()
            return
        }
        Task task = Task.get(id)

        if (task == null) {
            notFound()
            return
        }

        model: [taskid: id]
    }

    @Transactional
    @Secured(['ROLE_LEAD'])
    def saveUpdate() {
        def id = params.taskid
        if(id == null) {
            notFound()
            return
        }
        Task task = Task.get(id)

        if (task == null) {
            notFound()
            return
        }

        user = springSecurityService.getPrincipal().username
        String update = user + " : " + params.update

        DailyUpdate dailyUpdate = new DailyUpdate(new Date(),update)
        task.addToUpdates(dailyUpdate)
        //task.save()

        Employee e
        if(user.equals(task.employee.username)) {
            e = Employee.get(task.assignee)
        }
        else {
            e = task.employee
        }

        Notification not = new Notification("Task Update from " + user, new Date(), e)
        not.iconClass = "fas fa-info-circle text-white"
        url = grailsLinkGenerator.link(controller: 'task', action: 'show', id: task.id)
        not.url = url
        //not.save()
        task.addToAlerts(not)
        task.save()

        flash.message = "Update Submitted Successfully!"
        redirect action:"show", id:id, controller:"lead", model: [id: id]
    }

    @Transactional
    def makeSeen(List<Notification> notificationList, int count) {
        if(count<=0) {
            return
        }
        int i
        for(i=0; i<count; i++) {
            Notification notification = notificationList.get(i)
            notification.setSeen(true)
            notification.save()
        }
    }

    @Transactional
    def addAlert(String taskName, Notification alert) {

        Task task= Task.findByTaskName(taskName)
        url = grailsLinkGenerator.link(controller: 'task', action: 'show', id: task.id)
        alert.url = url
        task.addToAlerts(alert)
        task.save()

    }

    def send(String recipient, String sub, String body, String managers) {

        mailService.sendMail {
            to recipient
            subject sub
            if(managers) {
                cc managers
            }
            html body

        }
    }
}
