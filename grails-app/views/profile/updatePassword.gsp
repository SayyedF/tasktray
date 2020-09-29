<!DOCTYPE html>
<html xmlns:g="http://www.w3.org/1999/XSL/Transform" xmlns:f="http://www.w3.org/2001/XMLSchema">
<head>
    <meta name="layout" content="main" />
    <asset:stylesheet src="main.css" />
    <asset:stylesheet src="grails.css" />
    <asset:stylesheet src="mobile.css" />
    <g:set var="entityName" value="${message(code: 'employee.label', default: 'Employee')}" />
    <title>Change Password</title>
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

<div class="row">
    <div class="col-sm-9 col-md-7 col-lg-5 mx-auto">
        <div class="card card-signin my-5">
            <div class="card-body">
                <h5 class="card-title text-center">Reset Password</h5>
                <g:if test='${flash.message}'>
                    <div class="alert alert-danger" role="alert">${flash.message}</div>
                </g:if>
                <form class="form-signin" action="changePassword" controller="profile" method="POST" id="resetForm" autocomplete="off">

                    <div class="form-group">
                        <label for="password1">Password</label>
                        <input type="password" placeholder="Your password" class="form-control" name="password1" id="password1" required/>
                    </div>

                    <div class="form-group">
                        <label for="password1">Re-Enter Password</label>
                        <input type="password" placeholder="Re-enter password" class="form-control" name="repassword1" id="repassword1" required/>
                    </div>

                    <g:hiddenField name="mode" id="mode" value="update" />

                    <button id="submit" class="btn btn-lg btn-secondary btn-block text-uppercase" type="submit">Reset</button>
                    <hr class="my-4">
                </form>
            </div>
        </div>
    </div>
</div>

    <script type="text/javascript">
		document.addEventListener("DOMContentLoaded", function(event) {
			document.forms['resetForm'].elements['password'].focus();
		});
	</script>
    </div>
</div>
</body>
</html>
