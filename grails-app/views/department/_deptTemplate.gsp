<!DOCTYPE html>
<html xmlns:g="http://www.w3.org/1999/XSL/Transform" xmlns:f="http://www.w3.org/1999/xhtml">
<body>
<div>

    <!-- Basic Card Example -->
    <div class="card shadow mb-4">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary"><g:link action="show" id="${headDepartment?.department?.id}">${headDepartment.department}</g:link></h6>
        </div>
        <div class="card-body">
            Sub-Departments:
            <ol>
            <g:each in="${headDepartment.getSubDepartments()}" var="subDepartment">
                <li>
                    <g:link action="show" id="${subDepartment?.id}">${subDepartment}</g:link>
                </li>
            </g:each>
            </ol>
        </div>
    </div>

</div>

</body>
</html>