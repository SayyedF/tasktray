package olivetasks

import grails.gorm.transactions.Transactional
import org.springframework.beans.factory.annotation.Autowired

//TODO : Set proper Deadline

class TaskUtil {

    @Autowired
    def mailedTaskService

    @Autowired
    def grailsLinkGenerator

    @Transactional
    def saveTask(String sender, String receiver, String subject, String body) {
        //println("Inside TaskUtil.saveTask()")

        //println("Inside MailedTaskService.saveTask()")
        Employee assignee = Employee.findByEmail(sender)
        if(assignee == null)
            return -1



        Employee employee = Employee.findByEmail(receiver)
        if(employee == null)
            return -2

        //println("Assignee: " + assignee)
        //println("Employee" + employee)

        if(authorized(assignee, employee)) {
            println("Authorized")
            int id = assignee?.id
            println("ID " + id)

            Task task = new Task()

            //println("Fresh task created. Now setting properties")

            task.setTaskName(subject)
            //println("TaskName set")

            task.setType("Admin Assigned")
            //println("Type set")

            task.setStatus("Not Started")
            //println("Status set")

            task.setAssignee(id)
            //println("Assignee set")

            task.employee = employee
            //println("Employee set")

            task.setCreationDate(new Date())
            //println("CreationDate set")

            task.setDescription(body)
            //println("Description set")

            //TODO: Optimise it
            Date date = new Date()
            date.setHours(12)
            date.setMinutes(0)
            date.setSeconds(0)
            date = DateUtil.addDays(date, 3);

            task.setDeadline(date)
            //println("Deadline set")

            //Date deadline = new Date()
            //Task task = new Task(subject,new Date(),deadline,"Admin Assigned","Not Started",employee,assignee?.id)
            //println("Trying to save the task: " + task)
            task.save()
            Task.withSession {
                it.flush()
                it.clear()
            }
            println("Task Saved!")

            try {
                addAlert(task.taskName, employee)
            }
            catch(Exception e) {
                e.printStackTrace()
            }
            return 1
        }

        println("Task could not be saved - Authorization failed!")
        return -3

    }

    boolean authorized(Employee assignee, Employee employee) {
        String assigneeRole
        String employeeRole

        String assigneeDept
        String employeeDept

        if (assignee.authorities.any { it.authority == "ROLE_ADMIN" }) {
            return true
        }
        else if(assignee.authorities.any { it.authority == "ROLE_LEAD" }) {

            if(assignee.department == employee.department && employee.authorities.any { it.authority == "ROLE_USER" }) {
                return true
            }
        }
        else if(assignee.authorities.any { it.authority == "ROLE_MANAGER" }) {

            //TODO Manager of HeadDepartment can also assign tasks to employees of subDepartment
            if(assignee.department == employee.department && employee.authorities.any { it.authority == "ROLE_USER" }) {
                return true
            }
        }
        else {
            if(assignee.equals(employee)) {
                return true
            }
        }

        return false
    }

    @Transactional
    def addAlert(String taskName, Employee employee) {


        def url
        int taskId

        //println("addAlert()")

        Task task= Task.findByTaskName(taskName)
        taskId = task?.id

        Notification alert = new Notification()

        alert.setAlert("New Task Assigned")
        alert.setDate(new Date())
        alert.receiver = employee

        //println("ALert Created")

        /*if(grailsLinkGenerator == null) {
            println("grailsLinkGenerator is NULL")
            return
        }*/

        //TODO: generate only uri and while showing alerts in gsp append uri to the base url

        url = "http://localhost:8080/task/show/" + taskId //grailsLinkGenerator.link(controller: 'task', action: 'show', id: taskId)
        alert.url = url

        //println("Adding alert")
        task.addToAlerts(alert)
        task.save()
        Task.withSession {
            it.flush()
            it.clear()
        }
        println("Alert sent to user")
    }
}
