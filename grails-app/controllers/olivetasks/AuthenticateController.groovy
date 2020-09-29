package olivetasks

import grails.plugin.springsecurity.annotation.Secured

@Secured('permitAll')
class AuthenticateController {

    def dataSource
    def springSecurityService

    @Secured(['ROLE_ADMIN','ROLE_LEAD','ROLE_USER'])
    def auth() {
        def username = springSecurityService.getPrincipal().username
        def user = Employee.findByUsername(username)

        //log.info "-------****------------User agent: " + request.getHeader("User-Agent")
        //log.info "Requested URI: " + request.forwardURI

        if(user==null) {
            redirect controller: "login", action: "auth"
            return
        }

        //CEO, Manager
        if (user.authorities.any { it.authority == "ROLE_ADMIN" }) {
            redirect action:"home", controller:"admin"
            return
        }

        //Lead
        if (user.authorities.any { it.authority == "ROLE_LEAD" }) {
            redirect action:"home", controller:"lead"
            return
        }

        //Developer
        if (user.authorities.any { it.authority == "ROLE_USER" }) {
            redirect action:"home", controller:"user"
            return
        }

        redirect controller: "login", action: "auth"
    }

    /*@Transactional
    @Secured(['ROLE_ADMIN','ROLE_LEAD','ROLE_USER'])
    def sampleUpdate() {
        def fullname = "SFJILANI"
        new Sql(dataSource).executeUpdate('UPDATE Employee set fullname=?', [fullname])
        render 'Updated Successfully'
    }*/
}
