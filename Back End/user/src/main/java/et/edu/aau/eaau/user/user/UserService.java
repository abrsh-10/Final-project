package et.edu.aau.eaau.user.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

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
        return users;

    }

    public User getUser(String email) {
        Optional<User> optionalUser = userRepository.findUsersByEmail(email);
        if (optionalUser.isPresent()) {

            User user = optionalUser.get();

            return user;
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

    public int assignCourses(String email, String courseId,String teacherEmail) {
        User user = getUser(email);
        if (user == null) {
            return 1;
        }

        if (!user.getIsAllowed()) {
            return 2;
        }

        if (user.getRole() == Role.Admin) {
            return 3;
        }
        ResponseEntity<Course> responseEntity;
        try {
            responseEntity = restTemplate.getForEntity("http://localhost:8083/api/course/id/"+ courseId,Course.class);
        }
        catch (HttpClientErrorException e) {
            return 6;
        }
        Course course = Objects.requireNonNull(responseEntity.getBody());
        if (getUser(email).getRole() == Role.Teacher) {
            boolean isAssigned = false;
                if(course.getTeacherEmail().equals(email)){
                    isAssigned = true;
            }
            if(isAssigned){
                return 4;
            }
            if(Objects.equals(course.getTeacherEmail(), "none")){

                restTemplate.put("http://localhost:8083/api/course/teacher/"+course.getId(),getUser(email).getEmail());
            }
            else{
                course.setTeacherEmail(getUser(email).getEmail());
                restTemplate.postForEntity("http://localhost:8083/api/course",course,Course.class);
            }
            return 0;
        }
        if (getUser(email).getRole() == Role.Student) {
            if(user.getCourses().contains(courseId)){
                return 4;
            }
            boolean valid = false;
                if((course.getId().equals(courseId) && course.getTeacherEmail().equals(teacherEmail))){
                    valid = true;
            }
            if(!valid){
                return 5;
            }
        }
        List<String> ids = user.getCourses();
        ids.add(courseId);
        user.setCourses(ids);
        userRepository.save(user);
        return 0;
    }

    private String mapToObjectId(String courseId,String teacherEmail) {
        ResponseEntity<Course[]> responseEntity;
        try {
            responseEntity = restTemplate.getForEntity("http://localhost:8083/api/course/course-id/"+ courseId,Course[].class);
        }
        catch (HttpClientErrorException e) {
            return null;
        }
        List<Course> courses = Arrays.stream(Objects.requireNonNull(responseEntity.getBody())).toList();
        for(Course course:courses){
            if(course.getTeacherEmail().equals(teacherEmail)){
                return course.getId();
            }
        }
        return null;
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
