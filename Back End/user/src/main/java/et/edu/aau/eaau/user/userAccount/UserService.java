package et.edu.aau.eaau.user.userAccount;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Getter
@Setter
public class UserService {

    private final UserRepository userRepository;
    public void createUser(UserRequest userRequest){
        User user = User.builder().
                firstName(userRequest.getFirstName()).
                lastName(userRequest.getLastName()).
                email(userRequest.getEmail()).
                role(userRequest.getRole()).
                isAllowed(userRequest.getIsAllowed())
                        .build();
        userRepository.save(user);
        log.info("User with id {} is successfully saved to the database ",user.getId());
    }
    public List<User> getAllUsers(){
        List<User> users = userRepository.findAll();
        if(users.size()==0)
            return null;
        return users;

    }
    public User getUser(String email){
        Optional<User> optionalUser = userRepository.findUsersByEmail(email);
        if(optionalUser.isPresent()) {

            User user = optionalUser.get();

            return user;
        }
        else {
            return null;
        }
        }
        public List<User>getUsersbyRole(Role role){
        Optional<List<User>> optionalUsers = userRepository.findUsersByRole(role);
        if(optionalUsers.isPresent()){
            List<User> users = optionalUsers.get();
            if(users.size()==0)
                return null;
            return users;
        }
        else{
            return null;
        }
        }
        public void deleteUser(String email){
        userRepository.deleteByEmail(email);
        }
    public List<User> getAllowedUser(){
        Optional<List<User>> optionalUsers = userRepository.findUsersByIsAllowed();
        if(optionalUsers.isPresent()) {

            List<User> users = optionalUsers.get();

            return users;
        }
        else {
            return null;
        }

    }
    public boolean grantAccess(String email){
        Optional<User> optionalUser = userRepository.findUsersByEmail(email);
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setIsAllowed(true);
            userRepository.save(user);
            return true;
        }
        return false;

    }

    public boolean revokeAccess(String  email) {

        Optional<User> optionalUser = userRepository.findUsersByEmail(email);
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setIsAllowed(false);
            userRepository.save(user);
            return true;
        }
        return false;
    }

}
