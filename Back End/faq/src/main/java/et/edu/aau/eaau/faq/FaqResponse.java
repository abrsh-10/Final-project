package et.edu.aau.eaau.faq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FaqResponse {

    String Id;
    String question;
    String answer;
}
