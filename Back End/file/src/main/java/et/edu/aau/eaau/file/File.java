package et.edu.aau.eaau.file;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class File {
    private String filename;
    private String fileType;
    private String fileSize;
    private byte[] file;
}
