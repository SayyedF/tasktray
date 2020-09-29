package olivetasks

import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import grails.web.mapping.LinkGenerator
import groovy.time.TimeCategory
import groovy.time.TimeDuration

import static org.springframework.http.HttpStatus.*

@Secured('permitAll')
class TaskController {

    def mailedTaskService
    def mailService

    LinkGenerator grailsLinkGenerator
    String url

    def springSecurityService
    def user
    static allowedMethods = [save: "POST", update: "PUT", delete: ["DELETE","GET","PUT","POST"]]

    @Secured(['ROLE_ADMIN','ROLE_LEAD'])
    def index(Integer max) {

        def username = springSecurityService.getPrincipal().username
        def user = Employee.findByUsername(username)

        //Lead
        if (user.authorities.any { it.authority == "ROLE_LEAD" }) {
            redirect action:"home", controller:"lead"
            return
        }

        params.max = Math.min(max ?: 10, 100)
        respond Task.list(params), model:[taskCount: Task.count()]
    }

    @Secured(['ROLE_USER','ROLE_ADMIN','ROLE_LEAD'])
    def show(Long id) {


        def username = springSecurityService.getPrincipal().username
        def user = Employee.findByUsername(username)

        if (user.authorities.any { it.authority == "ROLE_LEAD" }) {
            redirect action:"show", id:id, controller:"lead", model: [id: id]
            return
        }

        if (user.authorities.any { it.authority == "ROLE_USER" }) {
            redirect action:"show", id:id, controller:"user", model: [id: id]
            return
        }

        if(id == null) {
            notFound()
            return
        }
        Task task = Task.get(id)

        if (task == null) {
            notFound()
            return
        }


        def nList = Notification.all.findAll {
            it?.receiver == user && !it?.seen && it?.task?.id == id
        }

        def nCount = nList.size()
        makeSeen(nList,nCount)

        def mode
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

    @Secured(['ROLE_ADMIN'])
    def create() {
        respond new Task(params)
    }

    @Secured(['ROLE_USER','ROLE_ADMIN','ROLE_LEAD'])
    def save(Task task) {

        if (task == null) {
            notFound()
            return
        }

        try {
            def user = springSecurityService.getPrincipal().username
            def emp = Employee.findByUsername(user)
            if( user.equals(task.employee?.username)) {
                task.setStatus("In Progress")
                task.setStartDate(new Date())
            }
            else
                task.setStatus("Not Started")

            task.setCreationDate(new Date())
            task.assignee = emp.id

            DailyUpdate dailyUpdate = new DailyUpdate(new Date(),"Created")
            task.addToUpdates(dailyUpdate)

            Change change = new Change('Created', new Date())
            task.addToChanges(change)
            task.save()

            // Notify by Email
            def html = new MyMailService().newTaskHtml(task)
            def cc = mailedTaskService.getManagers(task.employee)
            String recipient = task.employee.email
            MyMailService.sendEmail(recipient,task.taskName,html, cc)

            // Notify through webapp
            Notification not = new Notification("New Task Assigned", new Date(), task.employee)
            addAlert(task.taskName, not)

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

    @Secured(['ROLE_ADMIN'])
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

        respond Task.get(id)
    }

    @Transactional
    @Secured(['ROLE_ADMIN','ROLE_USER','ROLE_LEAD'])
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

            def username = springSecurityService.getPrincipal().username
            def user = Employee.findByUsername(username)
            def id = task.id

            if (user.authorities.any { it.authority == "ROLE_LEAD" }) {
                flash.message = "Task updated"
                redirect action:"show", id:id, controller:"lead", model: [id: id]
                return
            }

            if (user.authorities.any { it.authority == "ROLE_USER" }) {
                flash.message = "Task updated"
                redirect action:"show", id:id, controller:"user", model: [id: id]
                return
            }

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
    @Secured(['ROLE_ADMIN','ROLE_LEAD','ROLE_USER'])
    def delete(Long id) {
        def username = springSecurityService.getPrincipal().username
        def user = Employee.findByUsername(username)

        if (user.authorities.any { it.authority == "ROLE_LEAD" }) {
            redirect action:"delete", id:id, controller:"lead", model: [id: id]
            return
        }

        if (user.authorities.any { it.authority == "ROLE_USER" }) {
            flash.message = "Redirecting to users controller"
            redirect action:"delete", id:id, controller:"user", model: [id: id]
            return
        }


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

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'task.label', default: 'Task'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
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

    @Secured(['ROLE_LEAD','ROLE_ADMIN'])
    @Transactional
    def start(Long id) {
        if (id == null) {
            notFound()
            return
        }

        Task task1 = Task.get(id)
        //Task task = Task.get(id)

        if (task1 == null) {
            notFound()
            return
        }

        user = springSecurityService.getPrincipal().username
        def emp = Employee.findByUsername(user)
        if(task1.employee!=emp) {
            flash.message = "You are not authorized to start others task!"
            redirect action:"show", id:id, controller:"task", model: [id: id]
            return
        }

        if(task1.getStatus().equals("In Progress")) {
            flash.message = "Task is already started!"
            redirect action:"show", controller:"task", id:id, model: [id: id]
            return
        }
        else if(task1.getStatus().equals("Completed")) {
            task1.setStatus("In Progress")
            //task1.setStartDate(new Date())  // As Task is restarted
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

            flash.message = "Task Restarted!"
            redirect action: "show", id: id, controller: "task", model: [id: id]
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

            flash.message = "Task Started!"
            redirect action: "show", id: id, controller: "task", model: [id: id]
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
        //Task task = Task.get(id)

        if (task1 == null) {
            notFound()
            return
        }

        user = springSecurityService.getPrincipal().username
        def emp = Employee.findByUsername(user)
        if(task1.employee!=emp) {
            flash.message = "You are not authorized to end others task!"
            redirect action:"show", id:id, controller:"task", model: [id: id]
            return
        }

        if(task1.getStatus().equals("Completed")) {
            flash.message = "Task is already accomplished!"
            redirect action:"show", controller:"task", id:id, model: [id: id]
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

        flash.message = "Task Completed!"
        redirect action:"show", id:id, controller:"task", model: [id: id]
    }

    @Secured(['ROLE_ADMIN'])
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
    @Secured(['ROLE_ADMIN'])
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
        redirect action:"show", id:id, controller:"task", model: [id: id]
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
