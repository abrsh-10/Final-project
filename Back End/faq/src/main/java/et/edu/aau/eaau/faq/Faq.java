package et.edu.aau.eaau.faq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "Faq")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Faq {
    @Id
    private String id;
    private String question;
    private String answer;
    private Role role;
}
