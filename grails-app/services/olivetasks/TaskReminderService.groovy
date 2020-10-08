package olivetasks

import grails.gorm.transactions.Transactional

@Transactional
class TaskReminderService {

    //remind employees their pending tasks by email
    def remind() {
        if(SendMailConfig.count() == 0) {
            println('Email Account for sending emails is not configured')
            return
        }

        List<Employee> employees = Employee.list();
        for(int i=0; i<employees.size(); i++) {
            Employee employee = employees.get(i)
            List<Task> pendingTasks = findPendingTasks(employee)
            if(pendingTasks?.size() > 0) {
                def subject
                if(pendingTasks.size() == 1)
                    subject = 'Your Pending Task'
                else
                    subject = 'Your Pending Tasks'

                def html = new MyMailService().pendingTaskHtml(employee, pendingTasks)
                MyMailService.sendEmail(employee.email,subject,html, null)
            }
        }
    }

    List<Task> findPendingTasks(Employee employee) {
        def pendingTasks = Task.all.findAll {
            it.status == 'Not Started' && it.employee == employee
        }
        return pendingTasks
    }
}
