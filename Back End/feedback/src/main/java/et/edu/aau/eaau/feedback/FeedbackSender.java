package et.edu.aau.eaau.feedback;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackSender {

    private String firstName;
    private String lastName;
    private String email;
    private Role role;

}