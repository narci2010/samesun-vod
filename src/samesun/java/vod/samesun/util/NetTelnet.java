package vod.samesun.util;

import java.io.InputStream;
import java.io.PrintStream;
import org.apache.commons.net.telnet.TelnetClient;

/**
 * 利用apache net 开源包，使用telnet方式获取AIX主机信息 http://zhaoyl.iteye.com/blog/219563
 * 
 * @author zhaoyl
 * @date 20008.7.21
 * @version 1.2
 */

public class NetTelnet {

	// Telnet对象
	private TelnetClient telnet = new TelnetClient();
	private InputStream in;
	private PrintStream out;

	// 提示符。具体请telnet到AIX主机查看
	// private char prompt = '#';
	private char prompt = '#';
	// telnet端口
	private String port;
	// 用户
	private String user;
	// 密码
	private String password;
	// IP地址
	private String ip;

	private int MAX_BUFFER_CHAR_COUNT = 1000; // Telnet返回的数符的数量；

	public NetTelnet() {

		try {
			// AIX主机IP
			/*
			 * this.ip = "10.1.2.222"; this.password = "loeisdke"; this.user =
			 * "whdiwpasdq232sd2323"; this.port = "23";
			 */
			this.ip = ""; // VideoPlayConfig.getInstance().videoplay_Socket_IP;//从服务器配置的数据库内，进行获取
			this.port = "8000";
			this.user = "root";
			this.password = ""; // VideoPlayConfig.getInstance().videoplay_Socket_PORT;//从服务器配置的数据库内，进行获取

			//
			telnet.connect(ip, Integer.parseInt(port));
			in = telnet.getInputStream();
			out = new PrintStream(telnet.getOutputStream());

			/*
			 * //登录 readUntil("login: "); write(user); //
			 * readUntil("Password: "); write(password); // readUntil(prompt +
			 * " ");
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * RecordSrv录制服务器；注没有用户名及密码
	 * 
	 * @param _ip
	 * @param _port
	 */
	public NetTelnet(String _ip, String _port) {

		try {
			// AIX主机IP
			/*
			 * this.ip = "10.1.2.222"; this.password = "loeisdke"; this.user =
			 * "whdiwpasdq232sd2323"; this.port = "23";
			 */
			this.ip = _ip;
			this.port = _port;
			this.user = "";
			this.password = "";

			//
			telnet.connect(ip, Integer.parseInt(port));
			in = telnet.getInputStream();
			out = new PrintStream(telnet.getOutputStream());

			/*
			 * //登录 readUntil("login: "); write(user); //
			 * readUntil("Password: "); write(password); // readUntil(prompt +
			 * " ");
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public NetTelnet(String _ip, String _port, String _user, String _pwd) {

		try {
			// AIX主机IP
			/*
			 * this.ip = "10.1.2.222"; this.password = "loeisdke"; this.user =
			 * "whdiwpasdq232sd2323"; this.port = "23";
			 */
			this.ip = _ip;
			this.port = _port;
			this.user = _user;
			this.password = _pwd;

			//
			telnet.connect(ip, Integer.parseInt(port));
			in = telnet.getInputStream();
			out = new PrintStream(telnet.getOutputStream());

			/*
			 * //登录 readUntil("login: "); write(user); //
			 * readUntil("Password: "); write(password); // readUntil(prompt +
			 * " ");
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取分析结果
	 * 
	 * @param pattern
	 * @return
	 */
	public String readUntil(String pattern) {
		try {
			char lastChar = pattern.charAt(pattern.length() - 1);
			StringBuffer sb = new StringBuffer();
			char ch = (char) in.read();
			int count = 0;
			while (true) {
				// if (ch!=' ')
				sb.append(ch);
				count++; // ch的个数
				if (ch == lastChar) {
					if (sb.toString().endsWith(pattern)) {
						return sb.toString();
					}
				}
				//
				if (count > MAX_BUFFER_CHAR_COUNT) { // 缓存内，读取的字符串超过MAX_BUFFER_CHAR_COUNT时；就退出
					return sb.toString();
				}

				ch = (char) in.read();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 写
	 * 
	 * @param value
	 */
	public void write(String value) {
		try {
			out.println(value); // 不带回车换行；
			// out.print(value);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 向目标发送命令字符串
	 * 
	 * @param command
	 * @return
	 */
	public String sendCommand(String command) {
		try {
			write(command);
			// return readUntil(prompt + " ");
			return readUntil(prompt + "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 关闭连接
	 * 
	 */
	public void disconnect() {
		try {
			telnet.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 通讯是否正常建立
	 * 
	 */
	public boolean isAvailable() {
		try {
			return (telnet.isAvailable() && telnet.isConnected());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void main(String[] args) {
		try {
			String result = "";
			NetTelnet telnet = new NetTelnet();
			if (telnet.isAvailable()) {
				result = telnet
						.sendCommand("S000000000000000000000001366183452925E#");
			} else {
				System.out.println("————————服务器通讯不正常！——————");
			}

			System.out.println(result);
			// 最后一定要关闭
			telnet.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}