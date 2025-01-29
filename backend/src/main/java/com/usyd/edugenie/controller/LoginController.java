package com.usyd.edugenie.controller;

import com.usyd.edugenie.entity.Users;
import com.usyd.edugenie.model.UserUpdateReq;
import com.usyd.edugenie.model.UserVerifyReq;
import com.usyd.edugenie.model.LoginUserResp;
import com.usyd.edugenie.service.UserLoginService;
import com.usyd.edugenie.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


/**
 * @author caorong
 */
@RestController
@RequestMapping("/user")
public class LoginController {
    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private UsersService usersService;

    @PostMapping("/verify")
    public ResponseEntity<String> verifyGoogleToken(@RequestBody UserVerifyReq req) {
        try {
            String jwtToken = userLoginService.verifyUser(req.getCredential());
            return new ResponseEntity<String>(jwtToken, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/info")
    public ResponseEntity<LoginUserResp> getUserInfo() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("authentication:" + authentication);
            if (authentication != null && authentication.isAuthenticated()) {
                Users user = (Users) authentication.getPrincipal();
                LoginUserResp loginUserResp = new LoginUserResp();
                System.out.println("authentication User: " + user);
                loginUserResp.setEmail(user.getEmail());
                loginUserResp.setLastName(user.getLastName());
                loginUserResp.setFirstName(user.getFirstName());
                loginUserResp.setAvatar(user.getAvatarUrl());
                return new ResponseEntity<LoginUserResp>(loginUserResp, HttpStatus.OK);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateUserInfo(@RequestBody UserUpdateReq req) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Users user = (Users) authentication.getPrincipal();
            user.setFirstName(req.getFirstName());
            user.setLastName(req.getLastName());
            usersService.updateUser(user);
            return new ResponseEntity<String>("success", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
