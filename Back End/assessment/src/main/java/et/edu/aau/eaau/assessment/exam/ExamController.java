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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/exam")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201","http://localhost:4202"})
@RequiredArgsConstructor
@EnableScheduling
public class ExamController {
    private final ExamService examService;
    private final ExamRepository examRepository;
    @PostMapping()
    ResponseEntity<Map<String,Object>> createExam(@RequestBody ExamRequest examRequest){
        Map<String,Object> res = new HashMap<>();
           int response =  examService.addExam(examRequest);
           if(response == 1){
               res.put("message","exam weight must be above 0 and below 100");
               return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
           }
        if(response == 2){
            res.put("message","exam start date and time must be after current date and time ");
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        if(response == 3){
            res.put("message","exam duration must be greater than 0");
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
        if(response == 4){
            res.put("message","no course id was found matching the course you have chosen");
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
        if(response == 5){
            res.put("message","no user assigned for this course");
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        if(response == 6){
            res.put("message","creator is not teacher assigned for the course");
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
        if(response == 7){
            res.put("message","exam cannot be created because a student taking this course have an exam on this day");
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        res.put("message","exam successfully created");
            return new ResponseEntity<>(res, HttpStatus.CREATED);
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
    @GetMapping("questions-teacher/{id}")
    public ResponseEntity<List<Exam>> getQuestionsForTeacher(@PathVariable("id") String id) {
        return new ResponseEntity(examService.getQuestionsForTeacher(id), HttpStatus.OK);
    }

    @PutMapping("add-questions/{id}")
    public ResponseEntity addQuestions(@PathVariable("id") String id, @RequestBody List<QuestionDto> questions) {
        int response = examService.addQuestions(questions,id);
        if(response==1){
            System.out.println("1");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        else if(response == 2){
            System.out.println("2");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        else
            return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }
    @PutMapping("remove-question/{id}")
    public ResponseEntity removeQuestions(@PathVariable("id") String id, @RequestParam("questionType") QuestionType questionType,@RequestParam("questionNumber") String questionNum) {
        int response = examService.removeQuestion(id,questionType,Integer.parseInt(questionNum));
        if(response==1){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        else if(response == 2){
            System.out.println("2");
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
    public ResponseEntity changeStartTime(@PathVariable("id") String id, @RequestBody LocalDateTime newStartTime) {
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
