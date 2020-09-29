package olivetasks

import groovy.transform.Sortable

@Sortable(includes = ['day'])
class DailyUpdate implements Comparable<DailyUpdate> {
    Date day
    String update

    DailyUpdate(Date day, String update) {
        this.day = day
        this.update = update
    }
    static belongsTo = [task: Task]

    static constraints = {
    }

    /*static mapping = {
        sort day: "desc"
    }*/



    @Override
    String toString() {
        return day.toString() + " : " + update
    }

    @Override
    int compareTo(DailyUpdate o) {
        return this.day.compareTo(o.day)
    }
}
