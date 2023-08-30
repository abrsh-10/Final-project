package et.edu.aau.eaau.faq;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201","http://localhost:4202"})
@RequestMapping("/api/faq")
public class FaqController {
    private final FaqService faqService;
    @PostMapping()
    public ResponseEntity createFaq(@RequestBody FaqRequest faqRequest){
        faqService.CreateFaq(faqRequest);
        return new ResponseEntity(HttpStatus.CREATED);
    }
    @GetMapping()
    public ResponseEntity<List<FaqResponse>> getAllFaqs(){
            return new ResponseEntity<>(faqService.getAllFaqs(), HttpStatus.OK);
    }
    @GetMapping("/role/{role}")
    public ResponseEntity<List<FaqResponse>> getAllFaqs(@PathVariable Role role){
        return new ResponseEntity<>(faqService.getFaqByRole(role), HttpStatus.OK);
    }
    @PutMapping("/id/{id}")
    public ResponseEntity editFaq(@PathVariable String id ,@RequestBody String answer){
        boolean isEdited = faqService.editFaq(id,answer);
        if(isEdited)
         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("/id/{id}")
    public ResponseEntity deleteFaq(@PathVariable String id){
        boolean isDeleted = faqService.deleteFaq(id);
        if(isDeleted)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
