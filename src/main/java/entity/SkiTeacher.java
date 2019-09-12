package entity;

import org.bouncycastle.util.encoders.Hex;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;

@Entity
@NamedQueries({
        @NamedQuery(name="SkiTeacher.countEmail",
                query="SELECT COUNT(s) FROM SkiTeacher s WHERE s.email = :email"),
        @NamedQuery(name="SkiTeacher.getUser",
                query="SELECT s FROM SkiTeacher s WHERE s.username = :username"),
})
public class SkiTeacher {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String birthday;
    private String email;
    private long number;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<Role> roles;

    private String salt;

    public SkiTeacher() {}

    public SkiTeacher(String username, String password, String firstName, String lastName, String birthday, String email, long number, List<Role> roles) {
        this.username = username;
        hashPassword(password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.email = email;
        this.number = number;
        this.roles = roles;
    }

    public SkiTeacher(String firstName, String lastName, String email, List<Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.roles = roles;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
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
