package et.edu.aau.eaau.assessment.examSolution;

import et.edu.aau.eaau.assessment.exam.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Answer {
    private String answer;
    private int questionNumber;
    private QuestionType questionType;
}
