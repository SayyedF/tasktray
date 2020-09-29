<!DOCTYPE html>
<html xmlns:g="http://www.w3.org/1999/XSL/Transform" xmlns:f="http://www.w3.org/2001/XMLSchema">
    <head>
        <meta name="layout" content="main" />
        <asset:stylesheet src="main.css" />
        <asset:stylesheet src="grails.css" />
        <asset:stylesheet src="mobile.css" />
        <g:set var="entityName" value="${message(code: 'task.label', default: 'Task')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
    <div class="container">
        <div class="myclass">
        <a href="#create-task" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><g:link class="task-list" action="home"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
                <li>
                    <a href="javascript:history.back()"><i class="fas fa-arrow-circle-left"></i> Go Back</a>
                </li>
            </ul>
        </div>

            <br>

            <g:form controller="lead" action="saveUpdate"  method="POST">  <!-- resource="${this.task}" -->

                <div class="form-group">
                    <label for="update">Description</label>
                    <input type="text" placeholder="Write"  name="update" id="update" required/>
                </div>

                <g:hiddenField name="taskid" id="taskid" value="${taskid}" />

                <br>

                <fieldset class="buttons">
                    <g:submitButton name="create" class="save" value="Submit" />
                </fieldset>
                <hr>
                <br>
            </g:form>

        </div>
    </div>
    </body>
</html>
