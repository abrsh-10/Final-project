package et.edu.aau.eaau.assessment.examSolution;

import et.edu.aau.eaau.assessment.exam.Exam;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ExamSolutionRepository extends MongoRepository<ExamSolution,String> {
    Optional<List<ExamSolution>> findByExamId(String examId);
    Optional<List<ExamSolution>> findByStudentEmail(String email);

}
