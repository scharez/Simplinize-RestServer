package entity;

import javax.persistence.*;

@Entity
@NamedQueries({
        @NamedQuery(name="Group.getAllGroups",
                query="SELECT g FROM Group g"),
        @NamedQuery(name="Group.getGroupById",
                query="SELECT g FROM Group g where g.id = :id"),
        @NamedQuery(name="Group.getGroupsByCourseID",
                query="SELECT g FROM Group g WHERE g.course.id = :id"),
})
public class Group {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;

    private int participants;

    @ManyToOne
    private Course course;

    @ManyToOne
    private SkiTeacher skiTeacher;

    @Enumerated(EnumType.STRING)
    private Proficiency proficiency;

    public Group() {}

    public Group(Course course, SkiTeacher skiTeacher, Proficiency proficiency, int participants) {
        this.course = course;
        this.skiTeacher = skiTeacher;
        this.proficiency = proficiency;
        this.participants = participants;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public SkiTeacher getSkiTeacher() {
        return skiTeacher;
    }

    public void setSkiTeacher(SkiTeacher skiTeacher) {
        this.skiTeacher = skiTeacher;
    }

    public Proficiency getProficiency() {
        return proficiency;
    }

    public void setProficiency(Proficiency proficiency) {
        this.proficiency = proficiency;
    }

    public int getParticipants() {
        return participants;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }
}
