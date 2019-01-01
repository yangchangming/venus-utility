package venus.pub.util;


public class Encode
{
	
	/**
	 * 解密的命令字
	 */
	public static final String CMD_DECODE = "-decode";

	/**
	 * 加密的命令字
	 */
	public static final String CMD_ENCODE = "-encode";


	/**
	 * 帮助的命令字
	 */
	public static final String CMD_HELP = "-help";
	
/**
 *	解密
 *	
 *
 */
public static String decode(String s)
{
	String res = "";
	DES des = new DES(getKey());
	byte[] sBytes = s.getBytes();
	for (int i = 0; i < (sBytes.length / 16); i++)
	{
		byte[] theBytes = new byte[8];
		for (int j = 0; j <= 7; j++)
		{
			byte byte1 = (byte) (sBytes[16 * i + 2 * j] - 'a');
			byte byte2 = (byte) (sBytes[16 * i + 2 * j + 1] - 'a');
			theBytes[j] = (byte) (byte1 * 16 + byte2);
		}
		long x = des.bytes2long(theBytes);
		byte[] result = new byte[8];
		des.long2bytes(des.decrypt(x), result);
		res = res + (new String(result));
	}
	return res.trim();
}

/**
 *	加密
 *	
 *
 */
public static String encode(String s) {
	String res = "";
	DES des = new DES(getKey());
	byte space = 0x20;
	byte[] sBytes = s.getBytes();
	int length = sBytes.length;
	int newLength = length + (8 - length % 8) % 8;
	byte[] newBytes = new byte[newLength];
	for (int i = 0; i < newLength; i++) {
		if (i <= length - 1) {
			newBytes[i] = sBytes[i];
		}
		else {
			newBytes[i] = space;
		}
	}
	for (int i = 0; i < (newLength / 8); i++) {
		byte[] theBytes = new byte[8];
		for (int j = 0; j <= 7; j++) {
			theBytes[j] = newBytes[8 * i + j];
		}
		long x = des.bytes2long(theBytes);
		byte[] result = new byte[8];
		des.long2bytes(des.encrypt(x), result);
		byte[] doubleResult = new byte[16];
		for (int j = 0; j < 8; j++) {
			doubleResult[2 * j] = (byte) (((((char) result[j]) & 0xF0) >> 4) + 'a');
			doubleResult[2 * j + 1] = (byte) ((((char) result[j]) & 0x0F) + 'a');
		}
		res = res + new String(doubleResult);
	}
	return res;
}
/**
 * 固定的加密Key
 * @return long
 */
private static long getKey() {
	return 1231234234;
}

/**
 * 启动任务集中调度的守护服务程序，主要为命令行启动使用
 * 
 * @param args
 * @return void
 */
public static void main(String[] args) {


	//默认启动服务
	if (args.length < 2) {
		showUsage();
		//通过命令"-startup"启动服务
	} else if (CMD_DECODE.equals(args[0])) {
		
		System.out.println(decode(args[1]));
		
		//解密
	} else if (CMD_ENCODE.equals(args[0])) {

		
		System.out.println(encode(args[1]));
		
		
		//加密
	} else if (CMD_HELP.equals(args[0])) {
		showUsage();
		//返回帮助
	} else {
		showUsage();
	}

	
}


/**
 * 显示使用方法，命令行操作时提示该类用法
 * 
 * @return void
 */
public static void showUsage() {

	//显示使用方法
	showMessage("");
	showMessage("  Encode Tools [Version 0.1]");
	showMessage("  Usage: java venus.pub.util.Encode [-option] String");
	showMessage("  where options include:");
	showMessage("\t"+CMD_ENCODE+" StringToEncode \t encode the string");
	showMessage("\t"+CMD_DECODE+" StringToDecode \t decode the string");
	showMessage("\t"+CMD_HELP+"   \t\t Show this usage");
	
	
}

/**
 * @param msg - 传入的消息
 */
private static void showMessage(String msg) {

	System.out.println(msg);

}

}