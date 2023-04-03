package et.edu.aau.eaau.courseManagement.course;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends MongoRepository<Course, String>{
    Optional<List<Course>> findByCourseId(String courseId);
}
