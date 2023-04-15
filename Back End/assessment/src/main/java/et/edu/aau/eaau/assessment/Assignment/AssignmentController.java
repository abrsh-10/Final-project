package et.edu.aau.eaau.assessment.Assignment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
