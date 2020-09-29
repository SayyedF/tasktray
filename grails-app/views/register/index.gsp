<html xmlns:g="http://www.w3.org/1999/xhtml">
<head>
    <meta name="layout" content="${gspLayout ?: 'loginLayout'}"/>
    <title>Register</title>
</head>

<body class="bg-gray-800">
<div class="row">
    <div class="col-sm-9 col-md-7 col-lg-5 mx-auto">
        <div class="card card-signin my-5">
            <div class="card-body">
                <h5 class="card-title text-center">Register</h5>
                <g:if test='${flash.message}'>
                    <div class="alert alert-danger" role="alert">${flash.message}</div>
                </g:if>
                <form class="form-signin" action="register" method="POST" id="loginForm" autocomplete="off" enctype="multipart/form-data">
                    <!--
                    <div class="form-group">
                        <label for="role">Role</label>
                        <g:select id="role" class="form-control" name="role.id"
                                  from="${olivetasks.Role.list()}"
                                  optionKey="id" />
                    </div>-->

                    <div class="form-group">
                        <label for="username">Username</label>
                        <input type="text" placeholder="Your username" class="form-control" name="username" id="username" autocapitalize="none" required/>
                    </div>

                    <div class="form-group">
                        <label for="password">Password</label>
                        <input type="password" placeholder="Your password" class="form-control" name="password" id="password" required/>
                    </div>

                    <div class="form-group">
                        <label for="password">Re-Enter Password</label>
                        <input type="password" placeholder="Re-enter password" class="form-control" name="repassword" id="repassword" required/>
                    </div>

                    <div class="form-group">
                        <label for="username">Full Name</label>
                        <input type="text" placeholder="Your full name" class="form-control" name="fullname" id="fullname" autocapitalize="none" required/>
                    </div>

                    <div class="form-group">
                        <label for="email">Email</label>
                        <input type="email" placeholder="Your email" class="form-control" name="email" id="email" autocapitalize="none" required/>
                    </div>

                    <div class="form-group">
                        <label for="dob">Date of Birth</label>
                        <input type="date" class="form-control" name="dob" id="dob" required/>
                    </div>

                    <div class="form-group">
                        <label for="companyName">Company Name</label>
                        <input type="text" placeholder="Company Name" class="form-control" name="companyName" id="companyName" required/>
                    </div>

                    <div class="form-group">
                        <label for="logo">Logo:</label>
                        <input
                                class="float-right mb-2"
                                dir="rtl"
                                style="border: none;"
                                type="file" name="logo" id="logo"
                                onchange="validate_fileupload(this.value);"
                        />
                    </div>

                    <button id="submit" class="btn btn-lg btn-secondary btn-block text-uppercase" type="submit" disabled>Register</button>
                    <hr class="my-4">
                    <p>Already have an account? <g:link controller="login" action="auth">Login</g:link></p>
                </form>
            </div>
        </div>
    </div>
</div>


<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>>
<script type="text/javascript">
        $(document).ready(function(){

        });
		document.addEventListener("DOMContentLoaded", function(event) {
			document.forms['loginForm'].elements['username'].focus();
		});

		function validate_fileupload(fileName)
        {
            var allowed_extensions = new Array("png"); //new Array("jpg","png","gif");
            var file_extension = fileName.split('.').pop().toLowerCase(); // split function will split the filename by dot(.), and pop function will pop the last element from the array which will give you the extension as well. If there will be no extension then it will return the filename.

            const fi = document.getElementById('logo');

            for(var i = 0; i <= allowed_extensions.length; i++)
            {
                if(allowed_extensions[i]==file_extension)
                {
                    if (fi.files.length > 0) {
                        for (const i = 0; i <= fi.files.length - 1; i++) {

                            const fsize = fi.files.item(i).size;
                            const file = Math.round((fsize / 1024));
                            // The size of the file.
                            if (file >= 512) {
                                alert(
                                  "File too Big, please select a file less than 256KB");
                            } else if (file < 30) {
                                alert(
                                  "File too small, please select a file greater than 30KB");
                            } else {
                                document.getElementById("submit").disabled = false;
                                return true; // valid file extension
                            }
                        }
                    }
                }
            }
            document.getElementById("submit").disabled = true;
            return false;
        }
</script>
</body>
</html>
