package et.edu.aau.eaau.assessment.Assignment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Assignment {
    private String assignmentId;
    private String assignmentName;
    private int assignmentSize;
    private String assignmentDescription;
    private String courseId;

}
