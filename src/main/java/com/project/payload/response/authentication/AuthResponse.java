package com.project.payload.response.authentication;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)//asagidaki degiskenlerden null olan varsa(User'da hepsine null olmasin demmemis olabiliriz)
//bu durumda response json a dondurulurken null olanalri alma ve gonderme demis oliuyoruz @JsonInclude
//mesela asagida name de olsun donrululen responseda dedik ama User classda name degiskenine @NotNull demediysek ve kullanici name degerini girmediyse bu jsonda null olacakti @JsonInclude olmasaydi
public class AuthResponse {//neyi dondurecegimiz senaryoya gore degisir, burda asagidakileri sectik

    private String username;
    private String ssn;//bu bilgiler db'den aliniyor
    private String role;
    private String token;
    private String name;
}
