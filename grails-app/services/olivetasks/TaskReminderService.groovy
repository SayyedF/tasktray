package olivetasks

import grails.gorm.transactions.Transactional

@Transactional
class TaskReminderService {

    def remind() {
        if(SendMailConfig.count() == 0) {
            println('Email Account for sending emails is not configured')
            return
        }

        List<Employee> employees = Employee.list();
        for(int i=0; i<employees.size(); i++) {
            Employee employee = employees.get(i)
            def pendingTasks = findPendingTasks(employee)
            if(pendingTasks != null) {
                def html = new MyMailService().pendingTaskHtml(employee, pendingTasks)
                MyMailService.sendEmail(employee.email,'Your Pending Tasks',html, null)
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
