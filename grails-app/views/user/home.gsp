<!DOCTYPE html>
<html xmlns:g="http://www.w3.org/1999/XSL/Transform" xmlns:f="http://www.w3.org/1999/xhtml">
<head>
    <meta name="layout" content="main" />
    <asset:stylesheet src="main.css" />
    <asset:stylesheet src="grails.css" />
    <asset:stylesheet src="mobile.css" />
    <g:set var="entityName" value="${message(code: 'task.label', default: 'Task')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>

<g:set var="springSecurityService" bean="springSecurityService"/>
<%
    def user = springSecurityService.getPrincipal().username
    def emp = olivetasks.Employee.findAllByUsername(user)

    def userId = emp?.id
    int uId = userId[0]

    def userAllTasks = olivetasks.Task.all.findAll {
        it.employee == emp
    }

    def totalCount = userAllTasks.size()

    def completedTasks = olivetasks.Task.all.findAll {it.status in ["Completed"] && it.employee?.id == uId}
    def pendingTasks = olivetasks.Task.all.findAll { it.employee?.id == uId && it.status in ["Not Started"] }
    def runningTasks = olivetasks.Task.all.findAll {it.status in ["In Progress"] && it.employee?.id == uId}

    def completedCount = completedTasks.size()
    def runningCount = runningTasks.size()
    def pendingCount = pendingTasks.size()

%>

<div class="container-fluid">
    <div class="myclass">
        <a href="#list-task" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><g:link class="create" action="create" controller="user"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
            </ul>
        </div>
        <br>

        <!-- Content Row -->
        <div class="row">

            <!-- Total Card Example -->
            <div class="col-xl-3 col-md-6 mb-4">
                <div class="card border-left-primary shadow h-100 py-2">
                    <div class="card-body">
                        <div class="row no-gutters align-items-center">
                            <div class="col mr-2">
                                <a href="#taskTable" onclick="getTasks(${userId},0);">
                                    <div class="text-xs font-weight-bold text-primary text-uppercase mb-1">Total Tasks</div>
                                </a>
                                <div class="h5 mb-0 font-weight-bold text-gray-800">${taskCount}</div>
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
                                <a href="#taskTable" onclick="getTasks(${userId},3);">
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
                                <a href="#taskTable" onclick="getTasks(${userId},2);">
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
                                <a href="#taskTable" onclick="getTasks(${userId},1);">
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
            <div>
                <div class="alert alert-info alert-dismissible fade show" role="alert">
                    ${flash.message}
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            </div>
        </g:if>

        <!--
        <div id="list-task" class="content scaffold-list" role="main">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
        
                <div class="alert alert-info alert-dismissible fade show" role="alert">
                    ${flash.message}
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            </g:if>
            <f:table collection="${taskList}" properties="taskName, type, status, creationDate, startDate, endDate"/>
        
            <div class="pagination">
                <g:paginate total="${taskCount ?: 0}" />
            </div>
        </div>
        -->
    </div>

    <div id="taskTable" >
    </div>
    
</div>

<g:javascript library='jquery' plugin='jquery' />
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
<script>
$(document).ready(function(){
        //var did = ${dId};
        //alert(did);
        getTasks(${uId},0);

	});

	function getTasks(userId,typeId) {
	    var URL="${createLink(controller:'admin',action:'getUserTasks')}";

            if(typeId == undefined) {
                typeId = 0;
            }
            if(userId == undefined || userId == 0) {
                userId = null;
            }


                $.ajax({
                    url:URL,
                    data: {userId : userId, typeId: typeId},
                    dataType: "json",
                    success: function(resp){
                        console.log(resp);
                        var count = parseInt(resp.count);

                        var EmployeeName = resp.EmployeeName;

                        var cardClass = '';
                        if(typeId == 1) {
                            cardClass = 'border-left-warning';
                        }
                        else if(typeId == 2) {
                            cardClass = 'border-left-info';
                        }
                        else if(typeId == 3) {
                            cardClass = 'border-left-success';
                        }
                        else {
                            cardClass = 'border-left-primary';
                        }

                        //console.log(EmployeeName);

                        var html = '<div class="card ' + cardClass + ' shadow mb-4">';
                        html += '<div class="card-header py-3">';
                        html += '<h6 class="m-0 font-weight-bold text-primary">';

                        if(userId != null) {
                            html += EmployeeName + ' : ';
                        }

                        html += resp.type + ' Taks: ' + count + '</h6>';
                        html += '</div>';
                        html += '<div class="card-body">';
                        html += '<div class="myclass">';
                        html += '<div id="list-task" class="content scaffold-list" role="main">';
                        html += '<table>';
                        html += '<thead>';
                        html += '<tr>';
                        html += '<th>Task Name</th> <th>Type</th><th>Status</th> <th>Creation Date</th> <th>Start Date</th> <th>End Date</th>';
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