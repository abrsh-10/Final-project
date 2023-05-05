package et.edu.aau.eaau.assessment.exam;

import et.edu.aau.eaau.assessment.examSolution.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;

@Service
@RequiredArgsConstructor
public class ExamService {
    private final ExamRepository examRepository;

    private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
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
        List<Exam> exams = examRepository.findAllByStartTimeBetween(
                LocalDateTime.of(LocalDate.now(), LocalTime.MIN),
                LocalDateTime.of(LocalDate.now(), LocalTime.MAX)
        );
        List<User> studentsToBeScheduled;
        List<User> scheduledStudents = new ArrayList<>();
        try {
            String url = "http://localhost:8080/api/user/students/" + courseIdMapper(examRequest.getCourseId());
            studentsToBeScheduled = Arrays.stream(Objects.requireNonNull(restTemplate.getForObject(url, User[].class))).toList();
            for(Exam exam:exams){
                String url2 = "http://localhost:8080/api/user/students/" + courseIdMapper(exam.getCourseId());
                List<User> students = Arrays.stream(Objects.requireNonNull(restTemplate.getForObject(url2, User[].class))).toList();
                    scheduledStudents.addAll(students);
                 }
            }
        catch (HttpClientErrorException e) {
            return 5;
        }
        for(User student:studentsToBeScheduled){
            if(scheduledStudents.contains(student)){
                return 7;
            }
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

    private String courseIdMapper(String id) {
        ResponseEntity<Course> responseEntity;
        try {
            responseEntity = restTemplate.getForEntity("http://localhost:8083/api/course/id/" + id, Course.class);
        }
        catch (HttpClientErrorException e) {
            return null;
        }
        return Objects.requireNonNull(responseEntity.getBody()).getCourseId();
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
    public int deleteExam(String id){
        Exam exam = getExam(id);
        if(exam != null){
            if(exam.isActive()){
                return 2;
            }
            examRepository.deleteById(id);
            return 0;
        }
        return 1;
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
    @Scheduled(fixedDelay = 60000)
    public void activateOrDeactivateExams() {
        // Get exams scheduled for today
        LocalDate today = LocalDate.now();
        List<Exam> exams = examRepository.findAllByStartTimeBetween(
                LocalDateTime.of(today, LocalTime.MIN),
                LocalDateTime.of(today, LocalTime.MAX)
        );

        // If no exams are scheduled for today, delay the next invocation by one day and return
        if (exams.isEmpty()) {
            System.out.println("No exams scheduled for today.");
            long delay = Duration.between(LocalDateTime.now(), LocalDate.now().plusDays(1).atStartOfDay()).toMillis();
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }

        // Activate or deactivate the exam based on the current time
        for (Exam exam : exams) {
            LocalDateTime endTime = exam.getStartTime().plusMinutes(exam.getDuration());
            if (LocalDateTime.now().isBefore(exam.getStartTime())) {
                System.out.println("Exam "+exam.getExamName()+" scheduled for today, but not yet started.");
                exam.setActive(false);
            } else if (LocalDateTime.now().isAfter(endTime)) {
                System.out.println("Exam "+exam.getExamName()+" scheduled for today, but already ended.");
                exam.setActive(false);
            } else {
                exam.setActive(true);
                System.out.println("Exam "+exam.getExamName()+" is now activated.");
            }
            examRepository.save(exam);
        }
        System.out.println("Working.");
    }
}
