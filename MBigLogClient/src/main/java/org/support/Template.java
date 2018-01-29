package org.support;

/**
 * @author wangzhanwei
 */
public class Template {
	public static String configCommon = "closeMode:关闭模式 0 最小化托盘 1 关闭BigLog\n" +
			"port:服务器端口号\n" +
			"nomoreNotice：不再提示 0否 1是 \n" +
			"endFlag：2017-11-24 15:13:27,322 (ConnPort.java:30) [INFO][CORE_CONN] conn0当前连接数=0。 （[INFO][CORE_CONN] conn0当前连接数=0）可以区分一条相同的log 前面的 ) 即为endFlag\n" +
			"ip:服务器的ip地址";

	public static String[] templates = {"出现INFO了！\n{}\n快去看看吧！",
			"出现ERROR了！\n{}\n快去看看吧！",
			"出现WARN了！\n{}\n快去看看吧！",
			"出现EXCEPTION了！\n{}\n快去看看吧！",
			"出现DEBUG了！\n{}\n快去看看吧！",
			"出现TRACE了！\n{}\n快去看看吧！"};

	public static enum EnumTemplate {
		INFO(0),
		ERROR(1),
		WARN(2),
		EXCEPTION(3),
		DEBUG(4),
		TRACE(5),;

		private int index;

		EnumTemplate(int i) {
			this.index = i;
		}

		public int getIndex() {
			return index;
		}

		/**
		 * 通过属性索引，得到属性枚举
		 *
		 * @param index
		 * @return
		 */
		public static EnumTemplate getByIndex(int index) {
			EnumTemplate key = null;
			for (EnumTemplate k : EnumTemplate.values()) {
				if (k.index == index) {
					key = k;
					break;
				}
			}
			return key;
		}
	}

}
