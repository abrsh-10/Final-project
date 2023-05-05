package et.edu.aau.eaau.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("file")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file")MultipartFile file,
                                    @RequestParam("filetype") String type,
                                    @RequestParam("uploader") String uploader,
                                    @RequestParam("description") String description,
                                    @RequestParam("course_id") String course_id) throws IOException {
        if(type.equals("coursematerial") || type.equals("assignment") || type.equals("solution")) {

            MetaData metaData = MetaData.builder().
                    fileType(Type.valueOf(type))
                    .uploader(uploader)
                    .description(description)
                    .course_id(course_id).
                    build();
            if(fileService.addFile(file,metaData) == 0)
            return new ResponseEntity<>("file with file name: "+ file.getOriginalFilename() + " is added successfully", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("file type must be coursematerial,assignment or solution",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("something is wrong with your file or meta data",HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<ByteArrayResource> download(@PathVariable String fileId) throws IOException {
        File file = fileService.downloadFile(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getFileType() ))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(new ByteArrayResource(file.getFile()));
    }
    @GetMapping("/viewfileinformation")
    public  ResponseEntity<List<FileInformation>> getAllFileNames(){
        return new ResponseEntity<>(fileService.getAllFileInformations(),HttpStatus.OK);
    }
    @GetMapping("/view-course-materials/{course_id}")
    public  ResponseEntity<List<FileInformation>> getAllCoursematerials(@PathVariable String course_id){
        return new ResponseEntity<>(fileService.getAllCourseMaterials(course_id),HttpStatus.OK);
    }
    @GetMapping("/view-assignments/{course_id}")
    public  ResponseEntity<List<FileInformation>> getAllAssignments(@PathVariable String course_id){
        return new ResponseEntity<>(fileService.getAllAssignments(course_id),HttpStatus.OK);
    }
    @GetMapping("/view-solutions/{assignment_id}")
    public  ResponseEntity<List<FileInformation>> getAllSolutions(@PathVariable String assignment_id){
        return new ResponseEntity<>(fileService.getAllSolutions(assignment_id),HttpStatus.OK);
    }
    @GetMapping("/viewfileinformation/{fileId}")
    public  ResponseEntity<FileInformation> getAllFileNames(@PathVariable("fileId") String fileId){
            return new ResponseEntity<>(fileService.getFileInformation(fileId),HttpStatus.OK);
    }
    @DeleteMapping("/delete/{fileId}")
    public  ResponseEntity deleteFile(@PathVariable("fileId") String fileId){
        if(fileService.deleteFile(fileId))
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
