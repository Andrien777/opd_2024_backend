package com.opd.phonenumberapi;

import com.opd.phonenumberapi.datasource.UserRepository;
import com.opd.phonenumberapi.entity.SearchCriteria;
import com.opd.phonenumberapi.entity.User;

import com.opd.phonenumberapi.entity.UserSpecification;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController()
@RequestMapping("/api")
//@CrossOrigin(origins = "*")
public class ApiController {


    private final UserRepository userRepository;

    public ApiController(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @PostMapping("/getAllUsersData")
    public ResponseEntity<List<User>> getAllUsers(
            @RequestParam("page") @Min(value = 0, message = "Page must be greater or equals to 0") int page,
            @RequestParam("page-size") @Min(value = 1, message = "Page size must be greater than 0") int size,
            @RequestBody(required = false) List<SearchCriteria> filterCriteria) {
        Specification<User> spec = null;
        if(filterCriteria!=null) {
            for (SearchCriteria criteria : filterCriteria) {
                if (spec == null) {
                    spec = new UserSpecification(criteria);
                } else {
                    spec = spec.and(new UserSpecification(criteria));
                }
            }
        }
        Pageable pageRequest = PageRequest.of(page, size, Sort.by("lastName"));
        if(spec!=null){
            return ResponseEntity.ok(userRepository.findAll(spec,pageRequest).getContent());
        }else{
            return ResponseEntity.ok(userRepository.findAll(pageRequest).getContent());
        }
    }


    @PostMapping("/addUser")
    public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
        return ResponseEntity.ok(userRepository.save(user));
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<String> deleteUser(@RequestBody Long userId){
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with specified id was not found");
        }
        userRepository.delete(user.get());
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/editUser")
    public ResponseEntity<User> editUser(@RequestBody User user){
        Optional<User> dbUser = userRepository.findById(user.getId());
        if (dbUser.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        User updatedUser = userRepository.save(user);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/updatePhoneNumber")
    public ResponseEntity<User> updatePhoneNumber(@RequestBody @Valid String newPhoneNumber){
        String username = ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClaim("preferred_username");
        Optional<User> result = userRepository.findByUsername(username);
        if(result.isPresent()){
            User user = result.get();
            user.setMobilePhoneNumber(newPhoneNumber);
            userRepository.save(user);
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @GetMapping("/getUser")
    public ResponseEntity<User> getUser(){
        String username = ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClaim("preferred_username");
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok(userRepository.findByUsername(username).get());
    }

    @PostMapping("/searchUsers")
    public ResponseEntity<List<User>> searchUsers(
            @RequestParam("page") @Min(value = 0, message = "Page must be greater or equals to 0") int page,
            @RequestParam("page-size") @Min(value = 1, message = "Page size must be greater than 0") int size,
            @RequestBody(required = false) String searchText) {
        Pageable pageRequest = PageRequest.of(page, size, Sort.by("lastName"));
        if(searchText!=null){
            String filteredQuery = searchText.replace("\"", "");
            return ResponseEntity.ok(userRepository.findBySearchText(filteredQuery));
        }else{
            return ResponseEntity.ok(userRepository.findAll(pageRequest).getContent());
        }
    }

}
