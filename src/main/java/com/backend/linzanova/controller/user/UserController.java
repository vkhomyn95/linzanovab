package com.backend.linzanova.controller.user;

import com.backend.linzanova.dao.IUserDao;
import com.backend.linzanova.dto.*;
import com.backend.linzanova.entity.user.User;
import com.backend.linzanova.exeption.NoRightsException;
import com.backend.linzanova.service.IUserService;
import com.backend.linzanova.service.JwtService;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
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
    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping(value = "/count")
    public Long getUsersCount() {
        log.info("Handling GET /users/count");
        return userService.totalCount();
    }

    //only admin
    @GetMapping
    public UserPageDTO getUsers(@RequestParam int page,
                                @RequestParam int size) {
        log.info("Handling GET /users");
        final Pageable pageable = PageRequest.of(page, size);
        return userService.getAllUsers(pageable);
    }

    //only admin
    @GetMapping(value = "/name")
    public UserPageDTO getUsersByName(@RequestParam int page,
                                      @RequestParam int size,
                                      @RequestParam String name) {
        log.info("Handling GET /users/name where name: " + name);
        final Pageable pageable = PageRequest.of(page, size);
        return userService.getAllUsersByName(pageable, name);
    }

    //only admin or current user id
    @GetMapping(value = "/{id}")
    public User getUser(@PathVariable int id,
                        @RequestHeader(value = "Authorization") String auth){
        log.info("Handling GET /users/" + id);
        String jwtToken = auth.substring(7);
        String user = jwtService.extractUsername(jwtToken);
        if (user != null){
            return userService.getUser(id, user);
        }else {
            throw new NoRightsException("No rights for user: " + user);
        }
    }

    @GetMapping(value = "/auth")
    public AuthIdUserOrderDTO getAuthUser(@RequestHeader(value = "Authorization", required = false) String auth) {
        log.info("Handling GET /users/auth");
        if (auth != null) {
            String jwtToken = auth.substring(7);
            String user = jwtService.extractUsername(jwtToken);
            User byEmail = userService.findByEmail(user);
            return new AuthIdUserOrderDTO(byEmail.getId(), byEmail.getEmail(), byEmail.getPhone(), byEmail.getFirstName(), byEmail.getLastName(), byEmail.getPatronymic(), byEmail.getLocation(), byEmail.getWarehouse(), byEmail.getNumber(), byEmail.getPostIndex());
        }else {
            return null;
        }
    }

    @GetMapping(value = "/stats")
    public UserCabinetStatsDTO getAuthUserStats(@RequestHeader(value = "Authorization", required = false) String auth) {
        log.info("Handling GET /users/stats");
        if (auth != null) {
            String jwtToken = auth.substring(7);
            String user = jwtService.extractUsername(jwtToken);
            User byEmail = userService.findByEmail(user);
            return new UserCabinetStatsDTO(byEmail.getShoppingQuantity(), byEmail.getDeliveredShopsQuantity(), byEmail.getBonusesQuantity());
        }else {
            return null;
        }
    }

    @PostMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public User updateUser(@RequestHeader(value = "Authorization") String auth,
                           @PathVariable int id,
                           @RequestBody User user) {
        log.info("Handling update POST /users/" + id);
        String jwtToken = auth.substring(7);
        String jwtUser = jwtService.extractUsername(jwtToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtUser);
        if (userDetails != null && userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))){

            user.setId(id);
            return userService.updateUser(user);
        }else{
            throw new NoRightsException("No rights for user: " + user);
        }
    }

    @PostMapping(value = "/update")
    @ResponseStatus(HttpStatus.CREATED)
    public UserOrderDTO updateCurrentUser(@RequestHeader(value = "Authorization") String auth,
                                          @RequestBody UserOrderDTO user) {
        log.info("Handling POST /users/update | current user");
        if (auth != null) {
            String jwtToken = auth.substring(7);
            String jwtUser = jwtService.extractUsername(jwtToken);
            User byEmail = userService.findByEmail(jwtUser);
            if (byEmail.getRole().equals("ROLE_USER")) {
                byEmail.setFirstName(user.getFirstName());
                byEmail.setLastName(user.getLastName());
                byEmail.setEmail(user.getEmail());
                byEmail.setNumber(user.getNumber());
                byEmail.setPhone(user.getPhone());
                byEmail.setWarehouse(user.getWarehouse());
                byEmail.setLocation(user.getLocation());
                byEmail.setPostIndex(user.getPostIndex());
                byEmail.setPatronymic(user.getPatronymic());
                userService.updateUser(byEmail);
                return new UserOrderDTO(byEmail.getEmail(), byEmail.getPhone(), byEmail.getFirstName(), byEmail.getLastName(), byEmail.getPatronymic(), byEmail.getLocation(), byEmail.getWarehouse(), byEmail.getNumber(), byEmail.getPostIndex());
            }else {
                throw  new NoRightsException("No rights for user: " + user);
            }
        }else {
            throw new NoRightsException("No rights for user: " + user);
        }
    }
    @PostMapping(value = "/password")
    @ResponseStatus(HttpStatus.CREATED)
    public UserOrderDTO updateCurrentUser(@RequestHeader(value = "Authorization") String auth,
                                          @RequestBody UserPasswordRenewDTO passwordData) {
        log.info("Handling POST /users/password | current user password " + passwordData );
        if (auth != null) {
            String jwtToken = auth.substring(7);
            String jwtUser = jwtService.extractUsername(jwtToken);
            User byEmail = userService.findByEmail(jwtUser);
            if (byEmail.getRole().equals("ROLE_USER") && passwordEncoder.matches(passwordData.getOldPassword(), byEmail.getPassword())) {
                byEmail.setPassword(passwordData.getNewPassword());
                userService.updateUser(byEmail);
                return new UserOrderDTO(byEmail.getEmail(), byEmail.getPhone(), byEmail.getFirstName(), byEmail.getLastName(), byEmail.getPatronymic(), byEmail.getLocation(), byEmail.getWarehouse(), byEmail.getNumber(), byEmail.getPostIndex());
            }else {
                throw  new RuntimeException("Access denied or password not equals");
            }
        }else {
            throw new RuntimeException("Please, auth first");
        }
    }

    @PostMapping(value = "/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User registerUser(@RequestBody @Valid User user) {
        log.info("Handling POST /users/register " + user);
        user.setRole("ROLE_USER");
        userService.insertUser(user);
        return user;
    }

    @PostMapping(value = "/login")
    public AuthenticationResponse generateJWT(@RequestBody AuthRequest authRequest) {
        log.info("Handling POST /users/login");
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        return new AuthenticationResponse(jwtService.generateToken(authRequest.getUsername()));
    }


    @GetMapping(value = "/bool")
    public AdminDTO isAdminUser(@RequestHeader(value = "Authorization", required = false) String auth){
        log.info("Handling POST /users/bool");
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
        log.info("Handling POST /users/refreshtoken");
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
