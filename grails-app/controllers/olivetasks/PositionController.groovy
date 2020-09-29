package olivetasks

import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

@Secured('permitAll')
class PositionController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    @Secured(['ROLE_ADMIN'])
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Position.list(params), model:[positionCount: Position.count()]
    }

    @Secured(['ROLE_ADMIN'])
    def show(Long id) {
        respond Position.get(id)
    }

    @Secured(['ROLE_ADMIN'])
    def create() {
        respond new Position(params)
    }

    @Secured(['ROLE_ADMIN'])
    def save(Position position) {
        if (position == null) {
            notFound()
            return
        }

        if(position.employeeRole == null) {
            Role role = Role.get(3)
            position.employeeRole = role
        }

        try {
            position.save()
        } catch (ValidationException e) {
            respond position.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'position.label', default: 'Position'), position.id])
                redirect position
            }
            '*' { respond position, [status: CREATED] }
        }
    }

    @Secured(['ROLE_ADMIN'])
    def edit(Long id) {
        respond Position.get(id)
    }

    @Transactional
    @Secured(['ROLE_ADMIN'])
    def update(Position position) {
        if (position == null) {
            notFound()
            return
        }

        try {
            position.save()
        } catch (ValidationException e) {
            respond position.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'position.label', default: 'Position'), position.id])
                redirect position
            }
            '*'{ respond position, [status: OK] }
        }
    }

    @Transactional
    @Secured(['ROLE_ADMIN'])
    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        Position position = Position.get(id)
        def employees = position.employees

        if(!employees.isEmpty()) {
            flash.message = "Cannot delete this Position. It may have some Employees"
            redirect action: "show", id: id, method: "GET"
            return
        }
        else {

            position.delete()

            request.withFormat {
                form multipartForm {
                    flash.message = message(code: 'default.deleted.message', args: [message(code: 'position.label', default: 'Position'), id])
                    redirect action: "index", method: "GET"
                }
                '*' { render status: NO_CONTENT }
            }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'position.label', default: 'Position'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
