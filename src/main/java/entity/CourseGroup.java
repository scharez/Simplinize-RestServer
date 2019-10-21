package entity;

import javax.persistence.*;

@Entity
@NamedQueries({
        @NamedQuery(name="CourseGroup.getAllGroups",
                query="SELECT g FROM CourseGroup g"),
        @NamedQuery(name="CourseGroup.getCourseGroups",
                query="SELECT g FROM CourseGroup g WHERE g.course.id = :id"),
})
public class CourseGroup {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;

    @ManyToOne
    private Course course;

    @ManyToOne
    private SkiTeacher skiTeacher;

    @Enumerated(EnumType.STRING)
    private Proficiency proficiency;

    public CourseGroup() {}

    public CourseGroup(Course course, SkiTeacher skiTeacher, Proficiency proficiency) {
        this.course = course;
        this.skiTeacher = skiTeacher;
        this.proficiency = proficiency;
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
}
