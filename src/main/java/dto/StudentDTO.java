package dto;

import java.util.Date;

public class StudentDTO {

    private String firstName;
    private String lastName;

    private Date birthday;
    private int postCode;
    private String place;
    private String houseNumber;
    private String street;

    public StudentDTO() { }

    public StudentDTO(String firstName, String lastName, Date birthday, int postCode, String place, String houseNumber, String street) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.postCode = postCode;
        this.place = place;
        this.houseNumber = houseNumber;
        this.street = street;
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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public int getPostCode() {
        return postCode;
    }

    public void setPostCode(int postCode) {
        this.postCode = postCode;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
