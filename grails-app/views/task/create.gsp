<!DOCTYPE html>
<html xmlns:g="http://www.w3.org/1999/XSL/Transform" xmlns:f="http://www.w3.org/2001/XMLSchema">
    <head>
        <meta name="layout" content="admin" />
        <asset:stylesheet src="main.css" />
        <asset:stylesheet src="grails.css" />
        <asset:stylesheet src="mobile.css" />
        <g:set var="entityName" value="${message(code: 'task.label', default: 'Task')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
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
    <div id="create-task" class="content scaffold-create" role="main">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
                <div class="alert alert-info alert-dismissible fade show" role="alert">
                    ${flash.message}
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            </g:if>
            <g:hasErrors bean="${this.task}">
                <ul class="errors" role="alert">
                    <g:eachError bean="${this.task}" var="error">
                        <li>
                            <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"
                            </g:if><g:message error="${error}"/>
                        </li>
                    </g:eachError>
                </ul>
            </g:hasErrors>

            <g:form class="user" resource="${this.task}" method="POST">
                <fieldset class="user">
                    <f:all bean="task" except="status, creationDate, endDate, timeSpent, userNote, updates, changes" class="form-control"/>
                </fieldset>

                <fieldset class="buttons">
                    <g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                </fieldset>
                <hr>
                <br>
            </g:form>
        </div>
        </div> </div>
    </body>
</html>
