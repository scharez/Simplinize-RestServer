package entity;

import javax.persistence.*;

@Entity
@NamedQueries({
        @NamedQuery(name="CourseGroup.getAllGroups",
                query="SELECT g FROM CourseGroup g"),
})
public class CourseGroup {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;

    @ManyToOne
    private Course course;

    @ManyToOne
    private SkiTeacher skiTeacher;

    @Enumerated(EnumType.STRING)
    private Proficiency proficiency;



}
