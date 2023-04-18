package et.edu.aau.eaau.courseManagement.courseMaterial;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/course-material")
@CrossOrigin(origins = "http://localhost:4200/")
@RequiredArgsConstructor
public class CourseMaterialController {
    private final CourseMaterialService courseMaterialService;
    @GetMapping("id/{courseId}")
    public ResponseEntity<List<CourseMaterial>> getAllCourseMaterials(@PathVariable String courseId){
            return new ResponseEntity(courseMaterialService.getCourseMaterials(courseId), HttpStatus.OK);
    }
    @PostMapping()
    public ResponseEntity<String> addAssignmentSolution(@RequestParam("file") MultipartFile file,
                                                        @RequestParam("uploader") String uploader,
                                                        @RequestParam("description") String description,
                                                        @RequestParam("course_id") String course_id) throws IOException {
        int response = courseMaterialService.addCourseMaterial(file,uploader,description, course_id);
        if( response== 1){
            return new ResponseEntity<>("course doesn't exist",HttpStatus.NOT_FOUND);
        }
        else if(response== 2){
            return new ResponseEntity<>("uploader is not valid",HttpStatus.NOT_FOUND);
        }
        else if(response == 3){
            return new ResponseEntity<>("something is wrong with user micro service",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else if(response == 4){
            return new ResponseEntity<>("this user is not a teacher assigned to the course specified",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("course material uploaded",HttpStatus.OK);
    }
    @DeleteMapping("delete/{courseMaterialId}")
    public ResponseEntity<String> deleteAssignmentSolution(@PathVariable String courseMaterialId){
        if(courseMaterialService.deleteCourseMaterial(courseMaterialId)){
            return new ResponseEntity<>("course material deleted",HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>("course material not found",HttpStatus.NOT_FOUND);
    }
}
