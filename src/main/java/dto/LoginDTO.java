package dto;

public class LoginDTO {

    private String credentials;
    private String password;

    public LoginDTO() {}

    public LoginDTO(String credentials, String password) {
        this.credentials = credentials;
        this.password = password;
    }

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
