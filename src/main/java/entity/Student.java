package entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@NamedQueries({
        @NamedQuery(name="Student.getAllMembers",
                query="select s from Student s"),
})
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String firstName;
    private String lastName;

    private Date birthday;
    private int postCode;
    private String place;
    private String houseNumber;
    private String street;

    public Student() {}

    public Student(String firstName, String lastName, Date birthday, int postCode, String place, String houseNumber, String street) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.postCode = postCode;
        this.place = place;
        this.houseNumber = houseNumber;
        this.street = street;
    }


}
