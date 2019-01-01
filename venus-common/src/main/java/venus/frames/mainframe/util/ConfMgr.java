package venus.frames.mainframe.util;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import venus.VenusHelper;
import venus.frames.mainframe.log.LogMgr;
import venus.pub.util.Encode;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * 集中配置文件的拾取器.
 * 采用单实例模式( singleton model)，
 * 运用静态方法作为代理调用该单一实例的方法<br>
 */
public class ConfMgr implements Serializable {

    /**
     * XML配置文件的文档对象
     */
    private Document m_XmlDocument = null;

    /**
     * 存储所有IConfReader
     */
    private Hashtable m_hashIConfReader = null;

    /**
     * singleton model中用到的，用于存储该类的单一实例
     */
    private static ConfMgr m_Singleton = null;

    /**
     *
     */
    public static String NAME_MAP_KEY = "name-map";

    private Hashtable m_hashAlias = null;


    /**
     * XML配置文件的文档对象根节点对象
     */
    private Element m_RootDocumentElement = null;

    /**
     * 默认构造函数
     *
     * @roseuid 3F94E27403A6
     */
    public ConfMgr() {
        super();
        loadConf();
    }

    /**
     * 暂时不实现
     *
     * @return Element
     * @roseuid 3F933E0F0056
     */
    public static Element getElement() {
        return null;
    }

    public static Node getNodeByClassName(String classname) {
        ConfMgr cm = getSingleton();
        String alias = cm.getAliasByClassName(classname);
        if (alias == null) return null;
        return cm.fetchNode(alias);
    }

    public static Node getNodeByAlias(String alias) {
        ConfMgr cm = getSingleton();
        String clsname = cm.getClassNameByAlias(alias);
        if (clsname == null) return null;
        return cm.fetchNode(clsname);
    }

    public String getAliasByClassName(String classname) {
        if (m_hashAlias != null && m_hashAlias.containsKey(classname)) {
            return (String) m_hashAlias.get(classname);
        }
        return null;
    }

    public String getClassNameByAlias(String alias) {
        if (m_hashAlias != null && m_hashAlias.containsValue(alias)) {
            Enumeration e = m_hashAlias.keys();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                if (alias.equals(m_hashAlias.get(key))) return key;
            }
        }
        return null;
    }

    public static Node fetchNodeByTag(String tag) {
        ConfMgr cm = getSingleton();
        return cm.fetchNode(tag);
    }

    public Node fetchNode(String tag) {

        // 处理字符串为空的情况
        if (tag == null) {
            LogMgr.getLogger("venus.frames.mainframe.util.ConfMgr").error("ConfMgr类中的getNode方法，参数空指针！");
            return null;
        }
        //取得该类的单一实例
        ConfMgr cm = getSingleton();
        //取得根节点对象
        Element root = cm.getDocumentElement();

        try {
            NodeList elementList = root.getElementsByTagName(tag);
            Node tempNode = null;
            if (elementList.getLength() > 0) {
                //if the noe not
                tempNode = elementList.item(0);
            }

            if (tempNode == null) {
                //if the node no find
                LogMgr.getLogger("venus.frames.mainframe.util.ConfMgr").info("ConfMgr类中的getNode方法，未找到相应的节点！" + tag);
                return null;

            }

            //如果节点的link属性不为空，则转移到link属性表明的配置文件读取相应的节点
            Node attrNode = tempNode.getAttributes().getNamedItem("link");
            if (attrNode == null || attrNode.getNodeType() != Node.ATTRIBUTE_NODE) {
                return tempNode;
            }
            String strold = attrNode.getNodeValue();
            if (strold != null) {
                String str = "";
                if (strold.startsWith("{DES}")) {
                    str = Encode.decode(strold.substring(5));
                } else {
                    str = strold;
                }
                Document doc = null;
                Element ele = null;
                InputStream inStream = null;
                LogMgr.getLogger("venus.frames.mainframe.util.ConfMgr").debug("node config link: " + str);
                if (str.startsWith("frmConf://")) {
                    inStream = PathMgr.getResourceAsStream(PathMgr.getConfPath() + str.substring(str.indexOf("://") + 3));
                } else if (str.startsWith("file://")) {
                    inStream = new FileInputStream(str.substring(str.indexOf("://") + 3));
                } else if (str.startsWith("url://")) {

                    URL url = new URL(str.substring(str.indexOf("url://") + 6));
                    inStream = url.openStream();

                } else if (str.startsWith("frmRoot://")) {
                    inStream = PathMgr.getResourceAsStream(PathMgr.getSingleton().getRootPath() + str.substring(str.indexOf("://") + 3));
                } else if (str.startsWith("provider://")) {
                    String strPlugin = str.substring(str.indexOf("://") + 3);
                    if (strPlugin == null || strPlugin.indexOf(46) < 1) return tempNode;
                    try {
                        Class c = ClassLocator.loadClass(strPlugin);
                        IConfProvider icp = (IConfProvider) c.newInstance();
                        return icp.getNode(tag, this);
                    } catch (ClassNotFoundException cnfe) {
                        LogMgr.getLogger(this.getClass().getName()).error("fetchNode(" + tag + "): ClassNotFoundException in " + str, cnfe);
                        return tempNode;
                    } catch (IllegalAccessException iae) {
                        LogMgr.getLogger(this.getClass().getName()).error("fetchNode(" + tag + "): IllegalAccessException in " + str, iae);
                        return tempNode;
                    } catch (InstantiationException ie) {
                        LogMgr.getLogger(this.getClass().getName()).error("fetchNode(" + tag + "): InstantiationException in " + str, ie);
                        return tempNode;
                    }

                } else {
                    return tempNode;
                }

                //从配置文件中得到xml的文档对象
                DocumentBuilderFactory docBuilderFac = DocumentBuilderFactory.newInstance();
                try {
                    DocumentBuilder db = docBuilderFac.newDocumentBuilder();
                    doc = db.parse(inStream);
                    ele = doc.getDocumentElement();
                } catch (ParserConfigurationException e) {
                    LogMgr.getLogger(this.getClass().getName()).error(e.getMessage(), e);
                    return tempNode;
                } catch (SAXException se) {
                    LogMgr.getLogger(this.getClass().getName()).error(se.getMessage(), se);
                    return tempNode;
                }
                if (tag.equals(ele.getNodeName())) {
                    return ele;
                }
                NodeList eleList = ele.getElementsByTagName(tag);
                Node tmpNode = eleList.item(0);
                if (tempNode == null) {
                    LogMgr.getLogger("venus.frames.mainframe.util.ConfMgr").info("ConfMgr类中的getNode方法，未找到相应的节点！" + tag);
                    return null;
                }
                return tmpNode;
            }

            return tempNode;
        } catch (IOException ioe) {
            LogMgr.getLogger("venus.frames.mainframe.util.ConfMgr").error("config node <" + tag + "> Error in IO:");
            return null;
        }

    }

    /**
     * 根据参数tag取得xml中的Node对象，如果同时有多个名称与参数tag相同的节点，<br>
     * 系统默认取第一个节点。
     *
     * @param tag 节点名称
     * @return 第一个名称为tag的节点；如果发生异常则返回null
     * @roseuid 3F933E480299
     */
    public static Node getNode(String tag) {

        Node n = null;
        //char ch = 46;
        if (tag.indexOf(46) > 0) {
            n = getNodeByClassName(tag);
            if (n == null) n = fetchNodeByTag(tag);
        } else {
            n = fetchNodeByTag(tag);
            if (n == null) n = getNodeByAlias(tag);
        }
        return n;
    }

    /**
     * 单实例模式（singleton model），用户可以通过这种方式得到该类的实例，<br>
     * 这样不仅可以减少系统资源的损耗，而且能够减少程序运行出错的概率，增强<br>
     * 程序的健壮性。
     *
     * @return venus.frames.mainframe.util.ConfMgr
     * @roseuid 3F94E28B0089
     */
    public static ConfMgr getSingleton() {
        //先判断m_Singleton是否已实例化，然后返回
        if (m_Singleton == null) {
            m_Singleton = new ConfMgr();
        }
        return m_Singleton;
    }

    /**
     * 从PathMgr类中获取IO流加载配置数据
     *
     * @roseuid 3F94E31B02FB
     */
    public void loadConf() {
        //如果m_XmlDocument为空，需要加载该对象
        if (m_XmlDocument == null) {
            //重新生成一个PathMgr实例以获得输入流
            //PathMgr pm = new PathMgr();
            this.loadXmlFromStream(PathMgr.getConfAsStream());
        }

        //add "name-map" node
        if (m_XmlDocument != null) {

            NodeList elementList = m_XmlDocument.getElementsByTagName(NAME_MAP_KEY);
            Node tempNode = null;
            if (elementList.getLength() > 0) {
                //if the noe not
                tempNode = elementList.item(0);
            }
            if (tempNode == null) {
                LogMgr.getLogger(this.getClass().getName()).error("ConfMgr类中的loadConf方法，未找到相应的节点！" + NAME_MAP_KEY);
                return;
            }

            IConfReader icr = new DefaultConfReader(tempNode);

            if (icr == null) return;
            ArrayList usrAryLst = icr.readChildNodesAry("alias");
            int n_len = usrAryLst.size();

            if (m_hashAlias == null) m_hashAlias = new Hashtable(n_len);
            for (int i = 0; i < n_len; i++) {
                Node tmpNode = (Node) usrAryLst.get(i);
                //取信息
                NamedNodeMap tmpNodeMap = tmpNode.getAttributes();
                Node nameNode = tmpNodeMap.getNamedItem("name");
                Node classNode = tmpNodeMap.getNamedItem("class");

                String name = nameNode.getNodeValue();
                String clsName = classNode.getNodeValue();

                m_hashAlias.put(clsName, name);
            }

            //add Helper node
            String key = VenusHelper.class.getName();
            NodeList elementListhelp = m_XmlDocument.getElementsByTagName(key);


            Node tempNodehelp = null;
            if (elementListhelp.getLength() > 0) {
                tempNodehelp = elementListhelp.item(0);
            }
            if (tempNodehelp == null) {
                LogMgr.getLogger(this.getClass().getName()).error("ConfMgr类中的loadConf方法，未找到相应的节点！" + key);
                return;
            }

			/*
            java.lang.reflect.Field[] flds = Helper.getFields();
			
			Map fldNameMap = new HashMap();
			
			int fldslen = flds.length;
		
			for(int i = 0;i<fldslen;i++ ){
				
				
				fldNameMap.put(flds[i].getName(),flds[i].get())
				
			
			}
			*/

            // parse help node attributes
            NamedNodeMap tmpNodeMap = null;
            Node tmpNode = null;
            try {
                tmpNodeMap = tempNodehelp.getAttributes();
                int len = tmpNodeMap.getLength();
                for (int i = 0; i < len; i++) {
                    try {
                        tmpNode = tmpNodeMap.item(i);
                        String name = tmpNode.getNodeName();
                        String value = tmpNode.getNodeValue();
                        java.lang.reflect.Field fld = VenusHelper.class.getField(name);
                        if (fld.getType().getName().equals("boolean")) {
                            if ("1".equalsIgnoreCase(value) || "true".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value) || "y".equalsIgnoreCase(value)) {
                                fld.setBoolean(VenusHelper.class, true);
                            } else {
                                fld.setBoolean(VenusHelper.class, false);
                            }
                        }
                        if (fld.getType().getName().equals("int")) {
                            fld.setInt(VenusHelper.class, Integer.parseInt(value));
                        }
                        if (fld.getType().getName().equals("java.lang.String")) {
                            fld.set(VenusHelper.class, value);
                        }
                    } catch (Exception e) {
                        LogMgr.getLogger(this).info("error read venus.frames.mainframe.util.Helper Attribute in conf.xml!");
                        continue;
                    }
                }


                //deal "ConstantSetting"
                icr = new DefaultConfReader(tempNodehelp);
                if (icr == null) return;

                ArrayList csAryLst = icr.readChildNodesAry("ConstantSetting");

                int cslen = csAryLst.size();

                Hashtable hashCS = new Hashtable(cslen);

                //将信息存到数组中
                for (int i = 0; i < cslen; i++) {
                    try {
                        Node tmpNode1 = (Node) csAryLst.get(i);
                        //取信息
                        NamedNodeMap tmpNodeMap1 = tmpNode1.getAttributes();
                        Node classNameNode = tmpNodeMap1.getNamedItem("className");
                        Node propertyNameNode = tmpNodeMap1.getNamedItem("propertyName");
                        Node valueNode = tmpNodeMap1.getNamedItem("value");

                        String className = classNameNode.getNodeValue();
                        String propertyName = propertyNameNode.getNodeValue();
                        String value = valueNode.getNodeValue();

                        Class target = ClassLocator.loadClass(className);
                        java.lang.reflect.Field fld = target.getField(propertyName);

                        if (fld.getType().getName().equals("boolean")) {
                            fld.setBoolean(target, getBooleanFromString(value));
                        }
                        if (fld.getType().getName().equals("int")) {
                            fld.setInt(target, Integer.parseInt(value));
                        }
                        if (fld.getType().getName().equals("java.lang.String")) {
                            fld.set(target, value);
                        }
                    } catch (Exception e) {
                        LogMgr.getLogger(this).info("error read ConstantSetting in conf.xml!");
                        continue;
                    }
                }

            } catch (Exception e) {
                LogMgr.getLogger(this).info("error read venus.frames.mainframe.util.Helper Attribute or ConstantSetting in conf.xml!");
            }


        }

    }

    public static boolean getBooleanFromString(String value) {

        if ("1".equalsIgnoreCase(value) || "true".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value) || "y".equalsIgnoreCase(value)) {

            return true;

        } else {

            return false;

        }
    }

    /**
     * 此方法针对于接口IConfReader而用；
     * <p/>
     * 项目组件可以编写自己的组件实现接口中的方法，并在程序中调用该方法。<br>
     * <p/>
     * 传入IConfReader和Tag系统程序即可将对应于该Tag取得Node对象，并回调IConfReader.fromXml(...);
     *
     * @param cr  IConfReader实例
     * @param tag 节点名称
     * @roseuid 3F9E03C002BF
     */
    public static void buildReader(IConfReader cr, String tag) {
        cr.fromXml(getNode(tag));
    }

    /**
     * 系统初始化时从PathMgr类获取集中配置文件的xml doc对象
     *
     * @param is xml文档的输入流
     * @roseuid 3F9E1BE4033C
     */
    private void loadXmlFromStream(InputStream is) {
        //从配置文件中得到xml的文档对象
        DocumentBuilderFactory docBuilderFac = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        try {
            db = docBuilderFac.newDocumentBuilder();
            this.m_XmlDocument = db.parse(is);
        } catch (SAXException e) {
            LogMgr.getLogger(this).error("loadXmlFromStream方法", e);
        } catch (IOException e) {
            LogMgr.getLogger(this).error("loadXmlFromStream方法", e);
        } catch (ParserConfigurationException e) {
            LogMgr.getLogger(this).error("loadXmlFromStream方法", e);
        }
    }

    /**
     * 获取xml文档的根节点对象
     *
     * @return Element xml文档的根节点对象
     * @roseuid 3F9E217800BB
     */
    public Element getDocumentElement() {
        if (m_RootDocumentElement == null) {
            //处理xml配置文件文档对象为空
            if (m_XmlDocument == null) {
                //重新生成一个PathMgr实例以获得输入流
                //PathMgr pm = new PathMgr();
                this.loadXmlFromStream(PathMgr.getConfAsStream());
            }
            m_RootDocumentElement = m_XmlDocument.getDocumentElement();
        }
        return m_RootDocumentElement;
    }

    /**
     * 根据给定的TAG名，返回配置解析器
     * 此方法为辅助方法，帮助项目组件更便捷的获取配置数据<br>
     * 建议用户使用该方法取得解析xml节点的类实例。
     *
     * @param tag 待解析的节点名称
     * @return xml文档节点解析器
     */
    public static IConfReader getConfReader(String tag) {
        ConfMgr cm = getSingleton();

        //初始化哈希表
        if (cm.m_hashIConfReader == null) {
            cm.m_hashIConfReader = new Hashtable();
        }
        //如果哈希表存在相应对象，则直接取出；否则先构建然后再放入哈希表并返回该对象
        if (cm.m_hashIConfReader.containsKey(tag)) {
            return (IConfReader) cm.m_hashIConfReader.get(tag);
        } else {
            Node tmpNode = ConfMgr.getNode(tag);
            if (tmpNode == null) return null;
            IConfReader dcf = new DefaultConfReader(tmpNode);
            cm.m_hashIConfReader.put(tag, dcf);
            return dcf;
        }
    }
}
