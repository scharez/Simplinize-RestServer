package entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@NamedQueries({
        @NamedQuery(name="Course.lol",
                query=""),
})
public class Course {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;

    private Date from;
    private Date to;
    private String place;

    @ManyToOne
    private SkiTeacher instructor;

}
