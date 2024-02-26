package com.project.service.validator;
import com.project.entity.concretes.user.User;
import com.project.exception.ConflictException;
import com.project.payload.messages.ErrorMessages;
import com.project.payload.request.abstracts.AbstractUserRequest;
import com.project.payload.request.user.UserRequest;
import com.project.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniquePropertyValidator {

    private final UserRepository userRepository;

    //update islemi yapilirken unique olmasi gereken degisknelerden herhangi biirnde degisiklik var mi kontrolu yapan method varsa checkDuplicate() methodu tetiklenecek
    public void checkUniqueProperties(User user, AbstractUserRequest abstractUserRequest){//AbstractUserRequest-->hem teacher, hem student hem diger user'lari parent olrk kapsar ve ayrica iicnde password de yok
        String updatedUsername="";
        String updatedSsn="";
        String updatedPhone="";
        String updatedEmail="";
        boolean isChanged= false;

        if( ! user.getUsername().equalsIgnoreCase(abstractUserRequest.getUsername())){
            updatedUsername = abstractUserRequest.getUsername();
            isChanged = true;
        }

        if(! user.getSsn().equalsIgnoreCase(abstractUserRequest.getSsn())){
            updatedSsn = abstractUserRequest.getSsn();
            isChanged = true;
        }

        if( ! user.getPhoneNumber().equalsIgnoreCase(abstractUserRequest.getPhoneNumber())){
            updatedPhone = abstractUserRequest.getPhoneNumber();
            isChanged = true;
        }

        if(! user.getEmail().equalsIgnoreCase(abstractUserRequest.getEmail())){
            updatedEmail = abstractUserRequest.getEmail();
            isChanged = true;
        }

        if(isChanged){//unique olmasi gerken degiskenklerden herhangi birinde degisiklik var demek true olmasi
            checkDuplicate(updatedUsername,updatedSsn,updatedPhone, updatedEmail);
        }
    }


    //unique kontrolu--> username, ssn, phone, email unique olmali
    public void checkDuplicate(String username, String ssn, String phone, String email) {

        if (userRepository.existsByUsername(username)) {
            throw new ConflictException(String.format(ErrorMessages.ALREADY_REGISTER_MESSAGE_USERNAME, username));
            //%s dedik dinamik olsun diye %s=username bu yuzden String.format(ErrorMessages.. seklinde kullnmk zorubdayz
        }
        if (userRepository.existsBySsn(ssn)) {
            throw new ConflictException(String.format(ErrorMessages.ALREADY_REGISTER_MESSAGE_SSN, ssn));
        }
        if (userRepository.existsByPhoneNumber(phone)) {
            throw new ConflictException(String.format(ErrorMessages.ALREADY_REGISTER_MESSAGE_PHONE, phone));
        }
        if (userRepository.existsByEmail(email)) {
            throw new ConflictException(String.format(ErrorMessages.ALREADY_REGISTER_MESSAGE_EMAIL, email));
        }
    }






}
