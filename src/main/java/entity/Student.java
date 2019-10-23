package entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(name="Student.getAllMembers",
            query="select s from Student s"),
        @NamedQuery(name="Student.getAllCourseMembers",
                query="select s from Student s"),
        @NamedQuery(name="Student.getStudentById",
                query="select s from Student s where s.id = :id"),
})
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String firstName;
    private String lastName;

    @Temporal(TemporalType.DATE)
    private Date birthday;

    private int postCode;
    private String place;
    private String houseNumber;
    private String street;

    public Student() { }

    public Student(String firstName, String lastName, Date birthday, int postCode, String place, String houseNumber, String street) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.postCode = postCode;
        this.place = place;
        this.houseNumber = houseNumber;
        this.street = street;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public int getPostCode() {
        return postCode;
    }

    public void setPostCode(int postCode) {
        this.postCode = postCode;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
