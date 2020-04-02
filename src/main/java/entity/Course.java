package entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="\"COURSE\"")
@NamedQueries({
        @NamedQuery(name="Course.getCourse",
                query="SELECT c FROM Course c"),
        @NamedQuery(name="Course.getCourseById",
                query="SELECT c FROM Course c where c.id = :id")
})
public class Course {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;

    @Temporal(TemporalType.DATE)
    private Date _from;

    @Temporal(TemporalType.DATE)
    private Date _to;

    @Temporal(TemporalType.DATE)
    private Date assigned = new Date();

    private String place;

    @ManyToOne
    private SkiTeacher instructor;

    private boolean finished;

    public Course() {
    }

    public Course(Date from, Date to, String place, SkiTeacher instructor) {
        this._from = from;
        this._to = to;
        this.place = place;
        this.instructor = instructor;
    }

    public Course(long id, Date from, Date to, Date assigned,String place, SkiTeacher instructor, boolean finished) {
        this.id = id;
        this._from = from;
        this._to = to;
        this.place = place;
        this.instructor = instructor;
        this.finished = finished;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getFrom() {
        return _from;
    }

    public void setFrom(Date from) {
        this._from = from;
    }

    public Date getTo() {
        return _to;
    }

    public void setTo(Date to) {
        this._to = to;
    }

    public Date getAssigned() {
        return assigned;
    }

    public void setAssigned(Date assigned) {
        this.assigned = assigned;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public SkiTeacher getInstructor() {
        return instructor;
    }

    public void setInstructor(SkiTeacher instructor) {
        this.instructor = instructor;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
