package et.edu.aau.eaau.courseManagement.topic;

import et.edu.aau.eaau.courseManagement.course.CourseRepository;
import et.edu.aau.eaau.courseManagement.lesson.Lesson;
import et.edu.aau.eaau.courseManagement.lesson.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TopicService {
    private final TopicRepository topicRepository;
    private final CourseRepository courseRepository;
    private final LessonService lessonService;

    public boolean createTopic(TopicDto topicDto) {
        if(courseRepository.findById(topicDto.getCourseId()).isPresent()){
            Topic topic = Topic.builder()
                    .topicTitle(topicDto.getTopicTitle())
                    .topicDescription(topicDto.getTopicDescription())
                    .courseId(topicDto.getCourseId())
                    .build();
            topicRepository.save(topic);
            return true;
        }
        return false;
    }
    public List<Topic> getAllTopics(String courseId){
        Optional<List<Topic>> optionalTopics = topicRepository.findByCourseId(courseId);
        if(optionalTopics.isPresent()){
            List<Topic> topics = optionalTopics.get();
            return topics;
        }
        return null;
    }
    public List<TopicResponse> getAllTopicsWithLessons(String courseId){
        Optional<List<Topic>> optionalTopics = topicRepository.findByCourseId(courseId);
        if(optionalTopics.isPresent()){
            List<Topic> topics = optionalTopics.get();
           return topics.stream().map(this::maptoTopicResponse).toList();
        }
        return null;
    }
    public Topic getTopic(String topicId){
        Optional<Topic> optionalTopic = topicRepository.findById(topicId);
        return optionalTopic.isPresent()?optionalTopic.get():null;
    }
    public boolean changeTitle(String topicId,String newTitle){
        Optional<Topic> optionalTopic = topicRepository.findById(topicId);
        if(optionalTopic.isPresent()){
            Topic topic = optionalTopic.get();
            topic.setTopicTitle(newTitle);
            topicRepository.save(topic);
            return true;
        }
        return false;
    }
    public boolean changeDescription(String topicId,String newDescription){
        Optional<Topic> optionalTopic = topicRepository.findById(topicId);
        if(optionalTopic.isPresent()){
            Topic topic = optionalTopic.get();
            topic.setTopicDescription(newDescription);
            topicRepository.save(topic);
            return true;
        }
        return false;
    }
    public boolean deleteTopic(String topicId){
        Optional<Topic> optionalTopic = topicRepository.findById(topicId);
        if(optionalTopic.isPresent()){
            topicRepository.deleteById(topicId);
            return true;
        }
        return false;
    }
    private TopicResponse  maptoTopicResponse(Topic topic) {
        TopicResponse topicResponse = TopicResponse.builder()
                .topicId(topic.getTopicId())
                .topicTitle(topic.getTopicTitle())
                .topicDescription(topic.getTopicDescription())
                .build();
        List<Lesson> lessons = lessonService.getAllLessons(topic.getTopicId());
        topicResponse.setLessons(lessons);
        return topicResponse;
    }
}

