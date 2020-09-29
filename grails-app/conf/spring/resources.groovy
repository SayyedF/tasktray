import olivetasks.CustomUserDetailsService
import olivetasks.EmployeePasswordEncoderListener
import olivetasks.FileUploadService
import olivetasks.MailedTaskService

// Place your Spring DSL code here
beans = {
    employeePasswordEncoderListener(EmployeePasswordEncoderListener)
    userDetailsService(CustomUserDetailsService)
    mailedTaskService(MailedTaskService)
    fileUploadService(FileUploadService)

}
