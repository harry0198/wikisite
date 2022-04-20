package com.harrydrummond.wikisite.registration;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.lang.NonNull;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RegistrationRequest {

    @NonNull
    private final String username;
    @NonNull
    private final String password;
    @NonNull
    private final String email;
}