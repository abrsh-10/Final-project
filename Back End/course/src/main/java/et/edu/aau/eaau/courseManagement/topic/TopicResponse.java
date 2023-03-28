package et.edu.aau.eaau.courseManagement.topic;

import et.edu.aau.eaau.courseManagement.lesson.Lesson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopicResponse {
    private String topicId;
    private String topicTitle;
    private String topicDescription;
    private List<Lesson> lessons;

}
