package et.edu.aau.eaau.assessment.exam;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExamRequest {
    private String examName;
    private String CourseId;
    private String creator;
    private int weight;
    private LocalDateTime startTime;
    private int duration;
}
