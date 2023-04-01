package et.edu.aau.eaau.courseManagement.courseMaterial;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseMaterialService {
    private final RestTemplate restTemplate;
    public List<CourseMaterial> getCourseMaterials(String course_id){
        File[] files = restTemplate.getForEntity("http://localhost:8084/file/view-course-materials/"+course_id, File[].class).getBody();
        assert files != null;
        List<File> fileList = Arrays.stream(files).toList();
        return fileList.stream().map(this::mapToCourseMaterial).toList();
    }

    private CourseMaterial mapToCourseMaterial(File file) {
        return CourseMaterial.builder()
                .courseMaterialId(file.getFileId())
                .courseMaterialName(file.getFileName())
                .courseMaterialDescription(file.getDescription())
                .courseMaterialSize(file.getLength())
                .courseId(file.getCourse_id())
                .build();
    }
}
