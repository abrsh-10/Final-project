package et.edu.aau.eaau.assessment.exam;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ExamRepository extends MongoRepository<Exam,String> {
    Optional<List<Exam>> findByCourseId(String courseId);

    List<Exam> findByActive(boolean b);

    List<Exam> findAllByStartTimeBetween(LocalDateTime of, LocalDateTime of1);
}
