package entity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
@NamedQueries({
        @NamedQuery(name="Student.getAllMembers",
                query="select s from Student s"),
})
public class Student extends Person{

    private Date birthday;
    private int postCode;
    private String place;
    private String houseNumber;
    private String street;

}
