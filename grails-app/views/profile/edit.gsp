<!DOCTYPE html>
<html xmlns:g="http://www.w3.org/1999/XSL/Transform" xmlns:f="http://www.w3.org/2001/XMLSchema">
    <head>
        <meta name="layout" content="main" />
        <asset:stylesheet src="main.css" />
        <asset:stylesheet src="grails.css" />
        <asset:stylesheet src="mobile.css" />
        <g:set var="entityName" value="${message(code: 'employee.label', default: 'Employee')}" />
        <title>Edit Profile</title>
    </head>
    <body>
    <div class="container">
        <div class="myclass">
        <a href="#edit-employee" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li>
                    <a href="javascript:history.back()"><i class="fas fa-arrow-circle-left"></i> Go Back</a>
                </li>
            </ul>
        </div>
        <div id="edit-employee" class="content scaffold-edit" role="main">
            <h1>Edit Profile</h1>
            <g:if test="${flash.message}">
                <div class="alert alert-info alert-dismissible fade show" role="alert">
                    ${flash.message}
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            </g:if>
            <g:hasErrors bean="${this.employee}">
            <ul class="errors" role="alert">
                <g:eachError bean="${this.employee}" var="error">
                <li>
                    <g:if test="${error in org.springframework.validation.FieldError}">
                        data-field-id="${error.field}"
                    </g:if>
                    <g:message error="${error}"/>
                </li>
                </g:eachError>
            </ul>
            </g:hasErrors>
            <g:form resource="${this.employee}" method="PUT">
                <g:hiddenField name="version" value="${this.employee?.version}" />
                <g:hiddenField name="mode" id="mode" value="1" />
                <fieldset class="form">
                    <f:all bean="employee" except="username, password,position, department, tasks,
                     enabled, accountLocked, accountExpired, passwordExpired"/>
                </fieldset>
                <fieldset class="buttons">
                    <input class="save" type="submit" value="Update Profile" />
                </fieldset>                 <hr>
                <br>
            </g:form>
        </div>
        </div>
    </div>
    </body>
</html>
