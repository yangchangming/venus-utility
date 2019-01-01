package venus.portal.timerelease;

import org.springframework.beans.factory.annotation.Autowired;
import venus.portal.timerelease.bs.IEwpDocTimeReleaseBs;

/**
 * Created by ethan on 13-10-11.
 */
public class DocTimeReleaseJob {

    @Autowired
    private IEwpDocTimeReleaseBs ewpDocTimeReleaseBs;

    public void exec() {
        ewpDocTimeReleaseBs.releaseDoc();
    }
}
