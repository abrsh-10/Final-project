package et.edu.aau.eaau.courseManagement.course;

import et.edu.aau.eaau.courseManagement.courseMaterial.CourseMaterial;
import et.edu.aau.eaau.courseManagement.topic.TopicResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseDto {
    private String id;
    private String courseId;
    private String courseName;
    private String courseDescription;
    private List<CourseMaterial> courseMaterials;
    private List<TopicResponse> topics;
}
