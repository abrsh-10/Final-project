package et.edu.aau.eaau.assessment.examSolution;

import et.edu.aau.eaau.assessment.exam.Exam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exam-solution")
@CrossOrigin(origins = "http://localhost:4200/")
@RequiredArgsConstructor
public class ExamSolutionController {
    private final ExamSolutionService examSolutionService;
    @PostMapping()
    ResponseEntity<String> createExam(@RequestBody ExamSolutionDto examSolutionDto) {
        int response = examSolutionService.addExamSolution(examSolutionDto);
        if (response == 1) {
            return new ResponseEntity<>("no course exists with this id", HttpStatus.BAD_REQUEST);
        }
        else if (response == 2) {
            return new ResponseEntity<>("no user exists with this email", HttpStatus.BAD_REQUEST);
        }
        else if (response == 3) {
            return new ResponseEntity<>("couldn't contact user or course service", HttpStatus.BAD_REQUEST);
        }
        else if (response == 4) {
            return new ResponseEntity<>("student not registered for the course specified", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("exam solution sent", HttpStatus.OK);
    }
    @GetMapping("course-id/{courseId}")
    public ResponseEntity<Exam> getExamByCourse(@PathVariable("courseId") String courseId) {
        return new ResponseEntity(examSolutionService.getByExamId(courseId), HttpStatus.OK);
    }
    @GetMapping("student-email/{studentEmail}")
    public ResponseEntity<Exam> getExamByStudent(@PathVariable("studentEmail") String studentEmail) {
        return new ResponseEntity(examSolutionService.getByStudentEmail(studentEmail), HttpStatus.OK);
    }
    @GetMapping("course-student/{courseId}/{studentEmail}")
    public ResponseEntity<Exam> getExamByStudent(@PathVariable("courseId") String courseId,@PathVariable("studentEmail") String studentEmail) {
        return new ResponseEntity(examSolutionService.getByStudentAndExam(studentEmail,courseId), HttpStatus.OK);
    }

}
