package et.edu.aau.eaau.faq;

import et.edu.aau.eaau.faq.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FaqService {

    private final FaqRepository faqRepository;
    public void CreateFaq(FaqRequest faqRequest){
        Faq faq = Faq.builder().
                question(faqRequest.getQuestion()).
                answer(faqRequest.getAnswer()).
                role(faqRequest.getRole()).
                build();
        faqRepository.save(faq);
    }
    public List<FaqResponse> getAllFaqs(){
        List<Faq> faqs = faqRepository.findAll();
        if(faqs.size() == 0){
            return null;
        }
        return faqs.stream().map(this::mapToFaqResponse).toList();
    }
    public List<FaqResponse> getFaqByRole(Role role){
        Optional<List<Faq>> faqs = faqRepository.findByRole(role);
        if(faqs.isPresent()){
            return faqs.get().stream().map(this::mapToFaqResponse).toList();
        }
        return null;
    }
    public boolean editFaq(String id,String answer){
        Optional<Faq> optionalFaq = faqRepository.findFaqsById(id);
        if(optionalFaq.isPresent()) {
            Faq faq = optionalFaq.get();
            faq.setAnswer(answer);
            faqRepository.save(faq);
            return true;
        }
         return false;
    }
    public boolean deleteFaq(String id) {
        Optional<Faq> optionalFaq = faqRepository.findFaqsById(id);
        if(optionalFaq.isPresent()) {
            faqRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private FaqResponse mapToFaqResponse(Faq faq) {
        return FaqResponse.builder().answer(faq.getAnswer()).question(faq.getQuestion()).Id(faq.getId()).role(faq.getRole()).build();
    }
}
