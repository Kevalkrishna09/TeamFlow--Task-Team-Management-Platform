package com.keval.teamflow.controller;

import com.keval.teamflow.config.JWTUtil;
import com.keval.teamflow.domain.enums.UserRole;
import com.keval.teamflow.domain.models.ResponseUtil;
import com.keval.teamflow.domain.models.TeamMembers;
import com.keval.teamflow.domain.models.User;
import com.keval.teamflow.dto.ApiResponse;
import com.keval.teamflow.dto.LoginCredentials;
import com.keval.teamflow.dto.RegisterDTO;
import com.keval.teamflow.exceptionhandler.InvalidCredentialsException;
import com.keval.teamflow.services.AuthService;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JWTUtil jwtUtil;


    @PostMapping(value = "/register", produces ="application/json" )
    public Map<String,Object> signUp(@RequestBody @Valid RegisterDTO request, @RequestParam(value = "invitationToken", required = false)  String invitationToken){


        if(invitationToken != null && !invitationToken.isEmpty()){
            User userToken  = authService.createUserToTeamViaInvitationToken(invitationToken,request);
            if(userToken == null){
                throw new InvalidCredentialsException("Invalid invitation token");
            }

            List<String> roles = Arrays.asList(userToken.getRole().toString());

            String token = jwtUtil.generateJWTToken(userToken.getEmail(), request.getName(), userToken.getUserId(), roles);
            return Collections.singletonMap("jwt-token", token);
        }

      else  {
            User user = authService.signUp(request, UserRole.ADMIN);
            List<String> roles = Arrays.asList(user.getRole().toString());

            String token = jwtUtil.generateJWTToken(user.getEmail(), request.getName(), user.getUserId(), roles);
            return Collections.singletonMap("jwt-token", token);
        }
    }

    //login and send the JWT Token
    @PostMapping(value="/login",produces = "application/json")
    public ResponseEntity<ApiResponse<?>> logIn(@RequestBody @Valid LoginCredentials request, @RequestParam(value = "invitationToken", required = false) String invitationToken){

        try{
            String username = request.getEmail();
            String password = request.getPassword();
            Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
            List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
            User user = authService.findByEmail(username);
            String name = user.getName();
            String token = jwtUtil.generateJWTToken(username,name,user.getUserId(),roles);
            boolean addedToTeam = false;
            if (invitationToken != null && !invitationToken.isEmpty()) {
                addedToTeam = authService.addUserToTeamViaInvitationToken(invitationToken, user) != null;
            }

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("invitationToken", invitationToken);
            response.put("addedToTeam", addedToTeam);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ResponseUtil.success(response, HttpStatus.OK.value(), "Login successful"));
        }
        catch (AuthenticationException authenticationException){
            throw new InvalidCredentialsException("Invalid Credentials");
        }
    }

}
