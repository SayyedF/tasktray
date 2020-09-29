package olivetasks

import grails.gorm.DetachedCriteria
import groovy.transform.ToString

import org.codehaus.groovy.util.HashCodeHelper
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
@ToString(cache=true, includeNames=true, includePackage=false)
class EmployeeRole implements Serializable {

	private static final long serialVersionUID = 1

	Employee employee
	Role role

	@Override
	boolean equals(other) {
		if (other instanceof EmployeeRole) {
			other.employeeId == employee?.id && other.roleId == role?.id
		}
	}

    @Override
	int hashCode() {
	    int hashCode = HashCodeHelper.initHash()
        if (employee) {
            hashCode = HashCodeHelper.updateHash(hashCode, employee.id)
		}
		if (role) {
		    hashCode = HashCodeHelper.updateHash(hashCode, role.id)
		}
		hashCode
	}

	static EmployeeRole get(long employeeId, long roleId) {
		criteriaFor(employeeId, roleId).get()
	}

	static boolean exists(long employeeId, long roleId) {
		criteriaFor(employeeId, roleId).count()
	}

	private static DetachedCriteria criteriaFor(long employeeId, long roleId) {
		EmployeeRole.where {
			employee == Employee.load(employeeId) &&
			role == Role.load(roleId)
		}
	}

	static EmployeeRole create(Employee employee, Role role, boolean flush = false) {
		def instance = new EmployeeRole(employee: employee, role: role)
		instance.save(flush: flush)
		instance
	}

	static boolean remove(Employee u, Role r) {
		if (u != null && r != null) {
			EmployeeRole.where { employee == u && role == r }.deleteAll()
		}
	}

	static int removeAll(Employee u) {
		u == null ? 0 : EmployeeRole.where { employee == u }.deleteAll() as int
	}

	static int removeAll(Role r) {
		r == null ? 0 : EmployeeRole.where { role == r }.deleteAll() as int
	}

	static constraints = {
	    employee nullable: false
		role nullable: false, validator: { Role r, EmployeeRole ur ->
			if (ur.employee?.id) {
				if (EmployeeRole.exists(ur.employee.id, r.id)) {
				    return ['userRole.exists']
				}
			}
		}
	}

	static mapping = {
		id composite: ['employee', 'role']
		version false
	}
}
