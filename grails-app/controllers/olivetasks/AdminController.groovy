package olivetasks

import grails.converters.JSON
import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.annotation.Secured

import static groovyx.gpars.GParsPool.withPool

@Secured('permitAll')
class AdminController {

    List<Task> tasks
    List<Task> leadtasks

    def springSecurityService
    def user
    def fileUploadService
    def dataSource // autowired

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    @Secured(['ROLE_ADMIN'])
    def home(Integer max) {

        user = springSecurityService.getPrincipal().username
        Employee employee = Employee.findByUsername(user)
        def version = employee.version


        params.max = Math.min(max ?: 10, 100)
        leadtasks = Task.all.findAll {
            //it.employee.position.positionName == 'Lead'
            it.employee.position.positionName in ['Lead','CEO']  //Change it later: in List of positionNames taken from Position Table where Role is Admin, CEO Or Lead
        }
        def count = (leadtasks==null) ? 0 : leadtasks.size()
        if(employee.id > 1 && version==0){
            flash.message="Security at Risk! Please change your password."
        }
        respond leadtasks, model:[taskCount: count]
    }

    @Secured(['ROLE_ADMIN','ROLE_LEAD','ROLE_USER'])
    def getTasks() {
        def employeeList = Employee.list()
        def newList = [];
        newList = employeeList.collect {employee -> ["id": employee.id, "name" : employee.fullname]}
        def tasks
        def count
        int deptId = 0
        int empId = 0
        Employee emp
        int typeId = 1
        def deptName

        if(params.deptId != null) {
            if(params.deptId != 'null') {
                deptId = Integer.parseInt(params.deptId)
                deptName = Department.get(deptId)?.departmentName
            }
        }

        if(params.typeId != null) {
            typeId = Integer.parseInt(params.typeId)
        }

        def type
        switch(typeId) {
            case 1: type = "Not Started"
                break
            case 2: type = "In Progress"
                break
            case 3: type = "Completed"
                break
            default: type = "Not Started"
                break
        }

        if(deptId != 0) {
            if(typeId == 0) {
                tasks = Task.all.findAll {
                    it.employee.department.id == deptId
                }
                type = 'All'
            }
            else {
                tasks = Task.all.findAll {
                    it.employee.department.id == deptId && it.status == "${type}"
                }
            }
        }
        else {
            if(typeId == 0) {
                tasks = Task.list()
                type = 'All'
            }
            tasks = Task.all.findAll {
                it.status == "${type}"
            }
        }

        if(type.equals('Not Started')) {
            type = 'Pending'
        }

        count = tasks.size()
        def responseData = [
                'tasks': tasks,
                'count' : count,
                'deptId' : deptId,
                'type' : type,
                'employees' : newList,
                'employeesCount' : Employee.count(),
                'deptName' : deptName
        ]
        def infoo = "---------------------getTasks called ----------------------"
        //log.info(infoo)
        render responseData as JSON
    }

    @Secured(['ROLE_ADMIN','ROLE_LEAD','ROLE_USER'])
    def getUserTasks() {
        def tasks
        def count
        int userId = 0
        int empId = 0
        Employee emp
        int typeId = 1

        if (params.userId != null) {
            if (params.userId != 'null') {
                userId = Integer.parseInt(params.userId)
                emp = Employee.get(userId)
            }
        }

        if (params.typeId != null) {
            typeId = Integer.parseInt(params.typeId)
        }

        def type
        switch (typeId) {
            case 1: type = "Not Started"
                break
            case 2: type = "In Progress"
                break
            case 3: type = "Completed"
                break
            default: type = "Not Started"
                break
        }


        if (typeId == 0) {
            tasks = Task.all.findAll {
                it.employee == emp
            }
            type = 'All'
        }
        else {
            tasks = Task.all.findAll {
                it.employee == emp && it.status == "${type}"
            }
        }

        if (type.equals('Not Started')) {
            type = 'Pending'
        }

        count = tasks.size()

        def responseData = [
                'tasks'         : tasks,
                'count'         : count,
                'EmployeeName'  : emp.fullname,
                'type'          : type
        ]
        def infoo = "---------------------getUserTasks called ----------------------"
        //log.info(infoo)
        render responseData as JSON
    }

    @Secured(['ROLE_ADMIN'])
    def getEmployees() {
        def employeeList = Employee.list()
        def newList = [];
        newList = employeeList.collect {employee -> [employee.id, employee.fullname]}
        def responseData = [
                'employees' : newList
        ]
        render responseData as JSON
    }


    @Secured('ROLE_ADMIN')
    def runMonitor() {
        def responseData = [
                'Start' : 'Failed'
        ]

        if(EmailConfig.count() > 0) {
            EmailConfig emailConfig = EmailConfig.last(sort: 'id')
            if(!MailMonitor.isRunning) {
                Closure myExpensiveCalculation = { MailMonitor.monitorMailbox(emailConfig.getUsername(),
                        emailConfig.getPassword(),emailConfig.getProperties()) }
                responseData = [
                        'Start' : 'Success'
                ]

                withPool {
                    myExpensiveCalculation.callAsync()
                    render responseData as JSON
                }
            }

        }

        render responseData as JSON
    }

    @Secured('ROLE_ADMIN')
    def settings() {
        EmailConfig emailConfig
        SendMailConfig sendMailConfig

        if(EmailConfig.count() > 0) {
            emailConfig = EmailConfig.last(sort: 'id') //${olivetasks.EmailConfig.last(sort: 'id')}
        }
        else {
            emailConfig = new EmailConfig();
        }

        if(SendMailConfig.count() > 0) {
            sendMailConfig = SendMailConfig.last(sort: 'id')
        }
        else {
            sendMailConfig = new SendMailConfig();
        }

        model: [monitorConfig: emailConfig, mailConfig: sendMailConfig, company: Company.last(sort: 'id')]

    }

    @Secured('ROLE_ADMIN')
    def getMonitorStatus() {
        def status = MailMonitor.isRunning
        def responseData = [
                'Status' : status,
                'MonitorConfig' : EmailConfig.count(),
                'MailConfig' : SendMailConfig.count()
        ]
        render responseData as JSON
    }

    @Secured('ROLE_ADMIN')
    def stopMonitor() {

        def responseData = [
                'Stop' : 'Failed'
        ]

        if(MailMonitor.isRunning) {

            MailMonitor.stopMonitor()

            responseData = [
                    'Stop' : 'Success'
            ]
        }
        render responseData as JSON
    }

    @Transactional
    @Secured(['ROLE_ADMIN'])
    def saveMonitorConfig() {
        String username = params.username
        String password = params.password
        String host = params.host
        String port = params.port

        if(EmailConfig.count() == 0) {
            new EmailConfig(username,password, host, port).save()
        }
        else {
            EmailConfig emailConfig = EmailConfig.last(sort: 'id')
            emailConfig.setUsername(username)
            emailConfig.setPassword(password)
            emailConfig.setHost(host)
            emailConfig.setPort(port)
            emailConfig.save()
        }
        flash.message = "Settings Updated Successfully!"
        redirect action: "settings"
    }


    @Transactional
    @Secured(['ROLE_ADMIN'])
    def saveMailConfig() {
        String senderName = params.senderName
        String username = params.username
        String password = params.password
        String host = params.host
        String port = params.port

        if(SendMailConfig.count() == 0) {
            new SendMailConfig(senderName, username,password, host, port).save()
        }
        else {
            SendMailConfig emailConfig = SendMailConfig.last(sort: 'id')
            emailConfig.setSenderName(senderName)
            emailConfig.setUsername(username)
            emailConfig.setPassword(password)
            emailConfig.setHost(host)
            emailConfig.setPort(port)
            emailConfig.save()
        }
        flash.message = "Settings Updated Successfully!"
        redirect action: "settings"
    }

    @Transactional
    @Secured(['ROLE_ADMIN'])
    def updateName() {
        Company company = Company.last(sort: 'id')
        Department department = Department.get(1)

        company.setCompanyName(params.companyName)
        department.setDepartmentName(params.companyName)
        company.save()
        department.save()

        flash.message = "Changes Saved!"
        redirect action: "settings"
    }

    @Transactional
    @Secured(['ROLE_ADMIN'])
    def updateLogo() {
        Company company = Company.last(sort: 'id')

        def logo = request.getFile('logo')
        fileUploadService.uploadFile(logo, 'CompanyLogo.png')
        company.setLogo('CompanyLogo.png');
        company.save()
        flash.message = "Changes Saved!"
        redirect action: "settings"
    }
}
