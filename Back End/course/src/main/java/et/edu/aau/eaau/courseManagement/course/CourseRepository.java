package et.edu.aau.eaau.courseManagement.course;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CourseRepository extends MongoRepository<Course, String>{
    Optional<Course> findByCourseId(String courseId);
}
