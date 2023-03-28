package et.edu.aau.eaau.courseManagement.lesson;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(value = "lessons")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Lesson {
    @Id
    private String lessonId;
    private String lessonTitle;
    private String lessonVideoId;
    private String lessonDescription;
    private String topicId;

}
