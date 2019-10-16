package entity;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@NamedQueries({
        @NamedQuery(name="Token.getToken",
                query="select t from Token t WHERE t.token = :token")
})
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String token;

    @OneToOne(mappedBy = "token", cascade = CascadeType.PERSIST)
    private SkiTeacher skiTeacher;

    @OneToOne(mappedBy = "token", cascade = CascadeType.PERSIST)
    private ContactPerson contactPerson;

    public Token(){}

    public Token(SkiTeacher skiTeacher) {
        this.skiTeacher = skiTeacher;
        this.token = UUID.randomUUID().toString();
    }

    public Token(ContactPerson contactPerson) {
        this.contactPerson = contactPerson;
        this.token = UUID.randomUUID().toString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public SkiTeacher getSkiTeacher() {
        return skiTeacher;
    }

    public void setSkiTeacher(SkiTeacher skiTeacher) {
        this.skiTeacher = skiTeacher;
    }

    public ContactPerson getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(ContactPerson contactPerson) {
        this.contactPerson = contactPerson;
    }
}
