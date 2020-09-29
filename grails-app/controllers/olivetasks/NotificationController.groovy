package olivetasks

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured('permitAll')
class NotificationController {

    static int c = 0

    def springSecurityService
    def user
    List<Notification> notificationList

    def index() {
        user = springSecurityService.getPrincipal().username
        //def emp = Employee.findByUsername(user)

        notificationList = Notification.all.findAll {
            it.receiver.username == user && !it.seen
        }

        model: [notificationList: notificationList, alertCount: notificationList.size()]
    }

    def getAlerts() {
        user = springSecurityService.getPrincipal().username
        def notCount = 0
        notificationList = null

        if(user!=null) {
            notificationList = Notification.all.findAll {
                it.receiver.username == user && !it.seen
            }

            notCount = notificationList.size()
        }

        def responseData = [
                'alerts': notificationList,
                'count' : notCount
        ]
        c++;
        def infoo = "---------------------getAlerts called : " + c + " ----------------------"
        //log.info(infoo)
        render responseData as JSON
    }


}
