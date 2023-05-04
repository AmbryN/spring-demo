package edu.ambryn.demo.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import edu.ambryn.demo.models.User;
import edu.ambryn.demo.repositories.UserRepository;
import edu.ambryn.demo.security.JwtUtils;
import edu.ambryn.demo.security.MyUserDetails;
import edu.ambryn.demo.views.UserView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.regex.Pattern;

@RestController
@CrossOrigin("*")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwt;

    @PostMapping("/auth")
    public ResponseEntity authenticate(@RequestBody User user) {
        MyUserDetails userDetails;
        try {
            userDetails = (MyUserDetails) authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                                                                       user.getEmail(),
                                                                       user.getPassword()))
                                                               .getPrincipal();
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(jwt.generateJwt(userDetails));
    }

    @PostMapping("/register")
    @JsonView(UserView.class)
    public ResponseEntity<User> register(@RequestBody User user) {
        if (user.getId() != null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (user.getPassword()
                .length() < 3) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);

        if (!pattern.matcher(user.getEmail())
                    .matches()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<User> oUser = userRepository.findByEmail(user.getEmail());
        if (oUser.isPresent()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
