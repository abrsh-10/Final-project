package et.edu.aau.eaau.assessment.exam;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ExamRepository extends MongoRepository<Exam,String> {
    Optional<List<Exam>> findByCourseId(String courseId);
}
