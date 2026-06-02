package mana.mana33.domain.models;

public class TokenPayloadDTO {
    public String id = null;
    public String firstName;
    public String lastName;
    public String email;

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }
}
