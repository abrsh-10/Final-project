package et.edu.aau.eaau.assessment.AssignmentSolution;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssignmentSolution {
    private String assignmentSolutionId;
    private String assignmentSolutionName;
    private int assignmentSolutionSize;
    private String assignmentSolutionDescription;
    private String assignmentId;

}
