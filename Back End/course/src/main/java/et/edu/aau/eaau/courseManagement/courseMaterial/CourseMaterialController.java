package et.edu.aau.eaau.courseManagement.courseMaterial;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.microsoft.cognitiveservices.speech.*;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;
import org.apache.commons.io.FileUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/api/course-material")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201"})
@RequiredArgsConstructor
public class CourseMaterialController {
    private final CourseMaterialService courseMaterialService;
    @GetMapping("id/{courseId}")
    public ResponseEntity<List<CourseMaterial>> getAllCourseMaterials(@PathVariable String courseId){
            return new ResponseEntity(courseMaterialService.getCourseMaterials(courseId), HttpStatus.OK);
    }
    @PostMapping()
    public ResponseEntity<Map<String,Object>> addCourseMaterial(@RequestParam("file") MultipartFile file,
                                                        @RequestParam("uploader") String uploader,
                                                        @RequestParam("description") String description,
                                                        @RequestParam("course_id") String course_id) throws IOException {
        int response = courseMaterialService.addCourseMaterial(file,uploader,description, course_id);
        Map<String,Object> res = new HashMap<>();
        if( response== 1){
            res.put("message","invalid");
            return new ResponseEntity<>(res,HttpStatus.BAD_REQUEST);
        }
        else if(response== 2){
            res.put("message","invalid");
            return new ResponseEntity<>(res,HttpStatus.BAD_REQUEST);
        }
        else if(response == 3){
            res.put("message","invalid");
            return new ResponseEntity<>(res,HttpStatus.INTERNAL_SERVER_ERROR);          }
        else if(response == 4){
            res.put("message","invalid");
            return new ResponseEntity<>(res,HttpStatus.BAD_REQUEST);        }
        res.put("message","success");
        return new ResponseEntity<>(res,HttpStatus.OK);
    }
    @DeleteMapping("delete/{courseMaterialId}")
    public ResponseEntity<String> deleteAssignmentSolution(@PathVariable String courseMaterialId){
        if(courseMaterialService.deleteCourseMaterial(courseMaterialId)){
            return new ResponseEntity<>("course material deleted",HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>("course material not found",HttpStatus.NOT_FOUND);
    }




    @PostMapping("/translate/{to}")
    public ResponseEntity<String> translate(@RequestBody textModel text, @PathVariable String to) throws IOException, InterruptedException {

        String response = postAuto(text.getText(), to);
        System.out.println(text.getText());
        System.out.println(prettify(response));
        return ResponseEntity.ok(prettify(response));
    }

    // This function performs a POST request.
    public String postAuto(String text, String to) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.cognitive.microsofttranslator.com//translate?api-version=3.0&to=" + to))
                .header("content-type", "application/json")
                .header("Ocp-Apim-Subscription-Key", "6103613c1db74498ba2ca70d6ecf8372")
                .header("Ocp-Apim-Subscription-Region", "eastus")
                .method("POST", HttpRequest.BodyPublishers.ofString("[{\"Text\": \"" + escapeQuotes(text) + "\"}]"))
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    // This function prettifies the JSON response.
    public static String prettify(String json_text) {
        JsonParser parser = new JsonParser();
        JsonElement json = parser.parse(json_text);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(json);
    }

    // This function escapes double quotes in the text.
    public static String escapeQuotes(String text) {
        return text.replace("\"", "\\\"");
    }
    @PostMapping("/audioTranscribe/{language}")
    public ResponseEntity<Map<String,Object>> convertSpeechToText(@RequestParam("file") MultipartFile file1, @PathVariable String language) throws IOException {
        System.out.println("hello");
        Map<String,Object> response = new HashMap<>();
        byte[] wavData = file1.getBytes();
        String recognizedText = null;

        // api configuration
        String subscriptionKey = "c064ed5575b5433c9b4a7c61ebb6db26";
        String serviceRegion = "eastus";
        SpeechConfig config = SpeechConfig.fromSubscription(subscriptionKey, serviceRegion);
        config.setSpeechRecognitionLanguage(language); // en-US

        // save the byte code as a wave file
        String filePath = "C:/Users/ms/OneDrive/Desktop/audio/output.wav";
        File file = new File(filePath);

        try {
            FileUtils.writeByteArrayToFile(file, wavData);
            System.out.println("WAV file saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // wave file saved

        // transcription start
        try {
            AudioConfig audioInput = AudioConfig.fromWavFileInput(filePath);

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

        // delete the WAV file after transcription
//        FileUtils.deleteQuietly(file);
        response.put("text",recognizedText);
        // return the text
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
