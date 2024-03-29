package et.edu.aau.eaau.user.user;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201","http://localhost:4202"})
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody String id_token){
        Map<String, Object> response = new HashMap<>();
        // Verify the ID token with Google
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList("159479957223-qka39ssrtraeilbuhv0a5r07sn43oemq.apps.googleusercontent.com"))
                .build();
        GoogleIdToken idToken;
        try {
            idToken = verifier.verify(id_token);
        } catch (GeneralSecurityException | IOException e) {
            response.put("message","invalid token");
            // Handle the exception
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
        if (idToken == null) {
            System.out.println("h");
            response.put("message","invalid token");
            // Invalid ID token
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);

        }

        // Get the user's email from the ID token
        String email = idToken.getPayload().getEmail();
       User user = userService.getUser(email);
       if(user == null || !user.getIsAllowed()){
           System.out.println(email);
           response.put("message","user not allowed");
           return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
       }
          response.put("email",email);
          response.put("role",user.getRole());
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @PostMapping()
    public ResponseEntity<?> addUser(@RequestBody UserRequest user){
        userService.createUser(user);
        return new ResponseEntity<>("user added",HttpStatus.CREATED);
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
            return new ResponseEntity(userService.getUser(email), HttpStatus.OK);
    }
    @GetMapping("/role/{role}")
    public ResponseEntity<List<User>> getUsersbyRole(@PathVariable("role") Role role) {
        if (userService.getUsersbyRole(role) == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity(userService.getUsersbyRole(role), HttpStatus.OK);
        }
    }
    @GetMapping("/students/{course_id}")
    public ResponseEntity<List<User>> getStudentsByCourse(@PathVariable("course_id") String course_id) {
        if (userService.getStudentsByCourse(course_id).size()==0) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        } else {
            return new ResponseEntity(userService.getStudentsByCourse(course_id), HttpStatus.OK);
        }
    }
    @GetMapping("/teacher/{course_id}")
    public ResponseEntity<User> getTeacherByCourse(@PathVariable("course_id") String course_id) {
        if (userService.getTeacherByCourse(course_id) == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity(userService.getTeacherByCourse(course_id), HttpStatus.OK);
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
    @PutMapping("add-course/{email}/{course_id}")
    public ResponseEntity<String> addCourse(@PathVariable("email") String email, @PathVariable String course_id,@RequestParam(name = "teacherEmail", required = false) String teacherEmail) {
        if(userService.assignCourses(email, course_id,teacherEmail) == 0)
       return new ResponseEntity<>("course added",HttpStatus.OK);
        if(userService.assignCourses(email, course_id,teacherEmail) == 1)
            return new ResponseEntity<>("user not found",HttpStatus.NOT_FOUND);
        if(userService.assignCourses(email, course_id,teacherEmail) == 2)
            return new ResponseEntity<>("user not allowed",HttpStatus.BAD_REQUEST);
        if(userService.assignCourses(email, course_id,teacherEmail) == 4)
            return new ResponseEntity<>("course already assigned to the user",HttpStatus.BAD_REQUEST);
        if(userService.assignCourses(email, course_id,teacherEmail) == 5)
            return new ResponseEntity<>("cannot assign a course to a student before assigning to a teacher",HttpStatus.BAD_REQUEST);
        if(userService.assignCourses(email, course_id,teacherEmail) == 6)
            return new ResponseEntity<>("course id is not valid",HttpStatus.BAD_REQUEST);
        if(userService.assignCourses(email, course_id,teacherEmail) == 7)
            return new ResponseEntity<>("course id already assigned to this user",HttpStatus.BAD_REQUEST);
        if(userService.assignCourses(email, course_id,teacherEmail) == 8)
            return new ResponseEntity<>("could not contact course service",HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>("user is an admin",HttpStatus.BAD_REQUEST);
    }
    @PutMapping("remove-course/{email}/{course_id}")
    public ResponseEntity<String> removeCourse(@PathVariable("email") String email,@PathVariable String course_id) {
        if(userService.removeCourse(email, course_id) == 0)
            return new ResponseEntity<>("course removed",HttpStatus.OK);
        if(userService.removeCourse(email, course_id) == 1)
            return new ResponseEntity<>("user not found",HttpStatus.NOT_FOUND);
        if(userService.removeCourse(email, course_id) == 2)
            return new ResponseEntity<>("user not allowed",HttpStatus.BAD_REQUEST);
        if(userService.removeCourse(email, course_id) == 4)
            return new ResponseEntity<>("course doesn't exist",HttpStatus.NOT_FOUND);
        return new ResponseEntity<>("user is an admin",HttpStatus.BAD_REQUEST);
    }
}
