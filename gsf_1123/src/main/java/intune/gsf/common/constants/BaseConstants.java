package intune.gsf.common.constants;

import java.math.BigDecimal;

public class BaseConstants {
	
	public static final String		SYSTEM_USER_AGENT						= "User-Agent";
	public static final String[]	SYSTEM_MOBILE_OS						= new String[] { "iPhone", "iPod", "iPad", "Android",
			"BlackBerry", "Windows CE", "Nokia", "Webos", "Opera Mini", "SonyEricsson", "Opera Mobi", "IEMobile",
			"zerotosevenapp" };

	public static final String[]	SYSTEM_MOBILE_APP						= new String[] { "zerotosevenapp" };

	//================================ system
	public static final String		STORE_ID								= "1001";										//비회원 STORE ID
	public static final String		DEFAULT_LANG_CD							= "KO";

	public static final String	YN_Y							= "Y";
	public static final String	YN_N							= "N";

	public static final String	CRUD_TYPE_CREATE				= "C";
	public static final String	CRUD_TYPE_READ					= "R";
	public static final String	CRUD_TYPE_UPDATE				= "U";
	public static final String	CRUD_TYPE_DELETE				= "D";
	
	public static final String	SERVER_LOCAL					= "LOCAL";					//로컬
	public static final String	SERVER_DEV						= "DEV";					//개발
	public static final String	SERVER_STG						= "STG";					//STAGE
	public static final String	SERVER_PRD						= "PRD";					//운영

	public static final String	SYSTEM_BO						= "BO";						//backoffice
	public static final String	SYSTEM_FO						= "FO";						//front
	public static final String	SYSTEM_PO						= "PO";						//partner
	public static final String		SYSTEM_BATCH							= "BATCH";
	public static final String		SYSTEM_CAMPAIGN							= "CAMPAIGN";
	public static final String	DEFAULT_BATCH_USER_ID			= "BATCH";
	public static final String	DEFAULT_ADMIN_USER_ID			= "ADMIN";
	public static final String		MEMBERSHIP_ID							= "RX";
	
	public static final String	LOCALE_PARAM					= "locale";

	public static final String	BASE_ENTITY_NAME				= "BaseEntity";

	public static final String	PARAM_INSERT_ID					= "insId";
	public static final String	PARAM_UPDATE_ID					= "updId";
	public static final String	PARAM_STORE_ID					= "storeId";
	public static final String	PARAM_LANG_CD					= "langCd";
	public static final String		PARAM_SYSTEM							= "system";
	public static final String		PARAM_MEMBER_NO							= "memberNo";
	public static final String		PARAM_USER_ID							= "userId";
	public static final String		PARAM_USERID							= "userid";
	public static final String		PARAM_CART_ID							= "cartId";
	public static final String		PARAM_BUSINESS_ID						= "businessId";
	public static final String		NON_MEMBER_ID							= "guest";
	public static final String	DB_UPDATE_DT					= "UPD_DT";
	public static final String	DB_INSERT_DT					= "INS_DT";
	public static final String	DB_INSERT_ID					= "INS_ID";
	public static final String	DB_STORE_ID						= "STORE_ID";

	public static final String	VIEW_TYPE_COMMON				= "COMMON";
	public static final String	VIEW_TYPE_POPUP					= "POPUP";
	public static final String	VIEW_TYPE_POPUP_CCS				= "POPUP_CCS";

	public static final String	RESULT_MESSAGE					= "MESSAGE";
	public static final String	RESULT_FLAG						= "RESULT";
	public static final String	RESULT_FLAG_SUCCESS				= "SUCCESS";
	public static final String	RESULT_FLAG_FAIL				= "FAIL";

	// insertOneTable/updateOneTable 용 상수
	public static final BigDecimal	BIGDECIMAL_NULL						= BigDecimal.valueOf(Long.MAX_VALUE);
	public static final String		SYSDATE								= "_SYSDATE_";

	public static final String		COOKIE_NON_MEMBER_INFO					= "zts_non_member_info";

	//================================ session
	public static final String SESSION_KEY_LOGIN_INFO = "loginInfo";
	public static final String SESSION_KEY_RETURN_URL = "returnUrl";
	public static final String SESSION_KEY_LANG_CD_INFO = "langCdInfo";
	public static final String SESSION_KEY_SYSTEM_LANG_CD_INFO = "sysLangCdInfo";
	public static final String SESSION_KEY_CART_NO_MEMBER = "cartNoMember";
	public static final String SESSION_KEY_HTTPSESSION_ID = "httpSessionId";
	public static final String SESSION_KEY_STORE_ID = "storeId";
	public static final String SESSION_KEY_BEFORE_LANG_CD_INFO = "beforeLangCdInfo";
	public static final String	SESSION_KEY_SYSTEM_TYPE			= "_system_type";				//system type (BO,FO,PO)
	public static final String		SESSION_KEY_BLACK_LIST_URL			= "_blackListUrl_";
	
	public static final int		SESSION_TIMEOUT_ERROR			= 901;
	public static final String	SESSION_TIMEOUT_ERROR_INFO		= "SESSION TIMEOUT";
	// 채널정보
	public static final String		SESSION_KEY_CHANNEL						= "CHANNEL_ID";

	public static final String		SESSION_KEY_MOBILE_ORDER				= "MOBILE_ORDER";
	public static final String		SESSION_KEY_CHECK_ORDER					= "SESSION_CHECK_ORDER";

	//================================= ccs
	public static final String		DEVICE_TYPE_CD_PC					= "DEVICE_TYPE_CD.PC";
	public static final String		DEVICE_TYPE_CD_MW					= "DEVICE_TYPE_CD.MW";
	public static final String		DEVICE_TYPE_CD_APP					= "DEVICE_TYPE_CD.APP";

	public static final String	TARGET_TYPE_CD_ALL				= "TARGET_TYPE_CD.ALL";
	public static final String	TARGET_TYPE_CD_PRODUCT			= "TARGET_TYPE_CD.PRODUCT";
	public static final String	TARGET_TYPE_CD_BRAND			= "TARGET_TYPE_CD.BRAND";
	public static final String	TARGET_TYPE_CD_CATEGORY			= "TARGET_TYPE_CD.CATEGORY";

	public static final String	POLICY_ID_CART_END_DT			= "CART_END_DT";
	public static final String		POLICY_ID_VIRTUAL_END_DT				= "VIRTUAL_END_DT";
	public static final String		POLICY_ID_ANDROID_APP_VER				= "ANDROID_APP_VER";
	public static final String		POLICY_ID_IOS_APP_VER					= "IOS_APP_VER";
	public static final String		POLICY_ID_IOS_FORCE_YN					= "IOS_FORCE_YN";
	public static final String		POLICY_ID_ANDROID_FORCE_YN				= "ANDROID_FORCE_YN";

	public static final String	POLICY_TYPE_CD_COMMON			= "POLICY_TYPE_CD.COMMON";
	public static final String	POLICY_TYPE_CD_MEMBER			= "POLICY_TYPE_CD.MEMBER";
	public static final String	POLICY_TYPE_CD_DISPLAY			= "POLICY_TYPE_CD.DISPLAY";
	public static final String	POLICY_TYPE_CD_PRODUCT			= "POLICY_TYPE_CD.PRODUCT";
	public static final String	POLICY_TYPE_CD_PROMOTION		= "POLICY_TYPE_CD.PROMOTION";
	public static final String	POLICY_TYPE_CD_ORDER			= "POLICY_TYPE_CD.ORDER";
	
	public static final String  USER_TYPE_CD_CS					= "USER_TYPE_CD.CS";
	public static final String  USER_TYPE_CD_MD					= "USER_TYPE_CD.MD";
	public static final String	USER_TYPE_CD_BUSINESS				= "USER_TYPE_CD.BUSINESS";
	public static final String	USER_TYPE_CD_OFFSHOP				= "USER_TYPE_CD.OFFSHOP";

	public static final String	USER_STATE_CD_READY					= "USER_STATE_CD.READY";
	public static final String	USER_STATE_CD_RUN					= "USER_STATE_CD.RUN";
	public static final String	USER_STATE_CD_STOP					= "USER_STATE_CD.STOP";
	public static final String	USER_STATE_CD_DEL					= "USER_STATE_CD.DEL";
	public static final String		USER_STATE_CD_USE						= "USER_STATE_CD.USE";
	public static final String		USER_STATE_CD_UNUSE						= "USER_STATE_CD.UNUSE";
	
	public static final String INQUIRY_CHANNEL_CD_ONLINE		= "INQUIRY_CHANNEL_CD.ONLINE";
	public static final String INQUIRY_CHANNEL_CD_CALL			= "INQUIRY_CHANNEL_CD.CALL";
	
	public static final String 	INQUIRY_STATE_CD_COMPLETE			= "INQUIRY_STATE_CD.COMPLETE";
	
	public static final String	PRODUCT_QNA_STATE_CD_COMPLETE 		= "PRODUCT_QNA_STATE_CD.COMPLETE";

	public static final String		OFFSHOP_STATE_CD_READY					= "OFFSHOP_STATE_CD.READY";
	public static final String		OFFSHOP_STATE_CD_RUN					= "OFFSHOP_STATE_CD.RUN";
	public static final String		OFFSHOP_STATE_CD_STOP					= "OFFSHOP_STATE_CD.STOP";
	
	public static final String		SYSTEM_TYPE_CD_BO					= "SYSTEM_TYPE_CD.BO";
	public static final String		SYSTEM_TYPE_CD_PO					= "SYSTEM_TYPE_CD.PO";
	
	public static final String		NOTICE_TYPE_CD_FRONT				= "NOTICE_TYPE_CD.FRONT";
	public static final String		NOTICE_TYPE_CD_EVENT				= "NOTICE_TYPE_CD.EVENT";
	public static final String		NOTICE_TYPE_CD_BO					= "NOTICE_TYPE_CD.ADMIN";
	public static final String		NOTICE_TYPE_CD_PO					= "NOTICE_TYPE_CD.PARTNER";
	public static final String		NOTICE_TYPE_CD_PRODUCT				= "NOTICE_TYPE_CD.PRODUCT";

	public static final String		POPUP_TYPE_CD_FRONT						= "POPUP_TYPE_CD.FRONT";
	public static final String		POPUP_TYPE_CD_PARTNER					= "POPUP_TYPE_CD.PARTNER";

	//================================= pms
	public static final String		SALE_STATE_CD_REQ						= "SALE_STATE_CD.REQ";
	public static final String		SALE_STATE_CD_APPROVAL1					= "SALE_STATE_CD.APPROVAL1";
	public static final String		SALE_STATE_CD_APPROVAL2					= "SALE_STATE_CD.APPROVAL2";
	public static final String		SALE_STATE_CD_REJECT1					= "SALE_STATE_CD.REJECT";
	public static final String		SALE_STATE_CD_SALE						= "SALE_STATE_CD.SALE";
	public static final String		SALE_STATE_CD_SOLDOUT					= "SALE_STATE_CD.SOLDOUT";
	public static final String		SALE_STATE_CD_STOP						= "SALE_STATE_CD.STOP";
	public static final String		SALE_STATE_CD_MDSTOP					= "SALE_STATE_CD.MDSTOP";
	public static final String		SALE_STATE_CD_END						= "SALE_STATE_CD.END";

	public static final String	PRICE_RESERVE_STATE_CD_REQ		= "PRICE_RESERVE_STATE_CD.REQ";
	public static final String	PRICE_RESERVE_STATE_CD_APPROVAL	= "PRICE_RESERVE_STATE_CD.APPROVAL";
	public static final String	PRICE_RESERVE_STATE_CD_REJECT	= "PRICE_RESERVE_STATE_CD.REJECT";

	public static final String	PRODUCT_TYPE_CD_GENERAL			= "PRODUCT_TYPE_CD.GENERAL";
	public static final String	PRODUCT_TYPE_CD_SET				= "PRODUCT_TYPE_CD.SET";
	public static final String	PRODUCT_TYPE_CD_PRESENT			= "PRODUCT_TYPE_CD.PRESENT";

	public static final String	PRICE_RESERVE_STATE_CD_COMPLETE		= "PRICE_RESERVE_STATE_CD.COMPLETE";

	public static final String	PRODUCT_RESERVE_STATE_CD_COMPLETE	= "PRODUCT_RESERVE_STATE_CD.COMPLETE";

	public static final String		TAX_TYPE_CD_TAX							= "TAX_TYPE_CD.TAX";
	public static final String		TAX_TYPE_CD_FREE						= "TAX_TYPE_CD.FREE";

	public static final String		WAREHOUSE_ID						= "1";

	//================================= oms
	public static final String		SITE_ID_0TO7							= "1";											//자사
	public static final String	CART_STATE_CD_REG				= "CART_STATE_CD.REG";
	public static final String	CART_STATE_CD_DEL				= "CART_STATE_CD.DEL";

	public static final String	CART_TYPE_CD_GENERAL			= "CART_TYPE_CD.GENERAL";
	public static final String	CART_TYPE_CD_PICKUP				= "CART_TYPE_CD.PICKUP";
	public static final String	CART_TYPE_CD_REGULARDELIVERY	= "CART_TYPE_CD.REGULARDELIVERY";

	public static final String	CART_PRODUCT_TYPE_CD_GENERAL	= "CART_PRODUCT_TYPE_CD.GENERAL";
	public static final String	CART_PRODUCT_TYPE_CD_SET		= "CART_PRODUCT_TYPE_CD.SET";
	public static final String	CART_PRODUCT_TYPE_CD_SUB		= "CART_PRODUCT_TYPE_CD.SUB";

	public static final String		ORDER_TYPE_CD_GENERAL				= "ORDER_TYPE_CD.GENERAL";
	public static final String		ORDER_TYPE_CD_RESERVE				= "ORDER_TYPE_CD.RESERVE";
	public static final String		ORDER_TYPE_CD_GIFT					= "ORDER_TYPE_CD.GIFT";
	public static final String		ORDER_TYPE_CD_B2E					= "ORDER_TYPE_CD.B2E";
	public static final String		ORDER_TYPE_CD_EXTERNAL				= "ORDER_TYPE_CD.EXTERNAL";
	public static final String		ORDER_TYPE_CD_PROMOTION				= "ORDER_TYPE_CD.PROMOTION";
	public static final String		ORDER_TYPE_CD_REGULARDELIVERY		= "ORDER_TYPE_CD.REGULARDELIVERY";

	public static final String		ORDER_STATE_CD_REQ						= "ORDER_STATE_CD.REQ";							//주문접수
	public static final String		ORDER_STATE_CD_PAYED					= "ORDER_STATE_CD.PAYED";						//결제완료
	public static final String		ORDER_STATE_CD_COMPLETE					= "ORDER_STATE_CD.COMPLETE";									//주문완료
	public static final String		ORDER_STATE_CD_CONFIRMED				= "ORDER_STATE_CD.CONFIRMED";					//구매확정
	public static final String		ORDER_STATE_CD_CANCEL					= "ORDER_STATE_CD.CANCEL";						//주문취소

	public static final String		ORDER_DELIVERY_STATE_CD_READY			= "ORDER_DELIVERY_STATE_CD.READY";				//출고지시대기
	public static final String		ORDER_DELIVERY_STATE_CD_DELIVERY_ORDER	= "ORDER_DELIVERY_STATE_CD.DELIVERY_ORDER";		//출고지시
	public static final String		ORDER_DELIVERY_STATE_CD_SHIP			= "ORDER_DELIVERY_STATE_CD.SHIP";				//출고완료
	public static final String		ORDER_DELIVERY_STATE_CD_DELIVERY		= "ORDER_DELIVERY_STATE_CD.DELIVERY";			//배송오나료

	public static final String	    ORDER_PRODUCT_TYPE_CD_GENERAL		= "ORDER_PRODUCT_TYPE_CD.GENERAL";
	public static final String	    ORDER_PRODUCT_TYPE_CD_SET			= "ORDER_PRODUCT_TYPE_CD.SET";
	public static final String	    ORDER_PRODUCT_TYPE_CD_SUB			= "ORDER_PRODUCT_TYPE_CD.SUB";
	public static final String		ORDER_PRODUCT_TYPE_CD_ORDERPRESENT		= "ORDER_PRODUCT_TYPE_CD.ORDERPRESENT";
	public static final String		ORDER_PRODUCT_TYPE_CD_PRODUCTPRESENT	= "ORDER_PRODUCT_TYPE_CD.PRODUCTPRESENT";

	public static final String		PAYMENT_STATE_CD_PAYMENT_READY		= "PAYMENT_STATE_CD.PAYMENT_READY";							//입금대기
	public static final String		PAYMENT_STATE_CD_CANCEL				= "PAYMENT_STATE_CD.CANCEL";										//입금대기취소
	public static final String		PAYMENT_STATE_CD_PAYMENT			= "PAYMENT_STATE_CD.PAYMENT";										//결제완료
	public static final String		PAYMENT_STATE_CD_CANCEL_PAYMENT		= "PAYMENT_STATE_CD.CANCEL_PAYMENT";								//취소결제
	public static final String		PAYMENT_STATE_CD_REFUND_READY		= "PAYMENT_STATE_CD.REFUND_READY";									//환불접수
	public static final String		PAYMENT_STATE_CD_REFUND				= "PAYMENT_STATE_CD.REFUND";										//환불완료
	
	public static final String		WAREHOUSE_INOUT_TYPE_CD_IN		    = "WAREHOUSE_INOUT_TYPE_CD.IN";							    //입고
	public static final String		WAREHOUSE_INOUT_TYPE_CD_OUT		    = "WAREHOUSE_INOUT_TYPE_CD.OUT";							//출고

	public static final String		DELIVERY_IF_TYPE_CD_DAS		        = "DELIVERY_IF_TYPE_CD.DAS";							    //다스연동
	public static final String		DELIVERY_IF_TYPE_CD_HANJIN		    = "DELIVERY_IF_TYPE_CD.HANJIN";						        //한진연동
	
	public static final String		LOGISTICS_STATE_CD_APPROVAL		    = "LOGISTICS_STATE_CD.APPROVAL";							//배송승인
	public static final String		LOGISTICS_STATE_CD_CANCELAPPROVAL	= "LOGISTICS_STATE_CD.CANCELAPPROVAL";						//배송승인취소
	public static final String		LOGISTICS_STATE_CD_INVOICE		    = "LOGISTICS_STATE_CD.INVOICE";							    //운송장생성
	public static final String		LOGISTICS_STATE_CD_SHIP		        = "LOGISTICS_STATE_CD.SHIP";							    //출고완료
	public static final String		LOGISTICS_STATE_CD_RETURN_ORDER		= "LOGISTICS_STATE_CD.RETURN_ORDER";						//입고지시
	public static final String		LOGISTICS_STATE_CD_RETURN		    = "LOGISTICS_STATE_CD.RETURN";							    //입고완료
	public static final String		LOGISTICS_STATE_CD_CANCELRETURN		= "LOGISTICS_STATE_CD.CANCELRETURN";						//입고취소
	
	public static final String		ORDER_PRODUCT_STATE_CD_REQ		      = "ORDER_PRODUCT_STATE_CD.REQ";							//주문접수
	public static final String		ORDER_PRODUCT_STATE_CD_READY		  = "ORDER_PRODUCT_STATE_CD.READY";							//출고지시대기
	public static final String		ORDER_PRODUCT_STATE_CD_DELIVERY_ORDER = "ORDER_PRODUCT_STATE_CD.DELIVERY_ORDER";				//출고지시
	public static final String		ORDER_PRODUCT_STATE_CD_SHIP		      = "ORDER_PRODUCT_STATE_CD.SHIP";							//출고완료
	public static final String		ORDER_PRODUCT_STATE_CD_DELIVERY		  = "ORDER_PRODUCT_STATE_CD.DELIVERY";						//배송완료
	public static final String		ORDER_PRODUCT_STATE_CD_CANCEL		  = "ORDER_PRODUCT_STATE_CD.CANCEL";						//주문취소
	
	public static final String      OMS_LOGISTICS_STX                     = "S";
	public static final String      OMS_LOGISTICS_PICK_POST_NO            = "469883";
	public static final String      OMS_LOGISTICS_SELL_POST_NO            = "469883";
	public static final String      OMS_LOGISTICS_SELL_ADDRESS1           = "경기 여주군 가남면 본두리";
	public static final String      OMS_LOGISTICS_SELL_ADDRESS2           = "698-3 제로투세븐 물류센터";
	public static final String      OMS_LOGISTICS_SELL_NM                 = "제로투세븐(고객센터)";
	public static final String      OMS_LOGISTICS_SELL_PHONENO1           = "1588-8744";
	public static final String      OMS_LOGISTICS_BXCODE1                 = "1001509";
	public static final String      OMS_LOGISTICS_BXCODE2                 = "1000917";
	public static final String      OMS_LOGISTICS_PAYAMOUNT               = "2500";
	
	public static final String		ORDER_DELIVERY_TYPE_CD_ORDER			= "ORDER_DELIVERY_TYPE_CD.ORDER";						//주문
	public static final String		ORDER_DELIVERY_TYPE_CD_EXCHANGE			= "ORDER_DELIVERY_TYPE_CD.EXCHANGE";					//교환
	public static final String		ORDER_DELIVERY_TYPE_CD_REDELIVERY		= "ORDER_DELIVERY_TYPE_CD.REDELIVERY";					//재배송

	public static final String		DEPOSIT_TYPE_CD_MANAGER					= "DEPOSIT_TYPE_CD.MANAGER";

	//================================= sps
	public static final String	PRESENT_TYPE_CD_PRODUCT						= "PRESENT_TYPE_CD.PRODUCT";
	public static final String	PRESENT_TYPE_CD_ORDER						= "PRESENT_TYPE_CD.ORDER";

	public static final String	PRESENT_SELECT_TYPE_CD_ALL					= "PRESENT_SELECT_TYPE_CD.ALL";
	public static final String	PRESENT_SELECT_TYPE_CD_SELECT				= "PRESENT_SELECT_TYPE_CD.SELECT";

	public static final String		COUPON_TYPE_CD_PRODUCT					= "COUPON_TYPE_CD.PRODUCT";
	public static final String		COUPON_TYPE_CD_PLUS						= "COUPON_TYPE_CD.PLUS";
	public static final String		COUPON_TYPE_CD_ORDER					= "COUPON_TYPE_CD.ORDER";
	public static final String		COUPON_TYPE_CD_DELIVERY					= "COUPON_TYPE_CD.DELIVERY";
	public static final String		COUPON_TYPE_CD_WRAP						= "COUPON_TYPE_CD.WRAP";

	public static final String		POINT_SAVE_TYPE_CD_MULTI				= "POINT_SAVE_TYPE_CD.MULTIPLY";
	public static final String		POINT_SAVE_TYPE_CD_ADD					= "POINT_SAVE_TYPE_CD.ADD";

	public static final String		CARROT_TYPE_CD_CS						= "CARROT_TYPE_CD.CS";
	public static final String		CARROT_TYPE_CD_EVENT					= "CARROT_TYPE_CD.EVENT";
	public static final String		CARROT_TYPE_CD_EXPIRE					= "CARROT_TYPE_CD.EXPIRE";

	public static final String		COUPON_ISSUE_TYPE_CD_SYSTEM				= "COUPON_ISSUE_TYPE_CD.SYSTEM";
	public static final String		COUPON_ISSUE_TYPE_CD_PUBLIC				= "COUPON_ISSUE_TYPE_CD.PUBLIC";
	public static final String		COUPON_ISSUE_TYPE_CD_PRIVATE			= "COUPON_ISSUE_TYPE_CD.PRIVATE";
	
	public static final String		TERM_TYPE_CD_DAYS						= "TERM_TYPE_CD.DAYS";
	public static final String		TERM_TYPE_CD_TERM						= "TERM_TYPE_CD.TERM";
	public static final String		TERM_TYPE_CD_WEEK						= "TERM_TYPE_CD.WEEK";
	public static final String		TERM_TYPE_CD_LASTDAY					= "TERM_TYPE_CD.LASTDAY";
	
	public static final String		EVENT_TYPE_CD_COUPON					= "EVENT_TYPE_CD.COUPON";
	public static final String		EVENT_TYPE_CD_ATTEND					= "EVENT_TYPE_CD.ATTEND";
	public static final String		EVENT_TYPE_CD_JOIN						= "EVENT_TYPE_CD.JOIN";
	public static final String		EVENT_TYPE_CD_INFO						= "EVENT_TYPE_CD.INFO";
	public static final String		EVENT_TYPE_CD_EXP						= "EVENT_TYPE_CD.EXP";
	public static final String		EVENT_TYPE_CD_MANUAL					= "EVENT_TYPE_CD.MANUAL";
	public static final String		EVENT_TYPE_CD_REPLY						= "EVENT_TYPE_CD.REPLY";
	
	
	//================================= dms
	public static final String		EXHIBIT_TYPE_CD_ONEDAY					= "EXHIBIT_TYPE_CD.ONEDAY";
	public static final String		EXHIBIT_TYPE_CD_COUPON					= "EXHIBIT_TYPE_CD.COUPON";
	public static final String		EXHIBIT_TYPE_CD_OFFSHOP					= "EXHIBIT_TYPE_CD.OFFSHOP";
	
	public static final String		EXHIBIT_STATE_CD_READY					= "EXHIBIT_STATE_CD.READY";
	public static final String		EXHIBIT_STATE_CD_RUN					= "EXHIBIT_STATE_CD.RUN";
	public static final String		EXHIBIT_STATE_CD_STOP					= "EXHIBIT_STATE_CD.STOP";

	public static final String 		TEMPLATE_TYPE_CD_DISPLAYCATEGORY 		= "TEMPLATE_TYPE_CD.DISPLAYCATEGORY";
	public static final String 		TEMPLATE_TYPE_CD_BRAND 					= "TEMPLATE_TYPE_CD.BRAND";
	public static final String 		TEMPLATE_TYPE_CD_BRANDCATEGORY			= "TEMPLATE_TYPE_CD.BRANDCATEGORY";
	public static final String 		TEMPLATE_TYPE_CD_EXHIBIT				= "TEMPLATE_TYPE_CD.EXHIBIT";
	
	public static final String  	DISPLAY_ARRAY_ORDER_TYPE_SALE			= "SALE";
	public static final String  	DISPLAY_ARRAY_ORDER_TYPE_DISCOUNT		= "DISCOUNT";
	public static final String  	DISPLAY_ARRAY_ORDER_TYPE_UPPRICE		= "UPPRICE"; 
	public static final String  	DISPLAY_ARRAY_ORDER_TYPE_LOWPRICE		= "LOWPRICE";
	public static final String  	DISPLAY_ARRAY_ORDER_TYPE_NEWPRD			= "NEWPRD";
	public static final String  	DISPLAY_ARRAY_ORDER_TYPE_REVIEW			= "REVIEW";

	public static final String		MEMBER_TYPE_CD_GENERAL					= "MEMBER_TYPE_CD.GENERAL";
	public static final String		MEMBER_TYPE_CD_MEMBERSHIP				= "MEMBER_TYPE_CD.MEMBERSHIP";
	public static final String		MEMBER_TYPE_CD_PREMIUM					= "MEMBER_TYPE_CD.PREMIUM";
	public static final String		MEMBER_TYPE_CD_EMPLOYEE					= "MEMBER_TYPE_CD.EMPLOYEE";
	public static final String		MEMBER_TYPE_CD_CHILDREN					= "MEMBER_TYPE_CD.CHILDREN";
	public static final String		MEMBER_TYPE_CD_B2E						= "MEMBER_TYPE_CD.B2E";
	
	public static final String 		DISPLAY_TYPE_CD_COMMON					= "DISPLAY_TYPE_CD.COMMON";
	public static final String 		DISPLAY_TYPE_CD_DISPLAYCATEGORY			= "DISPLAY_TYPE_CD.DISPLAYCATEGORY";
	public static final String 		DISPLAY_TYPE_CD_BRAND					= "DISPLAY_TYPE_CD.BRAND";
	public static final String 		DISPLAY_TYPE_CD_BRANDCATEGORY			= "DISPLAY_TYPE_CD.BRANDCATEGORY";
	public static final String 		DISPLAY_TYPE_CD_EXHIBIT					= "DISPLAY_TYPE_CD.EXHIBIT";
	
	public static final String		QUICKMENU_TYPE_CD_MENU					= "QUICKMENU_TYPE_CD.MENU";
	public static final String		QUICKMENU_TYPE_CD_DEAL					= "QUICKMENU_TYPE_CD.DEAL";
	
	public static final String		DISPLAY_ITEM_TYPE_CD_PRODUCT			= "DISPLAY_ITEM_TYPE_CD.PRODUCT";
	public static final String		DISPLAY_ITEM_TYPE_CD_EXHIBIT			= "DISPLAY_ITEM_TYPE_CD.EXHIBIT";
	public static final String		DISPLAY_ITEM_TYPE_CD_IMG				= "DISPLAY_ITEM_TYPE_CD.IMG";
	public static final String		DISPLAY_ITEM_TYPE_CD_TEXT				= "DISPLAY_ITEM_TYPE_CD.TEXT";
	public static final String		DISPLAY_ITEM_TYPE_CD_HTML				= "DISPLAY_ITEM_TYPE_CD.HTML";

	public static final String		SEARCH_API_TYPE_AUTO					="A";
	public static final String		SEARCH_API_TYPE_POPULAR					="P"; 
	public static final String		SEARCH_API_TYPE_RELATION				="R";

	public static final String		SEARCH_API_VIEW_TYPE_CATEGORY			="CATEGORY";
	public static final String		SEARCH_API_VIEW_TYPE_KEYWORD			="KEYWORD";
	public static final String		SEARCH_API_VIEW_TYPE_OPTIONBRAND		="OPTIONBRAND";
	public static final String		SEARCH_API_VIEW_TYPE_BUSINESS			="BUSINESS";
	public static final String		SEARCH_API_VIEW_TYPE_SPECIAL			="SPECIAL";
	public static final String		SEARCH_API_VIEW_TYPE_BRAND				="BRAND";
	public static final String		SEARCH_API_VIEW_TYPE_AGE				="AGE";
	public static final String		SEARCH_API_VIEW_TYPE_BRANDSHOP			="BRANDSHOP";
	
	
	public static final String		SPCEIAL_VIEW_TYPE_BIRTHREADY			="BIRTH";
	public static final String		SPCEIAL_VIEW_TYPE_MILKPOWDER			="MILK";
	public static final String		SPCEIAL_VIEW_TYPE_BRAND					="BRAND";
	
	public static final String		DISPLAY_SHOP_TYPE_BASE					="BASE";
	public static final String		DISPLAY_SHOP_TYPE_SPECIAL				="SPECIAL";
	
	public static final String		DEAL_TYPE_CD_MEMBER						= "DEAL_TYPE_CD.MEMBER";
	public static final String		DEAL_TYPE_CD_PREMIUM					= "DEAL_TYPE_CD.PREMIUM";
	public static final String		DEAL_TYPE_CD_EMPLOYEE					= "DEAL_TYPE_CD.EMPLOYEE";
	public static final String		DEAL_TYPE_CD_CHILDREN					= "DEAL_TYPE_CD.CHILDREN";
	
	public static final String		BRAND_PRODUCT_TYPE_BRAND				="BRAND";
	public static final String		BRAND_PRODUCT_TYPE_CATEGORY				="CATEGORY";
	
	//================================= FO mms
	public static final String		PAGE_TYPE_PRODUCT						= "PRODUCT";
	public static final String		PAGE_TYPE_MYQA							= "MYQA";

	public static final String		MEMBER_GRADE_PRESTAGE					= "prestage";
	public static final String		MEMBER_GRADE_VIP						= "vip";
	public static final String		MEMBER_GRADE_GOLD						= "gold";
	public static final String		MEMBER_GRADE_SILVER						= "silver";
	public static final String		MEMBER_GRADE_FAMILY						= "family";
	public static final String		MEMBER_GRADE_WELCOME					= "welcome";

	public static final String		MEM_GRADE_CD_PRESTAGE					= "MEM_GRADE_CD.PRESTIGE";
	public static final String		MEM_GRADE_CD_VIP						= "MEM_GRADE_CD.VIP";
	public static final String		MEM_GRADE_CD_GOLD						= "MEM_GRADE_CD.GOLD";
	public static final String		MEM_GRADE_CD_SILVER						= "MEM_GRADE_CD.SILVER";
	public static final String		MEM_GRADE_CD_FAMILY						= "MEM_GRADE_CD.FAMILY";
	public static final String		MEM_GRADE_CD_WELCOME					= "MEM_GRADE_CD.WELCOME";
	
	public static final String		DEPOSIT_REFUND_REQ_RESULT_SUCCESS			= "0000";
	public static final String		DEPOSIT_REFUND_REQ_RESULT_LACK				= "0001";
	public static final String		DEPOSIT_REFUND_REQ_RESULT_WRONG_ACCOUNT		= "0002";
	
	public static final String		REFUND_REASON_CD_REFUNDDEPOSIT		= "REFUND_REASON_CD.REFUNDDEPOSIT";
	public static final String		PAYMENT_STATE_CD_REFUND_CANCEL		= "PAYMENT_STATE_CD.REFUND_CANCEL";
	
	public static final String		BRAND_TEMPLATE_SECTION_STORY			= "STORY";
	public static final String		BRAND_TEMPLATE_SECTION_CATALOGUE			= "CATALOGUE";
	public static final String		BRAND_TEMPLATE_SECTION_COORDI_BOOK		= "COORDIBOOK";
	public static final String		BRAND_TEMPLATE_SECTION_STYLE			= "STYLE";
	public static final String		BRAND_TEMPLATE_SECTION_EVENT			= "EVENT";
	public static final String		BRAND_TEMPLATE_SECTION_PRODUCT			= "PRODUCT";

	public static final String		COUPON_ISSUE_RESULT_SUCCESS				= "0000";
	public static final String		COUPON_ISSUE_RESULT_MAX					= "0001";
	public static final String		COUPON_ISSUE_RESULT_PERSON_MAX			= "0002";
	public static final String		COUPON_ISSUE_RESULT_INVALID				= "0003";
	public static final String		COUPON_ISSUE_RESULT_UNKNOWN				= "1111";
	
	public static final BigDecimal WRAPPING_AMT = BigDecimal.TEN.multiply(BigDecimal.TEN).multiply(BigDecimal.TEN);

	public static final String		MSG_CODE_SMS							= "100";
	public static final String		MSG_CODE_EMAIL							= "001";

	//신한 고운맘/키즈 카드 고정번호
	public static final String		AFFILIATE_SHINHAN_CARDNO				= "1111111";

}
