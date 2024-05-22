package com.santechture.api.service;

import com.santechture.api.dto.GeneralResponse;
import com.santechture.api.dto.admin.AdminDto;
import com.santechture.api.entity.Admin;
import com.santechture.api.exception.BusinessExceptions;
import com.santechture.api.repository.AdminRepository;
import com.santechture.api.security.JwtUtil;
import com.santechture.api.validation.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AdminService {


    //private final AdminRepository adminRepository;


//    public AdminService(AdminRepository adminRepository) {
//        this.adminRepository = adminRepository;
//    }
//
//    public ResponseEntity<GeneralResponse> login(LoginRequest request) throws BusinessExceptions {
//
//        Admin admin = adminRepository.findByUsernameIgnoreCase(request.getUsername());
//
//        if(Objects.isNull(admin) || !admin.getPassword().equals(request.getPassword())){
//            throw new BusinessExceptions("login.credentials.not.match");
//        }
//
//        return new GeneralResponse().response(new AdminDto(admin));
//    }





    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    public ResponseEntity<GeneralResponse> login(LoginRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (Exception e) {
            e.getStackTrace();
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        GeneralResponse response = new GeneralResponse();
        response.setMessage("Login successful");
        response.setJwt(jwt);
        return ResponseEntity.ok(response);
    }


}
