package entity;

import javax.persistence.Enumerated;

public enum Role {

    @Enumerated ADMIN, SKITEACHER, RACE, INSTRUCTOR, CONTACTPERSON , EVERYONE
}
