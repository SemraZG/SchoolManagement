package com.project.payload.response.abstracts;

import com.project.entity.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder//bu classdan turetilen concrete classlarda bu degiskenler kullanilabilcek
public abstract class BaseUserResponse {//abstract classdan nesne olustuurlmaz ama extednd ettigimizde burdaki degiskenleri kullanabilicez

    private Long userId;
    private String username;
    private String name;
    private String surname;
    private String ssn;
    private String birthPlace;
    private String email;
    private String userRole;
    private String phoneNumber;
    private LocalDate birthDay;
    private Gender gender;
}
