package entity;

import javax.persistence.*;

@Entity
@NamedQueries({
        @NamedQuery(name="CourseParticipation.getFromCouresId",
                query="SELECT p FROM CourseParticipation p WHERE p.course.id = :courseId"),

})
public class CourseParticipation {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;

    @ManyToOne
    private Student student;

    @ManyToOne // Das ist die einzahlende Person, kann SkiTeacher oder ContactPerson sein
    private Person person;

    @ManyToOne
    private Course course;

    //Bezahlt oder nicht
    private boolean deposit;

    private String drivingCanFromRegistration;
    private boolean waiting;

    public CourseParticipation() { }

    public CourseParticipation(Student student, Person person, Course course, boolean deposit, String drivingCanFromRegistration, boolean waiting) {
        this.student = student;
        this.person = person;
        this.course = course;
        this.deposit = deposit;
        this.drivingCanFromRegistration = drivingCanFromRegistration;
        this.waiting = waiting;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public boolean isDeposit() {
        return deposit;
    }

    public void setDeposit(boolean deposit) {
        this.deposit = deposit;
    }

    public String getdrivingCanFromRegistration() {
        return drivingCanFromRegistration;
    }

    public void setdrivingCanFromRegistration(String drivingCanFromRegistration) {
        this.drivingCanFromRegistration = drivingCanFromRegistration;
    }

    public boolean isWaiting() {
        return waiting;
    }

    public void setWaiting(boolean waiting) {
        this.waiting = waiting;
    }

}
