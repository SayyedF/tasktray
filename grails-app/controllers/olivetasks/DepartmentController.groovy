package olivetasks

import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

@Secured('permitAll')
class DepartmentController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    List<Department> headDepartmentList
    List<CustomDepartment> deptList

    @Secured(['ROLE_ADMIN'])
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        CustomDepartment customDepartment = new CustomDepartment(Department.get(1))

        headDepartmentList = Department.all.findAll {
            it.headDepartment == null
        }

        deptList = new ArrayList<CustomDepartment>()

        for(int i=0; i<headDepartmentList.size(); i++) {
            deptList.add(new CustomDepartment(headDepartmentList.get(i)))
        }

        respond Department.list(params), model:[departmentCount: Department.count(),
                                                customDepartment: customDepartment,
                                                headDepartmentList: headDepartmentList, deptList: deptList]
    }

    @Secured(['ROLE_ADMIN'])
    def show(Long id) {
        respond Department.get(id)
    }

    @Secured(['ROLE_ADMIN'])
    def create() {
        respond new Department(params)
    }

    @Secured(['ROLE_ADMIN'])
    def save(Department department) {
        if (department == null) {
            notFound()
            return
        }

        try {
            department.save()
        } catch (ValidationException e) {
            respond department.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'department.label', default: 'Department'), department.id])
                redirect department
            }
            '*' { respond department, [status: CREATED] }
        }
    }

    @Secured(['ROLE_ADMIN'])
    def edit(Long id) {
        respond Department.get(id)
    }

    @Transactional
    @Secured(['ROLE_ADMIN'])
    def update(Department department) {
        if (department == null) {
            notFound()
            return
        }

        try {
            department.save()
        } catch (ValidationException e) {
            respond department.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'department.label', default: 'Department'), department.id])
                redirect department
            }
            '*'{ respond department, [status: OK] }
        }
    }

    @Transactional
    @Secured(['ROLE_ADMIN'])
    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }



        Department dept = Department.get(id)
        def emps = dept.employees
        def deps = Department.all.findAll {
            it?.headDepartment == dept
        }

        if(dept.headDepartment!=null || !emps.isEmpty() || !deps.isEmpty()) {
            flash.message = "Department cannot be deleted! Possible Solutions: Remove All Employess from this Department/" +
                    "Set Head Department as NULL/If it has any Sub Department, remove it or set another department as head department."
            redirect action: "show", id: id, method: "GET"
            return
        }


        dept.delete()

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'department.label', default: 'Department'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'department.label', default: 'Department'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
