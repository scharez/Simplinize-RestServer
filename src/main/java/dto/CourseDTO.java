package dto;

import entity.SkiTeacher;

import java.util.Date;

public class CourseDTO {

    public CourseDTO() { }

    private Date from;
    private Date to;

    private String place;

    private SkiTeacher instructor;

    public CourseDTO(Date from, Date to, String place, SkiTeacher instructor) {
        this.from = from;
        this.to = to;
        this.place = place;
        this.instructor = instructor;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public SkiTeacher getInstructor() {
        return instructor;
    }

    public void setInstructor(SkiTeacher instructor) {
        this.instructor = instructor;
    }
}
