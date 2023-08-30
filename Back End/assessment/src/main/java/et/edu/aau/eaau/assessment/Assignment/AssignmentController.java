package et.edu.aau.eaau.assessment.Assignment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/assignment")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201","http://localhost:4202"})
@RequiredArgsConstructor
public class AssignmentController {
    private final AssignmentService assignmentService;
    @GetMapping("id/{courseId}")
    public ResponseEntity<List<Assignment>> getAllAssignments(@PathVariable String courseId){
            return new ResponseEntity(assignmentService.getAssignments(courseId), HttpStatus.OK);
    }
    @PostMapping()
    public ResponseEntity<Map<String,Object>> addAssignment(@RequestParam("file") MultipartFile file,
                                                        @RequestParam("uploader") String uploader,
                                                        @RequestParam("description") String description,
                                                        @RequestParam("course_id") String course_id) throws IOException {
        Map<String,Object> res = new HashMap<>();
        int response = assignmentService.addAssignment(file,uploader,description, course_id);
        if( response== 1){
            res.put("message","course doesn't exist");
            return new ResponseEntity<>(res,HttpStatus.BAD_REQUEST);
        }
        else if(response== 2){
            res.put("message","uploader is not valid");
            return new ResponseEntity<>(res,HttpStatus.BAD_REQUEST);
        }
        else if(response == 3){
            res.put("message","something is wrong with user or course micro service");
            return new ResponseEntity<>(res,HttpStatus.BAD_REQUEST);
        }
        else if(response == 4){
            res.put("message","this user is not a teacher assigned to the course specified");
            return new ResponseEntity<>(res,HttpStatus.BAD_REQUEST);
        }
        res.put("message","assignment successfully added");
        return new ResponseEntity<>(res,HttpStatus.OK);
    }
    @DeleteMapping("delete/{assignmentId}")
    public ResponseEntity<String> deleteAssignment(@PathVariable String assignmentId){
        if(assignmentService.deleteAssignment(assignmentId)){
            return new ResponseEntity<>("assignment deleted",HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>("assignment not found",HttpStatus.NOT_FOUND);
    }
}
