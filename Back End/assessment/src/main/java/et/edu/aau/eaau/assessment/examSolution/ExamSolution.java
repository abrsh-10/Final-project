package et.edu.aau.eaau.assessment.examSolution;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(value = "examSolutions")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExamSolution {
    @Id
    private String solutionId;
    private List<String> answers;
    private String examId;
    private String studentEmail;
    private boolean isSeen;
}
