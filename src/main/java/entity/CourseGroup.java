package entity;

import javax.persistence.*;

@Entity
@NamedQueries({
        @NamedQuery(name="CourseGroup.lol",
                query=""),
})
public class CourseGroup {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;
}
