<!DOCTYPE html>
<html xmlns:g="http://www.w3.org/1999/XSL/Transform">
<head>
    <meta name="layout" content="main" />
    <asset:stylesheet src="main.css" />
    <asset:stylesheet src="grails.css" />
    <asset:stylesheet src="mobile.css" />
    <g:set var="entityName" value="${message(code: 'employee.label', default: 'Employee')}" />
    <title>My Profile</title>
</head>
<body>
<div class="container">
    <div class="myclass">
<a href="#show-employee" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
<div class="nav" role="navigation">
    <ul>
        <li>
            <a href="javascript:history.back()"><i class="fas fa-arrow-circle-left"></i> Go Back</a>
        </li>
    </ul>
</div>
<div id="show-employee" class="content scaffold-show" role="main">
    <h1>My Profile</h1>
    <g:if test="${flash.message}">
        <div class="alert alert-info alert-dismissible fade show" role="alert">
            ${flash.message}
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </g:if>
    <f:display bean="employee" except="password, enabled, accountLocked, accountExpired, passwordExpired"/>
    <g:form controller="profile">
        <fieldset class="buttons">
            <g:link class="edit" action="edit">
                <g:message code="default.button.edit.label" default="Edit" />
            </g:link>
            <g:link class="edit" controller="profile" action="updatePassword">Change Password</g:link>
        </fieldset>                 <hr>
        <br>
    </g:form>
</div>
    </div>
</div>
</body>
</html>
