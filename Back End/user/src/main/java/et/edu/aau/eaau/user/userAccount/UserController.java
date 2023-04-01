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
    @GetMapping("/students/{course_id}")
    public ResponseEntity<List<User>> getStudentsByCourse(@PathVariable("course_id") String course_id) {
        if (userService.getStudentsByCourse(course_id).size()==0) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
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
    public ResponseEntity<String> addCourse(@PathVariable("email") String email,@PathVariable String course_id) {
        if(userService.assignCourses(email, course_id) == 0)
       return new ResponseEntity<>("course added",HttpStatus.OK);
        if(userService.assignCourses(email, course_id) == 1)
            return new ResponseEntity<>("user not found",HttpStatus.NOT_FOUND);
        if(userService.assignCourses(email, course_id) == 2)
            return new ResponseEntity<>("user not allowed",HttpStatus.BAD_REQUEST);
        if(userService.assignCourses(email, course_id) == 4)
            return new ResponseEntity<>("course already assigned to a teacher",HttpStatus.BAD_REQUEST);
        if(userService.assignCourses(email, course_id) == 5)
            return new ResponseEntity<>("cannot assign a course to a student before assigning to a teacher",HttpStatus.BAD_REQUEST);
        if(userService.assignCourses(email, course_id) == 6)
            return new ResponseEntity<>("course id is not valid",HttpStatus.BAD_REQUEST);
        if(userService.assignCourses(email, course_id) == 7)
            return new ResponseEntity<>("course id already assigned to this user",HttpStatus.BAD_REQUEST);
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
