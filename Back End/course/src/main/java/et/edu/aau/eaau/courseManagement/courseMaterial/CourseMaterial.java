package et.edu.aau.eaau.courseManagement.courseMaterial;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseMaterial {
    private String courseMaterialId;
    private String courseMaterialName;
    private String courseMaterialDescription;
    private String courseId;

}
