package olivetasks

import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException

import java.text.SimpleDateFormat

import static org.springframework.http.HttpStatus.NOT_FOUND

@Transactional
@Secured('permitAll')
class ProfileController {

    def springSecurityService
    def user

    def index() {
        def username = springSecurityService.getPrincipal().username
        respond Employee.findByUsername(username)
    }

    def edit() {
        def username = springSecurityService.getPrincipal().username
        respond Employee.findByUsername(username)
    }

    def lost() {}

    def reset() {
        if (params.username == "") {
            flash.message = "Username cannot be left blank"
            redirect action: "lost"
            return
        } else if (params.dob == "") {
            flash.message = "DOB is blank"
            redirect action: "lost"
            return
        } else {
            try {
                Employee user = Employee.findByUsername(params.username)
                if(user == null) {
                    flash.message = "User not found!"
                    redirect action: "lost"
                    return
                }
                String date = params.dob

                    if(user.dateOfBirth == null) {
                        flash.message = "Date of Birth does not match! Contact your admin"
                        redirect action: "lost"
                        return
                    }

                String date2 = user.dateOfBirth.toString()
                Date dob = new SimpleDateFormat("yyyy-MM-dd").parse(date)
                Date dob2 = new SimpleDateFormat("yyyy-MM-dd").parse(date2)
                if(!dob.equals(dob2)) {
                    flash.message = "Date of Birth does not match! Contact your admin"
                    redirect action: "lost"
                    return
                }

                respond user, model: [user: user]

            } catch (ValidationException e) {
                flash.message = "Password Reset Failed"
                redirect action: "lost"
                return
            }
        }
    }

    @Transactional
    def changePassword() {
        String mode = params.mode
        if(mode.equals("update")) {
            if (!params.password1.equals(params.repassword1)) {
                flash.message = "Password and Re-Password not match"
                render view: "updatePassword"
                return
            } else {
                try {
                    def username = springSecurityService.getPrincipal().username
                    def emp = Employee.findByUsername(username)
                    emp.password = params.password1
                    emp.save()
                    flash.message = "Password Reset Successfully"
                    redirect action: "index", controller: "profile"
                    return

                } catch (ValidationException e) {
                    flash.message = "Password Reset Failed"
                    redirect action: "index"
                    return
                }
            }
        }
        else {
            def user = params.employee
            if (!params.password1.equals(params.repassword1)) {
                flash.message = "Password and Re-Password not match"
//            forward action: "reset",controller:"profile", model: [params: params,user: Employee.get(user)]
                render view: "reset", model: [user: Employee.get(user)]
                return
            } else {
                try {
                    def emp = Employee.get(user)
                    emp.password = params.password1
                    emp.save()
                    flash.message = "Password Reset Successfully"
                    redirect action: "auth", controller: "login"
                    return

                } catch (ValidationException e) {
                    flash.message = "Reset Failed"
                    redirect action: "lost"
                    return
                }
            }
        }
    }

    def updatePassword() {
    }

    //def changePassword () {
        /*
        def passwordEncoder
        passwordEncoder.isPasswordValid(user.password, params.currentPassword, null)
        * */
    //}

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'task.label', default: 'Task'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}