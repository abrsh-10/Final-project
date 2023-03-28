package et.edu.aau.eaau.user.userAccount;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User,String> {
    Optional<User> findUsersByEmail(String email);
    void deleteByEmail(String email);
    @Query("{isAllowed: true}")
    Optional<List<User>> findUsersByIsAllowed();
    Optional<List<User>> findUsersByRole(Role role);

}
