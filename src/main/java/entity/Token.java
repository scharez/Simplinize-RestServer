package entity;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String token;

    @OneToOne
    private SkiTeacher skiTeacher;

    public Token(){}

    public Token(SkiTeacher skiTeacher) {
        this.skiTeacher = skiTeacher;
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
}
