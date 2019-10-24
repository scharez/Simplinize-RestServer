package entity;

import javax.persistence.*;

@Entity
@NamedQueries({
        @NamedQuery(name="ContactPerson.getUser",
                query="select p from ContactPerson p WHERE p.email = :email"),
        @NamedQuery(name="ContactPerson.uniqueEmail",
                query = "select count(p) from ContactPerson p where p.email = :email")
})
public class ContactPerson extends Person{

    private boolean verified;
    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;

    public ContactPerson(){ }

    public ContactPerson(Role role, String phone) {
        this.role = role;
        this.phone = phone;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
