package dto;

import entity.SkiTeacher;

import java.util.Date;

public class CourseDTO {

    public CourseDTO() { }

    private Date from;
    private Date to;

    private String place;

    private SkiTeacherDTO instructor;

    public CourseDTO(Date from, Date to, String place, SkiTeacherDTO instructor) {
        this.from = from;
        this.to = to;
        this.place = place;
        this.instructor = instructor;
    }

    public CourseDTO(Date from, Date to, String place) {
        this.from = from;
        this.to = to;
        this.place = place;
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

    public SkiTeacherDTO getInstructor() {
        return instructor;
    }

    public void setInstructor(SkiTeacherDTO instructor) {
        this.instructor = instructor;
    }
}
