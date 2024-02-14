package com.project.contactmessage.service;

import com.project.contactmessage.dto.ContactMessageRequest;
import com.project.contactmessage.dto.ContactMessageResponse;
import com.project.contactmessage.entity.ContactMessage;
import com.project.contactmessage.mapper.ContactMessageMapper;
import com.project.contactmessage.messages.Messages;
import com.project.contactmessage.repository.ContactMessageRepository;
import com.project.exception.ResourceNotFoundException;
import com.project.payload.response.business.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ContactMessageService {

    private final ContactMessageRepository contactMessageRepository;
    private final ContactMessageMapper contactMessageMapper;    //@AutoWired a gerek yok zaten class seviyesinde @RequiredArgsConstructor kullndk



    public ResponseMessage<ContactMessageResponse> save(ContactMessageRequest contactMessageRequest) {

        ContactMessage contactMessage = contactMessageMapper.requestToContactMessage(contactMessageRequest);
        ContactMessage savedData = contactMessageRepository.save(contactMessage);

        return ResponseMessage.<ContactMessageResponse>builder()
                .message("Contact Message Created Successfully")
                .httpStatus(HttpStatus.CREATED) // 201
                .object(contactMessageMapper.contactMessageToResponse(savedData))
                .build();
    }

    public Page<ContactMessageResponse> getAll(int page, int size, String sort, String type) {
       Pageable pageable= PageRequest.of(page,size, Sort.by(sort).ascending());
       if (Objects.equals(type,"desc")){
          pageable= PageRequest.of(page,size, Sort.by(sort).descending());
       }
       return contactMessageRepository.findAll(pageable).map(contactMessageMapper::contactMessageToResponse);
    }



    public Page<ContactMessageResponse> searchByEmail(String email, int page, int size, String sort, String type) {
        Pageable pageable= PageRequest.of(page,size, Sort.by(sort).ascending());
        if (Objects.equals(type,"desc")){
            pageable= PageRequest.of(page,size, Sort.by(sort).descending());
        }
        return contactMessageRepository.findByEmailEquals(email,pageable).
                map(contactMessageMapper::contactMessageToResponse);
    }


    public String deleteById(Long id) {
        getContactMessageById(id);
        contactMessageRepository.deleteById(id);
        return Messages.CONTACT_MESSAGE_DELETED_SUCCESSFULLY;
    }

    public ContactMessage getContactMessageById(Long id){
        return contactMessageRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(Messages.NOT_FOUND_MESSAGE));
    }

    public Page<ContactMessageResponse> searchBySubject(String subject, int page, int size, String sort, String type) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        if (Objects.equals(type, "desc")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }
        return contactMessageRepository.findBySubjectEquals(subject, pageable). // Derived
                map(contactMessageMapper::contactMessageToResponse);
    }
}














