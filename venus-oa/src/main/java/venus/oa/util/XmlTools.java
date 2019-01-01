/**
 * Copyright 2003-2010 UFIDA Software Engineering Co., Ltd. 
 */
package venus.oa.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author changming.Y <changming.yang.ah@gmail.com>
 *
 */
public class XmlTools {
    
    private static XStream xStream = new XStream(new DomDriver());
    
    static{
        xStream.alias("map", Map.class);
    }
    
    public static Map xmlToMap(String xml){
        try{
            return (Map) xStream.fromXML(xml);
        }catch(Exception e){
            e.printStackTrace();
        }
        return Collections.emptyMap();
    }
    public static String mapToXml(Map map){
        try{
            return xStream.toXML(map);
        }catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }
    
    public static void main(String args[]){
        Map map = new HashMap();
        map.put("name","chris");
        map.put("island","faranga");
        System.out.println(mapToXml(map));
        StringBuffer xml = new StringBuffer();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        xml.append("<map author=\"模型生成器\" date=\"2011-02-25 16:35:49\" destination=\"venus.rayoo.model.party.menupartytype.util.MetaData\">");
        xml.append("    <entry>");
        xml.append("        <string>query_fields_sql</string>");
        xml.append("        <string>name, creator, createdate, updator, updatedate, remark, tenantid, tablename, tony </string>");
        xml.append("    </entry>");
        xml.append("    <entry>");
        xml.append("        <string>primary_key_sql</string>");
        xml.append("        <string>id</string>");
        xml.append("    </entry>");
        xml.append("    <entry>");
        xml.append("        <string>query_value_sql</string>");    
        xml.append("        <string>?,?,?,?,?,?,?,?,?</string>");   
        xml.append("    </entry>");
        xml.append("    <entry>");
        xml.append("        <string>update_fields_sql</string>");
        xml.append("        <string>name=?, creator=?, createdate=?, updator=?, updatedate=?, remark=?, tenantid=?, tablename=?, tony =?</string>");
        xml.append("    </entry>");
        xml.append("    <entry>");
        xml.append("        <string>table_sql</string>");
        xml.append("        <string>MENU_PARTYTYPE</string>");
        xml.append("    </entry>");
        xml.append("    <entry>");
        xml.append("        <string>update_data</string>");
        xml.append("        <string>name,creator,createdate,updator,updatedate,remark,tenantid,tablename,tony,id</string>");
        xml.append("    </entry>");
        xml.append("    <entry>");
        xml.append("        <string>insert_data</string>");
        xml.append("        <string>id,name,creator,createdate,updator,updatedate,remark,tenantid,tablename,tony</string>");
        xml.append("    </entry>");
        xml.append("    <entry>");
        xml.append("        <string>key_data</string>");
        xml.append("        <string>id</string>");
        xml.append("    </entry>");
        xml.append("</map>");
        System.out.println(xmlToMap(xml.toString()));
    }
}
