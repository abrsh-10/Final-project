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
    ResponseEntity createCourse(@RequestBody CourseRequest courseRequest){
        if(courseService.getCourse(courseRequest.getCourseId())==null) {
            courseService.createCourse(courseRequest);
            return new ResponseEntity<>(null, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
    @GetMapping()
    public ResponseEntity<List<Course>> getAllCourses() {
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
    @GetMapping("/id/{id}")
    public ResponseEntity<Course> getCourse(@PathVariable("id") String id) {
            return new ResponseEntity(courseService.getCourse(id), HttpStatus.OK);
        }
    @GetMapping("/course-id/{courseId}")
    public ResponseEntity<Course> getCourseByCourseId(@PathVariable("courseId") String courseId) {
        if (courseService.getCourseByCourseId(courseId) == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity(courseService.getCourseByCourseId(courseId), HttpStatus.OK);
        }
    }
    @GetMapping("/course-with-full-information/id/{id}")
    public ResponseEntity<CourseDto> getCourseWithFullInformation(@PathVariable("id") String id) {
        if (courseService.getCourseWithFullInformation(id) == null) {
            System.out.println("course not found");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity(courseService.getCourseWithFullInformation(id), HttpStatus.OK);
        }
    }
    @PutMapping("description/{id}")
    public ResponseEntity changeCourseDescription(@PathVariable("id") String id, @RequestBody String newDescription) {
        boolean ischanged = courseService.changeCourseDescription(id,newDescription);
        if(!ischanged){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        else
            return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }
    @PutMapping("teacher/{id}")
    public ResponseEntity changeTeacher(@PathVariable("id") String id, @RequestBody String newEmail) {
        boolean ischanged = courseService.changeTeacher(id,newEmail);
        if(!ischanged){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        else
            return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }
}
