package et.edu.aau.eaau.assessment.Assignment;

import et.edu.aau.eaau.assessment.exam.Course;
import et.edu.aau.eaau.assessment.examSolution.Role;
import et.edu.aau.eaau.assessment.examSolution.User;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssignmentService {
    private final RestTemplate restTemplate;
    public List<Assignment> getAssignments(String course_id){
        File[] files = restTemplate.getForEntity("http://localhost:8084/file/view-assignments/"+course_id, File[].class).getBody();
        assert files != null;
        List<File> fileList = Arrays.stream(files).toList();
        return fileList.stream().map(this::mapToAssignment).toList();
    }
    public Assignment getAssignmentById(String id){
        File file = restTemplate.getForEntity("http://localhost:8084/file/viewfileinformation/"+id, File.class).getBody();
        if(file == null){
            return null;
        }
        return mapToAssignment(file);
    }
    public int addAssignment(MultipartFile file, String uploader, String description, String course_id) throws IOException {
        ResponseEntity<Course> courseResponseEntity;
        try {
            courseResponseEntity = restTemplate.getForEntity("http://localhost:8083/api/course/id/"+ course_id,Course.class);
        }
        catch (HttpClientErrorException e) {
            return 3;
        }

        if(courseResponseEntity.getBody() == null){
            return 1;
        }
        ResponseEntity<User> userResponseEntity;
        try {
            userResponseEntity = restTemplate.getForEntity("http://localhost:8086/api/user/email/"+ uploader,User.class);
        }
        catch (HttpClientErrorException e) {
            return 3;
        }
        if(userResponseEntity.getBody() == null){
            return 2;
        }
        if(!uploader.equals(courseResponseEntity.getBody().getTeacherEmail())){
            return 4;
        }
        String apiUrl = "http://localhost:8084/file/upload";
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        });
        body.add("uploader", uploader);
        body.add("description", description);
        body.add("filetype", "assignment");
        body.add("course_id", course_id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        restTemplate.postForObject(apiUrl, requestEntity, String.class);
        return 0;
    }
    public boolean deleteAssignment(String id){
        String apiUrl = "http://localhost:8084/file/delete/";
        try {
            restTemplate.delete(apiUrl + "{id}", id);
        }
        catch (HttpClientErrorException e){
            return false;
        }
        return true;
    }

    private Assignment mapToAssignment(File file) {
        return Assignment.builder()
                .assignmentId(file.getFileId())
                .assignmentName(file.getFileName())
                .assignmentDescription(file.getDescription())
                .assignmentSize(file.getLength())
                .courseId(file.getCourse_id())
                .build();
    }
}
