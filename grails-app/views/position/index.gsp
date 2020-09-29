<!DOCTYPE html>
<html xmlns:asset="http://www.w3.org/1999/XSL/Transform" xmlns:f="http://www.w3.org/1999/xhtml">
    <head>
        <meta name="layout" content="main" />
        <asset:stylesheet src="main.css" />
        <asset:stylesheet src="grails.css" />
        <asset:stylesheet src="mobile.css" />
        <g:set var="entityName" value="${message(code: 'position.label', default: 'Position')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
    <div class="container">
        <div class="myclass">
        <a href="#list-position" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
                <li>
                    <a href="javascript:history.back()"><i class="fas fa-arrow-circle-left"></i> Go Back</a>
                </li>
            </ul>
        </div>
        <div id="list-position" class="content scaffold-list" role="main">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
                <div class="alert alert-info alert-dismissible fade show" role="alert">
                    ${flash.message}
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            </g:if>
            <f:table collection="${positionList}" />

            <div class="pagination">
                <g:paginate total="${positionCount ?: 0}" />
            </div>
        </div>
        </div>
    </div>
    </body>
</html>