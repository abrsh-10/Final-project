package et.edu.aau.eaau.assessment.Assignment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class File {

    private String fileId;

    private String fileName;
    private String fileType;

    private int length;

    private String uploader;

    private String Description;

    private String course_id;
}
