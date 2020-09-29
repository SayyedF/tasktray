package olivetasks

class Change {

    String action
    Date date

    Change(String action, Date date) {
        this.action = action
        this.date = date
    }
    static belongsTo = [task: Task]

    static constraints = {
        action()
        date()
    }

    static mapping = {
        sort id:"asc"
    }

    @Override
    String toString() {
        return date.toString() + ' : ' + action;
    }
}
