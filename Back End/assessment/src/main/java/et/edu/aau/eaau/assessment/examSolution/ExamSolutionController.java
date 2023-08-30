package et.edu.aau.eaau.assessment.examSolution;

import et.edu.aau.eaau.assessment.exam.Exam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/exam-solution")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201","http://localhost:4202"})
@RequiredArgsConstructor
public class ExamSolutionController {
    private final ExamSolutionService examSolutionService;
    @PostMapping()
    ResponseEntity<Map<String, Object>> PostExamSolution(@RequestBody ExamSolutionDto examSolutionDto) {
        Map<String, Object> res = new HashMap<>();
        int response = examSolutionService.addExamSolution(examSolutionDto);
        if (response == 1) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);//no course exists with this id
        }
        else if (response == 2) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);//"no user exists with this email"
        }
        else if (response == 3) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);//couldn't contact user or course service
        }
        else if (response == 4) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);//student not registered for the course specified
        }
        else if (response == 5) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);//"this student have already submitted a solution for the exam specified"
        }
        res.put("success","exam solution sent");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
    @GetMapping("exam-id/{examId}")
    public ResponseEntity<List<ExamSolution>> getExamSolutionByExam(@PathVariable("examId") String examId) {
        return new ResponseEntity<>(examSolutionService.getByExamId(examId), HttpStatus.OK);
    }
    @GetMapping("student-email/{studentEmail}")
    public ResponseEntity<List<ExamSolution>> getExamSolutionByStudent(@PathVariable("studentEmail") String studentEmail) {
        return new ResponseEntity<>(examSolutionService.getByStudentEmail(studentEmail), HttpStatus.OK);
    }
    @GetMapping("exam-student/{examId}")
    public ResponseEntity<ExamSolution> getExamSolutionByExamAndStudent(@PathVariable("examId") String examId, @RequestParam("studentEmail") String studentEmail) {
        return new ResponseEntity<>(examSolutionService.getByStudentAndExam(studentEmail, examId), HttpStatus.OK);
    }
    @PutMapping("/mark-seen/{id}")
    public ResponseEntity<String> markSeen(@PathVariable String id){
        if(examSolutionService.markSeen(id))
            return new ResponseEntity<>("solution is marked as seen",HttpStatus.CREATED);
        return new ResponseEntity<>("invalid email",HttpStatus.BAD_REQUEST);
    }

}
