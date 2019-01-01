package venus.portal.util.model;

import org.apache.commons.lang.StringUtils;

/**
 *
 */
public class CopyFileModel {
    private String srcFileName;
    private String destFileName;

    public CopyFileModel(String srcFileName) {
        this.srcFileName = srcFileName;
    }

    public CopyFileModel(String srcFileName, String destFileName) {
        this.srcFileName = srcFileName;
        this.destFileName = destFileName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CopyFileModel that = (CopyFileModel) o;

        if (destFileName != null ? !destFileName.equals(that.destFileName) : that.destFileName != null) return false;
        if (srcFileName != null ? !srcFileName.equals(that.srcFileName) : that.srcFileName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = srcFileName != null ? srcFileName.hashCode() : 0;
        result = 31 * result + (destFileName != null ? destFileName.hashCode() : 0);
        return result;
    }

    public String getSrcFileName() {
        return srcFileName;
    }

    public void setSrcFileName(String srcFileName) {
        this.srcFileName = srcFileName;
    }

    public String getDestFileName() {
        if (StringUtils.isEmpty(this.destFileName)) {
            return this.srcFileName;
        }
        return destFileName;
    }

    public void setDestFileName(String destFileName) {
        this.destFileName = destFileName;
    }

}
