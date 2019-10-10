package entity;

import javax.persistence.*;

@Entity
@NamedQueries({
        @NamedQuery(name="ContactPerson.lol",
                query=""),
})
public class ContactPerson extends Person {

    private String password;

    private String email;
    private String phone;

    public ContactPerson(String email, String phone) {
        this.email = email;
        this.phone = phone;
    }

    public ContactPerson() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
