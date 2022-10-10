package venus.base;

import junit.framework.Assert;
import org.junit.Test;


public class PairTest {

    @Test
    public void buildPairTest(){
        Pair pair = Pair.of(new String("HangZhou"), new Integer(100));
        Assert.assertEquals("HangZhou", pair.getLeft());
        Assert.assertEquals(100, pair.getRight());
    }

    @Test
    public void equalsTest(){
        Pair pair = Pair.of(new String("HangZhou"), new Integer(100));
        Pair pair0 = Pair.of(new String("GuiZhou"), new Integer(200));
        Assert.assertFalse(pair.equals(pair0));
    }
}
