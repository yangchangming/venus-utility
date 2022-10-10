package venus.lang;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;


public class ArraysTest {

    @Test
    public void testNewArray(){
        String[] ss =  Arrays.newArray(String.class, 100);
        Assert.assertEquals(100, ss.length);
    }

    @Test
    public void testToArray(){
        Map map = new HashMap();
        map.put("name", "HangZhou");
        map.put("age", 33);
        Object[] temp = Arrays.toArray(map.values(), Object.class);
        Assert.assertEquals(2, temp.length);
    }
}
