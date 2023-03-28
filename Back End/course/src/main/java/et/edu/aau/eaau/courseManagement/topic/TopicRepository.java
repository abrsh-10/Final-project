package et.edu.aau.eaau.courseManagement.topic;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TopicRepository extends MongoRepository<Topic,String> {
    Optional<List<Topic>> findByCourseId(String courseId);
}
