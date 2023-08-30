package et.edu.aau.eaau.feedback.feedback;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/feedback")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201","http://localhost:4202"})

@Slf4j
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;

    @GetMapping()
    public ResponseEntity<List<FeedbackResponse>> getFeedbacks(){
        if(feedbackService.getFeedback() == null)
            return new ResponseEntity<>(null, HttpStatus.OK);
        return new ResponseEntity<>(feedbackService.getFeedback(), HttpStatus.OK);
    }
    @GetMapping("/archives")
    public ResponseEntity<List<FeedbackResponse>> getArchivedFeedbacks(){
        if(feedbackService.getArchivedFeedbacks() == null)
            return new ResponseEntity<>(null, HttpStatus.OK);
        return new ResponseEntity<>(feedbackService.getArchivedFeedbacks(), HttpStatus.OK);
    }
    @PostMapping()
    public ResponseEntity<String> addFeedbacks(@RequestBody FeedbackRequest feedbackRequest){
        if(feedbackService.addFeedback(feedbackRequest))
        return new ResponseEntity("feedback is sent",HttpStatus.CREATED);
        return new ResponseEntity("invalid email",HttpStatus.BAD_REQUEST);
    }
    @PutMapping("/archive/{id}")
    public ResponseEntity<Map<String,Object>> archiveFeedback(@PathVariable String id){
        Map<String,Object> response = new HashMap<>();
        if(feedbackService.archiveFeedback(id)) {
            response.put("message","feedback is archived");
            return new ResponseEntity(response, HttpStatus.CREATED);
        }
        response.put("message","invalid email");
        return new ResponseEntity(response,HttpStatus.BAD_REQUEST);
    }
    @DeleteMapping("/id/{id}")
    public ResponseEntity<Map<String,Object>> deleteFeedback(@PathVariable String id){
        Map<String,Object> response = new HashMap<>();
        if(feedbackService.deleteFeedback(id)==0) {
            response.put("message","feedback deleted");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        else if(feedbackService.deleteFeedback(id) == 1){
            response.put("message","feedback must be archived first");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        response.put("message","invalid id");
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
}
