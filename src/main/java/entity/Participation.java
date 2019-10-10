package entity;

import javax.persistence.*;

@Entity
@NamedQueries({
        @NamedQuery(name="Participation.lol",
                query=""),
})
public class Participation {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;

    @ManyToOne
    Student student;

    @ManyToOne // Das ist die einzahlende Person
    ContactPerson person;

    boolean registration;
    boolean deposit;
    String Rank;
    double time;
    double drivingCan;
    boolean waiting;
}
