<!DOCTYPE html>
<html xmlns:g="http://www.w3.org/1999/XSL/Transform" xmlns:f="http://www.w3.org/1999/xhtml">
<head>
    <meta name="layout" content="main" />
    <asset:stylesheet src="main.css" />
    <asset:stylesheet src="grails.css" />
    <asset:stylesheet src="mobile.css" />

    <title>App Settings</title>
</head>
<body>
<div class="container">
    <div class="myclass">

        <div class="nav" role="navigation">
            <ul>
                <li><a data-toggle="modal" data-target="#nameModal">
                        <i class="fas fa-pen"></i>
                        Edit Company Name
                    </a>
                </li>
                <li>
                    <a data-toggle="modal" data-target="#logoModal">
                        <i class="fas fa-edit"></i>
                        Change Logo
                    </a>
                </li>

                <li>
                    <a href="javascript:history.back()"><i class="fas fa-arrow-circle-left"></i> Go Back</a>
                </li>

            </ul>
        </div>
        <br>
        <g:if test="${flash.message}">
            <divs>
                <div class="alert alert-info alert-dismissible fade show" role="alert">
                    ${flash.message}
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            </divs>
        </g:if>

        <!-- Mail Monitor Service -->
        <div class="card shadow mb-4">
            <div class="card-header py-3">
                <h6 class="m-0 font-weight-bold text-primary">Mail Monitor
                    <a href="" data-toggle="modal" data-target="#mailMonitorModal">
                        <i class="fa fa-cog float-right" style="cursor : pointer;"
                           title="Configure Mail Monior">

                        </i>
                    </a>
                </h6>
            </div>
            <div class="card-body" >
                <div id="mailMonitor">
                </div>
            </div>
        </div>

        <!-- Mail Service -->
        <div class="card shadow mb-4">
            <div class="card-header py-3">
                <h6 class="m-0 font-weight-bold text-primary">Mail Notification Service
                    <a href="" data-toggle="modal" data-target="#mailNotifierModal">
                        <i class="fa fa-cog float-right" style="cursor : pointer;"
                           title="Configure Notification Service">

                        </i>
                    </a>
                </h6>
            </div>
            <div class="card-body" >
                <div id="mailNotifier">
                </div>
            </div>
        </div>

    </div>
</div>


<div class="modal fade" id="mailMonitorModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">Mail Monitor Service Settings</h5>
                    <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">×</span>
                    </button>
            </div>
            <g:form controller="admin" action="saveMonitorConfig">
                <div class="modal-body">


                    <div class="form-group">
                        <label for="username">Username:</label>
                        <input type="text" class="form-control" name="username" id="username" value="${monitorConfig?.username}" required/>
                    </div>

                    <div class="form-group">
                        <label for="password">Password:</label>
                        <input type="text" class="form-control" name="password" id="password" value="${monitorConfig?.password}" required/>
                    </div>

                    <div class="form-group">
                        <label for="host">Host:</label>
                        <input type="text" class="form-control" name="host" id="host" value="${monitorConfig?.host}" required/>
                    </div>

                    <div class="form-group">
                        <label for="port">Port:</label>
                        <input type="text" class="form-control" name="port" id="port" value="${monitorConfig?.port}" required/>
                    </div>

                </div>
                <div class="modal-footer">
                    <button class="btn btn-secondary" type="button" data-dismiss="modal">Cancel</button>
                    <g:submitButton class="btn btn-primary" name="Submit" value="Save Changes" />
                </div>
            </g:form>
        </div>
    </div>
</div>

<div class="modal fade" id="mailNotifierModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Mail Notification Service Settings</h5>
                <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">×</span>
                </button>
            </div>
            <g:form controller="admin" action="saveMailConfig">
                <div class="modal-body">

                    <div class="form-group">
                        <label for="senderName">Sender Name:</label>
                        <input type="text" class="form-control" name="senderName" id="senderName" value="${mailConfig?.senderName}" required/>
                    </div>

                    <div class="form-group">
                        <label for="username1">Username:</label>
                        <input type="text" class="form-control" name="username" id="username1" value="${mailConfig?.username}" required/>
                    </div>

                    <div class="form-group">
                        <label for="password1">Password:</label>
                        <input type="text" class="form-control" name="password" id="password1" value="${mailConfig?.password}" required/>
                    </div>

                    <div class="form-group">
                        <label for="host1">Host:</label>
                        <input type="text" class="form-control" name="host" id="host1" value="${mailConfig?.host}" required/>
                    </div>

                    <div class="form-group">
                        <label for="port1">Port:</label>
                        <input type="text" class="form-control" name="port" id="port1" value="${mailConfig?.port}" required/>
                    </div>

                </div>
                <div class="modal-footer">
                    <button class="btn btn-secondary" type="button" data-dismiss="modal">Cancel</button>
                    <g:submitButton class="btn btn-primary" name="Submit" value="Save Changes" />
                </div>
            </g:form>
        </div>
    </div>
</div>

<div class="modal fade" id="nameModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="nameModalLabel">Update Company Details</h5>
                <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">×</span>
                </button>
            </div>
            <g:form controller="admin" action="updateName">
                <div class="modal-body">

                    <div class="form-group">
                        <label for="companyName">Company Name:</label>
                        <input type="text" class="form-control" name="companyName" id="companyName" value="${company?.companyName}" required/>
                    </div>

                </div>
                <div class="modal-footer">
                    <button class="btn btn-secondary" type="button" data-dismiss="modal">Cancel</button>
                    <g:submitButton class="btn btn-primary" name="Submit" value="Save Changes" />
                </div>
            </g:form>
        </div>
    </div>
</div>

<div class="modal fade" id="logoModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="logoModalLabel">Update Company Details</h5>
                <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">×</span>
                </button>
            </div>
            <g:form controller="admin" action="updateLogo" method="POST" enctype="multipart/form-data">
                <div class="modal-body">

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

                </div>
                <div class="modal-footer">
                    <button class="btn btn-secondary" type="button" data-dismiss="modal">Cancel</button>
                    <button class="btn btn-primary" id="submit" name="Submit" value="Save Changes" disabled> Save Changes </button>
                </div>
            </g:form>
        </div>
    </div>
</div>

<g:javascript library='jquery' plugin='jquery' />
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
<script>

    $(document).ready(function(){
        getStatus();
	});

	function startMonitor() {
	    var URL="${createLink(controller:'admin',action:'runMonitor')}";

                $.ajax({
                    url:URL,
                    data: {},
                    dataType: "json",
                    method: "post",
                    success: function(resp){
                        console.log(resp);
                        var html = '<div>';
                        html += 'Status: Running <br>';
                        html += '<a href="#"' + 'onClick="stopMonitor()">' + 'Stop Monitor</a>';
                        html += '</div>';

                        $("#mailMonitor").html(html);
                    },
                    error: function() {
                        //alert("Connection Lost!");
                    }

                });
	}

	function stopMonitor() {
	    var URL="${createLink(controller:'admin',action:'stopMonitor')}";

                $.ajax({
                    url:URL,
                    data: {},
                    dataType: "json",
                    method: "post",
                    success: function(resp){
                        console.log(resp);
                        var html = '<div>';
                        html += 'Status: Stopped <br>';
                        html += '<a href="#"' + 'onClick="startMonitor()">' + 'Start Monitor</a>';
                        html += '</div>';

                        $("#mailMonitor").html(html);
                    },
                    error: function() {
                        //alert("Connection Lost!");
                    }

                });
	}

	function getStatus() {
	    var URL="${createLink(controller:'admin',action:'getMonitorStatus')}";

                $.ajax({
                    url:URL,
                    data: {},
                    dataType: "json",
                    type: "post",
                    success: function(resp){
                        console.log(resp);
                        var html = '<div>';

                        var html2 = '<div>';

                        if(resp.MailConfig > 0) {
                            html2 += 'Status: Running'
                        }
                        else {
                            html2 += 'Mail Notification Service is not configured. Click on gear icon to configure it.'
                        }
                        html2 += '</div>';
                        $("#mailNotifier").html(html2);

                        if(resp.MonitorConfig > 0) {
                            if(resp.Status) {

                                html += 'Status: Running <br>';
                                html += '<a href="#"' + 'onClick="stopMonitor()">' + 'Stop Monitor</a>';

                            }
                            else {
                                     html += 'Status: Stopped <br>';
                                     html += '<a href="#"' + 'onClick="startMonitor()">' + 'Start Monitor</a>';
                            }
                        }
                        else {
                            html += 'Mail Monitor is not configured. Click on gear icon to configure it.'
                        }

                        html += '</div>';

                        $("#mailMonitor").html(html);
                        //window.location.hash = '#mailMonitor';

                    },
                    error: function() {
                        var html = '<div>';
                        html += 'Status: Unknown --- Check your Internet Connectivity';
                        html += '</div>';

                        $("#mailMonitor").html(html);
                        //window.location.hash = '#mailMonitor';
                    }

                });
	}

	function configureMailMonitor() {
	    alert("Will add config options later");
	}

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