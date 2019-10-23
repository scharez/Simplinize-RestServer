package entity;

import org.bouncycastle.util.encoders.Hex;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
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

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<Role> roles;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Token token;

    public SkiTeacher(String username, Date birthday, List<Role> roles) {
        this.username = username;
        this.birthday = birthday;
        this.roles = roles;
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

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
