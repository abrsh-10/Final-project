package et.edu.aau.eaau.file;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static et.edu.aau.eaau.file.Role.Student;
import static et.edu.aau.eaau.file.Role.Teacher;
import static et.edu.aau.eaau.file.Type.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;


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

                DBObject metadata = new BasicDBObject();
                metadata.put("info", data);


                Object fileID = template.store(upload.getInputStream(), upload.getOriginalFilename(), upload.getContentType(), metadata);

                return 0;
    }


    public File downloadFile(String id) throws IOException {

        GridFSFile gridFSFile = template.findOne( new Query(where("_id").is(id)) );

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
    public List<FileInformation> getAllCourseMaterials(String course_id){
        List<FileInformation> fileInformations = fileInformationRepository.findAll();
        List<FileInformation> courseMaterials = new ArrayList<>();
        for(FileInformation fileInformation:fileInformations){
            if(fileInformation.getFileType().equals(coursematerial)&&fileInformation.getCourse_id().equals(course_id)){
                courseMaterials.add(fileInformation);
            }
        }
        return courseMaterials;
    }
    public List<FileInformation> getAllAssignments(String course_id){
        List<FileInformation> fileInformations = fileInformationRepository.findAll();
        List<FileInformation> assignments = new ArrayList<>();
        for(FileInformation fileInformation:fileInformations){
            if(fileInformation.getFileType().equals(assignment)&&fileInformation.getCourse_id().equals(course_id)){
                assignments.add(fileInformation);
            }
        }
        return assignments;
    }
    public List<FileInformation> getAllSolutions(String assignment_id){
        List<FileInformation> fileInformations = fileInformationRepository.findAll();
        List<FileInformation> solutions = new ArrayList<>();
        for(FileInformation fileInformation:fileInformations){
            if(fileInformation.getFileType().equals(solution)&&fileInformation.getCourse_id().equals(assignment_id)){
                solutions.add(fileInformation);
            }
        }
        return solutions;
    }
    public FileInformation getFileInformation(String id){
        Optional<FileInformation> optionalFileInformation = fileInformationRepository.findById(id);
        if(optionalFileInformation.isPresent()){
            FileInformation fileInformation = optionalFileInformation.get();
            return fileInformation;
        }
        return null;
    }
    public boolean deleteFile(String fileId){
        if(getFileInformation(fileId)==null){
            return false;
        }
        template.delete(query(where("_id").is(fileId)));
        return true;
    }

}