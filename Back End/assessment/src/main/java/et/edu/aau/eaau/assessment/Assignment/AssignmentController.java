package et.edu.aau.eaau.assessment.Assignment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/assignment")
@CrossOrigin(origins = "http://localhost:4200/")
@RequiredArgsConstructor
public class AssignmentController {
    private final AssignmentService assignmentService;
    @GetMapping("id/{courseId}")
    public ResponseEntity<List<Assignment>> getAllAssignments(@PathVariable String courseId){
            return new ResponseEntity(assignmentService.getAssignments(courseId), HttpStatus.OK);
    }
    @PostMapping()
    public ResponseEntity<String> addAssignmentSolution(@RequestParam("file") MultipartFile file,
                                                        @RequestParam("uploader") String uploader,
                                                        @RequestParam("description") String description,
                                                        @RequestParam("course_id") String course_id) throws IOException {
        int response = assignmentService.addAssignment(file,uploader,description, course_id);
        if( response== 1){
            return new ResponseEntity<>("course doesn't exist",HttpStatus.OK);
        }
        else if(response== 2){
            return new ResponseEntity<>("uploader is not valid",HttpStatus.OK);
        }
        else if(response == 3){
            return new ResponseEntity<>("something is wrong with user or course micro service",HttpStatus.OK);
        }
        else if(response == 4){
            return new ResponseEntity<>("this user is not a teacher assigned to the course specified",HttpStatus.OK);
        }
        return new ResponseEntity<>("assignment uploaded",HttpStatus.OK);
    }
    @DeleteMapping("delete/{assignmentId}")
    public ResponseEntity<String> deleteAssignmentSolution(@PathVariable String assignmentId){
        if(assignmentService.deleteAssignment(assignmentId)){
            return new ResponseEntity<>("assignment deleted",HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>("assignment not found",HttpStatus.NOT_FOUND);
    }
}
