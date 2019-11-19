package dto;

public class CourseGroupDTO {

    String proficiency;
    int amount;


    public CourseGroupDTO() {

    }

    public CourseGroupDTO(String proficiency, int amount) {
        this.proficiency = proficiency;
        this.amount = amount;
    }

    public String getProficiency() {
        return proficiency;
    }

    public void setProficiency(String proficiency) {
        this.proficiency = proficiency;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
