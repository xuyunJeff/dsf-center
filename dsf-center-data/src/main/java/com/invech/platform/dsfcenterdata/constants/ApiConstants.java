package com.invech.platform.dsfcenterdata.constants;

public class ApiConstants {

	public static final String UNAVAILABLE_GAME_NAME ="未知游戏名";

	public static final String BET_FIRST ="10000000000000000";

	public static final String BET_API_NAME = "Bet";

	public static final String SITE_SECURETY_KEY ="Site";

	public static final String PLAYER_LOGGIN_KEY ="MemberUser";

	public static final String MASTER_SCHEMA_SUFFIX = "dsf_center_master";
	// spring 缓存KEY定义开始

	public static final String GAME_REQUEST_LIMIT ="Limit";

	public static final String REDIS_USER_CACHE_KEY = "usercache";// 根据用户ID缓存用户
	public static final String REDIS_GAME_SITECODE_CACHE_KEY = "gameSiteCodeCache";// 根据站点代码缓存站点信息

	public static final String SITE_CODE ="sys:siteCode:";
    public static final String GAME_PLATFORM_API ="GamePlatform:Api";

	public interface SiteAvilable {
		Byte AVILABLE = Byte.valueOf("1");
		Byte NOT_AVILABLE = Byte.valueOf("0");
	}

	// 邮箱常量
	public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
	public static final String MAIL_SMTP_TIMEOUT = "mail.smtp.timeout";
	public static final String MAIL_SMTP_SSL_ENABLE = "mail.smtp.ssl.enable";
	public static final String MAIL_SMTP_SSL_SOCKETFACTORY = "mail.smtp.ssl_socketFactory";
	public static final String MAIL_DEBUG = "mail.debug";



	public  static  final  Byte ENABLE = 1;//启动
	public  static  final  Byte DISABLE = 0;//停止

	public static  final  Byte  BW = 0;//包网
	public static  final  Byte  API= 1;//API

	public  static  final  String EFFCTIVE = "1";//有效
	public  static  final  String NOT_EFFCTIVE = "0";//无效

	public  static final  String REDIS_GAME_LIST_KEY = "GAME_LIST";//游戏列表写入redis
	public  static final  String REDIS_PLAT_LIST_KEY = "PLATFORM_LIST";//平台列表写入redis
 }
