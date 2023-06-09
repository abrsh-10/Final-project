package et.edu.aau.eaau.assessment.exam;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/exam")
@CrossOrigin(origins = "http://localhost:4200/")
@RequiredArgsConstructor
@EnableScheduling
public class ExamController {
    private final ExamService examService;
    private final ExamRepository examRepository;
    @PostMapping()
    ResponseEntity<String> createExam(@RequestBody ExamRequest examRequest){
           int response =  examService.addExam(examRequest);
           if(response == 1){
               return new ResponseEntity<>("exam weight must be above 0 and below 100", HttpStatus.BAD_REQUEST);
           }
        if(response == 2){
            return new ResponseEntity<>("exam start date and time must be after current date and time ", HttpStatus.BAD_REQUEST);
        }
        if(response == 3){
            return new ResponseEntity<>("exam duration must be greater than 0", HttpStatus.BAD_REQUEST);
        }
        if(response == 4){
            return new ResponseEntity<>("no course id was found matching the course you have chosen", HttpStatus.BAD_REQUEST);
        }
        if(response == 5){
            return new ResponseEntity<>("sorry could not contact course or user microservice", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(response == 6){
            return new ResponseEntity<>("creator is not teacher assigned for the course", HttpStatus.BAD_REQUEST);
        }
        if(response == 7){
            return new ResponseEntity<>("exam cannot be created because a student taking this course have an exam on this day", HttpStatus.BAD_REQUEST);
        }
            return new ResponseEntity<>(null, HttpStatus.CREATED);
    }
    @GetMapping()
    public ResponseEntity<List<Exam>> getAllExams() {
            return new ResponseEntity(examService.getAllExams(), HttpStatus.OK);

        }
    @GetMapping("id/{id}")
    public ResponseEntity<Exam> getExam(@PathVariable("id") String id) {
        return new ResponseEntity(examService.getExam(id), HttpStatus.OK);
    }
    @GetMapping("course-id/{courseId}")
    public ResponseEntity<List<Exam>> getExamsByCourse(@PathVariable("courseId") String courseId) {
        return new ResponseEntity(examService.getExamByCourse(courseId), HttpStatus.OK);
    }
    @GetMapping("questions/{id}")
    public ResponseEntity<List<Exam>> getQuestionsByOrder(@PathVariable("id") String id) {
        return new ResponseEntity(examService.getQuestionsByOrder(id), HttpStatus.OK);
    }

    @PutMapping("add-questions/{id}")
    public ResponseEntity addQuestions(@PathVariable("id") String id, @RequestBody List<QuestionDto> questions) {
        int response = examService.addQuestions(questions,id);
        if(response==1){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        else if(response == 2){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        else
            return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }
    @PutMapping("remove-question/{id}/{questionType}/{questionNumber}")
    public ResponseEntity addQuestions(@PathVariable("id") String id, @PathVariable("questionType") QuestionType questionType,@PathVariable("questionNumber") int questionNum) {
        int response = examService.removeQuestion(id,questionType,questionNum);
        if(response==1){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        else if(response == 2){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        else
            return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }
    @PutMapping("change-duration/{id}")
    public ResponseEntity changeDuration(@PathVariable("id") String id, @RequestBody int newDuration) {
        int response = examService.changeDuration(id,newDuration);
        if(response==1){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        else if(response == 2){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        else
            return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }
    @PutMapping("change-weight/{id}")
    public ResponseEntity changeWeight(@PathVariable("id") String id, @RequestBody int newWeight) {
        int response = examService.changeWeight(id,newWeight);
        if(response==1){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        else if(response == 2){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        else
            return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }
    @PutMapping("change-start-time/{id}")
    public ResponseEntity changeDuration(@PathVariable("id") String id, @RequestBody LocalDateTime newStartTime) {
        int response = examService.changeStartTime(id,newStartTime);
        if(response==1){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        else if(response == 2){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        else
            return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }
    @DeleteMapping("delete/{examId}")
    public ResponseEntity<String> deleteExam(@PathVariable String examId){
        if(examService.deleteExam(examId) == 0){
            return new ResponseEntity<>("exam successfully deleted",HttpStatus.NO_CONTENT);
        }
        else if(examService.deleteExam(examId) == 1){
            return new ResponseEntity<>("exam not found",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("exam is currently active ",HttpStatus.NOT_FOUND);

    }
}
