package et.edu.aau.eaau.courseManagement.lesson;

import com.microsoft.cognitiveservices.speech.*;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ws.schild.jave.AudioAttributes;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncodingAttributes;
import ws.schild.jave.MultimediaObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201"})

@RequestMapping("api/lesson")
public class LessonController {
    private final LessonService lessonService;

    @GetMapping("topic-id/{topicId}")
    public ResponseEntity<List<Lesson>> getAllLessons(@PathVariable String topicId) {
        if (lessonService.getAllLessons(topicId) != null) {
            return new ResponseEntity(lessonService.getAllLessons(topicId), HttpStatus.OK);
        }
        return new ResponseEntity(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping()
    public ResponseEntity createLesson(@RequestBody LessonDto lessonDto) {
        if (lessonService.createLesson(lessonDto)) {
            return new ResponseEntity(HttpStatus.CREATED);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("lesson-title/{lessonId}")
    public ResponseEntity changeTopicTitle(@PathVariable("lessonId") String lessonId, @RequestBody String newTitle) {
        boolean ischanged = lessonService.changeTitle(lessonId, newTitle);
        if (!ischanged) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }

    @PutMapping("lesson-description/{lessonId}")
    public ResponseEntity changeTopicDescription(@PathVariable("lessonId") String lessonId, @RequestBody String newDescription) {
        boolean isChanged = lessonService.changeDescription(lessonId, newDescription);
        if (!isChanged) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }

    @PutMapping("lesson-videoId/{lessonId}")
    public ResponseEntity changeLessonVideoId(@PathVariable("lessonId") String lessonId, @RequestBody String newVideoId) {
        boolean isChanged = lessonService.changeVideoId(lessonId, newVideoId);
        if (!isChanged) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("lesson-id/{lessonId}")
    public ResponseEntity deleteTopic(@PathVariable("lessonId") String lessonId) {
        boolean isDeleted = lessonService.deleteLesson(lessonId);
        if (!isDeleted) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }

    @RestController
    @CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201"})
    public class SpeechToTextController {
        @PostMapping("/youtube/{vidId}")
        public ResponseEntity<Map<String,Object>> TranscribeFromYoutube(@PathVariable String vidId) throws IOException, InterruptedException {
            Map<String, Object> res = new HashMap<>();

            String fileLocation = "C:/Users/ms/OneDrive/Desktop";

            // get the downloadable link from a youtube link
            String videoUrl = "https://youtu.be/" + vidId;

            String requestBody = "{\"url\": \"" + videoUrl + "\"}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://youtube86.p.rapidapi.com/api/youtube/links"))
                    .header("Content-Type", "application/json")
                    .header("X-Forwarded-For", "70.41.3.18")
                    .header("X-RapidAPI-Key", "d120babe20msh3741e50f8d3955cp165fdfjsnc4d6394ba9f4")
                    .header("X-RapidAPI-Host", "youtube86.p.rapidapi.com")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            // Extract the first URL from the response body
            String firstUrl = "";

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.body());

            if (jsonNode.isArray() && jsonNode.size() > 0) {
                JsonNode firstItem = jsonNode.get(0);
                JsonNode urlsNode = firstItem.get("urls");

                if (urlsNode.isArray() && urlsNode.size() > 0) {
                    JsonNode firstUrlNode = urlsNode.get(0).get("url");
                    firstUrl = firstUrlNode.asText();
                }
            }

            System.out.println("First URL: " + firstUrl);

            // downloading mp4 file
            String outputFileName = fileLocation + "source.mp4";
            URL url = new URL(firstUrl);
            try (InputStream in = url.openStream();
                 ReadableByteChannel rbc = Channels.newChannel(in);
                 FileOutputStream fos = new FileOutputStream(outputFileName)) {
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            }

            // trying to convert
            File source = new File(fileLocation + "source.mp4");
            File target = new File(fileLocation + "target.wav");

            /* Step 2. Set Audio Attributes for conversion */
            AudioAttributes audio = new AudioAttributes();
            audio.setCodec("pcm_s16le");
            audio.setBitRate(128000);
            audio.setChannels(2);
            audio.setSamplingRate(44100);

            /* Step 3. Set Encoding Attributes */
            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setFormat("wav");
            attrs.setAudioAttributes(audio);

            /* Step 4. Do the Encoding */
            try {
                Encoder encoder = new Encoder();
                MultimediaObject multimediaObject = new MultimediaObject(source);
                encoder.encode(multimediaObject, target, attrs);
                System.out.println("Conversion completed successfully!");
            } catch (Exception e) {
                /* Handle conversion failure */
                e.printStackTrace();
            }
            System.out.println("Done!");

            // deleting the mp4 file
            File file = new File(outputFileName);
            FileUtils.deleteQuietly(file);
            System.out.println("mp4 file deleted!");

// transcription start

            String recognizedText = "";

            // api configuration
            String subscriptionKey = "c064ed5575b5433c9b4a7c61ebb6db26";
            String serviceRegion = "eastus";
            SpeechConfig config = SpeechConfig.fromSubscription(subscriptionKey, serviceRegion);
            config.setSpeechRecognitionLanguage("en-US");

            String wavFilePath = fileLocation + "target.wav";

            try {
                AudioConfig audioInput = AudioConfig.fromWavFileInput(wavFilePath);

                SpeechRecognizer recognizer = new SpeechRecognizer(config, audioInput);
                Future<SpeechRecognitionResult> task = recognizer.recognizeOnceAsync();
                SpeechRecognitionResult result = task.get();
                System.out.println("RECOGNIZED: Text=" + result.getText());

                if (result.getReason() == ResultReason.RecognizedSpeech) {
                    recognizedText = result.getText();
                    System.out.println("Recognized Text: " + recognizedText);
                } else if (result.getReason() == ResultReason.NoMatch) {
                    System.out.println("No speech could be recognized.");
                } else if (result.getReason() == ResultReason.Canceled) {
                    CancellationDetails cancellation = CancellationDetails.fromResult(result);
                    System.out.println("CANCELED: Reason=" + cancellation.getReason());
                }

                recognizedText = result.getText();
            } catch (Exception ex) {
                System.out.println("Exception: " + ex.getMessage());
            }
// transcription end

            // deleting the mp4 file
            File wavFileDelete = new File(wavFilePath);
            FileUtils.deleteQuietly(wavFileDelete);
            System.out.println("wav file deleted!");

            System.out.println(recognizedText);
            res.put("text",recognizedText);
            return new ResponseEntity<>(res,HttpStatus.OK);
        }

    }
}
