package et.edu.aau.eaau.courseManagement.topic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(value = "topics")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Topic {
    @Id
    private String topicId;
    private String topicTitle;
    private String topicDescription;
    private String courseId;

}
