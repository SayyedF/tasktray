<html xmlns:g="http://www.w3.org/1999/xhtml">
<head>
    <meta name="layout" content="loginLayout"/>
    <title>Password Reset</title>
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
    </div>
</div>
<div class="row">
    <div class="col-sm-9 col-md-7 col-lg-5 mx-auto">
        <div class="card card-signin my-5">
            <div class="card-body">
                <h5 class="card-title text-center">Password Reset</h5>
                <g:if test='${flash.message}'>
                    <div class="alert alert-danger" role="alert">${flash.message}</div>
                </g:if>
                <form class="form-signin" action="reset" method="POST" id="loginForm" autocomplete="off">

                    <div class="form-group">
                        <label for="username">Username</label>
                        <input type="text" placeholder="Your username" class="form-control" name="username" id="username" autocapitalize="none"/>
                    </div>

                    <div class="form-group">
                        <label for="dob">Date of Birth</label>
                        <input type="date" class="form-control" name="dob" id="dob"/>
                    </div>

                    <button id="submit" class="btn btn-lg btn-secondary btn-block text-uppercase" type="submit">Proceed</button>
                    <hr class="my-4">
                </form>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
		document.addEventListener("DOMContentLoaded", function(event) {
			document.forms['loginForm'].elements['username'].focus();
		});
	</script>
</body>
</html>
