package venus.oa.adapter;

import java.io.Serializable;

public interface ICredentials extends Serializable{

    public String getPassword();

    public String getLoginId();
}
