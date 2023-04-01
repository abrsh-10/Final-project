package et.edu.aau.eaau.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
 @Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetaData {
    private Type fileType;//differentiate course materials from assignments or solutions
    private String uploader;//email of the uploader
    private String description;
    private String course_id;

}
