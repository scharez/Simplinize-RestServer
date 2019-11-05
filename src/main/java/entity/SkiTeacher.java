package entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(name="SkiTeacher.uniqueEmail",
                query="SELECT COUNT(s) FROM SkiTeacher s WHERE s.email = :email"),
        @NamedQuery(name="SkiTeacher.getUser",
                query="SELECT s FROM SkiTeacher s WHERE s.username = :username"),
        @NamedQuery(name="SkiTeacher.getUserByEmail",
                query="SELECT s FROM SkiTeacher s WHERE s.email = :email"),
        @NamedQuery(name="SkiTeacher.getUserById",
                query="SELECT s FROM SkiTeacher s WHERE s.id = :id"),
})
public class SkiTeacher extends Person {

    @Column(unique = true)
    private String username;

    @Temporal(TemporalType.DATE)
    private Date birthday;

    @Temporal(TemporalType.DATE)
    private Date joined;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<Role> roles;

    public SkiTeacher(String username, Date birthday, List<Role> roles) {
        this.username = username;
        this.birthday = birthday;
        this.roles = roles;
        this.joined = new Date();
    }

    public SkiTeacher() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

}
