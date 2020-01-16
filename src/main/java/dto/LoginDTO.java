package dto;

import entity.Role;

import javax.validation.constraints.NotNull;


public class LoginDTO {
    
    private String credentials;
    private String password;
    private Role type;

    public LoginDTO() {}

    public LoginDTO(String credentials, String password, Role type) {
        this.credentials = credentials;
        this.password = password;
        this.type = type;
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

    public Role getType() {
        return type;
    }

    public void setType(Role type) {
        this.type = type;
    }
}
