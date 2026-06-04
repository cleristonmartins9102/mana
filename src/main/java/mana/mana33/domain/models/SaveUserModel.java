package mana.mana33.domain.models;

public record SaveUserModel(
    String firstName,
    String secondName,
    String email,
    String password,
    String mobileNumber,
    String refreshToken
) {
}
