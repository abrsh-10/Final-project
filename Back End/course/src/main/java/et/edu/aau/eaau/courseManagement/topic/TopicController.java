package et.edu.aau.eaau.courseManagement.topic;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/topic")
@CrossOrigin(origins = "http://localhost:4200")
public class TopicController {
    private final TopicService topicService;
    @GetMapping("id/{courseId}")
    public ResponseEntity<List<Topic>> getAllTopics(@PathVariable String courseId){
        if(topicService.getAllTopics(courseId) != null){
            return new ResponseEntity(topicService.getAllTopics(courseId), HttpStatus.OK);
        }
        return new ResponseEntity(null, HttpStatus.NOT_FOUND);
    }
    @GetMapping("/topic-with-lessons/id/{courseId}")
    public ResponseEntity<List<Topic>> getAllTopicsWithLessons(@PathVariable String courseId){
        if(topicService.getAllTopicsWithLessons(courseId) != null){
            return new ResponseEntity(topicService.getAllTopicsWithLessons(courseId), HttpStatus.OK);
        }
        return new ResponseEntity(null, HttpStatus.NOT_FOUND);
    }
    @PostMapping()
    public ResponseEntity createTopic(@RequestBody TopicDto topicDto){
        if(topicService.createTopic(topicDto)){
            return new ResponseEntity(HttpStatus.CREATED);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
    @PutMapping("topic-title/{topicId}")
    public ResponseEntity changeTopicTitle(@PathVariable("topicId") String topicId,@RequestBody String newTitle) {
        boolean ischanged = topicService.changeTitle(topicId,newTitle);
        if(!ischanged){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        else
            return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }
    @PutMapping("topic-description/{topicId}")
    public ResponseEntity changeTopicDescription(@PathVariable("topicId") String topicId,@RequestBody String newDescription) {
        boolean ischanged = topicService.changeDescription(topicId,newDescription);
        if(!ischanged){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        else
            return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }
    @DeleteMapping("topic-id/{topicId}")
    public ResponseEntity deleteTopic(@PathVariable("topicId") String topicId) {
        boolean isDeleted = topicService.deleteTopic(topicId);
        if(!isDeleted){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        else
            return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }
}
