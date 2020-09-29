package olivetasks

import grails.gorm.transactions.Transactional

@Transactional
class CustomDepartment {
    Department department
    List<Department> subDepartments

    CustomDepartment(Department department) {
        this.department = department

        String dept = department.getDepartmentName()

        subDepartments = Department.all.findAll {
            it.headDepartment == this.department
        }

    }

    List<Department> getSubDepartments() {
        return subDepartments
    }

    @Override
    String toString() {
        return "Department: " + department + " - SubDepartments: " + subDepartments
    }
}
