<!DOCTYPE html>
<html xmlns:g="http://www.w3.org/1999/XSL/Transform" xmlns:f="http://www.w3.org/1999/xhtml">
<head>
    <meta name="layout" content="admin" />
    <asset:stylesheet src="main.css" />
    <asset:stylesheet src="grails.css" />
    <asset:stylesheet src="mobile.css" />
    <asset:stylesheet src="leadTask.css" />
    <g:set var="entityName" value="${message(code: 'task.label', default: 'Task')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>

    <!-- -->

    <!-- -->

</head>
<body>
<div class="container-fluid">

    <%
    def totalCount = olivetasks.Task.count
    def completedTasks = olivetasks.Task.all.findAll {it.status in ["Completed"]}
    def pendingTasks = olivetasks.Task.all.findAll {it.status in ["Not Started"]}
    def runningTasks = olivetasks.Task.all.findAll {it.status in ["In Progress"]}

    def completedCount = completedTasks.size()
    def runningCount = runningTasks.size()
    def pendingCount = totalCount - (completedCount + runningCount)

    %>

        <!-- Page Heading -->
        <div class="d-sm-flex align-items-center justify-content-between mb-4">
            <h1 class="h3 mb-0 text-gray-800">Dashboard</h1>
            <!--
            <a href="#" class="d-none d-sm-inline-block btn btn-sm btn-primary shadow-sm"><i class="fas fa-download fa-sm text-white-50"></i> Generate Report</a>
            -->
        </div>

        <!-- Content Row -->
        <div class="row">

            <!-- Total Card Example -->
            <div class="col-xl-3 col-md-6 mb-4">
                <div class="card border-left-primary shadow h-100 py-2">
                    <div class="card-body">
                        <div class="row no-gutters align-items-center">
                            <div class="col mr-2">
                                <a href="${createLink(uri: '/task/index')}">
                                    <div class="text-xs font-weight-bold text-primary text-uppercase mb-1">Total Tasks</div>
                                </a>
                                <div class="h5 mb-0 font-weight-bold text-gray-800">${totalCount}</div>
                            </div>
                            <div class="col-auto">
                                <i class="fas fa-clipboard-list fa-2x text-gray-300"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Running Tasks -->
            <div class="col-xl-3 col-md-6 mb-4">
                <div class="card border-left-success shadow h-100 py-2">
                    <div class="card-body">
                        <div class="row no-gutters align-items-center">
                            <div class="col mr-2">
                                <a href="#taskTable" onclick="getTasks(0,3);">
                                    <div class="text-xs font-weight-bold text-success text-uppercase mb-1">Completed</div>
                                </a>
                                <div class="h5 mb-0 font-weight-bold text-gray-800">${completedCount}</div>
                            </div>
                            <div class="col-auto">
                                <i class="fas fa-clipboard-check fa-2x text-gray-300"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Earnings (Monthly) Card Example -->
            <div class="col-xl-3 col-md-6 mb-4">
                <div class="card border-left-info shadow h-100 py-2">
                    <div class="card-body">
                        <div class="row no-gutters align-items-center">
                            <div class="col mr-2">
                                <a href="#taskTable" onclick="getTasks(0,2);">
                                <div class="text-xs font-weight-bold text-info text-uppercase mb-1">In Progress</div>
                                </a>
                                <div class="h5 mb-0 font-weight-bold text-gray-800">${runningCount}</div>
                            </div>
                            <div class="col-auto">
                                <i class="fas fa-clock fa-2x text-gray-300"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Pending Tasks -->
            <div class="col-xl-3 col-md-6 mb-4">
                <div class="card border-left-warning shadow h-100 py-2">
                    <div class="card-body">
                        <div class="row no-gutters align-items-center">
                            <div class="col mr-2">
                                <a href="#taskTable" onclick="getTasks(0,1);">
                                    <div class="text-xs font-weight-bold text-warning text-uppercase mb-1">Pending</div>
                                </a>
                                <div class="h5 mb-0 font-weight-bold text-gray-800">${pendingCount}</div>
                            </div>
                            <div class="col-auto">
                                <i class="fas fa-exclamation-triangle fa-2x text-gray-300"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    <g:if test="${flash.message}">
        <div >
            <div class="alert alert-info alert-dismissible fade show" role="alert">
                ${flash.message}
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
        </div>
    </g:if>

    <div class="row d-none d-lg-flex">


        <div class="col-lg-12">
            <h1 class="h3 mb-0 text-gray-800" style="text-align: center; padding-bottom: 7px;">Task Status</h1>
            <hr>
            <br>
        </div>
        <!--
                <div class="col-lg-6">
                    <h4 class="h5 mb-0 text-gray-800" style="text-align: center;padding-bottom: 15px;">Team Members</h4>
                </div>
        -->
    </div>

    <g:render template="taskTemplate" var="dept" collection="${olivetasks.Department.all.findAll {
        it.id > 1 && it.employees.size() > 0
    }}"></g:render>

    <!-- Pending Tasks -->
    <div id="taskTable" >
    </div>

</div>

<g:javascript library='jquery' plugin='jquery' />
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
<script>
$(document).ready(function(){
        //getTasks(0,1);
        //setInterval(function(){ updateAlerts(); }, 3000)
	});

	function getTasks(deptId,typeId) {
	    var URL="${createLink(controller:'admin',action:'getTasks')}";

            if(typeId == undefined) {
                typeId = 1;
            }
            if(deptId == undefined || deptId == 0) {
                deptId = null;
            }


                $.ajax({
                    url:URL,
                    data: {deptId : deptId, typeId: typeId},
                    dataType: "json",
                    type: "post",
                    success: function(resp){
                        console.log(resp);
                        var count = parseInt(resp.count);
                        var empCount = parseInt(resp.employeesCount);

                        var EmployeeName = '';

                        var cardClass = '';
                        if(typeId == 1) {
                            cardClass = 'border-left-warning';
                        }
                        else if(typeId == 2) {
                            cardClass = 'border-left-info';
                        }
                        else {
                            cardClass = 'border-left-success';
                        }

                        //console.log(EmployeeName);

                        var html = '<div class="card ' + cardClass + ' shadow mb-4">';
                        html += '<div class="card-header py-3">';
                        html += '<h6 class="m-0 font-weight-bold text-primary">';

                        if(deptId != null) {
                            html += resp.deptName + ' : ';
                        }

                        html += resp.type + ' Taks: ' + count + '</h6>';
                        html += '</div>';
                        html += '<div class="card-body">';
                        html += '<div class="myclass">';
                        html += '<div id="list-task" class="content scaffold-list" role="main">';
                        html += '<table>';
                        html += '<thead>';
                        html += '<tr>';
                        html += '<th>Task Name</th> <th>Type</th><th>Status</th> <th>Creation Date</th> <th>Start Date</th> <th>End Date</th> <th>Employee</th>';
                        html += '</tr></thead><tbody>';

                        for (i = 0; i < count; i++) {
                            if(i % 2 == 0) {
                                html += '<tr class="even">';
                            }
                            else {
                                html += '<tr class="odd">';
                            }
                            html += '<td><a href="/task/show/' + resp.tasks[i].id +'">' + resp.tasks[i].taskName + '</a></td>';
                            html += '<td>' + resp.tasks[i].type + '</td>';
                            html += '<td>' + resp.tasks[i].status + '</td>';
                            html += '<td>' + resp.tasks[i].creationDate + '</td>';
                            html += '<td>' + resp.tasks[i].startDate + '</td>';
                            html += '<td>' + resp.tasks[i].endDate + '</td>';

                            for(j=0; j<empCount; j++) {
                            if(resp.employees[j].id == resp.tasks[i].employee.id) {
                                EmployeeName = resp.employees[j].name
                            }
                        }

                            html += '<td><a href="/employee/show/' + resp.tasks[i].employee.id + '">' + EmployeeName + '</a></td>';
                            html += '</tr>';
                        }

                        html+= '</tbody></table></div></div></div></div>';


                        //console.log(html);

                        $("#taskTable").html(html);
                        window.location.hash = '#taskTable';


                    },
                    error: function() {
                        alert("Connection Lost!");
                    }

                });
	}
</script>

</body>
</html>

<!-- <f:table collection="${taskList}" properties="taskName, type, status, creationDate, startDate, endDate, employee" /> -->