package venus.cron.bo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on Oct 9, 2003
 * 
 * @author Matthew Payne DefinitionManager = maintain a list of JobDefinitions
 */
public class DefinitionManager {
    

    private Map definitions = new HashMap();;
    //private String capacity;
    public DefinitionManager() {
    }

    public DefinitionManager(/*String capacity,*/ Map definitions){
        //this.capacity = capacity;
        this.definitions = definitions;
    }
    public void addDefinition(String key, JobDefinition definition) {
        this.definitions.put(key, definition);
    }

//    public void removeDefinition(String name) {
//        if (definitions.containsKey(name)) {
//            definitions.remove(name);
//        }
//    }

//    public JobDefinition getDefinition(String jobName) {
//        return (JobDefinition) definitions.get(jobName);
//    }

    public Map getDefinitions() {
        return this.definitions;
    }
    
    public void setDefinitions(Map defMap){
        this.definitions = defMap;
    }

//    /**
//     * @return 返回 capacity。
//     */
//    public String getCapacity() {
//        return capacity;
//    }
//    /**
//     * @param capacity 要设置的 capacity。
//     */
//    public void setCapacity(String capacity) {
//        this.capacity = capacity;
//    }
}