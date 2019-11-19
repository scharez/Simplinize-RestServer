package dto;

public class GroupDTO {

    String proficiency;
    int amount;


    public GroupDTO() {

    }

    public GroupDTO(String proficiency, int amount) {
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
