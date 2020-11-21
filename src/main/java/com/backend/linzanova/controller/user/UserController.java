package com.backend.linzanova.controller.user;

import com.backend.linzanova.dao.IUserDao;
import com.backend.linzanova.dto.*;
import com.backend.linzanova.entity.user.User;
import com.backend.linzanova.exeption.TokenExpiredExeption;
import com.backend.linzanova.service.IUserService;
import com.backend.linzanova.service.JwtService;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IUserDao userDao;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;


    @GetMapping(value = "/count")
    public Long getUsersCount() {
        return userService.totalCount();
    }

    @GetMapping
    public UserPageDTO getUsers(@RequestParam int page,
                                @RequestParam int size) {
        final Pageable pageable = PageRequest.of(page, size);
        return userService.getAllUsers(pageable);
    }
    @GetMapping(value = "/name")
    public UserPageDTO getUsersByName(@RequestParam int page,
                                      @RequestParam int size,
                                      @RequestParam String name) {
        final Pageable pageable = PageRequest.of(page, size);
        return userService.getAllUsersByName(pageable, name);
    }

    @GetMapping(value = "/{id}")
    public User getUser(@PathVariable int id){
        return userService.getUser(id);
    }

    @GetMapping(value = "/auth")
    public UserOrderDTO getAuthUser(@RequestHeader(value = "Authorization", required = false) String auth) {
        if (auth != null) {
            String jwtToken = auth.substring(7);
            String user = jwtService.extractUsername(jwtToken);
            User byEmail = userService.findByEmail(user);
            return new UserOrderDTO(byEmail.getEmail(), byEmail.getPhone(), byEmail.getFirstName(), byEmail.getLastName(), byEmail.getLocation(), byEmail.getWarehouse(), byEmail.getNumber());
        }else {
            return null;
        }
    }

    @PostMapping(value = "/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User user) {
        user.setId(id);
        user.setRole("ROLE_USER");
        return userService.updateUser(user);
    }

    @PostMapping(value = "/register")
    public User registerUser(@RequestBody @Valid User user) {
        user.setRole("ROLE_USER");
        userService.insertUser(user);
        return user;
    }

    @PostMapping(value = "/login")
    public AuthenticationResponse generateJWT(@RequestBody AuthRequest authRequest) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        return new AuthenticationResponse(jwtService.generateToken(authRequest.getUsername()));
    }


    @GetMapping(value = "/bool")
    public AdminDTO isAdminUser(@RequestHeader(value = "Authorization", required = false) String auth){
        if (auth == null){
            return new AdminDTO(false);
        }else {
            String jwtToken = auth.substring(7);
            String user = jwtService.extractUsername(jwtToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(user);
            if (userDetails != null && userDetails.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){
                return new AdminDTO(true);
            }else {
                return new AdminDTO(false);
            }
        }

    }

    @GetMapping(value = "/refreshtoken")
    public AuthenticationResponse refreshToken(HttpServletRequest request) throws  Exception {
        DefaultClaims claims = (io.jsonwebtoken.impl.DefaultClaims) request.getAttribute("claims");

        Map<String, Object> expectedMap = getMapFromIoJsonwebtokenClaims(claims);

        return new AuthenticationResponse(jwtService.doGenerateRefreshToken(expectedMap, expectedMap.get("sub").toString()));
    }

    public Map<String, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
        Map<String, Object> expectedMap = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : claims.entrySet()) {
            expectedMap.put(entry.getKey(), entry.getValue());
        }
        return expectedMap;
    }

}
