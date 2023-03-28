package et.edu.aau.eaau.faq;

import et.edu.aau.eaau.faq.Faq;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface FaqRepository extends MongoRepository<Faq, String> {
    Optional<Faq> findFaqsById(String id);
}
