<html xmlns:g="http://www.w3.org/1999/XSL/Transform">
<head>
    <!-- <meta name="layout" content="${gspLayout ?: 'main'}"/> -->
    <meta name="layout" content="loginLayout" />
    <title><g:message code='springSecurity.login.title'/></title>
    <style>
         .inputWithIcon {
            position: relative;

        }
        .inputWithIcon i{
            position: absolute;
            top: 17px;
            right: 5px;
            color : #777777;
            cursor : pointer;

        }

   </style>


</head>

<body>
<div class="row">
    <div class="col-sm-9 col-md-7 col-lg-5 mx-auto">
        <div class="card card-signin my-5">
            <div class="card-body">
                <h5 class="card-title text-center">Welcome Back!</h5>
                <g:if test='${flash.message}'>
                    <div class="alert alert-danger" role="alert">${flash.message}</div>
                </g:if>
                <form class="form-signin" action="${postUrl ?: '/login/authenticate'}" method="POST" id="loginForm" autocomplete="off">
                    <div class="form-group">
                        <label for="username">Username</label>
                        <input type="text" class="form-control" name="${usernameParameter ?: 'username'}" id="username" autocapitalize="none"/>
                    </div>

                    <div class="form-group">
                        <div class="inputWithIcon">
                            <label for="password">Password</label>
                            <input type="password" class="form-control" name="${passwordParameter ?: 'password'}" id="password"/>
                            <i id="passwordToggler" title="toggle password display" onclick="passwordDisplayToggle()"> <i class="fas fa-eye"></i></i>
                        </div>

                    </div>

                    <div class="form-group form-check">
                        <label class="form-check-label">
                            <input type="checkbox" class="form-check-input" name="${rememberMeParameter ?: 'remember-me'}" id="remember_me" <g:if test='${hasCookie}'>checked="checked"</g:if>/> Remember me
                        </label>
                    </div>
                    <button id="submit" class="btn btn-lg btn-secondary btn-block text-uppercase" type="submit">Sign in</button>

                    <hr class="my-4">
                    <p><g:link controller="profile" action="lost">Forgot Password?</g:link></p>

                    <g:if test='${olivetasks.Employee.count == 0}'>
                        <div>
                            <hr class="my-4">
                            <p>Don't have an account? <g:link controller="register">Register</g:link></p>
                        </div>
                    </g:if>
                </form>
            </div>
        </div>
    </div>

</div>
<script type="text/javascript">
        document.addEventListener("DOMContentLoaded", function(event) {
            document.forms['loginForm'].elements['username'].focus();
        });
        function passwordDisplayToggle() {
            var toggleEl = document.getElementById("passwordToggler");
            var eyeIcon = ' <i class="fas fa-eye"></i>';
            var xIcon = ' <i class="fas fa-eye-slash"></i>';
            var passEl = document.getElementById("password");
            if (passEl.type === "password") {
                toggleEl.innerHTML = xIcon;
                passEl.type = "text";
            } else {
                toggleEl.innerHTML = eyeIcon;
                passEl.type = "password";
            }
        }
    </script>
</body>
</html>