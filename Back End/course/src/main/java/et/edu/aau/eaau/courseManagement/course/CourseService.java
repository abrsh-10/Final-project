package et.edu.aau.eaau.courseManagement.course;


import et.edu.aau.eaau.courseManagement.courseMaterial.CourseMaterialService;
import et.edu.aau.eaau.courseManagement.topic.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final TopicService topicService;
    private final CourseMaterialService courseMaterialService;

    public void createCourse(CourseRequest courseRequest) {
        String teacherEmail;
        if(courseRequest.getTeacherEmail() == null){
            teacherEmail = "none";
        }
        else{
            teacherEmail = courseRequest.getTeacherEmail();
        }

        Course course = Course.builder()
                .courseId(courseRequest.getCourseId())
                .courseName(courseRequest.getCourseName())
                .courseDescription(courseRequest.getCourseDescription())
                .teacherEmail(teacherEmail)
                .courseImage(courseRequest.getCourseImage())
                .build();
        courseRepository.save(course);
    }

    public List<Course> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        if (courses.size() == 0) {
            return null;
        }
        return courses;
    }
    public List<CourseDto> getAllCoursesWithFullInformation() {
        List<Course> courses = courseRepository.findAll();
        if (courses.size() == 0) {
            return null;
        }
        return courses.stream().map(this::mapToCourseDto).toList();
    }

    public Course getCourse(String id) {
        Optional<Course> optionalCourse = courseRepository.findById(id);
        if (optionalCourse.isPresent()) {

            Course course = optionalCourse.get();

            return course;
        } else {
            return null;
        }
    }
    public List<Course> getCourseByCourseId(String courseId) {
        Optional<List<Course>> optionalCourse = courseRepository.findByCourseId(courseId);
        if (optionalCourse.get().size()>0) {

            List<Course> courses = optionalCourse.get();

            return courses;
        } else {
            return null;
        }
    }
    public CourseDto getCourseWithFullInformation(String id) {
        Optional<Course> optionalCourse = courseRepository.findById(id);
        if (optionalCourse.isPresent()) {

            Course course = optionalCourse.get();

            return mapToCourseDto(course);
        } else {
            return null;
        }
    }
    public boolean changeCourseDescription(String id,String newDescription){
        Optional<Course> optionalCourse = courseRepository.findById(id);
        if (optionalCourse.isPresent()) {

            Course course = optionalCourse.get();
            course.setCourseDescription(newDescription);
            courseRepository.save(course);
            return true;
        } else {
            return false;
        }
    }
    public boolean changeTeacher(String id,String newEmail){
        Optional<Course> optionalCourse = courseRepository.findById(id);
        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            course.setTeacherEmail(newEmail);
            courseRepository.save(course);
            return true;
        } else {
            return false;
        }
    }
    private CourseDto mapToCourseDto(Course course) {
        CourseDto courseDto = CourseDto.builder()
                .id(course.getId())
                .courseId(course.getCourseId())
                .courseName(course.getCourseName())
                .courseDescription(course.getCourseDescription())
                .teacherEmail(course.getTeacherEmail())
                .build();
        courseDto.setCourseMaterials(courseMaterialService.getCourseMaterials(courseDto.getId()));
        courseDto.setTopics(topicService.getAllTopicsWithLessons(courseDto.getId()));
        return courseDto;
    }
}
