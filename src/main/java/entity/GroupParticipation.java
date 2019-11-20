package entity;

import javax.persistence.*;

@Entity
@NamedQueries({
        @NamedQuery(name = "GroupParticipation.getPartByGroupId",
                query = "select gp from GroupParticipation gp where gp.group.id = :id")

})

public class GroupParticipation {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;

    private int rank;
    private double time;
    private double drivingCan;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Group group;

    public GroupParticipation() {}

    public GroupParticipation(Group group, Student student, int rank, double time, double drivingCan) {
        this.group = group;
        this.student = student;
        this.rank = rank;
        this.time = time;
        this.drivingCan = drivingCan;
    }

    public GroupParticipation(Group group, Student student) {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public double getDrivingCan() {
        return drivingCan;
    }

    public void setDrivingCan(double drivingCan) {
        this.drivingCan = drivingCan;
    }
}
