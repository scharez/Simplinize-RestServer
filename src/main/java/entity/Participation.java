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
}
