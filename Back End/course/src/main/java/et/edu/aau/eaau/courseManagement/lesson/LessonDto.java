package et.edu.aau.eaau.courseManagement.lesson;

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
public class LessonDto {
    private String lessonTitle;
    private String lessonVideoId;
    private String lessonDescription;
    private String topicId;

}
