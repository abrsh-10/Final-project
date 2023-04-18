package et.edu.aau.eaau.courseManagement.courseMaterial;

import et.edu.aau.eaau.courseManagement.course.Course;
import et.edu.aau.eaau.courseManagement.course.CourseRepository;
import et.edu.aau.eaau.courseManagement.course.CourseService;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseMaterialService {
    private final RestTemplate restTemplate;
    private final CourseRepository courseRepository;
    public List<CourseMaterial> getCourseMaterials(String course_id){
        File[] files = restTemplate.getForEntity("http://localhost:8084/file/view-course-materials/"+course_id, File[].class).getBody();
        assert files != null;
        List<File> fileList = Arrays.stream(files).toList();
        return fileList.stream().map(this::mapToCourseMaterial).toList();
    }
    public int addCourseMaterial(MultipartFile file, String uploader, String description, String course_id) throws IOException {
        Optional<Course> optionalCourse = courseRepository.findById(course_id);
        if(optionalCourse.isEmpty()){
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
        if(!uploader.equals(optionalCourse.get().getTeacherEmail())){
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
        body.add("filetype", "coursematerial");
        body.add("course_id", course_id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        restTemplate.postForObject(apiUrl, requestEntity, String.class);
        return 0;
    }
    public boolean deleteCourseMaterial(String id){
        String apiUrl = "http://localhost:8084/file/delete/";
        try {
            restTemplate.delete(apiUrl + "{id}", id);
        }
        catch (HttpClientErrorException e){
            return false;
        }
        return true;
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
