package entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@NamedQueries({
        @NamedQuery(name="Course.lol",
                query=""),
})
public class Course {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;

    private Date from;
    private Date to;
    private String place;

    @ManyToOne
    private SkiTeacher instructor;

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
