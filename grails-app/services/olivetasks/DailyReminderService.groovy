package olivetasks


import groovy.util.logging.Slf4j
import org.springframework.scheduling.annotation.Scheduled

@Slf4j
//@CompileStatic
class DailyReminderService {

    static lazyInit = false
    def taskReminderService

    //TODO: Change cron dynamically according to the time set by the User
    @Scheduled(cron = "0 30 9 * * ?")  //Everyday at 9:30 AM
    void execute() {
        log.info "DailyMailReminder Service running"
        taskReminderService.remind()
    }
}
