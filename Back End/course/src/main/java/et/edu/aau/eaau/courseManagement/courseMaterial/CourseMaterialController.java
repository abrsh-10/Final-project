package et.edu.aau.eaau.courseManagement.courseMaterial;

import et.edu.aau.eaau.courseManagement.topic.Topic;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course-material")
@CrossOrigin(origins = "http://localhost:4200/")
@RequiredArgsConstructor
public class CourseMaterialController {
    private final CourseMaterialService courseMaterialService;
    @GetMapping("id/{courseId}")
    public ResponseEntity<List<CourseMaterial>> getAllCourseMaterials(@PathVariable String courseId){
            return new ResponseEntity(courseMaterialService.getCourseMaterials(courseId), HttpStatus.OK);
    }
}
