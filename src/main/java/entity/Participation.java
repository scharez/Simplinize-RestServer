package entity;

import javax.persistence.*;

@Entity
@NamedQueries({

})
public class Participation {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;

    @ManyToOne
    Student student;

    @ManyToOne // Das ist die einzahlende Person, kann SkiTeacher oder ContactPerson sein
    Person person;

    @ManyToOne
    Course course;

    private boolean registration;
    private boolean deposit;
    private String rank;
    private double time;
    private double drivingCan;
    private boolean waiting;

    public Participation() { }

    public Participation(Student student, Person person, Course course, boolean registration, boolean deposit, String rank, double time, double drivingCan, boolean waiting) {
        this.student = student;
        this.person = person;
        this.course = course;
        this.registration = registration;
        this.deposit = deposit;
        this.rank = rank;
        this.time = time;
        this.drivingCan = drivingCan;
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

    public boolean isRegistration() {
        return registration;
    }

    public void setRegistration(boolean registration) {
        this.registration = registration;
    }

    public boolean isDeposit() {
        return deposit;
    }

    public void setDeposit(boolean deposit) {
        this.deposit = deposit;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getDrivingCan() {
        return drivingCan;
    }

    public void setDrivingCan(double drivingCan) {
        this.drivingCan = drivingCan;
    }

    public boolean isWaiting() {
        return waiting;
    }

    public void setWaiting(boolean waiting) {
        this.waiting = waiting;
    }
}