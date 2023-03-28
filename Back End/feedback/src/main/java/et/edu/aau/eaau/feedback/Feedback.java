package et.edu.aau.eaau.feedback;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(value = "feedbacks")
@Data
@Builder
public class Feedback {
    private String id;
    private String message;
    private String sentBy;
    private LocalDateTime sentAt;
    private Boolean isArchived = false;

}
