<!DOCTYPE html>
<html xmlns:g="http://www.w3.org/1999/XSL/Transform">
    <head>
        <meta name="layout" content="admin" />
        <asset:stylesheet src="main.css" />
        <asset:stylesheet src="grails.css" />
        <asset:stylesheet src="mobile.css" />
        <g:set var="entityName" value="${message(code: 'task.label', default: 'Task')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
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
        <div id="show-task" class="content scaffold-show" role="main">
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
                <div class="alert alert-info alert-dismissible fade show" role="alert">
                    ${flash.message}
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            </g:if>



            <f:display bean="task" except="changes"/>
            <g:form resource="${this.task}" method="DELETE">
                <fieldset class="buttons">
                    <g:link class="edit" action="edit" resource="${this.task}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
                    <g:link class="delete" data-toggle="modal" data-target="#deleteModal" >Delete</g:link>
<!--                    <input class="delete" type="submit" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
-->
                    <g:if test="${mode== 'Start'}">
                        <g:link class="taskstart" action="start" id="${this.task.id}"
                                onclick="return confirm('${message(code: 'default.button.delete.confirm.message',
                        default: 'Are you sure to start this task?')}');">
                            Start
                        </g:link> <!-- <g:message code="default.button.edit.label" default="Edit" /> -->
                    </g:if>

                    <g:if test="${ mode== 'Restart'}">
                        <g:link class="taskstart" action="start" id="${this.task.id}"
                                onclick="return confirm('${message(code: 'default.button.delete.confirm.message',
                        default: 'Are you sure to start this task?')}');">
                            Restart
                        </g:link> <!-- <g:message code="default.button.edit.label" default="Edit" /> -->
                    </g:if>

                    <g:if test="${mode== 'End'}">
                        <g:link class="taskstop" action="end" id="${this.task.id}"
                                onclick="return confirm('${message(code: 'default.button.delete.confirm.message',
                        default: 'Are you sure to End this task?')}');">
                            End
                        </g:link>

                        <g:link class="save" action="createUpdate" id="${this.task.id}">
                            Give an Update
                        </g:link>
                    </g:if>

                </fieldset>
                <hr>
                <br>
            </g:form>
        </div> </div>
    </div>

    <!-- Delete Modal -->
    <div class="modal fade" id="deleteModal" tabindex="-1" role="dialog" aria-labelledby="deleteModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="deleteModalLabel">Delete Task</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    Are you sure? This action cannot be undone.
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                    <g:form resource="${this.task}" method="DELETE">
                        <g:submitButton class="btn btn-primary" name="Submit" value="Delete" />
                    </g:form>
                </div>
            </div>
        </div>
    </div>

    </body>

</html>
