package et.edu.aau.eaau.courseManagement.course;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course")
@CrossOrigin(origins = "http://localhost:4200/")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    @PostMapping()
    ResponseEntity createCourse(@RequestBody Course course){
        if(courseService.getCourse(course.getCourseId())==null) {
            courseService.createCourse(course);
            return new ResponseEntity<>(null, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
    @GetMapping()
    public ResponseEntity<List<Course>> getAllUsers() {
        if (courseService.getAllCourses() == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity(courseService.getAllCourses(), HttpStatus.OK);
        }
    }
    @GetMapping("/course-with-full-information")
    public ResponseEntity<List<CourseDto>> getAllCoursesWithFullInformation() {
        if (courseService.getAllCoursesWithFullInformation() == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity(courseService.getAllCoursesWithFullInformation(), HttpStatus.OK);
        }
    }
    @GetMapping("/courseid/{courseId}")
    public ResponseEntity<Course> getCourse(@PathVariable("courseId") String courseId) {
        if (courseService.getCourse(courseId) == null) {
            System.out.println("course not found");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity(courseService.getCourse(courseId), HttpStatus.OK);
        }
    }
    @GetMapping("/course-with-full-information/courseid/{courseId}")
    public ResponseEntity<CourseDto> getCourseWithFullInformation(@PathVariable("courseId") String courseId) {
        if (courseService.getCourseWithFullInformation(courseId) == null) {
            System.out.println("course not found");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity(courseService.getCourseWithFullInformation(courseId), HttpStatus.OK);
        }
    }
    @PutMapping("description/{courseId}")
    public ResponseEntity changeCourseDescription(@PathVariable("courseId") String courseId,@RequestBody String newDescription) {
        boolean ischanged = courseService.changeCourseDescription(courseId,newDescription);
        if(!ischanged){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        else
            return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }
}
