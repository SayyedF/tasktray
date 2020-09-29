package olivetasks

import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.annotation.Secured
import grails.web.context.ServletContextHolder
import grails.validation.ValidationException

import java.text.SimpleDateFormat

@Transactional
@Secured('permitAll')
class RegisterController {

    static allowedMethods = [register: "POST"]
    def fileUploadService
    //AssetProcessorService assetProcessorService
    def assetResourceLocator
    def assetProcessorService
    //ResourceLocator assetResourceLocator
    

    def index() {

        /*if(Employee.count() > 0) {
            flash.message = "You are already registered! Please Login"
            redirect controller: "login", action: "auth"
            return
        }*/

        if(Employee.count==0) {
            initUsers()
            flash.message = "You have registered successfully. Please login."
            redirect controller: "login", action: "auth"
            return
        }
        else {
            flash.message = "App is already initialised! Please Login"
            redirect controller: "login", action: "auth"
            return 
        }
    }

    @Secured('permitAll')
    def test() {

        String obj = params.assetPath
        def servletContext = ServletContextHolder.servletContext
        def storagePath = servletContext.getRealPath(obj)  //destinationDirectory

        render(params.assetPath + ':' + storagePath)
    }

//    @Transactional
//    def save() {
//        def companyName = params.companyName
//
//        def logo = request.getFile('logo')
//
//        fileUploadService.uploadFile(logo, companyName+'.png')
//        new Company(companyName,companyName+'_logo.png').save()
//
//        flash.message =  "Registered Successfully"  // + "${assetPath(src: 'olive-white.png', absolute: true)}" + url
//        redirect controller: "login", action: "auth"
//    }

    @Transactional
    def register() {
        if(!params.password.equals(params.repassword)) {
            flash.message = "Password and Re-Password not match"
            redirect action: "index"
            return
        } else {
            try {

                def logo = request.getFile('logo')

                def companyName = params.companyName
                //def role = Role.get(params.role.id)
                def role = new Role('ROLE_ADMIN')
                role.save()

                def role2 = new Role('ROLE_LEAD')
                role2.save()

                def role3 = new Role('ROLE_USER')
                role3.save()

                def positionLead = new Position("Lead",role2)
                def positionDeveloper = new Position("Developer",role3)
                positionLead.save()
                positionDeveloper.save()

                def mainDepartment = new Department(companyName)
                def positionCEO = new Position("CEO", role)
                String date = params.dob
                Date dob = new SimpleDateFormat("yyyy-MM-dd").parse(date)
                def user = new Employee(params.username, params.password, params.fullname, params.email, dob)

                mainDepartment.addToEmployees(user)
                mainDepartment.save()

                positionCEO.addToEmployees(user)
                positionCEO.save()

                if(user && role) {
                    EmployeeRole.create user, role

                    EmployeeRole.withSession {
                        it.flush()
                        it.clear()
                    }

                    fileUploadService.uploadFile(logo, 'CompanyLogo.png')
                    new Company(companyName,'CompanyLogo.png').save()

                    flash.message = "You have registered successfully. Please login."
                    redirect controller: "login", action: "auth"
                } else {
                    flash.message = "Register failed"
                    render view: "index"
                    return
                }
            } catch (ValidationException e) {
                flash.message = "Register Failed"
                redirect action: "index"
                return
            }
        }
    }

    @Transactional
    def initUsers () {

        //new EmailConfig('olivedhule@gmail.com','eloxzoqdhjwhpnzp','imap.gmail.com','993').save()

        //new SendMailConfig('OliveTasks','olivedhule@gmail.com','eloxzoqdhjwhpnzp','smtp.gmail.com','465').save()

        def role1 = new Role('ROLE_ADMIN')
        role1.save()

        def role2 = new Role('ROLE_LEAD')
        role2.save()

        def role3 = new Role('ROLE_USER')
        role3.save()

        def role4 = new Role('ROLE_MANAGER')
        role4.save()

        def mainDepartment = new Department("Olive")
        def devDepartment = new Department("Development")

        def phpDepartment = new Department("PHP")
        phpDepartment.headDepartment = devDepartment

        def javaDepartment = new Department("Java")
        javaDepartment.headDepartment = devDepartment

        def oracleDepartment = new Department("Oracle")
        oracleDepartment.headDepartment = devDepartment

        def positionCEO = new Position("CEO", role1)
        def positionLead = new Position("Lead",role2)
        def positionDeveloper = new Position("Developer",role3)
        //def positionManager = new Position("Manager", role4)

        def user1 = new Employee('aashaikh','aashaikh','Abdul Ahad Shaikh','abdulahads@yahoo.com')
        def user2 = new Employee('sfjilani','sfjilani','Fakhruddin Sayyed','sfjilani5dec@gmail.com')
        def user3 = new Employee('omair','omair','Omair Sayyed','sm.7omair@gmail.com')
        def user4 = new Employee('aqib','aqib','Aqib Shaikh','aquibsorcl@gmail.com')
        def user5 = new Employee('noman','noman','Noman Shaikh','namirza333@gmail.com')
        def user6 = new Employee('azim','azim','Azim Shaikh','ajimsk00@gmail.com')
        def user7 = new Employee('saad','saad','Saad Shaikh','saadkha77n@gmail.com')
        def user8 = new Employee('saquib','saquib','Saquib Shaikh','saquibg8@gmail.com')

        //user1.save()


        mainDepartment.addToEmployees(user1)
        javaDepartment.addToEmployees(user2)
        javaDepartment.addToEmployees(user3)
        phpDepartment.addToEmployees(user4)
        phpDepartment.addToEmployees(user5)
        phpDepartment.addToEmployees(user6)
        phpDepartment.addToEmployees(user7)
        phpDepartment.addToEmployees(user8)

        mainDepartment.save()
        devDepartment.save()
        phpDepartment.save()
        javaDepartment.save()
        oracleDepartment.save()

        positionCEO.addToEmployees(user1)
        positionLead.addToEmployees(user2)
        positionLead.addToEmployees(user4)
        positionLead.addToEmployees(user6)
        positionDeveloper.addToEmployees(user5)
        positionDeveloper.addToEmployees(user3)
        positionDeveloper.addToEmployees(user7)
        positionDeveloper.addToEmployees(user8)

        positionCEO.save()
        positionLead.save()
        positionDeveloper.save()
        //positionManager.save()

        new Task("Task Management System",new Date(),new Date(),"Admin Assigned","Not Started",user2,1).save()
        new Task("Grails Demo",new Date(),new Date(),"Admin Assigned","Not Started",user3,1).save()
        new Task("School Website",new Date(),new Date(),"Admin Assigned","Not Started",user4,1).save()
        new Task("School DB forms",new Date(),new Date(),"Admin Assigned","Not Started",user5,1).save()

        //new Company('Olive Software Solution','olive-white.png').save()

        EmployeeRole.create(user1,role1)
        EmployeeRole.create(user2,role2)
        EmployeeRole.create(user3,role3)
        EmployeeRole.create(user4,role2)
        EmployeeRole.create(user6,role2)
        EmployeeRole.create(user5,role3)
        EmployeeRole.create(user7,role3)
        EmployeeRole.create(user8,role3)
        EmployeeRole.withSession {
            it.flush()
            it.clear()
        }
    }
}
