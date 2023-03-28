package et.edu.aau.eaau.feedback;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@CrossOrigin(origins = "http://localhost:4200")
@Slf4j
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;

    @GetMapping()
    public ResponseEntity<List<FeedbackResponse>> getFeedbacks(){
        if(feedbackService.getFeedback() == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(feedbackService.getFeedback(), HttpStatus.OK);
    }
    @GetMapping("/archives")
    public ResponseEntity<List<FeedbackResponse>> getArchivedFeedbacks(){
        if(feedbackService.getArchivedFeedbacks() == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(feedbackService.getArchivedFeedbacks(), HttpStatus.OK);
    }
    @PostMapping()
    public ResponseEntity<String> addFeedbacks(@RequestBody FeedbackRequest feedbackRequest){
        if(feedbackService.addFeedback(feedbackRequest))
        return new ResponseEntity("feedback is sent",HttpStatus.CREATED);
        return new ResponseEntity("invalid email",HttpStatus.BAD_REQUEST);
    }
    @PutMapping("/archive/{id}")
    public ResponseEntity<String> archiveFeedback(@PathVariable String id){
        if(feedbackService.archiveFeedback(id))
            return new ResponseEntity("feedback is archived",HttpStatus.CREATED);
        return new ResponseEntity("invalid email",HttpStatus.BAD_REQUEST);
    }
    @DeleteMapping("/id/{id}")
    public ResponseEntity<String> delteFeedback(@PathVariable String id){
        if(feedbackService.deleteFeedback(id) == 0)
            return new ResponseEntity("feedback is deleted",HttpStatus.CREATED);
        else if(feedbackService.deleteFeedback(id) == 1)
            return new ResponseEntity("archive feedback first",HttpStatus.CREATED);
        return new ResponseEntity("invalid email",HttpStatus.BAD_REQUEST);
    }
}
