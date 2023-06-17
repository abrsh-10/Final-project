package et.edu.aau.eaau.assessment.AssignmentSolution;

import et.edu.aau.eaau.assessment.Assignment.Assignment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/assignment-solution")
@CrossOrigin(origins = "http://localhost:4200/")
@RequiredArgsConstructor
public class AssignmentSolutionController {
    private final AssignmentSolutionService assignmentSolutionService;
    @GetMapping("id/{assignmentId}")
    public ResponseEntity<List<Assignment>> getAssignmentSolutions(@PathVariable String assignmentId){
            return new ResponseEntity(assignmentSolutionService.getAssignmentSolutions(assignmentId), HttpStatus.OK);
    }
    @GetMapping("uploader/{uploader}")
    public ResponseEntity<List<Assignment>> getAssignmentSolutionsByUploader(@PathVariable String uploader){
        return new ResponseEntity(assignmentSolutionService.getAssignmentSolutionsByUploader(uploader), HttpStatus.OK);
    }
    @PostMapping()
    public ResponseEntity<String> addAssignmentSolution(@RequestParam("file") MultipartFile file,
                                    @RequestParam("uploader") String uploader,
                                    @RequestParam("description") String description,
                                    @RequestParam("assignment_id") String assignment_id) throws IOException {
        int response = assignmentSolutionService.addAssignmentSolution(file,uploader,description,assignment_id);
    if( response== 1){
        return new ResponseEntity<>("assignment doesn't exist",HttpStatus.NOT_FOUND);
    }
    else if(response== 2){
            return new ResponseEntity<>("uploader doesn't exist",HttpStatus.NOT_FOUND);
        }
    else if(response == 3){
        return new ResponseEntity<>("something is wrong with user micro service",HttpStatus.INTERNAL_SERVER_ERROR);
    }
        return new ResponseEntity<>("assignment solution sent",HttpStatus.OK);
    }
    @DeleteMapping("delete/{assignmentId}")
    public ResponseEntity<String> deleteAssignmentSolution(@PathVariable String assignmentId){
        if(assignmentSolutionService.deleteAssignmentSolution(assignmentId)){
            return new ResponseEntity<>("assignment solution deleted",HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>("assignment solution not found",HttpStatus.NOT_FOUND);
    }

}

