package venus.oa.util.transform.json;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimestampJsonValueProcessor implements JsonValueProcessor {
	
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";   
    private DateFormat dateFormat;
    
	public TimestampJsonValueProcessor() {
		dateFormat = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
	}
	
	public TimestampJsonValueProcessor(String datePattern) {
        try {   
            dateFormat = new SimpleDateFormat(datePattern);   
        } catch (Exception ex) {   
            dateFormat = new SimpleDateFormat(DEFAULT_DATE_PATTERN);   
        }
	}
	
	public Object processArrayValue(Object value, JsonConfig jsonConfig) {
		return process(value,jsonConfig);
	}
	
	public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
		return process(value,jsonConfig);
	}	
	
	private Object process(Object value, JsonConfig jsonConfig) {
		if (value == null) 
			return null;
		Calendar c = Calendar.getInstance();
		c.setTime((Date)value);
		return dateFormat.format(c.getTime());
	}

}

