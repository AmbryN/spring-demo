package edu.ambryn.demo.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import edu.ambryn.demo.models.Role;
import edu.ambryn.demo.models.User;
import edu.ambryn.demo.repositories.UserRepository;
import edu.ambryn.demo.security.JwtUtils;
import edu.ambryn.demo.services.FileService;
import edu.ambryn.demo.views.UserView;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(value = "*")
public class UserController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    FileService fileService;

    @Autowired
    JwtUtils jwt;

    @GetMapping("/admin/users")
    @JsonView(UserView.class)
    public Iterable<User> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    @JsonView(UserView.class)
    public ResponseEntity<User> getUser(@PathVariable("id") Integer id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/profile")
    @JsonView(UserView.class)
    public ResponseEntity<User> getProfile(@RequestHeader("Authorization") String bearer) {
        return jwt.extractToken(bearer)
                .flatMap(jwt::getData)
                .map(Claims::getSubject)
                .flatMap(userRepository::findByEmail)
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/profile-picture/{id}")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable int id) {
        Optional<User> oUser = userRepository.findById(id);

        if (oUser.isPresent()) {
            String picture = oUser.get().getProfilePictureUrl();
            try {
                byte[] file = fileService.getPictureByName(picture);
                String mimeType = Files.probeContentType(new File(picture).toPath());
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.valueOf(mimeType));

                return new ResponseEntity<>(file, headers, HttpStatus.OK);
            } catch (FileNotFoundException fnf) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } catch (IOException e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/admin/users")
    public ResponseEntity<?> addUser(@RequestPart("user") User newUser, @Nullable @RequestParam("file") MultipartFile file) {
        if (newUser.getId() != null) {
            return userRepository.findById(newUser.getId())
                    .map(user -> {
                        user.setLastname(newUser.getLastname());
                        user.setFirstname(newUser.getFirstname());
                        user.setEmail(newUser.getEmail());
                        user.setCountry(newUser.getCountry());

                        userRepository.save(user);

                        if (file != null) {
                            try {
                                String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
                                fileService.transferFile(file, filename);
                                user.setProfilePictureUrl(filename);
                                userRepository.save(user);
                            } catch (IOException e) {
                                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                            }
                        }

                        return new ResponseEntity<>(newUser, HttpStatus.OK);
                    })
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        }
        Role userRole = new Role();
        userRole.setId(1);
        newUser.addRole(userRole);
        userRepository.save(newUser);

        if (file != null) {
            try {
                String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
                fileService.transferFile(file, filename);
                newUser.setProfilePictureUrl(filename);
                userRepository.save(newUser);
            } catch (IOException e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/users/{id}")
    public boolean deleteUser(@PathVariable("id") int id) {
        userRepository.deleteById(id);
        return true;
    }
}
