package olivetasks

import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import grails.web.mapping.LinkGenerator
import groovy.time.TimeCategory
import groovy.time.TimeDuration

import static org.springframework.http.HttpStatus.*

@Secured('permitAll')
class UserController {

    LinkGenerator grailsLinkGenerator
    String url

    List<Task> tasks
    def springSecurityService
    def user

    def mailedTaskService
    def mailService

    static allowedMethods = [save: "POST", update: "PUT", delete: ["DELETE",'GET','POST','PUT']]

    @Secured(['ROLE_USER'])
    def home(Integer max) {
        user = springSecurityService.getPrincipal().username
        Employee employee = Employee.findByUsername(user)
        params.max = Math.min(max ?: 10, 100)
        tasks = Task.findAllByEmployee(employee)
        def count = (tasks==null) ? 0 : tasks.size()

        def version = employee.version
        if(version==0){
            flash.message="Security at Risk! Please change your password."
        }

        respond tasks, model:[taskCount: count]
    }

    @Secured(['ROLE_USER'])
    def show(Long id) {
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
        def emp = Employee.findByUsername(user)
        if(task.employee!=emp) {
            flash.message = "You are not authorized to view others task!"
            redirect action: "home", controller: "user"
            return
        }

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

    @Secured(['ROLE_USER'])
    def create() {
        respond new Task(params)
    }

    @Secured(['ROLE_USER'])
    def save(Task task) {
        if (task == null) {
            notFound()
            return
        }

        user = springSecurityService.getPrincipal().username
        def emp = Employee.findByUsername(user)
        task.employee = emp
        task.setCreationDate(new Date())
        task.setStatus("In Progress")
        task.setStartDate(new Date())
        task.assignee = emp.id

        try {
            Change change = new Change('Created', new Date())
            task.addToChanges(change)

            DailyUpdate dailyUpdate = new DailyUpdate(new Date(),"Created")
            task.addToUpdates(dailyUpdate)

            task.save()
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

    @Secured(['ROLE_USER'])
    def edit(Long id) {
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
        def emp = Employee.findByUsername(user)
        if(task.employee!=emp) {
            flash.message = "You are not authorized to edit others task!"
            redirect action: "home", controller: "user"
            return
        }

        respond Task.get(id)
    }

    @Transactional
    @Secured(['ROLE_USER'])
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
            redirect action: "show", controller: "user", id: task.id
            return
        } catch (ValidationException e) {
            respond task.errors, view:'edit'
            return
        }
    }

    @Transactional
    @Secured(['ROLE_USER'])
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

        user = springSecurityService.getPrincipal().username
        def emp = Employee.findByUsername(user)
        if(task.employee!=emp) {
            flash.message = "You are not authorized to delete others task!"
            redirect action: "home", controller: "user"
            return
        }

        (Task.get(id)).delete()

        log.info "............Task Deleted............"
        flash.message = "Task Deleted"
        redirect action: "home", controller: "user"
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

    @Secured(['ROLE_USER'])
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
            flash.message = "You are not authorized to view start task!"
            redirect action: "home", controller: "user"
            return
        }

        if(task1.getStatus().equals("In Progress")) {
            flash.message = "Task is already started!"
            redirect action:"show", id:id, controller:"user", model: [id: id]
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
            redirect action: "show", id: id, controller: "user", model: [id: id]
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
            redirect action:"show", id:id, controller:"user", model: [id: id]
        }
    }

    @Secured(['ROLE_ADMIN', 'ROLE_USER'])
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
            redirect action: "home", controller: "user"
            return
        }

        if(task1.getStatus().equals("Completed")) {
            flash.message = "Task is already accomplished!"
            redirect action:"show", id:id, controller:"user", model: [id: id]
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
        //MyMailService.sendEmail(recipient,task1.taskName,html, cc)
        MyMailService.sendEmail(recipient,task1.taskName,html, cc)

        flash.message = "Task Completed"
        redirect action:"show", id:id, controller:"user", model: [id: id]
    }

    @Secured(['ROLE_USER'])
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
    @Secured(['ROLE_USER'])
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
        redirect action:"show", id:id, controller:"user", model: [id: id]
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
