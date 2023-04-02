package et.edu.aau.eaau.file;

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
public class Course {
    @Id
    private String id;
    private String courseId;
    private String courseName;
    private String courseDescription;

}
