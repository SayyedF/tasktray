package olivetasks

import grails.compiler.GrailsCompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import java.text.ParseException

@GrailsCompileStatic
@EqualsAndHashCode(includes='username')
@ToString(includes='username', includeNames=true, includePackage=false)
class Employee implements Serializable {

    private static final long serialVersionUID = 1

    String username
    String password
    boolean enabled = true
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired

    String fullname
    String email
    Date dateOfBirth

    Employee(String username, String password, String fullname, String email) {
        this.username = username
        this.password = password
        this.fullname = fullname
        this.email = email

    }

    Employee(String username, String password, String fullname, String email, Date dob) {
        this.username = username
        this.password = password
        this.fullname = fullname
        this.email = email
        this.dateOfBirth = dob
    }

    Set<Role> getAuthorities() {
        (EmployeeRole.findAllByEmployee(this) as List<EmployeeRole>)*.role as Set<Role>
    }

    static belongsTo = [department: Department, position: Position] //
    static hasMany = [tasks: Task, notifications: Notification] // notifications: Notification
    //static mappedBy = [manager: "none"]

    static constraints = {
        fullname nullable: false, blank: false
        username nullable: false, blank: false, unique: true
        password nullable: false, blank: false, password: true
        email nullable:false, blank: false, email: true
        dateOfBirth nullable: true, format: 'dd-MM-yyyy', date: true
        position nullable:true
        department()
        tasks()
        notifications(nullable: true, display: false)
    }

    static mapping = {
	    password column: '`password`'
    }

    @Override
    String toString() {
        return fullname
    }
}
