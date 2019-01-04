package venus.oa.util;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.TypeMismatchException;
import venus.frames.base.action.IRequest;
import venus.frames.base.dao.BaseTemplateDao;
import venus.frames.mainframe.util.Helper;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.*;

/**
 * 作用是实现自动处理VO操作中的一些事情
 */
public class VoHelperTools  {
	public VoHelperTools() {
	}


	/**
	 * 把VO中值为""的数据一律替换成"null"
	 * 
	 * @param obj
	 *            输入一个VO
	 * @return 被替换的值个数
	 */
	public static int nothing2Null(Object obj) {
		return accessVo(obj, new ITransctVoField() {
			public int transctVo(BeanWrapper bw, PropertyDescriptor pd) {
				if (!pd.getName().equals("class")) {
					if (String.valueOf(bw.getPropertyValue(pd.getName())) == "") {
						bw.setPropertyValue(pd.getName(), null);
						return 1;
					} else {
						return 0;
					}
				} else {
					return 0;
				}
			}
		});
	}

	/**
	 * 把VO中的数据一律把空格trim()
	 * 
	 * @param obj
	 *            输入一个VO
	 * @return 被替换的值个数
	 */
	public static int trim(Object obj) {
		return accessVo(obj, new ITransctVoField() {
			public int transctVo(BeanWrapper bw, PropertyDescriptor pd) {
				if (!pd.getName().equals("class")) {
					if (bw.getPropertyType(pd.getName()).getName().equals(
							String.class.getName())
							&& ((String) (bw.getPropertyValue(pd.getName()))) != null) {
						bw.setPropertyValue(pd.getName(), String.valueOf(
								bw.getPropertyValue(pd.getName())).trim());
						return 1;
					} else
						return 0;
				} else {
					return 0;
				}
			}
		});
	}
	
	/**
     * 借助BeanWrapper循环Vo
     * 
     * @param obj 输入一个VO
     * @return 被替换的值个数
     */
    public static int accessVo(Object obj, ITransctVoField transctVoField) {
        int returnCount = 0;
        try {
            BeanWrapper bw = new BeanWrapperImpl(obj);
            PropertyDescriptor pd[] = bw.getPropertyDescriptors();
            for (int i = 0; i < pd.length; i++) {
                try {
                    returnCount += transctVoField.transctVo(bw, pd[i]);
                } catch (ClassCastException e) {
                    //e.printStackTrace();
                    continue;
                } catch (NotReadablePropertyException e) {
                    //e.printStackTrace();
                    continue;
                }catch (TypeMismatchException e) {
                    //e.printStackTrace();
                    continue;
                }
            }
        } catch (Exception e) {
        }
        return returnCount;
    }

    /**
     * 把VO中值为null的数据一律替换成""
     * 
     * @param obj 输入一个VO
     * @return 被替换的值个数
     */
    public static int null2Nothing(Object obj) {
        return accessVo(obj, new ITransctVoField() {
            public int transctVo(BeanWrapper bw, PropertyDescriptor pd) {
                if (!pd.getName().equals("class")) {
                    if (bw.getPropertyValue(pd.getName()) == null) {
                        if(bw.getPropertyType(pd.getName()).getName().equals("java.sql.Timestamp")) {
                            bw.setPropertyValue(pd.getName(), null); 
                        } else {
                            bw.setPropertyValue(pd.getName(), ""); 
                        }
                        return 1;
                    } else {
                        return 0;
                    }
                } else {
                    return 0;
                }
            }
        });
    }
    
    /**
     * 把VO中的关键字值一律替换成ASCII码表示，同时把null换为""
     * 
     * @param obj 输入一个VO
     * @return 操作次数
     */
    public static int replaceToHtml(Object obj) {
        return replaceToHtml(obj, null);
    }

    /**
     * 把VO中的关键字值一律替换成ASCII码表示，同时把null换为""
     * 
     * @param obj 输入一个VO
     * @param ignoreName 
     * @return 操作次数
     */
    public static int replaceToHtml(Object obj, final String[] ignoreName) {
        return accessVo(obj, new ITransctVoField() {
            public int transctVo(BeanWrapper bw, PropertyDescriptor pd) {
                if (!pd.getName().equals("class")) {
                    if(ignoreName != null && ignoreName.length > 0 && StringHelperTools.ArrayContainString(ignoreName, pd.getName())) {
                        return 0;
                    }
                    String tempValue = (String) bw.getPropertyValue(pd.getName());
                    if (tempValue == null && "java.lang.String".equals(pd.getPropertyType().getName())) {
                        bw.setPropertyValue(pd.getName(), "");
                        return 1;
                    } else if("java.lang.String".equals(pd.getPropertyType().getName())){
                        bw.setPropertyValue(pd.getName(), StringHelperTools.replaceStringToHtml(tempValue));
                        return 1;
                    } else {
                        return 0;
                    }
                } else {
                    return 0;
                }
            }
        });

    }

    /**
     * 把VO中的关键字值按指定规则替换成ASCII码表示，同时把null换为""
     * 
     * @param obj 输入一个VO
     * @return 操作次数
     */
    public static int replaceToScript(Object obj) {
        return accessVo(obj, new ITransctVoField() {
            public int transctVo(BeanWrapper bw, PropertyDescriptor pd) {
                if (!pd.getName().equals("class")) {
                    String tempValue = (String) bw.getPropertyValue(pd.getName());
                    if (tempValue == null && "java.lang.String".equals(pd.getPropertyType().getName())) {
                        bw.setPropertyValue(pd.getName(), "");
                        return 1;
                    } else if("java.lang.String".equals(pd.getPropertyType().getName())) {
                        bw.setPropertyValue(pd.getName(), StringHelperTools.replaceStringToScript(tempValue));
                        return 1;
                    } else {
                        return 0;
                    }
                } else {
                    return 0;
                }
            }
        });
    }

    /**
     * 从vo中获取表单值
     * 
     * @param request
     * @return
     */
    public static Map getMapFromVo(Object obj) {
        Map rtMap = new TreeMap();
        if(obj == null) {
            return rtMap;
        }
        if(obj instanceof DynaBean){
            DynaBean dynaObj = (DynaBean) obj;
            DynaProperty[] descriptors = dynaObj.getDynaClass().getDynaProperties();
            for (int i = 0; i < descriptors.length; i++) {
                String tempKey = descriptors[i].getName();
                rtMap.put(tempKey, dynaObj.get(tempKey));
            } 
        }else{
            BeanWrapper bw = new BeanWrapperImpl(obj);
            PropertyDescriptor pd[] = bw.getPropertyDescriptors();
            
            for (int i = 0; i < pd.length; i++) {
                try {
                    if (!pd[i].getName().equals("class")) {
                        String tempKey = pd[i].getName();
                        rtMap.put(tempKey, bw.getPropertyValue(pd[i].getName()));
                    }
                } catch (ClassCastException e) {
                    //e.printStackTrace();
                    continue;
                } catch (NotReadablePropertyException e) {
                    //e.printStackTrace();
                    continue;
                }
            }
        }        
        return rtMap;
    }
    
    /**
     * 功能: 从request中获取表单值
     *
     * @param request
     * @return
     */
    public static Map getMapFromRequest(IRequest request) {
        return getMapFromRequest(request, null);
    }
    
    /**
     * 从request中获取表单值
     * 
     * @param request
     * @return
     */
    public static Map getMapFromRequest(HttpServletRequest request) {
        return getMapFromRequest(request, null);
    }
    
    
    /**
     * 功能: 从request中获取表单值
     *
     * @param request
     * @param aNeedName 关注的key值
     * @return
     */
    public static Map getMapFromRequest(IRequest request, String[] aNeedName) {
        return getMapFromRequest((HttpServletRequest)request, aNeedName);
    }

    /**
     * 从request中获取表单值
     * 
     * @param request
     * @param aNeedName 关注的key值 
     * @return
     */
    public static Map getMapFromRequest(HttpServletRequest request, String[] aNeedName) {
        Map rtMap = new TreeMap();
        Iterator itParms = request.getParameterMap().keySet().iterator();
        while (itParms.hasNext()) {
            String tempKey = (String) itParms.next();
            if(aNeedName != null && !StringHelperTools.ArrayContainString(aNeedName, tempKey)) {
                continue;
            }
            String[] tempArray = request.getParameterValues(tempKey);
            if (tempArray == null || tempArray.length == 0) {
                continue;
            }
            if (tempArray.length == 1) {
                rtMap.put(tempKey, request.getParameter(tempKey));
            } else {  //杜绝了相同value的提交值多次被回写
                Set sUniqueValue = new HashSet();
                for(int i=0; i<tempArray.length; i++) {
                    sUniqueValue.add(tempArray[i]);
                }
                rtMap.put(tempKey, sUniqueValue.toArray(new String[0]));
            }
        }
        return rtMap;
    }
    
    /**
     * 回写表单
     *
     * @param mRequest
     * @return
     */
    public static String writeBackMapToForm(Map mRequest) {
        return writeBackMapToForm(mRequest, new String[]{});
    }


    /**
     * 回写表单
     *
     * @param mRequest
     * @param ignoreName 定义哪些key值的input不回写
     * @return
     */
    public static String writeBackMapToForm(Map mRequest, String[] ignoreName) {
        StringBuffer rtValue = new StringBuffer();
        rtValue.append("  var mForm = new Object();\n");
        rtValue.append("  var indexArray = new Array();\n");
        rtValue.append("  function writeBackMapToForm() {\n");
        Iterator itMRequest = mRequest.keySet().iterator();
        while (itMRequest.hasNext()) {
            String tempKey = (String) itMRequest.next();
            Object tempValue = mRequest.get(tempKey);
            if (tempKey.startsWith("VENUS") || tempKey.startsWith("RANMIN")) {
                continue;                
            }
            if (StringHelperTools.ArrayContainString(ignoreName, tempKey)) {
                continue;                
            }
            String tempValueNew = "";
            if (tempValue instanceof String) { //如果是单值，直接注入
                tempValueNew = StringHelperTools.replaceStringToScript((String)tempValue);  //从数据库中取出来以后需要转换1次
                rtValue.append("    indexArray[indexArray.length] = \"" + tempKey + "\";\n");
                rtValue.append("    mForm[\"" + tempKey + "\"] = \"" + tempValueNew + "\";\n");
            } else if (tempValue instanceof String[]) { //如果是多值，放入数组
                rtValue.append("    indexArray[indexArray.length] = \"" + tempKey + "\";\n");
                String[] myArray = (String[]) tempValue;
                rtValue.append("    mForm[\"" + tempKey + "\"] = [");
                for (int i = 0; i < myArray.length; i++) {
                    if (i > 0)
                        rtValue.append(",");
                    tempValueNew = StringHelperTools.replaceStringToScript(myArray[i]);
                    rtValue.append("\"" + tempValueNew + "\"");
                }
                rtValue.append("];\n");
            } else if (tempValue instanceof Timestamp) { //如果是时间戳，直接注入
                if(tempValue == null) {
                    continue;
                }
                tempValueNew = StringHelperTools.replaceStringToScript(tempValue.toString().substring(0,19));
                rtValue.append("    indexArray[indexArray.length] = \"" + tempKey + "\";\n");
                rtValue.append("    mForm[\"" + tempKey + "\"] = \"" + tempValueNew + "\";\n");
            } else {
                if(tempValue != null) {
                                       
                }
                continue;
            }
        }
        rtValue.append("    for(var i=0; i<indexArray.length; i++) {\n");
        rtValue.append("	  writeBackValue(indexArray[i]);\n");
        rtValue.append("    }\n");
        rtValue.append("  }\n");
        return rtValue.toString();
    }
    /**
     * 功能: 把Iterator中的元素清空
     * 
     * @param iterator
     * @return
     */
    public static int clearIterator(Iterator iterator) {
        int count = 0;
        if (iterator == null)
            return -1;
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
            count++;
        }
        return count;
    }

    /**
     * 功能: 把Set中的null和""去掉
     * 
     * @param iterator
     * @return
     */
    public static int removeNullFromSet(Set set) {
        int count = 0;
        if (set == null)
            return -1;
        try {
            if (set.contains("")) {
                set.remove("");
                count++;
            }
            if (set.contains(null)) {
                set.remove(null);
                count++;
            }
        } catch (NullPointerException e) {
            //e.printStackTrace();
        }
        return count;
    }

    /**
     * 在2个Vo中复制值
     * 
     * @param destinationVo
     * @param originVo
     * @return
     */
    protected static int transferValueFromVo_old(Object destinationVo, Object originVo) {
        int count = 0;
        BeanWrapper bwDestination = new BeanWrapperImpl(destinationVo);
        BeanWrapper bwSource = new BeanWrapperImpl(originVo);
        PropertyDescriptor pdSource[] = bwSource.getPropertyDescriptors();
        for (int i = 0; i < pdSource.length; i++) {
            String name = pdSource[i].getName();
            if (!"class".equals(name))
                try {
                    String tempValue = (String) bwSource.getPropertyValue(name);
                    bwDestination.setPropertyValue(name, tempValue);
                    count++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        return count;
    }

    /**
     * 在2个Vo中复制值
     * 
     * @param destinationVo
     * @param originVo
     */
    public static void transferValueFromVo(Object destinationVo, Object originVo) {
        try {
            BeanUtils.copyProperties(destinationVo, originVo);
        } catch (IllegalAccessException e) {
            //e.printStackTrace();
        } catch (InvocationTargetException e) {
            //e.printStackTrace();
        }
    }
    
    /**
     * 功能: 
     * //        String[] aField = new String[]{"id", "name", "buy_date", "page_number", "author", "type", "brief"};
     * //        RmVoHelper.update(this, vo, aField, SQL_INSERT, obj);
     *
     * @param btd
     * @param thisVo
     * @param strsql
     * @param aObj
     */
    public static void update( BaseTemplateDao btd, Object thisVo, String[] aField, String strsql, Object[] aObj) {
        if(aObj != null && aObj.length > 0) {
            boolean hasNull = false;
            for(int i=0; i<aObj.length; i++) {
                if(aObj[i] == null) {
                    hasNull = true;
                    break;
                }
            }
            if(hasNull) {
                int[] aType = new int[aObj.length];
                for(int i=0; i<aType.length; i++) {
                    aType[i] = getTypeByVoField(thisVo, aField[i]);
                }
                btd.update(strsql, aObj, aType); 
            } else {
                btd.update(strsql, aObj);  
            }
        } else {
            btd.update(strsql);
        }
    }

    /**
     * 功能: 
     *
     * @param thisVo
     * @param object
     * @return
     */
    private static int getTypeByVoField(Object thisVo, String field) {
        BeanWrapper bw = new BeanWrapperImpl(thisVo);
        int sqlType = Types.VARCHAR;
        try {
            //PropertyDescriptor pd = bw.getPropertyDescriptor(field);
            Class fieldClass = bw.getPropertyType(field);
        
            if(fieldClass.equals(String.class)) {
                sqlType = Types.VARCHAR; 
            } else if(fieldClass.equals(Date.class)) {
                sqlType = Types.TIMESTAMP; 
            } else if(fieldClass.equals(Calendar.class)) {
                sqlType = Types.TIMESTAMP; 
            } else if(fieldClass.equals(Timestamp.class)) {
                sqlType = Types.TIMESTAMP; 
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqlType;
    }

	/**
	 * @param vo
	 * @param request
	 * @return
	 */
	public static boolean populate(Object vo, IRequest request) {
		boolean rtValue = true;
		final Map mValue = getMapFromRequest(request);
		rtValue = Helper.populate(vo, request);
		final IRequest thisRequest = request;
		accessVo(vo, new ITransctVoField() {
            public int transctVo(BeanWrapper bw, PropertyDescriptor pd) {
                if (!pd.getName().equals("class")) {
                    //TODO 需要严密整合
                    if( (mValue.get(pd.getName())) instanceof String[]) {
                        String[] aStr = (String[])mValue.get(pd.getName());
                        bw.setPropertyValue(pd.getName(), StringHelperTools.parseToSQLString(aStr));
                    }
                    if ("java.sql.Timestamp".equals(pd.getPropertyType().getName())) {
                        bw.setPropertyValue(pd.getName(), DateTools.getTimestamp(thisRequest.getParameter(pd.getName())));
                        return 1;
                    } else {
                        return 0;
                    }
                } else {
                    return 0;
                }
            }
        });
		
		return rtValue;
	}
	
	/**
	 * 功能: 从map中往vo注值
	 *
	 * @param vo
	 * @param mValue
	 * @return
	 */
	public static boolean populate(Object vo, final Map mValue) {
		boolean rtValue = true;
		accessVo(vo, new ITransctVoField() {
            public int transctVo(BeanWrapper bw, PropertyDescriptor pd) {
                if (!pd.getName().equals("class")) {
                    //String tempValue = (String) bw.getPropertyValue(pd.getName());
                    if(mValue.get(pd.getName()) == null) {
                        return 0;
                    } else {
                        Object targetValue = mValue.get(pd.getName());
                        bw.setPropertyValue(pd.getName(), targetValue); 
                        return 1;
                    }
                } else {
                    return 0;
                }
            }
        });
		
		return rtValue;
	}
	
	/**
	 * 功能: 把vo中的值求出来
	 *
	 * @param vo
	 * @return
	 */
	public static String voToString(Object vo) {
	    final StringBuffer sb = new StringBuffer();
	    final Map mFinalValue = new HashMap();
	    mFinalValue.put("tempIndex", "0");
	    if(vo != null) {
	        sb.append(vo.getClass().getName() + ":");
	        accessVo(vo, new ITransctVoField() {
	            public int transctVo(BeanWrapper bw, PropertyDescriptor pd) {
	                if (!pd.getName().equals("class")) {
	                    int index = Integer.parseInt(String.valueOf(mFinalValue.get("tempIndex")));
	                    mFinalValue.put("tempIndex", String.valueOf(++ index) );
	                    sb.append( "\n" + index + ": " + pd.getName() + "='" + bw.getPropertyValue(pd.getName()) + "'" );
	                    return 1;
	                } else {
	                    return 0;
	                }
	            }
	        });
	    }
        return sb.toString();
	}

	
	/**
	 * 功能: 判断2个vo的值是否相等
	 *
	 * @param vo1
	 * @param vo2
	 * @return
	 */
	public static boolean voEquals(Object vo1, final Object vo2) {
	    final Map mFinalValue = new HashMap();
	    mFinalValue.put("tempIndex", "0");
	    mFinalValue.put("tempEquals", "1");
	    if(vo1 != null && vo2 != null) {
	        accessVo(vo1, new ITransctVoField() {
	            public int transctVo(BeanWrapper bw, PropertyDescriptor pd) {
	                String currentKey = pd.getName();
	                if (!currentKey.equals("class")) {
	                    boolean bEquals = (String.valueOf(mFinalValue.get("tempEquals"))).equals("1") ? true : false ;
                        if(bEquals) {  //只有true才进行比较
                            BeanWrapper bw2 = new BeanWrapperImpl(vo2);
                            if(bw.getPropertyValue(currentKey) == null) {
                                if(bw2.getPropertyValue(currentKey) != null) {
                                    bEquals = false;
                                }
                            } else {
                                if(!bw.getPropertyValue(currentKey).equals(bw2.getPropertyValue(currentKey))) {
                                    bEquals = false; 
                                }
                            }
    	                    int index = Integer.parseInt(String.valueOf(mFinalValue.get("tempIndex")));
    	                    mFinalValue.put("tempIndex", String.valueOf(++ index) );
                            
                            mFinalValue.put("tempEquals", bEquals ? "1" : "0" );
                        }
	                    return 1;
	                } else {
	                    return 0;
	                }
	            }
	        });
	    } else if(vo1 == null && vo2 == null) {
	        return true;
	    }
	    boolean bEquals = (String.valueOf(mFinalValue.get("tempEquals"))).equals("1") ? true : false ;
	    return bEquals;
	}
	
	
	
	/**
	 * 功能: 把object中的值求出来
	 *
	 * @param vo
	 * @return
	 */
	public static String objectToString(Object thisObj) {
	    String returnStr = "";
	    if(thisObj == null) {
	        return String.valueOf(thisObj);
	    }
	    returnStr += thisObj.getClass().getName() + ": ";
	    if(thisObj instanceof List) {
	        returnStr += "\nsize()=" + ((List)thisObj).size();
	        int index = 0;
	        for(Iterator itThisObj = ((List)thisObj).iterator(); itThisObj.hasNext(); ) {
	            Object singleObj = itThisObj.next();
	            returnStr += "\n" + ++index + ": " + singleObj;
	        }
	    } else if(thisObj instanceof Object[]) {
	        returnStr += "\nsize()=" + ((Object[])thisObj).length;
	        for(int i=0; i<((Object[])thisObj).length; i++) {
	            Object singleObj = ((Object[])thisObj)[i];
	            returnStr += "\n" + i + ": " + singleObj;
	        }
	    } else {
	        returnStr += thisObj;
	    }
	    return returnStr;
	}
	
	
	/**
	 * 功能: 得到vo中的某个field的值
	 *
	 * @param vo
	 * @param field
	 * @return
	 */
	public static Object getVoFieldValue(Object vo, String field) {
	    Object rtObj = null;
	    Class fromcls = vo.getClass();
	    String getMethodName = "get" + StringHelperTools.toFirstUpperCase(field);
	    try {
	        Method getMethod = fromcls.getDeclaredMethod(getMethodName,new Class[]{});
            rtObj = getMethod.invoke(vo ,new Object[] {});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtObj;
	}
	
	/**
	 * 功能: 设置vo中的某个field的值
	 *
	 * @param vo
	 * @param field
	 * @return
	 */
	public static boolean setVoFieldValue(Object vo, String field, Object value) {
	    boolean rtValue = false;
	    Class fromcls = vo.getClass();
	    String getMethodName = "set" + StringHelperTools.toFirstUpperCase(field);
	    try {
	        Method getMethod = null;
	        if(value != null) {
	            getMethod = fromcls.getDeclaredMethod(getMethodName,new Class[]{value.getClass()});
	        } else {
	            getMethod = fromcls.getDeclaredMethod(getMethodName,new Class[]{String.class});	            
	        }
            
	        getMethod.invoke(vo ,new Object[] {value});
        } catch (Exception e) {
            e.printStackTrace();
            return rtValue;
        }
        rtValue = true;
        return rtValue;
	}
	
	/**
	 * 功能: 看数组有几个null
	 *
	 * @param aObj
	 * @return
	 */
	public static int arrayHasNull(Object[] aObj) {
	   if(aObj == null) {
	       return -1;
	   } else {
	       int sum = 0;
	       for(int i=0; i<aObj.length; i++) {
	           if(aObj[i] == null) {
	               sum ++;
	           }
	       }
	       return sum;
	   }
	}
	
	/**
	 * 功能: 返回数组第一个为空的index
	 *
	 * @param aObj
	 * @return
	 */
	public static int firstArrayNull(Object[] aObj) {
		   if(aObj == null) {
		       return -1;
		   } else {
		       for(int i=0; i<aObj.length; i++) {
		           if(aObj[i] == null) {
		               return i;
		           }
		       }
		       return -1;
		   }
		}
	
    /**
     * 功能：去掉指定list中重复的元素
     * @param list
     * @return
     */
    public  static List removeDuplicate(List list)  {  
        Set set = new HashSet();
        List newList = new ArrayList();
        for (Iterator iter = list.iterator();iter.hasNext(); ) {
        Object element = iter.next();
          if (set.add(element))
             newList.add(element);
           }
        list.clear();
        list.addAll(newList);
       return  list;
    }

}
