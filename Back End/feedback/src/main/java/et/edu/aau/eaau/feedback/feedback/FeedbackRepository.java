package et.edu.aau.eaau.feedback.feedback;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends MongoRepository<Feedback,String> {
    @Query("{isArchived: false}")
    Optional<List<Feedback>> findFeedbacks();
    @Query("{isArchived: true}")
    Optional<List<Feedback>> findArchivedFeedbacks();
}
