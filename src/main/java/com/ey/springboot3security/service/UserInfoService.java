package com.ey.springboot3security.service;

import com.ey.springboot3security.entity.Response;
import com.ey.springboot3security.entity.UserInfo;
import com.ey.springboot3security.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserInfoService implements UserDetailsService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        Optional<UserInfo> userDetail = userInfoRepository.findByEmail(username);
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(()-> new UsernameNotFoundException("user not found : " + username));
    }

    public ResponseEntity<Response<String>> addUser(UserInfo userInfo){
        if(userInfoRepository.findByEmail(userInfo.getEmail()).isPresent()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.error("user is exist", 403));
        }
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        userInfoRepository.save(userInfo);
        return ResponseEntity.status(HttpStatus.OK).body(Response.success(null,"success"));
    }
}
