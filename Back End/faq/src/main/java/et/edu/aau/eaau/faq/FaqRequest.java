package et.edu.aau.eaau.faq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class FaqRequest {
    String question;
    String answer;
    Role role;
}
