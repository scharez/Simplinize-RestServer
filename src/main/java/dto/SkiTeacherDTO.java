package dto;

import entity.Role;

import java.util.Date;
import java.util.List;

public class SkiTeacherDTO {

    private long id;

    private String username;
    private String password;

    private String firstName;
    private String lastName;
    private String email;
    private Date birthday;

    private List<Role> roles;

    public SkiTeacherDTO(){}

    public SkiTeacherDTO(long id){
        this.id = id;
    }

    public SkiTeacherDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public SkiTeacherDTO(String firstName, String lastName, String email, List<Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.roles = roles;
    }

    public SkiTeacherDTO(String password, String firstName, String lastName, String email, Date birthday, List<Role> roles) {
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthday = birthday;
        this.roles = roles;
    }

    public SkiTeacherDTO(String username, String password, String firstName, String lastName, String email, Date birthday, List<Role> roles) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthday = birthday;
        this.roles = roles;
    }

    public SkiTeacherDTO(String username) {
        this.username = username;
    }

    public SkiTeacherDTO(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
