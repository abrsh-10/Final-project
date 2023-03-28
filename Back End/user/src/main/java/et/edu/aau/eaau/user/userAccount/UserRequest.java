package et.edu.aau.eaau.user.userAccount;

import et.edu.aau.eaau.user.userAccount.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private Boolean isAllowed;
}
