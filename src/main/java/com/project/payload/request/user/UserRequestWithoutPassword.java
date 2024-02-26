package com.project.payload.request.user;

import com.project.payload.request.abstracts.AbstractUserRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
public class UserRequestWithoutPassword extends AbstractUserRequest {
}
