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
                answer(faqRequest.getAnswer()).build();
        faqRepository.save(faq);
    }
    public List<FaqResponse> getAllFaqs(){
        List<Faq> faqs = faqRepository.findAll();
        if(faqs.size() == 0){
            return null;
        }
        return faqs.stream().map(this::mapToFaqResponse).toList();
    }
    public boolean editFaq(FaqEdit faqEdit){
        Optional<Faq> optionalFaq = faqRepository.findFaqsById(faqEdit.getId());
        if(optionalFaq.isPresent()) {
            Faq faq = optionalFaq.get();
            faq.setAnswer(faqEdit.getAnswer());
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
        return FaqResponse.builder().answer(faq.getAnswer()).question(faq.getQuestion()).Id(faq.getId()).build();
    }
}
