package nus.iss.server.Controllers;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import nus.iss.server.Components.JWTComponent;
import nus.iss.server.Model.AuthenticationRequest;
import nus.iss.server.Model.UserInfo;
import nus.iss.server.Services.LoginService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    JWTComponent jwtComponent;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private LoginService loginService;

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticateGetToken(@RequestBody AuthenticationRequest authRequest){
        
        System.out.println("authenticating user : " + authRequest.getName() + " with password : " + authRequest.getPassword());

        System.out.println("in authentication");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getName(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return ResponseEntity.ok(jwtComponent.generateToken(authRequest.getName()));
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }


    @PostMapping("/register")
    public ResponseEntity<String> registerNewUser(@RequestBody UserInfo userInfo) {
        userInfo.setRoles("ROLE_USER");
        String response = loginService.addUser(userInfo);
        return ResponseEntity.ok(response);
    }

}
