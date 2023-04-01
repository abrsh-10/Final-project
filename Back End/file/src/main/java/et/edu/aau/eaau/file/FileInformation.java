package et.edu.aau.eaau.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(value = "fs.files")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileInformation {
    @Field("_id")
    private String fileId;
    @Field("filename")
    private String fileName;
    @Field("length")
    private int length;
    @Field("metadata.info.fileType")
    private Type fileType;
    @Field("metadata.info.uploader")
    private String uploader;
    @Field("metadata.info.description")
    private String Description;
    @Field("metadata.info.course_id")
    private String course_id;
}
