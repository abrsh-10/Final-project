package et.edu.aau.eaau.assessment.examSolution;

import et.edu.aau.eaau.assessment.exam.Course;
import et.edu.aau.eaau.assessment.exam.Exam;
import et.edu.aau.eaau.assessment.exam.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExamSolutionService {
    private final ExamSolutionRepository examSolutionRepository;
    private final ExamService examService;
    private final RestTemplate restTemplate;
    public int addExamSolution(ExamSolutionDto examSolutionDto){
        Exam exam = examService.getExam(examSolutionDto.getExamId());
        if(exam==null){
            return 1;
        }
        ResponseEntity<User> userResponseEntity;
        ResponseEntity<Course> courseResponseEntity;
        try {
            userResponseEntity = restTemplate.getForEntity("http://localhost:8080/api/user/email/"+ examSolutionDto.getStudentEmail(),User.class);
        }
        catch (HttpClientErrorException e) {
            return 3;
        }

        if(userResponseEntity.getBody() == null){
            return 2;
        }
        try {
            courseResponseEntity = restTemplate.getForEntity("http://localhost:8083/api/course/id/"+ exam.getCourseId(),Course.class);
        }
        catch (HttpClientErrorException e) {
            return 3;
        }
List<String> ids = userResponseEntity.getBody().getCourses();
        if(!ids.contains(courseResponseEntity.getBody().getCourseId())){
            return 4;
        }
        ExamSolution examSolution = ExamSolution.builder()
                .answers(examSolutionDto.getAnswers())
                .examId(examSolutionDto.getExamId())
                .studentEmail(examSolutionDto.getStudentEmail())
                .isSeen(false)
                .build();
        examSolutionRepository.save(examSolution);
        return 0;
    }

    public List<ExamSolution> getByExamId(String examId){
        Optional<List<ExamSolution>> optionalExam = examSolutionRepository.findByExamId(examId);
        return optionalExam.orElse(null);
    }
    public List<ExamSolution> getByStudentEmail(String studentEmail){
        Optional<List<ExamSolution>> optionalExam = examSolutionRepository.findByStudentEmail(studentEmail);
        return optionalExam.orElse(null);
    }
    public List<ExamSolution> getByStudentAndExam(String studentEmail, String examId){
         List<ExamSolution> examSolutions = getByStudentEmail(studentEmail);
         if(examSolutions.size() == 0){
             return null;
         }
         List<ExamSolution> filteredExamSolutions = new ArrayList<>();
        for(ExamSolution examSolution:examSolutions){
             if(examSolution.getExamId() == examId){
                 filteredExamSolutions.add(examSolution);
             }
         }
        return filteredExamSolutions;
    }
}
