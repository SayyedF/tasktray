package olivetasks

class BootStrap {

    def init = { servletContext ->

        new Company('Olive Software Solution','olive-white.png').save()
      //  initUsers()
    }

/*
    @Transactional
    def initUsers () {

        new EmailConfig('olivedhule@gmail.com','eloxzoqdhjwhpnzp','imap.gmail.com','993').save()

        new SendMailConfig('OliveTasks','olivedhule@gmail.com','eloxzoqdhjwhpnzp','smtp.gmail.com','465').save()

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

        devDepartment.save()
        mainDepartment.save()
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

        new Company('Olive Software Solution','olive-white.png').save()

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
*/
    def destroy = {
    }
}
