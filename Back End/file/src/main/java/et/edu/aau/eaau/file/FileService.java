package et.edu.aau.eaau.file;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static et.edu.aau.eaau.file.Role.Student;
import static et.edu.aau.eaau.file.Role.Teacher;
import static et.edu.aau.eaau.file.Type.*;


@Service
public class FileService {
    @Autowired
    private FileInformationRepository fileInformationRepository;

    @Autowired
    private GridFsTemplate template;

    @Autowired
    private GridFsOperations operations;
    @Autowired
    private RestTemplate restTemplate;

    public int addFile(MultipartFile upload, MetaData data) throws IOException {
        ResponseEntity<UserResponse> responseEntity;
        try {
             responseEntity = restTemplate.getForEntity("http://localhost:8080/api/user/email/"+data.getUploader(),UserResponse.class);
        } catch (HttpClientErrorException e) {
            return 1;
        }
        if(responseEntity.getStatusCodeValue() == 200){
            Role role = responseEntity.getBody().getRole();
            if(((role == Student) && (data.getFileType() == solution)) ||
                    ((role == Teacher) && ((data.getFileType() == assignment) || (data.getFileType() == coursematerial))))
            {

                DBObject metadata = new BasicDBObject();
                metadata.put("info", data);


                Object fileID = template.store(upload.getInputStream(), upload.getOriginalFilename(), upload.getContentType(), metadata);

                return 0;
            }
            else{
                return 2;
            }
        }
        return 1;
    }


    public File downloadFile(String id) throws IOException {

        GridFSFile gridFSFile = template.findOne( new Query(Criteria.where("_id").is(id)) );

        File file = new File();

        if (gridFSFile != null && gridFSFile.getMetadata() != null) {
            file.setFilename( gridFSFile.getFilename() );

            file.setFileType( gridFSFile.getMetadata().get("_contentType").toString() );

            file.setFileSize(String.valueOf(gridFSFile.getLength()));

            file.setFile( IOUtils.toByteArray(operations.getResource(gridFSFile).getInputStream()) );
        }
        return file;
    }
    public List<FileInformation> getAllFileInformations(){
        List<FileInformation> fileInformations = fileInformationRepository.findAll();
        return fileInformations;
    }
    public FileInformation getFileInformation(String id){
        Optional<FileInformation> optionalFileInformation = fileInformationRepository.findById(id);
        if(optionalFileInformation.isPresent()){
            FileInformation fileInformation = optionalFileInformation.get();
            return fileInformation;
        }
        return null;
    }

}