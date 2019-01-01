package venus.portal.util;

import com.google.common.io.Files;
import venus.portal.util.model.CopyFileModel;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by ethan on 14-2-25.
 */
public class EwpFileUtils {

    public static void copyFilesByPattern(File srcDir, File destDir, List<CopyFileModel> copyFileModels) throws IOException {
        for (CopyFileModel model : copyFileModels) {
            File srcFile = new File(srcDir.getAbsolutePath() + File.separator + model.getSrcFileName());
            File destFile = new File(destDir.getAbsolutePath() + File.separator + model.getDestFileName());
            Files.copy(srcFile, destFile);
        }
    }
}
