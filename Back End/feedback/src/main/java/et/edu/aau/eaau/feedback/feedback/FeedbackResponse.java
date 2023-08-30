package et.edu.aau.eaau.feedback.feedback;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackResponse {

    private String id;
    private String message;
    private FeedbackSender sentBy;
    private LocalDateTime sentAt;
    private Boolean isArchived;

}