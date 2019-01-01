/*
 * Copyright (c) 2006 UFIDA Software Engineering Co, Inc.
 * All Rights Reserved
 *
 * Created date 2006-11-5
 * @author changming.y
 **/
package venus.commons.xmlenum;

import java.util.*;

public class EnumValueMap {

// ------------------------------ FIELDS ------------------------------
    // log utility
//    private static Logger logger = Logger.getLogger(EnumValueMap.class.getName());

    protected String name;                       // name of the value map
    protected String resource = null;           // resource bundle url
    protected Map enums = new HashMap();        // enum map (value to label)
    protected List enumList = new ArrayList();  // enumList

    protected Map mapEnumLabels = new HashMap();     // enum map (label to value)

    protected ResourceBundle bundle = null;
    protected boolean initialized = false;      // initialize falsg

// --------------------------- CONSTRUCTORS ---------------------------

    public EnumValueMap() {
        initialized = false;
        resource = null;
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

// ------------------------ CANONICAL METHODS ------------------------

    public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append("EnumValueMap");
        buf.append("{name=").append(name);
        buf.append(",enums=").append(enums);
        buf.append('}');
        return buf.toString();
    }

// -------------------------- OTHER METHODS --------------------------

    /**
     * add enum item
     *
     * @param item
     */
    public void addEnum(EnumItem item) {
        if (item == null) return;
        
        String label = item.getLabel();
		item.setLabel(label);

        //enums.put(item.getValue(), item.getLabel());
        //modified by zhl on Apr 28, 2004 to correct the bug occured in getLabel.
        this.enums.put(item.getValue(), item);

        //added to support getValue according its label.
        this.mapEnumLabels.put(item.getLabel(), item);
        this.enumList.add(item);
    }

    /**
     * return a list containing all the enums
     *
     * @return list of enums
     */
    public List getEnumList() {

        List list = new ArrayList();
        list.addAll(enumList);

        return list;
    }

    /**
     * get the resource bundle
     *
     * @return resource bundle
     */
//    public ResourceBundle getResourceBundle() {
//
//        // try load the resource bundle
//        if (!initialized && bundle == null && resource != null) {
//            try {
//                initialized = true;
//
//                // solve the problem of load resource from multiple classloader
//                bundle = ResourceUtils.getBundle(resource);
//            } catch (Exception e) {
//                logger.log(Level.WARNING, "Can't initialize the resource bundle using the " +
//                        "resource url :" + resource);
//                resource = null;
//                bundle = null;
//            }
//        }
//
//        return bundle;
//    }

    /**
     * get enum label
     *
     * @param value
     * @return
     */
    public String getLabel(String value) {

        EnumItem itm = (EnumItem) enums.get(value);
        if (itm == null)
            return null;

        return itm.getLabel();
    }

    /**
     * get enum value by the original label
     *
     * @param label the original label
     * @return the corresponding value of label, null returned if the label doesn't exist.
     */
    public String getValue(String label) {
        String value = null;
        EnumItem item = (EnumItem) this.mapEnumLabels.get(label);
        if (item != null) {
            value = item.getValue();
        }
        return value;
    }
}

