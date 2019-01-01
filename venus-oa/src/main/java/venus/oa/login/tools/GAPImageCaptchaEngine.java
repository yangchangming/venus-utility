package venus.oa.login.tools;

import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.UniColorBackgroundGenerator;
import com.octo.captcha.component.image.color.RandomRangeColorGenerator;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.RandomTextPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;
import com.octo.captcha.engine.image.ListImageCaptchaEngine;
import com.octo.captcha.image.gimpy.GimpyFactory;

import java.awt.*;

public class GAPImageCaptchaEngine extends ListImageCaptchaEngine {
	protected void buildInitialFactories() {
		// 随机生成的字符
		WordGenerator wgen = new RandomWordGenerator("ABDEFGHJMNQRTYabcdefghjkmnpqrstuvwxyz23456789");//没有i、0、1和o
		RandomRangeColorGenerator cgen =new RandomRangeColorGenerator(
											new int[] { 0, 100 }, new int[] { 0, 100 },
											new int[] { 0, 100 });
		// 文字显示的个数
		TextPaster textPaster = new RandomTextPaster(new Integer(4),
											new Integer(4), cgen);
		// 图片的大小
		BackgroundGenerator backgroundGenerator = new UniColorBackgroundGenerator(
											new Integer(100), new Integer(45));
		// 字体格式
		Font[] fontsList = new Font[] {
								new Font("Arial", 0, 10),
								new Font("Tahoma", 0, 10), new Font("Verdana", 0, 10), };
		// 文字的大小
		FontGenerator fontGenerator = new RandomFontGenerator(new Integer(15),
											new Integer(30), fontsList);    
		WordToImage wordToImage = new ComposedWordToImage(fontGenerator,
											backgroundGenerator, textPaster);
		addFactory(new GimpyFactory(wgen, wordToImage));
	}
}

