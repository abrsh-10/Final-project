package et.edu.aau.eaau.courseManagement.lesson;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface LessonRepository extends MongoRepository<Lesson,String> {
    Optional<List<Lesson>> findByTopicId(String topicId);
}
