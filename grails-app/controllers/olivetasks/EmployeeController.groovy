package olivetasks

import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

@Secured('permitAll')
class EmployeeController {

    def springSecurityService
    def user

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    @Secured(['ROLE_ADMIN'])
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Employee.list(params), model:[employeeCount: Employee.count()]
    }

    @Secured(['ROLE_ADMIN'])
    def show(Long id) {
        respond Employee.get(id)
    }

    @Secured(['ROLE_ADMIN'])
    def create() {
        respond new Employee(params)
    }


    @Transactional
    @Secured(['ROLE_ADMIN'])
    def save(Employee employee) {
        if (employee == null) {
            notFound()
            return
        }

        /*def username = employee.getUsername()
        def user = Employee.findByUsername(username)
        if(user) {
            flash.message = "Username not available"
            create()
            return
        }*/

        employee.setPassword("youpickit")
        def pos1 = employee.position
        if(pos1==null) {
            pos1 = Position.get(3)
            employee.position = pos1
        }
        def role = pos1.employeeRole

        try {
            if(!employee.save()) {
                flash.message = "Username already exists!"
                redirect action: "create"
                return
            }
            EmployeeRole.create employee, role
            EmployeeRole.withSession {
                it.flush()
                it.clear()
            }
            log.info "New Employee Added"
        } catch (ValidationException e) {
            respond employee.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'employee.label', default: 'Employee'), employee.id])
                redirect employee
            }
            '*' { respond employee, [status: CREATED] }
        }
    }

    @Secured(['ROLE_ADMIN'])
    def edit(Long id) {
        respond Employee.get(id)
    }

    @Secured(['ROLE_ADMIN'])
    def changePassword(Long id) {
        respond Employee.get(id)
    }

    @Transactional
    @Secured(['ROLE_ADMIN','ROLE_LEAD','ROLE_USER'])
    def update(Employee employee) {
        if (employee == null) {
            notFound()
            return
        }
        if(params.mode == "1" ) {
            try {

                /*def username = employee.getUsername()
                def user = Employee.findByUsername(username)
                if(user.id != employee.id) {
                    flash.message = "Username not available"
                    redirect controller:"profile", action:"index"
                    return
                }*/

                if(employee.dateOfBirth.day!=null && employee.dateOfBirth.month!=null && employee.dateOfBirth.year!=null) {
                    employee.save()
                    flash.message = "Profile Updated!"
                    redirect controller: "profile", action: "index"
                    return
                }
                else {
                    flash.message = "Invalid Date of Birth"
                    redirect controller: "profile", action: "index"
                    return
                }
            }
            catch (ValidationException e) {
                respond employee.errors, view: 'edit' // Change it later
                return
            }
        }
        EmployeeRole.removeAll(employee)

        def pos1 = employee.position
        if(pos1==null) {
            pos1 = Position.get(3)
            employee.position = pos1

        }
        def role = pos1.employeeRole

        //def emprole = EmployeeRole.findByEmployee(employee)
        //emprole.role = role
        //emprole.delete()

        try {
            employee.save()
            EmployeeRole.create employee, role

            EmployeeRole.withSession {
                it.flush()
                it.clear()
            }
        } catch (ValidationException e) {
            respond employee.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'employee.label', default: 'Employee'), employee.id])
                redirect employee
            }
            '*'{ respond employee, [status: OK] }
        }
    }

    @Transactional
    @Secured(['ROLE_ADMIN'])
    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        def username = springSecurityService.getPrincipal().username
        user = Employee.findByUsername(username)

        Employee employee = Employee.get(id)

        if(employee.equals(user)) {
            flash.message = "You cannot delete yourself!"
            redirect action: "show", id: id, method: "GET"
            return
        }

        EmployeeRole.removeAll(employee)
        employee.delete()

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'employee.label', default: 'Employee'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'employee.label', default: 'Employee'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
