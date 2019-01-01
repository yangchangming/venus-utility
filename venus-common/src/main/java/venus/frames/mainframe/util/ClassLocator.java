//Source file: D:\\venus_view\\Tech_Department\\Platform\\Venus\\4项目开发\\1工作区\\4实现\\venus\\frames\\mainframe\\util\\ClassLocator.java

package venus.frames.mainframe.util;

/**
 * Class 加载定位类：
 * 项目中所有"Class.forName(...)"
 * 都需要被"ClassLocator.loadClass(...)"替换
 * 
 * @author 张文韬
 */
public class ClassLocator {

	/**
	 * Load a class by its name.  Similar to Class.forName() except it looks for
	 * a context class loader and tries that if it exists.  Otherwise it then
	 * just uses the default system classloader<br>
	 * 通过类名来加载类。该方法是对Class.forName(...)的封装。
	 * @param className 待加载的类名
	 * @return 已经加载的Class对象
	 * @throws ClassNotFoundException 如果不能找到相应的类
	 * @roseuid 3F8E48A5027E
	 */
	public static Class loadClass(String className)
		throws ClassNotFoundException {
		//对Class.forName()进行封装
		//ClassLoader cl = Thread.currentThread().getContextClassLoader();
		//if (cl == null) {
			return Class.forName(className);
		//} else {
		//	return Class.forName(className, true, cl);
		//}
	}
}
