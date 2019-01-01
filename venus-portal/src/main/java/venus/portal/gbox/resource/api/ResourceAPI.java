package venus.portal.gbox.resource.api;

import venus.frames.mainframe.util.Helper;
import venus.portal.gbox.common.upload.FileUpload;
import venus.portal.gbox.resource.resourceinfo.bs.IResourceBs;
import venus.portal.gbox.resource.resourceinfo.util.IResourceConstants;
import venus.portal.gbox.resource.resourceinfo.vo.ResourceVo;
import venus.portal.gbox.util.FileTools;

public class ResourceAPI {
    
    /**
     * 根据资源ID获得资源VO
     * @param id 资源ID
     * @return ResourceVo 资源VO对象
     */
    public static ResourceVo getResource(String id) {
        IResourceBs bs = (IResourceBs) Helper.getBean(IResourceConstants.BS_KEY);
        return bs.findAll(id);
    }

    /**
     * 根据资源ID获得资源路径
     * @param id 资源ID
     * @return String 资源路径
     */
    public static String getResourcePath(String id) {
        IResourceBs bs = (IResourceBs) Helper.getBean(IResourceConstants.BS_KEY);
        ResourceVo vo = bs.findAll(id);
        String resourceType = ResourceTypeAPI.getResourceTypeUploadPath(vo.getType());
        if (vo == null)
            return null;
        //获取导入资源管理中的文件路径
        if ("1".equals(vo.getIsExternal())) {
            return vo.getFileName();
        }
        //获取非导入资源管理上传的文件路径（组装）
        if ("1".equals(vo.getIsProtected())) { //返回受保护文件路径
            return FileUpload.getDefaultUploadPath() + resourceType + FileTools.FILE_SEPARATOR +  vo.getFileName();
        } else { //返回非受保护文件路径
            String resourceServersPath = OptionAPI.getOptionValue(OptionAPI.getResourceServersPathId());
            if (resourceServersPath.endsWith(FileTools.FILE_SEPARATOR)) 
                return resourceServersPath + resourceType + FileTools.FILE_SEPARATOR + vo.getFileName();
            else
                return resourceServersPath + FileTools.FILE_SEPARATOR + resourceType +  FileTools.FILE_SEPARATOR  + vo.getFileName();
        }
    }
    
}
