
package com.opd.phonenumberapi.datasource;

import com.opd.phonenumberapi.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User,Long>, JpaSpecificationExecutor<User> {

    Page<User> findAll(Pageable pageRequest);
    Optional<User> findByUsername(String username);

    @Query("SELECT a FROM User a WHERE " +
            "LOWER(a.firstName) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(a.fatherName) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(a.lastName) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(a.directorate) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(a.department) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(a.unit) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(a.service) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +
            "LOWER(a.jobTitle) LIKE LOWER(CONCAT('%', :searchText, '%'))")
    List<User> findBySearchText(@Param("searchText") String searchText);
}
