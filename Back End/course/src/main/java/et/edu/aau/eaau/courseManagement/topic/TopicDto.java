package et.edu.aau.eaau.courseManagement.topic;

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
public class TopicDto {
    private String topicTitle;
    private String topicDescription;
    private String courseId;

}
