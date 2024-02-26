package com.project.service.user;

import com.project.entity.concretes.user.UserRole;
import com.project.entity.enums.RoleType;
import com.project.exception.ResourceNotFoundException;
import com.project.payload.messages.ErrorMessages;
import com.project.repository.user.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public UserRole getUserRole(RoleType roleType){//tabloda Userrole varsa geriten yoksa exc firlatan method
        //findByEnumRoleEquals-->RoleType enum classinda Enum adinda bir degisken olmadigi icin bu turetilen bir method olamaz bu yuzden jpql yazicaz repoda
        return userRoleRepository.findByEnumRoleEquals(roleType).orElseThrow(()->new ResourceNotFoundException(ErrorMessages.ROLE_NOT_FOUND));
        //service de olan method genelde dto dondurur ama biz burda pojo dondurduk
        //eger servicedeki bir method pojo donduruyorsa bu method baska servicelr tarafindan da kullanilacak (veya guvenlik derdi yok demektir)demek, eger dto donseydi baska serviceda bunu kullnamamazdik
        //mesela bu method UserService tarafindan kullanilacak
    }


    public List<UserRole> getAllUserRole(){
        return userRoleRepository.findAll();
    }
}
