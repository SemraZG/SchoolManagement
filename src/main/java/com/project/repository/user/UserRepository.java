package com.project.repository.user;

import com.project.entity.concretes.user.User;
import com.project.entity.enums.RoleType;
import com.project.payload.response.user.StudentResponse;
import com.project.payload.response.user.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameEquals(String username);

    boolean existsByUsername(String username);

    boolean existsBySsn(String ssn);

    boolean existsByPhoneNumber(String phone);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.userRole.roleName = ?1")
    Page<User> findByUserByRole(String userRole, Pageable pageable);

    List<User> getUserByNameContaining(String name);

    @Query(value = "SELECT COUNT (u) FROM User u WHERE u.userRole.roleType = ?1 ")
    long countAdmin(RoleType roleType);

    //List<StudentResponse> findByAdvisorTeacherId(Long id); -->serviceden geldigi hali bu sekildeydi ama;
    List<User> findByAdvisorTeacherId(Long id);// repodan dto donmeyecegi icin(StudentResponse bir dto), User yaptik Teacher entity si olsaydi tecaher da yapabilirdik;

    @Query("SELECT u FROM User u WHERE u.isAdvisor =?1")//isAdvisor true demek o ogretmen ayni zamanda advisor yani rehber ogretmn demek
    List<User> findAllByAdvisor(Boolean aTrue);//a otomatik geldi

    @Query("SELECT (COUNT (u)>0) FROM User u WHERE u.userRole.roleType = ?1 ")
    boolean findStudent(RoleType roleType);

    @Query("SELECT MAX (u.studentNumber) FROM User u")
    int getMaxStudentNumber();

}
