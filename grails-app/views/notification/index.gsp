<!DOCTYPE html>
<html xmlns:g="http://www.w3.org/1999/XSL/Transform" xmlns:f="http://www.w3.org/1999/xhtml">
    <head>
        <meta name="layout" content="main" />
        <asset:stylesheet src="main.css" />
        <asset:stylesheet src="grails.css" />
        <asset:stylesheet src="mobile.css" />
        <title>Alerts Center</title>
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

            <g:if test="${alertCount<=0}">
                <div class="card shadow mb-4">

                    <div class="card-body">
                        No Alerts!
                    </div>
                </div>
            </g:if>

            <g:render template="alertTemplate" var="alert" collection="${notificationList}" />

        </div>
    </div>
    </body>
</html>