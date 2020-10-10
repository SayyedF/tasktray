<!doctype html>
<html lang="en" class="no-js" xmlns:asset="http://www.w3.org/1999/xhtml" xmlns:g="http://www.w3.org/1999/XSL/Transform">
<head>
    <meta charset="utf-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">


    <title>
        <g:layoutTitle default="OliveTasks"/>
    </title>

    <asset:link rel="icon" href="favicon.png" type="image/png"/>

    <asset:stylesheet src="all.min.css" />
    <asset:stylesheet src="fontawesome-free/css/all.min.css" />
    <asset:stylesheet src="vendor/datatables/dataTables.bootstrap4.min.css"/>
    <asset:stylesheet href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i" />

    <!-- Custom styles for this template-->
    <asset:stylesheet src="sb-admin-2.min.css"/>
    <asset:stylesheet src="main.css" />
    <asset:stylesheet src="grails.css" />
    <asset:stylesheet src="mobile.css" />
    <asset:stylesheet src="leadTask.css" />

    <g:set var="secService" bean="springSecurityService"/>


    <g:layoutHead/>
</head>

<body id="page-top">


<!-- Page Wrapper -->
<div id="wrapper">

    <sec:ifAllGranted roles="ROLE_ADMIN">
        <!-- Sidebar -->
        <ul class="navbar-nav bg-gray-800 sidebar sidebar-dark accordion" id="accordionSidebar">
            <br>
            <!-- Sidebar - Brand -->
            <a class="sidebar-brand d-flex align-items-center justify-content-center" href="${createLink(uri: '/')}">
                <div class="sidebar-brand-icon">
                    <asset:image class="img-fluid" src="${olivetasks.Company.last(sort: 'id')?.logo}" width="200"/>
                    <!--<img class="img-fluid" src="/assets/olive-white.png" width="200"/> -->
                </div>

            </a>
            <br>

            <!-- Divider -->
            <hr class="sidebar-divider my-0">

            <!-- Nav Item - Dashboard -->
            <li class="nav-item active">
                <a class="nav-link" href="${createLink(uri: '/')}">
                    <i class="fas fa-fw fa-tachometer-alt"></i>
                    <span>Dashboard</span></a>
            </li>

            <!-- Divider -->
            <hr class="sidebar-divider">

            <!-- Heading -->
            <div class="sidebar-heading">
                Department
            </div>

            <!-- Nav Item - Pages Collapse Menu -->
            <li class="nav-item">
                <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapseTwo" aria-expanded="true" aria-controls="collapseTwo">
                    <!--<i class="fas fa-fw fa-cog"></i> -->
                    <i class="far fa-building"></i>
                    <span style="margin-left: 5px;">Departments</span>
                </a>
                <div id="collapseTwo" class="collapse" aria-labelledby="headingTwo" data-parent="#accordionSidebar">
                    <div class="bg-white py-2 collapse-inner rounded">
                        <h6 class="collapse-header">Departments:</h6>
                        <a class="collapse-item" href="${createLink(uri: '/department/index')}">List</a>
                        <a class="collapse-item" href="${createLink(uri: '/department/create')}">New</a>
                    </div>
                </div>
            </li>

            <!-- Nav Item - Utilities Collapse Menu -->
            <li class="nav-item">
                <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapseUtilities" aria-expanded="true" aria-controls="collapseUtilities">
                    <i class="fas fa-users"></i>
                    <span>Employees</span>
                </a>
                <div id="collapseUtilities" class="collapse" aria-labelledby="headingUtilities" data-parent="#accordionSidebar">
                    <div class="bg-white py-2 collapse-inner rounded">
                        <h6 class="collapse-header">Employees</h6>
                        <a class="collapse-item" href="${createLink(uri: '/employee/index')}">List</a>
                        <a class="collapse-item" href="${createLink(uri: '/employee/create')}">Add New Employee</a>
                    </div>
                </div>
            </li>

            <!-- Divider -->
            <hr class="sidebar-divider">

            <!-- Heading -->
            <div class="sidebar-heading">
                Tasks
            </div>

            <!-- Nav Item - All Tasks -->
            <li class="nav-item">
                <a class="nav-link" href="${createLink(uri: '/task/index')}">
                    <i class="fas fa-clipboard-list"></i>
                    <span style="margin-left: 3px;">All Tasks</span></a>
            </li>

            <!-- Nav Item - Tasks -->
            <li class="nav-item">
                <a class="nav-link" href="${createLink(uri: '/task/create')}">

                    <i class="fas fa-plus-circle"></i>
                    <span>New Task</span></a>
            </li>

            <!-- Divider -->
            <hr class="sidebar-divider">

            <!-- Heading -->
            <div class="sidebar-heading">
                Settings
            </div>

            <!-- Nav Item - Tasks -->
            <li class="nav-item">
                <a class="nav-link" href="${createLink(uri: '/admin/settings')}">

                    <i class="fas fa-cog"></i>
                    <span>Settings</span></a>
            </li>

            <!-- Divider -->
            <hr class="sidebar-divider d-none d-md-block">

            <!-- Sidebar Toggler (Sidebar) -->
            <div class="text-center d-none d-md-inline">
                <button class="rounded-circle border-0" id="sidebarToggle"></button>
            </div>


        </ul>
        <!-- End of Sidebar -->
    </sec:ifAllGranted>
    <sec:ifAllGranted roles="ROLE_LEAD">
        <!-- Sidebar -->
        <ul class="navbar-nav bg-gray-800 sidebar sidebar-dark accordion" id="accordionSidebar">

            <!-- Sidebar - Brand -->
            <br>
            <a class="sidebar-brand d-flex align-items-center justify-content-center" href="${createLink(uri: '/')}">
                <div class="sidebar-brand-icon">
                    <asset:image class="img-fluid" src="olive-white.png" width="200"/>
                </div>
                <!--<div class="sidebar-brand-text mx-3">OliveTasks</div>-->
            </a>
            <br>

            <!-- Divider -->
            <hr class="sidebar-divider my-0">

            <!-- Nav Item - Dashboard -->
            <li class="nav-item active">
                <a class="nav-link" href="${createLink(uri: '/')}">
                    <i class="fas fa-fw fa-tachometer-alt"></i>
                    <span>Dashboard</span></a>
            </li>

            <!-- Divider -->
            <hr class="sidebar-divider">

            <!-- Heading -->
            <div class="sidebar-heading">
                Tasks
            </div>

            <!-- Nav Item - Charts -->
            <li class="nav-item">
                <a class="nav-link" href="${createLink(uri: '/lead/alltasks')}">
                    <i class="fas fa-clipboard-list"></i>
                    <span style="margin-left: 3px;">My Tasks</span></a>
            </li>

            <!-- Nav Item - Tables -->
            <li class="nav-item">
                <a class="nav-link" href="${createLink(uri: '/lead/create')}">

                    <i class="fas fa-plus-circle"></i>
                    <span>New Task</span></a>
            </li>

            <!-- Divider -->
            <hr class="sidebar-divider d-none d-md-block">

            <!-- Sidebar Toggler (Sidebar) -->
            <div class="text-center d-none d-md-inline">
                <button class="rounded-circle border-0" id="sidebarToggle"></button>
            </div>

        </ul>
        <!-- End of Sidebar -->
    </sec:ifAllGranted>
    <sec:ifAllGranted roles="ROLE_USER">
        <!-- Sidebar -->
        <ul class="navbar-nav bg-gray-800 sidebar sidebar-dark accordion" id="accordionSidebar">

            <!-- Sidebar - Brand -->
            <br>
            <a class="sidebar-brand d-flex align-items-center justify-content-center" href="${createLink(uri: '/')}">
                <div class="sidebar-brand-icon">
                    <asset:image class="img-fluid" src="olive-white.png" width="200"/>
                </div>
                <!--<div class="sidebar-brand-text mx-3">OliveTasks</div>-->
            </a>
            <br>

            <!-- Divider -->
            <hr class="sidebar-divider my-0">

            <!-- Nav Item - Dashboard -->
            <li class="nav-item active">
                <a class="nav-link" href="${createLink(uri: '/')}">
                    <i class="fas fa-fw fa-tachometer-alt"></i>
                    <span>Dashboard</span></a>
            </li>

            <!-- Divider -->
            <hr class="sidebar-divider">

            <!-- Heading -->
            <div class="sidebar-heading">
                Tasks
            </div>

            <!-- Nav Item - Tables -->
            <li class="nav-item">
                <a class="nav-link" href="${createLink(uri: '/user/create')}">

                    <i class="fas fa-plus-circle"></i>
                    <span>New Task</span></a>
            </li>

            <!-- Divider -->
            <hr class="sidebar-divider d-none d-md-block">

            <!-- Sidebar Toggler (Sidebar) -->
            <div class="text-center d-none d-md-inline">
                <button class="rounded-circle border-0" id="sidebarToggle"></button>
            </div>

        </ul>
        <!-- End of Sidebar -->
    </sec:ifAllGranted>

    <!-- Content Wrapper -->
    <div id="content-wrapper" class="d-flex flex-column">

        <!-- Main Content -->
        <div id="content">

            <!-- Topbar -->
            <nav class="navbar navbar-expand navbar-light bg-white topbar mb-4 static-top shadow">

                <!-- Sidebar Toggle (Topbar) -->
                <button id="sidebarToggleTop" class="btn btn-link d-md-none rounded-circle mr-3">
                    <i class="fa fa-bars"></i>
                </button>

                <div class="heading">${olivetasks.Company.last(sort: 'id')?.companyName}</div>

                <!-- Topbar Navbar -->
                <ul class="navbar-nav ml-auto">

                    <sec:ifLoggedIn>

                        <%
                        def username = secService.getPrincipal().username
                        def List<olivetasks.Notification> notList = olivetasks.Notification.all.findAll {
                        it.receiver.username == username && !it.seen
                        }

                        def notCount = notList.size()
                        %>

                        <li class="nav-item dropdown no-arrow mx-1">
                            <a class="nav-link dropdown-toggle" href="#" onclick="location.reload()"><i class="fas fa-sync-alt fa-fw"></i></a>
                        </li>

                        <!-- Nav Item - Alerts -->
                        <li class="nav-item dropdown no-arrow mx-1">
                            <a class="nav-link dropdown-toggle" href="#" id="alertsDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                <i class="fas fa-bell fa-fw"></i>
                                <!-- Counter - Alerts -->
                                <span id="alertCounter" class="badge badge-danger badge-counter">${notCount}</span>
                            </a>
                            <!-- Dropdown - Alerts -->

                            <div class="dropdown-list dropdown-menu dropdown-menu-right shadow animated--grow-in" aria-labelledby="alertsDropdown">
                                <h6 class="dropdown-header">
                                    Alerts Center
                                </h6>

                                <div id="alertDiv"></div>
                                <!--<g:render template="/shared/AlertTemplate" var="alert" collection="${notList}" />-->


                                <a class="dropdown-item text-center small text-gray-500" href="${createLink(uri: '/notification/index')}">Show All Alerts</a>
                            </div>
                        </li>


                        <div class="topbar-divider d-none d-sm-block"></div>

                        <!-- Nav Item - User Information -->
                        <li class="nav-item dropdown ">
                            <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <span class="mr-2 d-none d-lg-inline text-gray-600 small">
                                <sec:loggedInUserInfo field='username'/>
                            </span>

                            </a>
                            <!-- Dropdown - User Information -->
                            <div class="dropdown-menu dropdown-menu-right shadow animated--grow-in" aria-labelledby="userDropdown">
                                <a class="dropdown-item" href="${createLink(uri: '/profile/index')}">
                                    <i class="fas fa-user fa-sm fa-fw mr-2 text-gray-400"></i>
                                    Profile
                                </a>
                                <div class="dropdown-divider"></div>

                                <a class="dropdown-item" href="#" data-toggle="modal" data-target="#logoutModal">
                                    <i class="fas fa-sign-out-alt fa-sm fa-fw mr-2 text-gray-400"></i>
                                    Logout
                                </a>
                            </div>
                        </li>

                    </sec:ifLoggedIn>

                </ul>

            </nav>
            <!-- End of Topbar -->



            <g:layoutBody/>



            <!-- Begin Page Content -->

            <!-- /.container-fluid -->

        </div>
        <!-- End of Main Content -->

    </div>
    <!-- End of Content Wrapper -->

</div>


<!-- End of Page Wrapper -->


<!-- Scroll to Top Button-->
<a class="scroll-to-top rounded" href="#page-top">
    <i class="fas fa-angle-up"></i>
</a>

<!-- Logout Modal-->
<div class="modal fade" id="logoutModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">Ready to Leave?</h5>
                <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">×</span>
                </button>
            </div>
            <div class="modal-body">Select "Logout" below if you are ready to end your current session.</div>
            <div class="modal-footer">
                <button class="btn btn-secondary" type="button" data-dismiss="modal">Cancel</button>
                <g:form controller="logout">
                    <g:submitButton class="btn btn-primary" name="Submit" value="Logout" />
                </g:form>
            </div>
        </div>
    </div>
</div>


<!-- Bootstrap core JavaScript-->
<asset:javascript src="jquery.min.js"></asset:javascript>
<asset:javascript src="bootstrap.bundle.min.js"></asset:javascript>

<!-- Core plugin JavaScript-->
<asset:javascript src="jquery.easing.min.js"></asset:javascript>

<!-- Custom scripts for all pages-->
<asset:javascript src="sb-admin-2.min.js"></asset:javascript>

<!-- Page level plugins -->
<asset:javascript src="Chart.min.js"></asset:javascript>

<!-- Page level custom scripts -->
<asset:javascript src="chart-area-demo.js"></asset:javascript>
<asset:javascript src="chart-pie-demo.js"></asset:javascript>
<!-- Footer -->
<footer class="sticky-footer bg-white">
    <div class="container my-auto">
        <div class="copyright text-center my-auto">
            <span>Copyright &copy; OliveSofts 2020</span>
        </div>
    </div>
</footer>
<!-- End of Footer -->

<g:javascript library='jquery' plugin='jquery' />
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
<script>
function updateAlerts(){
            var URL="${createLink(controller:'notification',action:'getAlerts')}";
            var id = 1;
                $.ajax({
                    url:URL,
                    data: {id: id},
                    dataType: "json",
                    type: "post",
                    success: function(resp){
                        //console.log(resp);
                        var count = parseInt(resp.count);
                        var html = '<div>';

                         for (i = 0; i < count; i++) {
                            var taskUrl = "${createLink(uri: '/task/show/')}" + resp.alerts[i].task.id

                            html += '<a class="dropdown-item d-flex align-items-center" href="' + taskUrl + '" xmlns:g="http://www.w3.org/1999/XSL/Transform">';

                            html += '<div class="mr-3"><div class="' + resp.alerts[i].divClass + '">';

                            html += '<i class="' + resp.alerts[i].iconClass + '"></i></div></div>';

                            html += '<div><div class="small text-gray-500">' + resp.alerts[i].date + '</div>';

                            html += '<span>' + resp.alerts[i].alert  + '</span>' + '</div></a>';

                        }

                        html+= '</div>';


                        //console.log(html);

                        $("#alertDiv").html(html);

                        var x = document.getElementById("alertCounter");
                        x.innerText = count;

                    },
                    error: function() {
                        //alert("Connection Lost!");
                    }

                });
        }
$(document).ready(function(){

        setInterval(function(){ updateAlerts(); }, 3000)
	});
</script>



</body>
</html>
