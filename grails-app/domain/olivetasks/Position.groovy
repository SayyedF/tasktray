package olivetasks

class Position {
    String positionName
    Role employeeRole

    Position(String positionName, Role role) {
        this.positionName = positionName
        this.employeeRole = role
    }
    static hasMany = [employees: Employee]

    static constraints = {
        positionName()
        employeeRole(nullable: true)
    }

    @Override
    String toString() {
        return positionName
    }
}
