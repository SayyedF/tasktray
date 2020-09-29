package olivetasks

class Task {
    String taskName
    String description
    String userNote
    Date creationDate
    Date startDate
    String timeSpent
    Date deadline
    Date endDate
    String type
    //String category
    String status
    int assignee

    Task() {}

    Task(String taskName, Date creationDate, Date deadline, String type, String status, Employee employee, int assignee) {
        this.taskName = taskName
        this.creationDate = creationDate
        this.deadline = deadline
        this.type = type
        this.status = status
        this.employee = employee
        this.assignee = assignee
    }

    static belongsTo = [employee: Employee]
    static hasMany = [updates: DailyUpdate, alerts: Notification, changes: Change]

    static mapping = {
        sort creationDate: "desc" // or "asc"
    }

    static constraints = {
        taskName()
        description(nullable: true)
        type(inList: ["Admin Assigned","Self Assigned", "Custom Assigned"])
        //category(inList: ["Front End", "Back End","Database","Full Stack","Power BI","Documentation","Other"])
        status(inList: ["Not Started","In Progress", "On Hault", "Aborted","Overdue", "Completed"], nullable: true,
                default: "In Progress")
        startDate(display:false, nullable: true)
        deadline()
        endDate(nullable:true)
        timeSpent(nullable:true)
        creationDate(nullable: true)
        userNote(nullable: true)
        employee()
        updates(nullable: true)
        assignee(nullable: true, display: false)
        alerts(nullable: true, display: false)
        changes(nullable: true)
    }

    @Override
    String toString() {
        return taskName
    }
}
