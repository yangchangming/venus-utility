package venus.frames.web.message;

/**
 * <p>  </p>
 *
 * @author changming.Y <changming.yang.ah@gmail.com>
 * @since 2018-04-09 18:26
 */
public class Messager {

    /********登录名********/
    private String currentAccount = null;

    /********当前业务********/
    private String currentOperation = null;

    /********当前人员********/
    private String loginPersonnel = null;

    /********操作信息********/
    private String operationHint = null;


    /****/
    private StringBuffer messagerHTML = new StringBuffer();

    public String getJavaScript()
    {

        messagerHTML.append("<SCRIPT>");

        messagerHTML.append("var mg = new Messager();");
        if(getCurrentAccount()!=null)
            messagerHTML.append("mg.setCurrentAccount(\""+getCurrentAccount()+"\");" );

        if(getLoginPersonnel()!=null)
            messagerHTML.append("mg.setLoginPersonnel(\""+getLoginPersonnel()+"\");" );

        if(getCurrentOperation()!=null)
            messagerHTML.append("mg.setCurrentOperation(\""+getCurrentOperation()+"\");" );

        if(getOperationHint()!=null)
            messagerHTML.append("mg.setOperationHint(\""+getOperationHint()+"\");" );

        messagerHTML.append("venus_append_commit(mg);" );

        messagerHTML.append("</SCRIPT>");

        /****/
        return messagerHTML.toString();

    }

    /**
     * @param request
     */
    public void save(venus.frames.base.action.IRequest request)
    {
        request.setAttribute("messagerHTML",messagerHTML);
    }

    /**
     * @return 返回 messagerHTML。
     */
    public StringBuffer getMessagerHTML() {
        return messagerHTML;
    }
    /**
     * @param messagerHTML 要设置的 messagerHTML。
     */
    public void setMessagerHTML(StringBuffer messagerHTML) {
        this.messagerHTML = messagerHTML;
    }
    /**
     * @return 返回 currentAccount。
     */
    public String getCurrentAccount() {
        return currentAccount;
    }
    /**
     * @param currentAccount 要设置的 currentAccount。
     */
    public void setCurrentAccount(String currentAccount) {
        this.currentAccount = currentAccount;
    }
    /**
     * @return 返回 currentOperation。
     */
    public String getCurrentOperation() {
        return currentOperation;
    }
    /**
     * @param currentOperation 要设置的 currentOperation。
     */
    public void setCurrentOperation(String currentOperation) {
        this.currentOperation = currentOperation;
    }
    /**
     * @return 返回 loginPersonnel。
     */
    public String getLoginPersonnel() {
        return loginPersonnel;
    }
    /**
     * @param loginPersonnel 要设置的 loginPersonnel。
     */
    public void setLoginPersonnel(String loginPersonnel) {
        this.loginPersonnel = loginPersonnel;
    }
    /**
     * @return 返回 operationHint。
     */
    public String getOperationHint() {
        return operationHint;
    }
    /**
     * @param operationHint 要设置的 operationHint。
     */
    public void setOperationHint(String operationHint) {
            this.operationHint = operationHint;
        }

}
