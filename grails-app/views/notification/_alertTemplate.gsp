<!DOCTYPE html>
<html xmlns:g="http://www.w3.org/1999/XSL/Transform" xmlns:f="http://www.w3.org/1999/xhtml">
<body>
<div>

    <!-- Basic Card Example -->
    <div class="card shadow mb-4">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">${alert?.date}</h6>
        </div>
        <div class="card-body">
            <a href="${createLink(uri: '/task/show/'+alert?.task?.id)}">${alert?.alert}</a>
        </div>
    </div>

</div>


</body>
</html>