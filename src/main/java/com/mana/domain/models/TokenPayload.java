package com.mana.domain.models;

public class TokenPayload {
  String firstName;
  String lastName;
  String email;
  public TokenPayload(String firstName, String lastName, String email) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
  }
 
}
