<!DOCTYPE html>
<html xmlns:g="http://www.w3.org/1999/XSL/Transform" xmlns:f="http://www.w3.org/2001/XMLSchema">
    <head>
        <meta name="layout" content="main" />
        <asset:stylesheet src="main.css" />
        <asset:stylesheet src="grails.css" />
        <asset:stylesheet src="mobile.css" />
        <g:set var="entityName" value="${message(code: 'task.label', default: 'Task')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
    <div class="container">
        <div class="myclass">
        <a href="#edit-task" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><g:link class="task-list" action="home"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
                <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
                <li>
                    <a href="javascript:history.back()"><i class="fas fa-arrow-circle-left"></i> Go Back</a>
                </li>
            </ul>
        </div>
        <div id="edit-task" class="content scaffold-edit" role="main">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
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
                <li> <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if><g:message error="${error}"/></li>
                </g:eachError>
            </ul>
            </g:hasErrors>
            <g:form resource="${this.task}" method="PUT">
                <g:hiddenField name="version" value="${this.task?.version}" />
                <fieldset class="form">
                    <f:all bean="task" except="status, creationDate, endDate, timeSpent, employee, updates, changes"/>
                </fieldset>
                <fieldset class="buttons">
                    <input class="save" type="submit" value="${message(code: 'default.button.update.label', default: 'Update')}" />
                </fieldset>
                <hr>
                <br>
            </g:form>
        </div>
        </div>
    </div>
    </body>
</html>
