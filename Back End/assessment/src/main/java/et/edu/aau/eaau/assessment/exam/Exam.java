package et.edu.aau.eaau.assessment.exam;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(value = "exams")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Exam {
    @Id
    private String examId;
    private String examName;
    private List<Question> questions = new ArrayList<>();
    private int weight;
    private LocalDateTime startTime;
    private int duration;
    private boolean isActive;
    private String courseId;
}
