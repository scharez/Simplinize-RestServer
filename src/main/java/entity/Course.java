package entity;

import javax.persistence.*;
import java.util.Date;

@Entity
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
    private Date from;

    @Temporal(TemporalType.DATE)
    private Date to;
    private String place;

    @ManyToOne
    private SkiTeacher instructor;

    public Course () {}

    public Course(Date from, Date to, String place, SkiTeacher instructor) {
        this.from = from;
        this.to = to;
        this.place = place;
        this.instructor = instructor;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
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
}
