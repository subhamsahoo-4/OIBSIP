package com.library.controller;


import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.library.dto.request.ForgotPasswordRequest;
import com.library.dto.request.LoginRequest;
import com.library.dto.request.RegisterRequest;
import com.library.dto.request.ResetPasswordRequest;
import com.library.dto.response.MemberResponse;
import com.library.entity.Member;
import com.library.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController @RequestMapping("/api/auth") public class AuthController{
    private final AuthService auth; 
    private final HttpSessionSecurityContextRepository contextRepo=new HttpSessionSecurityContextRepository();
    public AuthController(AuthService auth){this.auth=auth;}

    @PostMapping("/register") 
    public ResponseEntity<?> register(@RequestBody RegisterRequest r){
        Member m=auth.register(r.name(),r.email(),r.password(),r.role(),r.adminRegistrationCode());
        return ResponseEntity.status(201).body(MemberResponse.from(m));
    }
    @PostMapping("/login") 
    public MemberResponse login(
        @RequestBody LoginRequest r,HttpServletRequest request,HttpServletResponse response)
        {Member m=auth.authenticate(r.email(),r.password());
            var authorities=List.of(new SimpleGrantedAuthority("ROLE_"+m.getRole().name()));
            var token=new UsernamePasswordAuthenticationToken(m.getEmail(),null,authorities);
            SecurityContext context=SecurityContextHolder.createEmptyContext();
            context.setAuthentication(token);SecurityContextHolder.setContext(context);
            contextRepo.saveContext(context,request,response);return MemberResponse.from(m);
        }
    @GetMapping("/me") 
    public MemberResponse me(
        org.springframework.security.core.Authentication a)
        {if(a==null||!a.isAuthenticated()||"anonymousUser".equals(a.getPrincipal()))throw new ResponseStatusException(
            HttpStatus.UNAUTHORIZED);
            return MemberResponse.from(auth.getByEmail(a.getName()));
        }
    @PostMapping("/logout") 
    public ResponseEntity<Void> logout(){
        SecurityContextHolder.clearContext();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @RequestBody ForgotPasswordRequest request) {

        auth.sendResetCode(request.getEmail());

        return ResponseEntity.ok(
                Map.of("message",
                        "Verification code sent to your email")
        );
    }
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestBody ResetPasswordRequest request) {

        auth.resetPassword(
                request.getEmail(),
                request.getCode(),
                request.getNewPassword()
        );

        return ResponseEntity.ok(
                java.util.Map.of(
                        "message",
                        "Password reset successfully"
                )
        );
    }
}
