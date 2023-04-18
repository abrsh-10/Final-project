package et.edu.aau.eaau.assessment.AssignmentSolution;

import et.edu.aau.eaau.assessment.Assignment.Assignment;
import et.edu.aau.eaau.assessment.Assignment.AssignmentService;
import et.edu.aau.eaau.assessment.Assignment.File;
import et.edu.aau.eaau.assessment.examSolution.User;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
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
public class AssignmentSolutionService {
    private final RestTemplate restTemplate;
    private final AssignmentService assignmentService;

    public List<AssignmentSolution> getAssignmentSolutions(String assignment_id) {
        File[] files = restTemplate.getForEntity("http://localhost:8084/file/view-solutions/" + assignment_id, File[].class).getBody();
        assert files != null;
        List<File> fileList = Arrays.stream(files).toList();
        return fileList.stream().map(this::mapToAssignmentSolution).toList();
    }

    public int addAssignmentSolution(MultipartFile file, String uploader, String description, String assignment_id) throws IOException {
        Assignment assignment = assignmentService.getAssignmentById(assignment_id);
        if(assignment ==null){
            return 1;
        }
        ResponseEntity<User> userResponseEntity;
        try {
            userResponseEntity = restTemplate.getForEntity("http://localhost:8080/api/user/email/"+ uploader,User.class);
        }
        catch (HttpClientErrorException e) {
            return 3;
        }

        if(userResponseEntity.getBody() == null){
            return 2;
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
        body.add("filetype", "solution");
        body.add("course_id", assignment_id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        restTemplate.postForObject(apiUrl, requestEntity, String.class);
        return 0;
    }
    public boolean deleteAssignmentSolution(String id){
        String apiUrl = "http://localhost:8084/file/delete/";
        try {
            restTemplate.delete(apiUrl + "{id}", id);
        }
        catch (HttpClientErrorException e){
            return false;
        }
        return true;
    }

    private AssignmentSolution mapToAssignmentSolution(File file) {
        return AssignmentSolution.builder()
                .assignmentSolutionId(file.getFileId())
                .assignmentSolutionName(file.getFileName())
                .assignmentSolutionDescription(file.getDescription())
                .assignmentSolutionSize(file.getLength())
                .assignmentId(file.getCourse_id())
                .build();
    }
}