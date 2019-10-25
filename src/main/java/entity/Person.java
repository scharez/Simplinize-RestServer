package entity;

import org.bouncycastle.util.encoders.Hex;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@NamedQueries({
        @NamedQuery(name="Person.getUser",
                query="SELECT p FROM Person p WHERE p.email = :email"),
        @NamedQuery(name="Person.uniqueEmail",
                query="SELECT COUNT(p) FROM Person p WHERE p.email = :email"),
})
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String email;
    private String password;

    private String salt;

    @ManyToMany()
    private List<Student> students;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Token token;

    public Person () {
        this.students = new ArrayList<>();
    }

    public Person(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        hashPassword(password);
        this.students = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        hashPassword(password);
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    private void hashPassword(String password) {

        // generate the salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        try {
            // setup the encryption
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);

            // encrypt
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            this.password = new String(Hex.encode(hash));
            this.salt = new String(Hex.encode(salt));

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


}
