package et.edu.aau.eaau.courseManagement.courseMaterial;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class File {

    private String fileId;

    private String fileName;

    private int length;

    private String uploader;

    private String Description;

    private String course_id;
}
