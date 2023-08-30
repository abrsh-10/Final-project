package et.edu.aau.eaau.user.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(value = "courses")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Course {
    @Id
    private String id;
    private String courseId;
    private String courseName;
    private String courseDescription;
    private String courseImage;
    private String teacherEmail;

}
