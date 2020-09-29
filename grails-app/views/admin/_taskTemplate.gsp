<div xmlns:g="http://www.w3.org/1999/xhtml">
    <div class="row">

        <div class="col-lg-6">
            <!-- Basic Card Example -->
            <div class="card shadow mb-4">
                <div class="card-header py-3">
                    <g:link action="show" controller="department" id="${dept?.id}">
                        <h6 class="m-0 font-weight-bold text-primary">${dept?.departmentName}: Project Manager</h6> </g:link>
                </div>

                <div class="card-body">

                            <g:render template="leadTaskTemplate" var="employee"
                                      collection="${olivetasks.Employee.all.findAll {it.department == dept && it.position.positionName in ["Lead","CEO","Manager"]}}">
                            </g:render>

                </div>
            </div>
        </div>

        <div class="col-lg-6">
            <!-- Basic Card Example -->
            <div class="card shadow mb-4">
                <div class="card-header py-3">
                    <g:link action="show" controller="department" id="${dept?.id}">
                        <h6 class="m-0 font-weight-bold text-primary">Team Members</h6>
                    </g:link>
                </div>
                <div class="card-body">

                        <%
                            def employeeList = olivetasks.Employee.all.findAll {it.department == dept && it.position.positionName in ["Developer"]}
                            def employeeCount = employeeList.size()
                        %>
                        <g:if test="${employeeCount > 0}">

                                <g:render template="leadTaskTemplate" var="employee"
                                          collection="${employeeList}">
                                </g:render>

                        </g:if>

                        <g:if test="${employeeCount == 0}">
                            ---
                        </g:if>

                </div>
            </div>
        </div>


    </div>

</div>