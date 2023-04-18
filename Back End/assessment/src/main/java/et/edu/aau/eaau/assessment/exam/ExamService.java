package et.edu.aau.eaau.assessment.exam;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ExamService {
    private final ExamRepository examRepository;
    private final RestTemplate restTemplate;

    public int addExam(ExamRequest examRequest) {
        if (examRequest.getWeight() > 100 || examRequest.getWeight() <= 0) {
            return 1;
        }
        if (examRequest.getStartTime().isBefore(LocalDateTime.now())) {
            return 2;
        }
        if (examRequest.getDuration() <= 0) {
            return 3;
        }
        ResponseEntity<Course> responseEntity;
        try {
            responseEntity = restTemplate.getForEntity("http://localhost:8083/api/course/id/"+ examRequest.getCourseId(),Course.class);
        }
        catch (HttpClientErrorException e) {
            return 5;
        }
        if(responseEntity.getBody() == null){
            return 4;
        }
        if(!examRequest.getCreator().equals(responseEntity.getBody().getTeacherEmail())){
            return 6;
        }
        Exam exam = Exam.builder()
                .examName(examRequest.getExamName())
                .courseId(examRequest.getCourseId())
                .weight(examRequest.getWeight())
                .startTime(examRequest.getStartTime())
                .duration(examRequest.getDuration())
                .build();
        examRepository.save(exam);
        return 0;
    }

    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }

    public Exam getExam(String examId) {
        Optional<Exam> optionalExam = examRepository.findById(examId);
        return optionalExam.orElse(null);
    }
    public List<Exam> getExamByCourse(String courseId) {
        Optional<List<Exam>> optionalExam = examRepository.findByCourseId(courseId);
        return optionalExam.orElse(null);
    }
    public int addQuestions(List<QuestionDto> questionDtos, String examId) {
        Optional<Exam> optionalExam = examRepository.findById(examId);
        if (optionalExam.isPresent()) {
            int questionNum;
            Exam exam = optionalExam.get();
            List<Question> questionList = exam.getQuestions();
            for (QuestionDto questionDto : questionDtos) {
                if (questionDto.getQuestionType() == QuestionType.True_False) {
                    questionDto.setOptions(Arrays.asList("True", "False"));
                    questionNum = getNumberOfQuestions(questionList,QuestionType.True_False) + 1;
                } else if (questionDto.getQuestionType() == QuestionType.Fill) {
                    questionDto.setOptions(new ArrayList<>());
                    questionNum = getNumberOfQuestions(questionList,QuestionType.Fill) + 1;
                } else if (questionDto.getQuestionType() == QuestionType.Short_Answer) {
                    questionDto.setOptions(new ArrayList<>());
                    questionNum = getNumberOfQuestions(questionList,QuestionType.Short_Answer) + 1;
                } else {
                    if (questionDto.getOptions().size() < 2) {
                        return 2;
                    }
                    questionNum = getNumberOfQuestions(questionList,QuestionType.Choose) + 1;
                }
                Question question = Question.builder()
                        .questionNumber(questionNum)
                        .question(questionDto.getQuestion())
                        .questionType(questionDto.getQuestionType())

                        .options(questionDto.getOptions())
                        .build();
                questionList.add(question);
            }
            exam.setQuestions(questionList);
            examRepository.save(exam);
            return 0;
        }
        return 1;
    }
    public int removeQuestion(String examId,QuestionType questionType,int questionNum) {
        Optional<Exam> optionalExam = examRepository.findById(examId);
        int index = -1;
        if (optionalExam.isPresent()) {
            Exam exam = optionalExam.get();
            List<Question> questionList = exam.getQuestions();
            for (int i = 0; i < questionList.size(); i++) {
                Question question = questionList.get(i);
                if (question.getQuestionType() == questionType && question.getQuestionNumber() == questionNum) {
                    index=i;
                    break;
                }
            }
            if (index == -1) {
                return 2;
            }
            questionList.remove(index);
            examRepository.save(exam);
            return 0;
        }
        return 1;
    }
    public int changeWeight(String id,int newWeight){
        Optional<Exam> optionalExam = examRepository.findById(id);
        if (optionalExam.isPresent()) {
            if(newWeight<=0){
                return 2;
            }
            Exam exam = optionalExam.get();
            exam.setWeight(newWeight);
            examRepository.save(exam);
            return 0;
        } else {
            return 1;
        }
    }
    public int changeDuration(String id,int newDuration){
        Optional<Exam> optionalExam = examRepository.findById(id);
        if (optionalExam.isPresent()) {
           if(newDuration<=0){
               return 2;
           }
            Exam exam = optionalExam.get();
            exam.setDuration(newDuration);
            examRepository.save(exam);
            return 0;
        } else {
            return 1;
        }
    }
    public int changeStartTime(String id,LocalDateTime newStartTime){
        Optional<Exam> optionalExam = examRepository.findById(id);
        if (optionalExam.isPresent()) {
            if(newStartTime.isBefore(LocalDateTime.now())){
                return 2;
            }
            Exam exam = optionalExam.get();
            exam.setStartTime(newStartTime);
            examRepository.save(exam);
            return 0;
        } else {
            return 1;
        }
    }
    public List<Question> getQuestionsByOrder(String examId) {
        Optional<Exam> optionalExam = examRepository.findById(examId);
        if (optionalExam.isPresent()) {
            Exam exam = optionalExam.get();
            List<Question> questions = exam.getQuestions();
            questions.sort(Comparator.comparing(question -> question.getQuestionType()));
            return questions;
        }
        return null;
    }
    private int getNumberOfQuestions(List<Question> questions , QuestionType questionType){
        int num = 0;
        for(Question question:questions){
            if(question.getQuestionType() == questionType){
                num++;
            }
        }
        return num;
    }
}
