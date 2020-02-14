package entity;

import javax.persistence.*;

@Entity
@Table(name="\"GROUP\"")
@NamedQueries({
        @NamedQuery(name="Group.getAllGroups",
                query="SELECT g FROM Group g"),
        @NamedQuery(name="Group.getGroupById",
                query="SELECT g FROM Group g where g.id = :id"),
        @NamedQuery(name="Group.getGroupsByCourseID",
                query="SELECT g FROM Group g WHERE g.course.id = :id"),
        @NamedQuery(name="Group.getGroupsByTeacherId",
                query="SELECT g FROM Group g WHERE g.skiTeacher.id = :id"),
        @NamedQuery(name="Group.getGroupsByTeacherIdANDCourseId",
                query="SELECT g FROM Group g WHERE g.skiTeacher.id = :sId AND g.course.id = :cId")
})
public class Group {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;

    private int participants;

    private String startTime;

    @ManyToOne
    private Course course;

    @ManyToOne
    private SkiTeacher skiTeacher;

    @Enumerated(EnumType.STRING)
    private Proficiency proficiency;

    public Group() {}

    public Group(long id, String startTime, Course course, SkiTeacher skiTeacher) {
        this.id = id;
        this.startTime = startTime;
        this.course = course;
        this.skiTeacher = skiTeacher;
    }

    public Group(Proficiency proficiency, int participants) {
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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
