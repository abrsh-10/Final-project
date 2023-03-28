package et.edu.aau.eaau.user.userAccount;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:4200/")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping()
    ResponseEntity createUser(@RequestBody UserRequest user) {
        if (userService.getUser(user.getEmail()) == null) {
            userService.createUser(user);
            return new ResponseEntity<>(null, HttpStatus.CREATED);
        } else {
            log.info("unable to create new user because email already exists");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping()
    public ResponseEntity<List<User>> getAllUsers() {
        if (userService.getAllUsers() == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity(userService.getAllUsers(), HttpStatus.OK);
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUser(@PathVariable("email") String email) {
        if (userService.getUser(email) == null) {
            System.out.println("user not found");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity(userService.getUser(email), HttpStatus.OK);
        }
    }
    @GetMapping("/role/{role}")
    public ResponseEntity<List<User>> getUsersbyRole(@PathVariable("role") Role role) {
        if (userService.getUsersbyRole(role) == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity(userService.getUsersbyRole(role), HttpStatus.OK);
        }
    }

    @PutMapping("grant/{email}")
    public ResponseEntity grantAccess(@PathVariable("email") String email) {
        boolean isGranted = userService.grantAccess(email);
        if(!isGranted){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        else
            return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }
    @PutMapping("revoke/{email}")
    public ResponseEntity revokeAccess(@PathVariable("email") String email) {
        boolean isRevoked = userService.revokeAccess(email);
        if(!isRevoked){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        else
            return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("email/{email}")
    public ResponseEntity deleteByEmail(@PathVariable("email") String email) {
        if(userService.getUser(email)!=null){
        userService.deleteUser(email);
        return new ResponseEntity(null,HttpStatus.NO_CONTENT);
    }
        else{
            return new ResponseEntity(null,HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/allowed")
    public ResponseEntity<User> getUser() {
        if (userService.getAllowedUser() == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity(userService.getAllowedUser(), HttpStatus.OK);
        }
    }
}
