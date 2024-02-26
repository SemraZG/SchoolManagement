package com.project.payload.request.user;

import com.project.payload.request.abstracts.BaseUserRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class UserRequest extends BaseUserRequest {
    //BaseUserRequest CLASSINDA pasword be built-in degiskneleri var sadece, diger degiskenler BaseUserRequest clasiini extend ettigimiz AbstractUserRequest classinda
    //password icinde olcak sekilde bir dto'ya ihtiyacim varsa BaseUserRequestten, password olmadan bir dto'ya ihtiyacim varsa AbstractUserRequest classina extend edicem
}
