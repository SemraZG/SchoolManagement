package com.project.service.user;

import com.project.entity.concretes.user.User;
import com.project.entity.enums.RoleType;
import com.project.exception.BadRequestException;
import com.project.exception.ResourceNotFoundException;
import com.project.payload.mappers.UserMapper;
import com.project.payload.messages.ErrorMessages;
import com.project.payload.messages.SuccessMessages;
import com.project.payload.request.user.UserRequest;
import com.project.payload.request.user.UserRequestWithoutPassword;
import com.project.payload.response.abstracts.BaseUserResponse;
import com.project.payload.response.business.ResponseMessage;
import com.project.payload.response.user.UserResponse;
import com.project.repository.user.UserRepository;
import com.project.service.helper.MethodHelper;
import com.project.service.helper.PageableHelper;
import com.project.service.validator.UniquePropertyValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UniquePropertyValidator uniquePropertyValidator;
    private final UserMapper userMapper;
    private final UserRoleService userRoleService;
    private final PasswordEncoder passwordEncoder;
    private final PageableHelper pageableHelper;
    private final MethodHelper methodHelper;


    //save user methodu
    public ResponseMessage<UserResponse> saveUser(UserRequest userRequest, String userRole) {
        // girilen username, email, phone ssn unique mi diye yardimci bir method oludturduk yardimci bir classda
        uniquePropertyValidator.checkDuplicate(userRequest.getUsername(), userRequest.getSsn(),
                userRequest.getPhoneNumber(), userRequest.getEmail());

        // service katinda oldugumuz icin UserRequest dto'su pojo'ya cevrilecek, cevirme islemlerini mapper'da yapiyoruz
        User user = userMapper.mapUserRequestToUser(userRequest);

        //rol bilgisini setliyoruz
        //normalde tum userlarin built-in degeri false, Admin diye bir user olusturucaz ve sadece onun built-in i true olacak
        if (userRole.equalsIgnoreCase(RoleType.ADMIN.name())) {
            if (Objects.equals(userRequest.getUsername(), "Admin")) {
                user.setBuilt_in(true);
            }
            // amdin rolu veriliyor
            user.setUserRole(userRoleService.getUserRole(RoleType.ADMIN));//getUserRole methodu db'de bu rol var mi yok mu kontrol ediyor rolu setlemeden once
        } else if (userRole.equalsIgnoreCase("Dean")) {//RoleType enumdaki "Dean" karsiligi olan enum degerini bu sekilde string olarak aldik cunku parametrede roletype string olarak alinmis
            user.setUserRole(userRoleService.getUserRole(RoleType.MANAGER));
        } else if (userRole.equalsIgnoreCase("ViceDean")) {
            user.setUserRole(userRoleService.getUserRole(RoleType.ASSISTANT_MANAGER));
        } else {
            throw new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_USER_USERROLE_MESSAGE));
        }

        // Password encode/hash
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //!!! isAdvisor degerini False yapiyoruz
        user.setIsAdvisor(Boolean.FALSE);
        User savedUser = userRepository.save(user);

        return ResponseMessage.<UserResponse>builder()
                .message(SuccessMessages.USER_CREATED)
                .object(userMapper.mapUserToUserResponse(savedUser))
                .build();
    }


    //getUsersByPage
    public Page<UserResponse> getUsersByPage(int page, int size, String sort, String type, String userRole) {
        Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, type);
        return userRepository.findByUserByRole(userRole, pageable)
                .map(userMapper::mapUserToUserResponse);
    }


    //getUserById
    public ResponseMessage<BaseUserResponse> getUserById(Long userId) {
        BaseUserResponse baseUserResponse = null;
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_USER_MESSAGE, userId)));
        if (user.getUserRole().getRoleType() == RoleType.STUDENT) {
            baseUserResponse = userMapper.mapUserToStudentResponse(user);
        } else if (user.getUserRole().getRoleType() == RoleType.TEACHER) {
            baseUserResponse = userMapper.mapUserToTeacherResponse(user);
        } else {
            baseUserResponse = userMapper.mapUserToUserResponse(user);
        }

        return ResponseMessage.<BaseUserResponse>builder()
                .message(SuccessMessages.USER_FOUND)
                .httpStatus(HttpStatus.OK)
                .object(baseUserResponse)
                .build();
    }


    //deleteUserById-->Admin kendi haric herkesi (built-in olmayan baska bir admin i de) silebilir,
    // MANAGER sadece Teacher, student, Assistant_Manager silebilir,Mudur Yardimcisi sadece Teacher veya Student silebilir
    public String deleteUserById(Long id, HttpServletRequest request) {
        //once silinecek olan user var mi yok mu controlu yapmaliyim
        //bu kontrolu her yerde cok fazla kullandigim icin MethodHelper classinda bu methodu olusturuyrm
        User user = methodHelper.isUserExist(id);//bu user silinecek user
        // methodu tetikleyen kullanicinin rol bilgisini aliyoruz, cunku rolune gore silme yetkileri degiisyor
        String userName = (String) request.getAttribute("username");//key degeri username olanin valuesunu getir demek
        //userName silme talebinde bulunan kisinin username i, silinecek kiisnin degil
        //getAttribute() Object dondurdugu icin String yaptigimizda manuel cast islemi yapip (String) yazdik
        User user2 = userRepository.findByUsernameEquals(userName);//user2-->silme talebinde bulunan user, user-->silinmek istenen user
        //built-in ve rol kontrolu yapicaz
        if (Boolean.TRUE.equals(user.getBuilt_in())) {//silinmek istenen kullanici built-in ise yani silinmemesi gereken bir kullanici ise Admin gibi
            throw new BadRequestException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
            // MANAGER sadece Teacher, student, Assistant_Manager silebilir
        } else if (user2.getUserRole().getRoleType() == RoleType.MANAGER) {//silmek isteyen kisi manager ise;
            if (!((user.getUserRole().getRoleType() == RoleType.TEACHER) ||//silinmek istenen teacher degilse;
                    (user.getUserRole().getRoleType() == RoleType.STUDENT) ||////silinmek istenen student degilse;
                    (user.getUserRole().getRoleType() == RoleType.ASSISTANT_MANAGER))) {////silinmek istenen assistant manager degilse;
                throw new BadRequestException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
            }
            // Mudur Yardimcisi sadece Teacher veya Student silebilir
        } else if (user2.getUserRole().getRoleType() == RoleType.ASSISTANT_MANAGER) {
            if (!((user.getUserRole().getRoleType() == RoleType.TEACHER) ||
                    (user.getUserRole().getRoleType() == RoleType.STUDENT))) {
                throw new BadRequestException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
            }
        }
        userRepository.deleteById(id);
        return SuccessMessages.USER_DELETE;
    }


    //updateUser
    public ResponseMessage<BaseUserResponse> updateUser(UserRequest userRequest, Long userId) {
       User user= methodHelper.isUserExist(userId);//update edilmek istenen user var mi kontrolu
       //built-in kontrolu
        methodHelper.checkBuiltIn(user);
        // unique kontrolu-->update edilmek istenen degerler username,ssn, phonenumber veya email ise unique kontrolu yapmaliyiz
        uniquePropertyValidator.checkUniqueProperties(user, userRequest);
        // dto --. pojo
        User updatedUser = userMapper.mapUserRequestToUpdatedUser(userRequest, userId);
        //!!! password Hashlenecek
        updatedUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        updatedUser.setUserRole(user.getUserRole());
        User savedUser = userRepository.save(updatedUser);

        return ResponseMessage.<BaseUserResponse>builder()
                .message(SuccessMessages.USER_UPDATE_MESSAGE)
                .httpStatus(HttpStatus.OK)
                .object(userMapper.mapUserToUserResponse(savedUser))
                .build();

    }

    public ResponseEntity<String> updateUserForUsers(UserRequestWithoutPassword userRequest, HttpServletRequest request) {
        String userName = (String) request.getAttribute("username");
        User user = userRepository.findByUsernameEquals(userName);

        //!!! builtIn
        methodHelper.checkBuiltIn(user);
        // unique kontrolu
        uniquePropertyValidator.checkUniqueProperties(user, userRequest);
        //!!! DTO --> POJO
        user.setUsername(userRequest.getUsername());
        user.setBirthDay(userRequest.getBirthDay());
        user.setEmail(userRequest.getEmail());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setBirthPlace(userRequest.getBirthPlace());
        user.setGender(userRequest.getGender());
        user.setName(userRequest.getName());
        user.setSurname(userRequest.getSurname());
        user.setSsn(userRequest.getSsn());

        userRepository.save(user);

        String message = SuccessMessages.USER_UPDATE;
        return ResponseEntity.ok(message);
    }

    public List<UserResponse> getUserByName(String name) {

        return userRepository.getUserByNameContaining(name) // List<User>
                .stream() // stream<User>
                .map(userMapper::mapUserToUserResponse) // stream<UserResponse>
                .collect(Collectors.toList()); // List<UserResponse>
    }



    //Runner class icin admin sayisini getiren method cunku 1 admin bile varsa built-in admin var demek
    public long countAllAdmins(){
        return userRepository.countAdmin(RoleType.ADMIN);
    }





}




