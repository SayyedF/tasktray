<div xmlns:g="http://www.w3.org/1999/xhtml">

    <%
        def runningTasks = olivetasks.Task.all.findAll { it.employee == employee && it.status in ["In Progress"] }
        def pendingTasks = olivetasks.Task.all.findAll { it.employee == employee && it.status in ["Not Started"] }

        def runningCount = runningTasks.size()
        def pendingCount = pendingTasks.size()
    %>

    &#8226;<g:link action="show" controller="employee" id="${employee?.id}">
        ${employee.fullname} </g:link>

    <div style="margin-left: 20px;">
        <a href="#taskTable" onclick="getTasks(${employee?.department?.id},2);">
            <p>In Progress: ${runningCount}</p></a>
    <g:if test="${runningCount > 0}">

        <g:render template="eachTaskTemplate" var="task" collection="${runningTasks}"></g:render>
    </g:if>
        <a href="#taskTable" onclick="getTasks(${employee?.department?.id},1);">
            <p>Pending: ${pendingCount}</p> </a>
    <g:if test="${pendingCount > 0}">

        <g:render template="eachTaskTemplate" var="task" collection="${pendingTasks}"></g:render>
    </g:if>

    <br>
    </div>

</div>