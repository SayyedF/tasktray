<!DOCTYPE html>
<html xmlns:g="http://www.w3.org/1999/XSL/Transform" xmlns:f="http://www.w3.org/1999/xhtml">
    <head>
        <meta name="layout" content="admin" />
        <asset:stylesheet src="main.css" />
        <asset:stylesheet src="grails.css" />
        <asset:stylesheet src="mobile.css" />
        <g:set var="entityName" value="${message(code: 'task.label', default: 'Task')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
    <div class="container">
        <div class="myclass">
            <div class="nav" role="navigation">
                <ul>
                    <li>
                        <a href="javascript:history.back()"><i class="fas fa-arrow-circle-left"></i> Go Back</a>
                    </li>
                </ul>
            </div>
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
            <f:table collection="${taskList}" properties="taskName, type, status, creationDate, startDate, endDate, employee"/>

            <div class="pagination">
                <g:paginate total="${taskCount ?: 0}" />
            </div>
        </div>
    </div>
    </div>
    </body>
</html>