package et.edu.aau.eaau.courseManagement.lesson;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/lessons")
public class LessonController {
    private final LessonService lessonService;
    @GetMapping("topic-id/{topicId}")
    public ResponseEntity<List<Lesson>> getAllLessons(@PathVariable String topicId){
        if(lessonService.getAllLessons(topicId) != null){
            return new ResponseEntity(lessonService.getAllLessons(topicId), HttpStatus.OK);
        }
        return new ResponseEntity(null, HttpStatus.NOT_FOUND);
    }
    @PostMapping()
    public ResponseEntity createLesson(@RequestBody LessonDto lessonDto){
        if(lessonService.createLesson(lessonDto)){
            return new ResponseEntity(HttpStatus.CREATED);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
    @PutMapping("lesson-title/{lessonId}")
    public ResponseEntity changeTopicTitle(@PathVariable("lessonId") String lessonId, @RequestBody String newTitle) {
        boolean ischanged = lessonService.changeTitle(lessonId,newTitle);
        if(!ischanged){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        else
            return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }
    @PutMapping("lesson-description/{lessonId}")
    public ResponseEntity changeTopicDescription(@PathVariable("lessonId") String lessonId,@RequestBody String newDescription) {
        boolean ischanged = lessonService.changeDescription(lessonId,newDescription);
        if(!ischanged){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        else
            return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }
    @DeleteMapping("lesson-id/{lessonId}")
    public ResponseEntity deleteTopic(@PathVariable("lessonId") String lessonId) {
        boolean isDeleted = lessonService.deleteTopic(lessonId);
        if(!isDeleted){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        else
            return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }
}
