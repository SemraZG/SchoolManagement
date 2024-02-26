package com.project.payload.request.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {//login olunurken kullaniicnin girecegi degiskenler icin olan dto classi
    //login islemi icin senaryoya uygun(kullanici mail ve sifreyle mi, usernam ve sifreykle mi giris yapacak gibi) dto olusturmak zorunlu

    @NotNull(message = "Username must not be empty")
    private String username;

    @NotNull(message = "Password must not be empty")
    private String password;
}
