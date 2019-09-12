package entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Person {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;

    private String firstName;
    private String lastName;

}
