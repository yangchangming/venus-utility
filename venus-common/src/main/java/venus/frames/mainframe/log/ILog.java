package venus.frames.mainframe.log;

import java.io.Serializable;

/**
 * log 所要提供的基本功能接口
 * 
 * @author 岳国云
 */
public interface ILog extends Serializable 
{
   
   /**
    * log类型：调试信息
    */
   public final static int DEBUG_INT = 1;
   
   /**
    * log类型：一般信息
    */
   public final static int INFO_INT = 2;
   
   /**
    * log类型：警告信息
    */
   public final static int WARN_INT = 3;
   
   /**
    * log类型：错误信息
    */
   public final static int ERROR_INT = 4;
   
   /**
    * log类型：严重错误信息
    */
   public final static int FATAL_INT = 5;
   
   /**
    * 记录 调试信息
    * @param message
    * @roseuid 3F4487C0036A
    */
   public void debug(Object message);
   
   /**
    * 记录 一般信息
    * @param message
    * @roseuid 3F4487C2001E
    */
   public void info(Object message);
   
   /**
    * 记录 警告信息
    * @param message
    * @roseuid 3F44883A0260
    */
   public void warn(Object message);
   
   /**
    * 记录 错误信息
    * @param message
    * @roseuid 3F4487C203C8
    */
   public void error(Object message);
   
   /**
    * 记录 严重错误信息
    * @param message
    * @roseuid 3F4487C4008B
    */
   public void fatal(Object message);
   
   /**
    * 按 nLevel 定义的级别（该级别即 DEBUG_INT定义的）
    * 
    * 按级记录 系统日志信息
    * @param nLevel
    * @param message
    * @roseuid 3F4487BF00DA
    */
   public void log(int nLevel, Object message);
   
   /**
    * 记录 调试信息 和 抛出的异常诱因
    * @param message
    * @param t
    * @roseuid 3F77EC2000AB
    */
   public void debug(Object message, Throwable t);
   
   /**
    * 记录 一般信息 和 抛出的异常诱因
    * @param message
    * @param t
    * @roseuid 3F77F0D50231
    */
   public void info(Object message, Throwable t);
   
   /**
    * 记录 警告信息 和 抛出的异常诱因
    * @param message
    * @param t
    * @roseuid 3F77F1190203
    */
   public void warn(Object message, Throwable t);
   
   /**
    * 记录 错误信息 和 抛出的异常诱因
    * @param message
    * @param t
    * @roseuid 3F77F12001C4
    */
   public void error(Object message, Throwable t);
   
   /**
    * 记录 严重错误信息 和 抛出的异常诱因
    * @param message
    * @param t
    * @roseuid 3F77F126030C
    */
   public void fatal(Object message, Throwable t);
   
   /**
    * 按 nLevel 定义的级别（该级别即 DEBUG_INT定义的）
    * 
    * 按级记录 系统日志信息 和 抛出的异常诱因
    * @param nLevel
    * @param message
    * @param t
    * @roseuid 3F77F13000F9
    */
   public void log(int nLevel, Object message, Throwable t);
   
   public boolean isDebugEnabled();
	
   public boolean isInfoEnabled();
   
   
}
