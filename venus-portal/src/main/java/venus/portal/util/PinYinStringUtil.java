package venus.portal.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.log4j.Logger;

/**
 * @author zhaoyapeng
 */
public class PinYinStringUtil {
    private static Logger loger = Logger.getLogger(PinYinStringUtil.class);

    /**
     * 取得给定汉字串的首字母串,即声母串
     *
     * @param chinese 给定汉字串
     * @return 声母串
     */
    public static String getAllFirstLetter(String chinese) {
        if (chinese == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < chinese.length(); ++i) {
            sb.append(getFirstLetter(String.valueOf(chinese.charAt(i))));
        }
        return sb.toString();
    }

    /**
     * 取得给定汉字的首字母,即声母
     * 如果传入多个汉字，仅返回第一个汉字的声母
     * 如果首字符不是汉字，保持原样
     *
     * @param chinese 给定的汉字
     * @return 给定汉字的声母
     */
    public static String getFirstLetter(String chinese) {
        if (chinese == null) {
            return "";
        }

        String firstPY = getCharacterPinYin(chinese.charAt(0));

        // 如果首字符不是汉字，保持原样
        if (firstPY == null) {
            return chinese.substring(0, 1);
        }

        return firstPY.substring(0, 1);
    }

    /**
     * 转换单个汉字, 如果是多音字，仅取第一个发音
     *
     * @param c 给定的汉字
     * @return 给定汉字的拼音
     */
    private static String getCharacterPinYin(char c) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        String pinyin[] = null;

        try {
            pinyin = PinyinHelper.toHanyuPinyinStringArray(c, format);
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            loger.error("拼音输出格式错误");
        }

        // 如果c不是汉字，toHanyuPinyinStringArray会返回null
        if (pinyin == null) {
            return null;
        }

        return pinyin[0];
    }
}
