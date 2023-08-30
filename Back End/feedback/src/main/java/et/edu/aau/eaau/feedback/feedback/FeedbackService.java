package et.edu.aau.eaau.feedback.feedback;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final RestTemplate restTemplate;

    public boolean addFeedback(FeedbackRequest feedbackRequest) {
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.getForEntity("http://localhost:8086/api/user/email/" + feedbackRequest.getSentBy(), String.class);
        } catch (HttpClientErrorException e) {
            return false;
        }
        if (responseEntity.getStatusCodeValue() == 200) {
            Feedback feedback = Feedback.builder().
                    message(feedbackRequest.getMessage()).
                    sentBy(feedbackRequest.getSentBy()).
                    sentAt(LocalDateTime.now()).
                    isArchived(false).
                    build();
            feedbackRepository.save(feedback);
            return true;
        }
        return false;
    }

    public List<FeedbackResponse> getFeedback(){
        Optional<List<Feedback>> optionalFeedbacks = feedbackRepository.findFeedbacks();
        if(optionalFeedbacks.get().size() == 0)
            return null;
        else {
            return optionalFeedbacks.get().stream().map(this::mapToFeedbackResponse).toList();
        }
    }
    public List<FeedbackResponse> getArchivedFeedbacks(){
        Optional<List<Feedback>> optionalFeedbacks = feedbackRepository.findArchivedFeedbacks();
        if(optionalFeedbacks.get().size() == 0)
            return null;
        else {
            return optionalFeedbacks.get().stream().map(this::mapToFeedbackResponse).toList();
        }
    }
   public Boolean archiveFeedback(String id){
       Optional<Feedback> optionalFeedback = feedbackRepository.findById(id);
       if(optionalFeedback.isPresent()) {
           Feedback feedback = optionalFeedback.get();
           feedback.setIsArchived(true);
           feedbackRepository.save(feedback);
           return true;
       }
       return false;
   }
    public int deleteFeedback(String id){
        Optional<Feedback> optionalFeedback = feedbackRepository.findById(id);
        if(optionalFeedback.isPresent()) {
            Feedback feedback = optionalFeedback.get();
            if(feedback.getIsArchived()) {
                feedbackRepository.deleteById(id);
                return 0;
            }
            return 1;
        }
        return 2;
    }
    private FeedbackResponse mapToFeedbackResponse(Feedback feedback) {
        ResponseEntity<UserResponse> responseEntity = restTemplate.getForEntity("http://localhost:8086/api/user/email/"+feedback.getSentBy(),UserResponse.class);
        UserResponse userResponse = responseEntity.getBody();
        String firstName,lastName;
        firstName = userResponse.getFirstName();
        lastName = userResponse.getLastName();
        Role role = userResponse.getRole();
        FeedbackSender feedbackSender = FeedbackSender.builder().
                email(feedback.getSentBy()).
                firstName(firstName).
                lastName(lastName).
                role(role).
                build();
        return FeedbackResponse.builder()
                .id(feedback.getId())
                .message(feedback.getMessage())
                .sentBy(feedbackSender)
                .sentAt(feedback.getSentAt())
                .isArchived(feedback.getIsArchived())
                .build();
    }
}
