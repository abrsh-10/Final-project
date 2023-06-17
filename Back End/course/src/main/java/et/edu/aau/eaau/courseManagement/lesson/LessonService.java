package et.edu.aau.eaau.courseManagement.lesson;

import et.edu.aau.eaau.courseManagement.topic.Topic;
import et.edu.aau.eaau.courseManagement.topic.TopicRepository;
import et.edu.aau.eaau.courseManagement.topic.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;
  private final TopicRepository topicRepository;
    public boolean createLesson(LessonDto lessonDto){
        if(topicRepository.findById(lessonDto.getTopicId()).isPresent()) {
            Lesson lesson = Lesson.builder()
                    .lessonTitle(lessonDto.getLessonTitle())
                    .lessonDescription(lessonDto.getLessonDescription())
                    .lessonVideoId(lessonDto.getLessonVideoId())
                    .topicId(lessonDto.getTopicId())
                    .build();
            lessonRepository.save(lesson);
            return true;
        }
        return false;
    }
    public List<Lesson> getAllLessons(String topicId){
        Optional<List<Lesson>> optionalLessons = lessonRepository.findByTopicId(topicId);
        if(optionalLessons.isPresent()){
            List<Lesson> lessons = optionalLessons.get();
            return lessons;
        }
        return null;
    }
    public Lesson getLesson(String lessonId){
        Optional<Lesson> optionalLesson = lessonRepository.findById(lessonId);
        return optionalLesson.isPresent()?optionalLesson.get():null;
    }
    public boolean changeTitle(String lessonId, String newTitle){
        Optional<Lesson> optionalLesson = lessonRepository.findById(lessonId);
        if(optionalLesson.isPresent()){
            Lesson lesson = optionalLesson.get();
            lesson.setLessonTitle(newTitle);
            lessonRepository.save(lesson);
            return true;
        }
        return false;
    }
    public boolean changeDescription(String lessonId, String newDescription){
        Optional<Lesson> optionalTopic = lessonRepository.findById(lessonId);
        if(optionalTopic.isPresent()){
            Lesson lesson = optionalTopic.get();
            lesson.setLessonDescription(newDescription);
            lessonRepository.save(lesson);
            return true;
        }
        return false;
    }
    public boolean changeVideoId(String lessonId, String newVideoId){
        Optional<Lesson> optionalTopic = lessonRepository.findById(lessonId);
        if(optionalTopic.isPresent()){
            Lesson lesson = optionalTopic.get();
            lesson.setLessonVideoId(newVideoId);
            lessonRepository.save(lesson);
            return true;
        }
        return false;
    }
    public boolean deleteLesson(String lessonId){
        Optional<Lesson> optionalLesson = lessonRepository.findById(lessonId);
        if(optionalLesson.isPresent()){
            lessonRepository.deleteById(lessonId);
            return true;
        }
        return false;
    }
}
