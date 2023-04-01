package et.edu.aau.eaau.user.userAccount;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Getter
@Setter
public class UserService {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    public void createUser(UserRequest userRequest) {
        User user = User.builder().
                firstName(userRequest.getFirstName()).
                lastName(userRequest.getLastName()).
                email(userRequest.getEmail()).
                role(userRequest.getRole()).
                isAllowed(userRequest.getIsAllowed())
                .build();
        userRepository.save(user);
        log.info("User with id {} is successfully saved to the database ", user.getId());
    }

    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.size() == 0)
            return null;
        return users.stream().map(this::mapToUser).toList();

    }

    public User getUser(String email) {
        Optional<User> optionalUser = userRepository.findUsersByEmail(email);
        if (optionalUser.isPresent()) {

            User user = optionalUser.get();

            return mapToUser(user);
        } else {
            return null;
        }
    }

    public List<User> getUsersbyRole(Role role) {
        Optional<List<User>> optionalUsers = userRepository.findUsersByRole(role);
        if (optionalUsers.isPresent()) {
            List<User> users = optionalUsers.get();
            if (users.size() == 0)
                return null;
            return users;
        } else {
            return null;
        }
    }

    public void deleteUser(String email) {
        userRepository.deleteByEmail(email);
    }

    public List<User> getAllowedUser() {
        Optional<List<User>> optionalUsers = userRepository.findUsersByIsAllowed();
        if (optionalUsers.isPresent()) {

            List<User> users = optionalUsers.get();

            return users;
        } else {
            return null;
        }

    }

    public boolean grantAccess(String email) {
        Optional<User> optionalUser = userRepository.findUsersByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setIsAllowed(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public boolean revokeAccess(String email) {

        Optional<User> optionalUser = userRepository.findUsersByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setIsAllowed(false);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public int assignCourses(String email, String course_id) {
        if (getUser(email) == null) {
            return 1;
        }

        if (!getUser(email).getIsAllowed()) {
            return 2;
        }

        if (getUser(email).getRole() == Role.Admin) {
            return 3;
        }
        ResponseEntity<Course> responseEntity;
        try {
            responseEntity = restTemplate.getForEntity("http://localhost:8083/api/course/id/"+course_id,Course.class);
        } catch (HttpClientErrorException e) {
            return 6;
        }

        if (getUser(email).getRole() == Role.Teacher) {
            List<User> teachers = getUsersbyRole(Role.Teacher);
            for (User teacher : teachers) {
                List<String> courses = teacher.getCourses();
                if(courses.contains(course_id)){
                    return 4;
                }
            }
        }
        if (getUser(email).getRole() == Role.Student) {
            boolean isTeacherAssigned = false;
            List<User> teachers = getUsersbyRole(Role.Teacher);
            for (User teacher : teachers) {
                List<String> courses = teacher.getCourses();
                if(courses.contains(course_id)){
                    isTeacherAssigned = true;
                    break;
                }
            }
            if(!isTeacherAssigned){
                return 5;
            }
        }
        User user = userRepository.findUsersByEmail(email).get();
        List<String> ids = user.getCourses();
        List<String> courseIds = getUser(email).getCourses();
        if(courseIds.contains(responseEntity.getBody().getCourseId())){
            return 7;
        }
        ids.add(course_id);
        user.setCourses(ids);
        userRepository.save(user);
        return 0;
    }
    public int removeCourse(String email, String course_id){
        if (getUser(email) == null) {
            return 1;
        }

        if (!getUser(email).getIsAllowed()) {
            return 2;
        }

        if (getUser(email).getRole() == Role.Admin) {
            return 3;
        }
        User user = getUser(email);
        List<String> courses = user.getCourses();
        if(!courses.contains(course_id)){
            return 4;
        }
        courses.remove(courses.indexOf(course_id));
        user.setCourses(courses);
        userRepository.save(user);
        return 0;
    }
    public List<User> getStudentsByCourse(String course_id){
        List<User> students = getUsersbyRole(Role.Student);
        List<User> studentsTaking = new ArrayList<>();
        for (User student : students) {
            if(student.getCourses().contains(course_id)){
                studentsTaking.add(student);
            }
        }
        return studentsTaking;
    }
    public User getTeacherByCourse(String course_id){
        List<User> teachers = getUsersbyRole(Role.Teacher);
        for (User teacher : teachers) {
            if(teacher.getCourses().contains(course_id)){
               return teacher;
            }
        }
        return null;
    }
    private User mapToUser(User user) {
        List<String> courses = new ArrayList<>();
        for(String course: user.getCourses()){
            ResponseEntity<Course> responseEntity;
            try {
                responseEntity = restTemplate.getForEntity("http://localhost:8083/api/course/id/"+course,Course.class);
            } catch (HttpClientErrorException e) {
                return user;
            }

            courses.add(responseEntity.getBody().getCourseId());
        }
        user.setCourses(courses);
        return user;

    }
}
