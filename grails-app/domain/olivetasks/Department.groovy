package olivetasks

class Department {

    String departmentName
    //Department headDepartment

    Department(String departmentName) {
        this.departmentName = departmentName
        this.headDepartment = null
    }

    static hasMany = [employees: Employee]
    static belongsTo = [headDepartment: Department]
    static mappedBy = [headDepartment: "none"]

    static constraints = {
        departmentName()
        headDepartment nullable: true
    }

    @Override
    String toString() {
        return departmentName
    }
}
