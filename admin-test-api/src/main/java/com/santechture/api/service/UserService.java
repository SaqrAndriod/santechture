package com.santechture.api.service;

import com.santechture.api.dto.GeneralResponse;
import com.santechture.api.dto.user.UserDto;
import com.santechture.api.entity.User;
import com.santechture.api.exception.BusinessExceptions;
import com.santechture.api.repository.UserRepository;
import com.santechture.api.validation.AddUserRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<GeneralResponse> list(Pageable pageable){
        Page<User> users = userRepository.findAll(pageable);
        return new GeneralResponse().response(users.map(UserDto::new));
    }

    public ResponseEntity<GeneralResponse> addNewUser(AddUserRequest request) throws BusinessExceptions {
        if(userRepository.existsByUsernameIgnoreCase(request.getUsername())){
            throw new BusinessExceptions("username.exist");
        } else if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new BusinessExceptions("email.exist");
        }

        User user = new User(request.getUsername(), request.getEmail());
        userRepository.save(user);

        // Log out the current admin
        UserDetails adminDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String loggedOutAdmin = "Logged out admin username: " + adminDetails.getUsername();

        GeneralResponse response = new GeneralResponse();
        response.setMessage("User added successfully. " + loggedOutAdmin);
        response.setData(new UserDto(user));
        return ResponseEntity.ok(response);
    }
}
