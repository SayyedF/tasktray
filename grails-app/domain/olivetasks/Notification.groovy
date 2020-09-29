package olivetasks

class Notification {
    String alert
    Date date
    boolean seen = false
    String divClass = "icon-circle bg-primary"
    String iconClass = "fas fa-file-alt text-white"
    String fontClass = "" //"font-weight-bold"
    String url = "#"

    static belongsTo = [receiver: Employee, task: Task]
    static mapping = {
        sort date: "desc" // or "asc"
    }

    Notification() {}

    Notification(String alert, Date date, Employee employee) {
        this.alert = alert
        this.date = date
        this.receiver = employee
    }
    static constraints = {
        task(nullable: true, display: false)
    }

    @Override
    String toString() {
        return alert
    }
}
