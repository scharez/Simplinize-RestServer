package entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@NamedQueries({
        @NamedQuery(name="Student.lol",
                query=""),
})
public class Student extends Person{

    private Date birthday;
    private int postCode;
    private String place;
    private String houseNumber;
    private String street;
}
