package et.edu.aau.eaau.file;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface FileInformationRepository extends MongoRepository<FileInformation,String > {

    Optional<FileInformation> findByFileName(String fileName);
}
