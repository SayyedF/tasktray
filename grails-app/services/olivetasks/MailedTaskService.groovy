package olivetasks

import grails.gorm.transactions.Transactional

@Transactional
class MailedTaskService {

    //TODO: Select Managers along with Lead
    def getManagers(Employee employee) {
        String managerIds = ""
        List<String> managerList = new ArrayList<String>()
        if(employee.authorities.any { it.authority == "ROLE_USER" }) {
            def managers = Employee.all.findAll {
                it.department == employee.department && it.authorities.any { it.authority == "ROLE_LEAD" }
            }

            for(int i=0; i<managers.size(); i++) {

                managerList.add(managers[i].email)

                if(i==0) {
                    managerIds = '"' + managers[i].email + '"'
                }
                else
                    managerIds =  managerIds + ',' + '"' + managers[i].email + '"'
            }
            return managerList //managerIds
        }
        return null
    }

    def saveTask(String sender, String receiver, String subject, String body) {

        println("Inside MailedTaskService.saveTask()")
        Employee assignee = Employee.findByEmail(sender)
        if(assignee == null)
            return -1

        println("Assignee: " + assignee)

        Employee employee = Employee.findByEmail(receiver)
        if(employee == null)
            return -2

        println("Employee" + employee)

        if(authorized(assignee, employee)) {
            println("Authorized")
            Date deadline = new Date()
            Task task = new Task(subject,new Date(),deadline,"Admin Assigned","Not Started",employee,assignee?.id)
            task.save()
            println("Task Saved!!!!!")
            return 1
        }

        println("exiting MailedTaskService.saveTask()")
        return -3

    }

    boolean authorized(Employee assignee, Employee employee) {
        String assigneeRole
        String employeeRole

        String assigneeDept
        String employeeDept

        return true
    }

    def sample() {}
}
