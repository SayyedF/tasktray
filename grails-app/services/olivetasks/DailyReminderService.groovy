package olivetasks


import groovy.util.logging.Slf4j
import org.springframework.scheduling.annotation.Scheduled

@Slf4j
//@CompileStatic
class DailyReminderService {

    static lazyInit = false
    def taskReminderService

    @Scheduled(cron = "0 30 9 1/1 * ?")  //Everyday at 9:30 AM
    void execute() {
        log.info "DailyMailReminder Service running"
        taskReminderService.remind()
    }
}
