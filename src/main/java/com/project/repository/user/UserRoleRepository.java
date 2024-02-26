package com.project.repository.user;

import com.project.entity.concretes.user.UserRole;
import com.project.entity.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {

    //UserRole findByEnumRoleEquals(RoleType roleType); -->normalde bu skeilde geldi serviceden
    @Query("SELECT r FROM UserRole r WHERE r.roleType=?1")//turetilen bir method olmadigi icin jpql yazdik, ?1--> method parametresindeki 1.parametre demek
    Optional<UserRole> findByEnumRoleEquals(RoleType roleType);// roletype in null donme ihtimaline karsi Optional<UserRole> seklinde degistirdik ustteki methodu

}
