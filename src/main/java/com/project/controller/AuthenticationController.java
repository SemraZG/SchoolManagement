package com.project.controller;

import com.project.payload.messages.SuccessMessages;
import com.project.payload.request.authentication.LoginRequest;
import com.project.payload.request.business.UpdatePasswordRequest;
import com.project.payload.response.authentication.AuthResponse;
import com.project.payload.response.user.UserResponse;
import com.project.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")//security katmninda WebSecurityConfig classda "/auth" ile gelen requestleri securityden haric tuttuk
@RequiredArgsConstructor
public class AuthenticationController {//normalde user controller da var burda sadece jwt ile ilgili islemler(login) icin ayrin bir controller oliusturduk
    private final AuthenticationService authenticationService;
    //controller olunca da ayri bir service olusturduk jwt icin, ama ayri bir repo classa ihtiyac yok cunku cunku db zaten ayni varolan repo classini AuthenticationService classinda injekte ettik

    @PostMapping("/login")//http://localhost:8080/auth/login -->post olmasinin sebebi kullanici adi ve sifre body ile json formatinda sifrelenerek gonderiliyor, get olsaydi endpointte sifre ve username de gorunucekti
    public ResponseEntity<AuthResponse> authenticationUser(@RequestBody @Valid LoginRequest loginRequest){//LoginRequest yerine User deseydim kullanici login olurken User'daki tum degisknlerin degerlerini girmek zorunda kalacakti, bu olmasin diye LoginRequest dto'yu olisturduk
        //LoginRequest dto ile login icin gelen requestin sonucu AuthResponse dto ile response olrak dondurulur
        return authenticationService.authenticateUser(loginRequest);
    }


     @GetMapping("/user") // http://localhost:8080/auth/user + GET
     @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','TEACHER','STUDENT')")//bu methodu burada belirtilen rol seviyelerindekiler tetikleyebilsin dedik
     public ResponseEntity<UserResponse> findByUsername(HttpServletRequest request){
        String username= (String) request.getAttribute("username");
        UserResponse userResponse= authenticationService.findByUsername(username);
        return ResponseEntity.ok(userResponse);
    }

    //update password
    @PatchMapping("/updatePassword") // http://localhost:8080/auth/updatePassword + Patch + JSON
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','TEACHER','STUDENT')")
    public ResponseEntity<String> updatePassword(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest,
                                                 HttpServletRequest request){
        authenticationService.updatePassword(updatePasswordRequest, request);
        String response = SuccessMessages.PASSWORD_CHANGED_RESPONSE_MESSAGE;
        return ResponseEntity.ok(response);

    }

}
