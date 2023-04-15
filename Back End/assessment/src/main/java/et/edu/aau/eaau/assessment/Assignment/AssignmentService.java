package et.edu.aau.eaau.assessment.Assignment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
