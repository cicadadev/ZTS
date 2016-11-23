package gcp.admin.controller.view;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import gcp.ccs.model.CcsBusiness;
import gcp.ccs.model.CcsBusinessinquiry;
import gcp.ccs.model.CcsChannel;
import gcp.ccs.model.CcsCode;
import gcp.ccs.model.CcsCodegroup;
import gcp.ccs.model.CcsFaq;
import gcp.ccs.model.CcsInquiry;
import gcp.ccs.model.CcsMenu;
import gcp.ccs.model.CcsNotice;
import gcp.ccs.model.CcsPopup;
import gcp.ccs.model.CcsUser;
import gcp.ccs.model.search.CcsBusinessSearch;
import gcp.ccs.model.search.CcsBusinessinquirySearch;
import gcp.ccs.model.search.CcsChannelSearch;
import gcp.ccs.model.search.CcsCodeSearch;
import gcp.ccs.model.search.CcsFaqSearch;
import gcp.ccs.model.search.CcsInquirySearch;
import gcp.ccs.model.search.CcsMenuSearch;
import gcp.ccs.model.search.CcsNoticeSearch;
import gcp.ccs.model.search.CcsPopupNoticeSearch;
import gcp.ccs.model.search.CcsUserSearch;
import gcp.ccs.service.BusinessService;
import gcp.ccs.service.BusinessinquiryService;
import gcp.ccs.service.CodeService;
import gcp.ccs.service.CommonService;
import gcp.ccs.service.FaqService;
import gcp.ccs.service.MainService;
import gcp.ccs.service.MenuService;
import gcp.ccs.service.NoticeService;
import gcp.ccs.service.PopupNoticeService;
import gcp.ccs.service.QnaService;
import gcp.ccs.service.UserService;
import gcp.common.util.BoSessionUtil;
import gcp.common.util.CcsUtil;
import gcp.dms.model.DmsCatalog;
import gcp.dms.model.DmsExhibit;
import gcp.dms.model.DmsExhibitproduct;
import gcp.dms.model.search.DmsCatalogSearch;
import gcp.dms.model.search.DmsExhibitSearch;
import gcp.dms.service.CatalogService;
import gcp.dms.service.ExhibitService;
import gcp.external.model.OmsReceiveordermapping;
import gcp.external.model.search.PmsSendgoodsSearch;
import gcp.external.service.SendgoodsService;
import gcp.mms.model.MmsCarrot;
import gcp.mms.model.MmsChildrencard;
import gcp.mms.model.MmsDeposit;
import gcp.mms.model.MmsMemberZts;
import gcp.mms.model.MmsReadhistory;
import gcp.mms.model.search.MmsChildrenSearch;
import gcp.mms.model.search.MmsMemberPopupSearch;
import gcp.mms.model.search.MmsMemberSearch;
import gcp.mms.service.ChildrenService;
import gcp.mms.service.HistoryService;
import gcp.mms.service.MemberService;
import gcp.oms.model.OmsClaimproduct;
import gcp.oms.model.OmsLogistics;
import gcp.oms.model.OmsOrderproduct;
import gcp.oms.model.OmsPaymentsettle;
import gcp.oms.model.OmsSettle;
import gcp.oms.model.OmsSettleBiz;
import gcp.oms.model.search.OmsLogisticsSearch;
import gcp.oms.model.search.OmsPgSettleSearch;
import gcp.oms.model.search.OmsSettleSearch;
import gcp.oms.model.search.OmsTermorderSearch;
import gcp.oms.service.LogisticsService;
import gcp.oms.service.PgSettleService;
import gcp.oms.service.SettleBizService;
import gcp.oms.service.SettleService;
import gcp.oms.service.TermOrderService;
import gcp.pms.model.PmsAttribute;
import gcp.pms.model.PmsBrand;
import gcp.pms.model.PmsEpexcproduct;
import gcp.pms.model.PmsProduct;
import gcp.pms.model.PmsProductqna;
import gcp.pms.model.PmsReview;
import gcp.pms.model.PmsSaleproduct;
import gcp.pms.model.PmsStyleproduct;
import gcp.pms.model.PmsWarehouselocation;
import gcp.pms.model.search.BrandSearch;
import gcp.pms.model.search.PmsAttributeSearch;
import gcp.pms.model.search.PmsEpexcproductSearch;
import gcp.pms.model.search.PmsProductQnaSearch;
import gcp.pms.model.search.PmsProductReviewSearch;
import gcp.pms.model.search.PmsProductSearch;
import gcp.pms.model.search.PmsStyleProductSearch;
import gcp.pms.service.AttributeService;
import gcp.pms.service.BrandService;
import gcp.pms.service.EpexcproductService;
import gcp.pms.service.MdnoticeService;
import gcp.pms.service.ProductReviewService;
import gcp.pms.service.ProductService;
import gcp.pms.service.StyleProductService;
import gcp.sps.model.SpsCardpromotion;
import gcp.sps.model.SpsCoupon;
import gcp.sps.model.SpsCouponissue;
import gcp.sps.model.SpsEvent;
import gcp.sps.model.SpsEventcoupon;
import gcp.sps.model.SpsEventjoin;
import gcp.sps.model.SpsPointsave;
import gcp.sps.model.SpsPresent;
import gcp.sps.model.search.SpsCardPromotionSearch;
import gcp.sps.model.search.SpsCouponIssueSearch;
import gcp.sps.model.search.SpsCouponSearch;
import gcp.sps.model.search.SpsEventSearch;
import gcp.sps.model.search.SpsPointSearch;
import gcp.sps.model.search.SpsPresentSearch;
import gcp.sps.service.CouponService;
import gcp.sps.service.DiscountService;
import gcp.sps.service.EventService;
import gcp.sps.service.PointService;
import gcp.sps.service.PresentPromotionService;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.DateUtil;
import intune.gsf.common.utils.SessionUtil;

@Controller
@RequestMapping({ "/excel" })
public class ExcelViewController {

	protected final Log				logger	= LogFactory.getLog(getClass());

	@Autowired
	private ProductService			productService;

	@Autowired
	private MdnoticeService			mdnoticeService;

	@Autowired
	private CodeService				codeService;

	@Autowired
	private MenuService				menuService;

	@Autowired
	private QnaService				qnaService;

	@Autowired
	private UserService				userService;

	@Autowired
	private FaqService				faqService;

	@Autowired
	private NoticeService			noticeService;

	@Autowired
	private PopupNoticeService		popupService;

	@Autowired
	private CouponService			couponService;

	@Autowired
	private PresentPromotionService	presentPromotionService;

	@Autowired
	private PointService			pointService;

	@Autowired
	private MemberService			memberService;

	@Autowired
	private LogisticsService		logisticsService;
	
	@Autowired
	private ExhibitService			exhibitService;
	
	@Autowired
	private BusinessService			businessService;
	
	@Autowired
	private BusinessinquiryService	businessinquiryService;
	
	@Autowired
	private EventService			eventService;
	
	@Autowired
	private BrandService			brandService;
	
	@Autowired
	private DiscountService			discountService;
	
	@Autowired
	private CommonService		commonService;
	
	@Autowired
	private StyleProductService		styleProductService;
	
	@Autowired
	private CatalogService		catalogService;
	
	@Autowired
	private ProductReviewService	productReviewService;

	@Autowired
	private MainService				mainService;

	@Autowired
	private AttributeService		attrService;

	@Autowired
	private EpexcproductService		epexcService;

	@Autowired
	private SendgoodsService		sendgoodsService;

	@Autowired
	private TermOrderService		termOrderService;

	@Autowired
	private SettleService			settleService;

	@Autowired
	private SettleBizService		settleBizService;

	@Autowired
	private PgSettleService			pgSettleService;

	@Autowired
	private ChildrenService			childrenService;

	@Autowired
	private HistoryService			historyService;

	private ModelAndView returnView(String[] headers, List<String[]> dataList) {
		ModelAndView mv = new ModelAndView("excelView");
		mv.addObject("header", headers);
		mv.addObject("dataList", dataList);
		return mv;
	}
	
	private ModelAndView returnView(String[] dataType, String[] headers, List<String[]> dataList, String fileName) {
		ModelAndView mv = new ModelAndView("excelView");
		mv.addObject("dataType", dataType);
		mv.addObject("header", headers);
		mv.addObject("dataList", dataList);
		mv.addObject("fileName", fileName);
		return mv;
	}
	
	public String bigDecimalToString(BigDecimal param) {
		try {
			if (CommonUtil.isNotEmpty(param)) {
				return param.toString();
			} else {
				return "";
			}
		} catch (Exception e) {
			return "";
		}

	}

	/**
	 * 
	 * @Method Name : getProductListExcel
	 * @author : eddie
	 * @date : 2016. 7. 11.
	 * @description : 상품 목록 엑셀 다운로드
	 * @param pmsProductSearch
	 * @return
	 */
	@RequestMapping(value = "/pms/product", method = { RequestMethod.GET })
	public ModelAndView getProductListExcel(PmsProductSearch pmsProductSearch) {

		// 상품 목록 조회
		List<PmsProduct> list = new ArrayList<PmsProduct>();
		pmsProductSearch.setStoreId(SessionUtil.getStoreId());
		if (CommonUtil.isEmpty(pmsProductSearch.getBusinessId())) {

			pmsProductSearch.setBusinessId(BoSessionUtil.getBusinessId());
		}
		list = productService.getProductList(pmsProductSearch);

		List<String[]> dataList = new ArrayList<String[]>();

		String[] dataType = { "숫자", "텍스트", "텍스트", "텍스트", "텍스트", "텍스트", "숫자", "숫자", "숫자", "퍼센트", "텍스트", "텍스트", "텍스트", "날짜", "날짜",
				"텍스트", "날짜", "텍스트", "날짜" };

		// 헤더
		String[] headers = { "상품ID", "상품명", "표준카테고리", "상품유형코드", "상품판매코드", "브랜드명", "정상가", "판매가", "공급가", "수수료율", "ERP상품ID", "상품구분",
				"전시카테고리", "판매시작일시", "판매종료일시", "등록자", "등록일", "수정자", "수정일" };

		String fileName = "상품목록_" + DateUtil.getCurrentDate(DateUtil.FORMAT_3);

		// 리스트 데이터
		for (PmsProduct product : list) {

			String[] data = { product.getProductId(), product.getName(), product.getPmsCategory().getName(),
					product.getProductTypeName(), product.getSaleStateName(),

					product.getPmsBrand().getName() == null ? "" : product.getPmsBrand().getName(),
					product.getListPrice() == null ? "" : product.getListPrice().toString(),
					product.getSalePrice() == null ? "" : product.getSalePrice().toString(),
					product.getSupplyPrice() == null ? "" : product.getSupplyPrice().toString(),
					product.getCommissionRate() == null ? "" : product.getCommissionRate().toString(),

					product.getErpProductId(), product.getProductGubun(), product.getDmsCategoryName(), product.getSaleStartDt(),
					product.getSaleEndDt()

					, product.getInsName() == null ? product.getInsId() : product.getInsId() + "(" + product.getInsName() + ")"
					, product.getInsDt()
					, product.getUpdName() == null ? product.getUpdId() : product.getUpdId() + "(" + product.getUpdName() + ")"
					, product.getUpdDt() };
			dataList.add(data);
		}

		return returnView(dataType, headers, dataList, fileName);

	}

	/**
	 * 
	 * @Method Name : getMdNoticeExcel
	 * @author : eddie
	 * @date : 2016. 7. 11.
	 * @description : MD공지 엑셀 다운로드
	 * @param ccsNoticeSearch
	 * @return
	 */
	@RequestMapping(value = "/pms/mdnotice", method = { RequestMethod.GET })
	public ModelAndView getMdNoticeExcel(CcsNoticeSearch ccsNoticeSearch) {

		// 목록 조회
		List<CcsNotice> list = new ArrayList<CcsNotice>();
		ccsNoticeSearch.setStoreId(SessionUtil.getStoreId());
		list = mdnoticeService.selectNoticeList(ccsNoticeSearch);

		List<String[]> dataList = new ArrayList<String[]>();

		// 헤더
		String[] headers = { "MD공지번호", "공지명", "전시여부", "등록자", "등록일시", "최종수정자", "최종수정일시" };

		// 리스트 데이터
		for (CcsNotice notice : list) {
			String[] data = new String[headers.length];

			data[0] = notice.getNoticeNo().toString();
			data[1] = notice.getTitle();
			data[2] = notice.getDisplayYn();
			data[3] = notice.getInsName() == null ? notice.getInsId() : notice.getInsId() + "(" + notice.getInsName() + ")";
			data[4] = notice.getInsDt();
			data[5] = notice.getUpdName() == null ? notice.getUpdId() : notice.getUpdId() + "(" + notice.getUpdName() + ")";
			data[6] = notice.getUpdDt();

			dataList.add(data);
		}

		return returnView(headers, dataList);

	}

	/**
	 * 코드 그룹 엑셀 다운로드
	 * 
	 * @Method Name : getCodeGroupExcel
	 * @author : roy
	 * @date : 2016. 7. 11.
	 * @description :
	 *
	 * @param codegroupSearch
	 * @throws Exception
	 */
	@RequestMapping(value = "/ccs/code/codegroup", method = { RequestMethod.GET })
	public ModelAndView getCodeGroupExcel(CcsCodeSearch codegroupSearch) {

		// 목록 조회
		List<CcsCodegroup> list = new ArrayList<CcsCodegroup>();
		codegroupSearch.setStoreId(SessionUtil.getStoreId());
		list = codeService.selectCodegroupList(codegroupSearch);

		List<String[]> dataList = new ArrayList<String[]>();

		// 헤더
		String[] headers = { "그룹코드값", "코드그룹명", "등록자", "등록일시", "최종수정자", "최종수정일시" };

		// 리스트 데이터
		for (CcsCodegroup codegroup : list) {
			String[] data = new String[headers.length];

			data[0] = codegroup.getCdGroupCd();
			data[1] = codegroup.getName();
			data[2] = codegroup.getInsName() == null ? codegroup.getInsId() : codegroup.getInsId() + "(" + codegroup.getInsName() + ")";
			data[3] = codegroup.getInsDt();
			data[4] = codegroup.getUpdName() == null ? codegroup.getUpdId() : codegroup.getUpdId() + "(" + codegroup.getUpdName() + ")";
			data[5] = codegroup.getUpdDt();

			dataList.add(data);
		}

		return returnView(headers, dataList);

	}

	/**
	 * 코드 엑셀 다운로드
	 * 
	 * @Method Name : getCodeExcel
	 * @author : roy
	 * @date : 2016. 7. 11.
	 * @description :
	 *
	 * @param codeSearch
	 * @throws Exception
	 */

	@RequestMapping(value = "/ccs/code", method = { RequestMethod.GET })
	public ModelAndView getCodeExcel(CcsCodeSearch codeSearch) {

		// 목록 조회
		List<CcsCode> list = new ArrayList<CcsCode>();
		codeSearch.setStoreId(SessionUtil.getStoreId());
		list = codeService.selectCodeList(codeSearch);

		List<String[]> dataList = new ArrayList<String[]>();

		// 헤더
		String[] headers = { "코드값", "코드명", "우선순위", "사용여부", "등록자", "등록일시", "최종수정자", "최종수정일시" };

		// 리스트 데이터
		for (CcsCode code : list) {
			String[] data = new String[headers.length];

			data[0] = code.getCd();
			data[1] = code.getName();
			data[2] = (String) (code.getSortNo() == null ? code.getSortNo() : code.getSortNo().toString());
			data[3] = code.getUseYn();
			data[4] = code.getInsName() == null ? code.getInsId() : code.getInsId() + "(" + code.getInsName() + ")";
			data[5] = code.getInsDt();
			data[6] = code.getUpdName() == null ? code.getUpdId() : code.getUpdId() + "(" + code.getUpdName() + ")";
			data[7] = code.getUpdDt();

			dataList.add(data);
		}

		return returnView(headers, dataList);

	}

	/**
	 * 메뉴 엑셀 다운로드
	 * 
	 * @Method Name : getMenuListExcel
	 * @author : roy
	 * @date : 2016. 7. 11.
	 * @description :
	 *
	 * @param menuSearch
	 * @throws Exception
	 */

	@RequestMapping(value = "/ccs/menu/menu", method = { RequestMethod.GET })
	public ModelAndView getMenuExcel(CcsMenuSearch menuSearch) {

		// 목록 조회
		// 메뉴 목록 조회
		List<CcsMenu> list = new ArrayList<CcsMenu>();
		menuSearch.setStoreId(SessionUtil.getStoreId());
		list = menuService.selectMenuList(menuSearch);

		List<String[]> dataList = new ArrayList<String[]>();

		// 헤더
		String[] headers = { "메뉴번호", "메뉴명", "우선순위", "URL", "등록자", "등록일시", "최종수정자", "최종수정일시" };

		// 리스트 데이터
		for (CcsMenu ccsMenu : list) {

			String[] data = { ccsMenu.getMenuId(), ccsMenu.getName(),
					(String) (ccsMenu.getSortNo() == null ? ccsMenu.getSortNo() : ccsMenu.getSortNo().toString()),
					ccsMenu.getUrl(),
					ccsMenu.getInsName() == null ? ccsMenu.getInsId() : ccsMenu.getInsId() + "(" + ccsMenu.getInsName() + ")"
					, ccsMenu.getInsDt()
					, ccsMenu.getUpdName() == null ? ccsMenu.getUpdId() : ccsMenu.getUpdId() + "(" + ccsMenu.getUpdName() + ")"
					, ccsMenu.getUpdDt() };
			dataList.add(data);
		}

		return returnView(headers, dataList);

	}

	/**
	 * 상품QnA 엑셀 다운로드
	 * 
	 * @Method Name : getPrdQnAExcel
	 * @author : roy
	 * @date : 2016. 7. 11.
	 * @description :
	 *
	 * @param menuSearch
	 * @throws Exception
	 */

	@RequestMapping(value = "/ccs/productQna", method = { RequestMethod.GET })
	public ModelAndView getPrdQnAExcel(PmsProductQnaSearch search) {

		// 목록 조회
		List<PmsProductqna> list = new ArrayList<PmsProductqna>();
		search.setStoreId(SessionUtil.getStoreId());
		list = qnaService.getProductQnaList(search);

		List<String[]> dataList = new ArrayList<String[]>();

		// 헤더
		String[] headers = { "QnA번호", "제목", "상품번호", "상품명", "담당업체", "회원등급", "QnA상태", "QnA등록자", "QnA등록일시", "QnA확인자", 
				"확인일시", "답변등록자", "답변등록일", "경과시간" };

		// 리스트 데이터
		for (PmsProductqna prdQna : list) {

			String[] data = new String[headers.length];

			data[0] = prdQna.getProductQnaNo().toString();
			data[1] = prdQna.getTitle();
			data[2] = prdQna.getProductId();
			data[3] = prdQna.getPmsProduct().getName();
			data[4] = prdQna.getCcsBusiness().getName();
			data[5] = prdQna.getMmsMemberzts().getMemGradeName();
			data[6] = prdQna.getProductQnaStateName();
			data[7] = prdQna.getQuestioner();
			data[8] = prdQna.getInsName() == null ? prdQna.getInsId() : prdQna.getInsId() + "(" + prdQna.getInsName() + ")";
			data[9] = prdQna.getConfirmName() == null ? prdQna.getConfirmId() : prdQna.getConfirmId() + "(" + prdQna.getConfirmName() + ")";
			data[10] = prdQna.getConfirmDt();
			data[11] = prdQna.getAnswerer();
			data[12] = prdQna.getAnswerDt();
			data[13] = CcsUtil.getPassTime(prdQna.getInsDt(), prdQna.getAnswerDt());
			dataList.add(data);
			
			
		}

		return returnView(headers, dataList);

	}

	/**
	 * 체험단 엑셀 다운로드
	 * 
	 * @Method Name : getReviewpermitExcel
	 * @author : roy
	 * @date : 2016. 7. 11.
	 * @description :
	 *
	 * @param PmsReviewpermitSearch
	 * @throws Exception
	 */

	/**
	 * 문의 엑셀 다운로드
	 * 
	 * @Method Name : getInquiryExcel
	 * @author : roy
	 * @date : 2016. 7. 11.
	 * @description :
	 *
	 * @param MmsQnaSearch
	 * @throws Exception
	 */

	@RequestMapping(value = "/ccs/inquiry", method = { RequestMethod.GET })
	public ModelAndView getInquiryExcel(CcsInquirySearch search) {

		// 목록 조회
		List<CcsInquiry> list = new ArrayList<CcsInquiry>();
		search.setStoreId(SessionUtil.getStoreId());
		list = qnaService.selectQnaList(search);
		List<String[]> dataList = new ArrayList<String[]>();

		// 헤더
		String[] headers = { "문의번호", "제목", "문의채널", "문의고객", "회원 등급", "문의유형", "문의한 브랜드", "문의상태", "문의등록자(CS등록자)", "문의등록일시", "문의확인자", "확인일시",
				"답변등록자", "답변등록일", "경과시간" };
		// 리스트 데이터
		for (CcsInquiry ccsQnaInfo : list) {

			String[] data = new String[headers.length];

			data[0] = ccsQnaInfo.getInquiryNo().toString();
			data[1] = ccsQnaInfo.getTitle();
			data[2] = ccsQnaInfo.getInquiryChannelName();
			data[3] = ccsQnaInfo.getMemberName() == null ? ccsQnaInfo.getMemberId() : ccsQnaInfo.getMemberId() + "(" + ccsQnaInfo.getMemberName() + ")";
			if (ccsQnaInfo.getMemberNo() != null && ccsQnaInfo.getMmsMemberzts() != null) {
				data[4] = ccsQnaInfo.getMmsMemberzts().getMemGradeName();
			} else {
				data[4] = "비회원";
			}
			data[5] = ccsQnaInfo.getInquiryTypeName();
			if(ccsQnaInfo.getPmsBrand() != null){
				data[6] = ccsQnaInfo.getPmsBrand().getName();
			}else{
				data[6] = "-";
			}
			data[7] = ccsQnaInfo.getInquiryStateName();
			data[8] = ccsQnaInfo.getInsName() == null ? ccsQnaInfo.getInsId() : ccsQnaInfo.getInsId() + "(" + ccsQnaInfo.getInsName() + ")";
			data[9] = ccsQnaInfo.getInsDt();
			data[10] = ccsQnaInfo.getConfirmName() == null ? ccsQnaInfo.getConfirmId() : ccsQnaInfo.getConfirmId() + "(" + ccsQnaInfo.getConfirmName() + ")";
			data[11] = ccsQnaInfo.getConfirmDt();
			data[12] = ccsQnaInfo.getAnswerer();
			data[13] = ccsQnaInfo.getAnswerDt();
			data[14] = CcsUtil.getPassTime(ccsQnaInfo.getInsDt(), ccsQnaInfo.getAnswerDt());
			dataList.add(data);
		}

		return returnView(headers, dataList);

	}

	/**
	 * 상품평 엑셀 다운로드
	 * 
	 * @Method Name : getProductReviewExcel
	 * @author : roy
	 * @date : 2016. 8. 1.
	 * @description :
	 *
	 * @param PmsProductReviewSearch
	 * @throws Exception
	 */

	@RequestMapping(value = "/pms/product/productReview", method = { RequestMethod.GET })
	public ModelAndView getProductReviewExcel(PmsProductReviewSearch search) {

		// 목록 조회
		List<PmsReview> list = new ArrayList<PmsReview>();
		search.setStoreId(SessionUtil.getStoreId());
		list = productReviewService.getReviewList(search);
		List<String[]> dataList = new ArrayList<String[]>();

		// 헤더
		String[] headers = { "상품평번호", "제목", "상품번호", "상품명", "단품번호", "유형", "회원명", "등록일시", "최종구매일시", "점수", "전시여부", "우수 상품평", "최종수정자",
				"최종수정일시" };
		// 리스트 데이터
		for (PmsReview review : list) {
			String[] data = new String[headers.length];

			data[0] = review.getReviewNo().toString();
			data[1] = review.getTitle();
			data[2] = review.getProductId();
			data[3] = review.getPmsProduct().getName();
			data[4] = review.getSaleproductId();
			if ("Y".equals(review.getPermitYn())) {
				data[5] = "체험단";

			} else {
				data[5] = "일반";
			}
			data[6] = review.getMmsMember().getMemberName() == null ? review.getMmsMember().getMemberId() : review.getMmsMember().getMemberId() + "(" + review.getMmsMember().getMemberName() + ")";
			data[7] = review.getInsDt();
			data[8] = review.getOrderDt();
			data[9] = review.getRating().toString();
			data[10] = review.getDisplayYn();
			data[11] = review.getBestYn();
			data[12] = review.getUpdName() == null ? review.getUpdId() : review.getUpdId() + "(" + review.getUpdName() + ")";
			data[13] = review.getUpdDt();
			dataList.add(data);
		}

		return returnView(headers, dataList);

	}

	/**
	 * FAQ 엑셀 다운로드
	 * 
	 * @Method Name : getFaqExcel
	 * @author : roy
	 * @date : 2016. 7. 12.
	 * @description :
	 *
	 * @param CcsFaqSearch
	 * @throws Exception
	 */

	@RequestMapping(value = "/ccs/faq", method = { RequestMethod.GET })
	public ModelAndView getFaqExcel(CcsFaqSearch search) {

		// 목록 조회
		List<CcsFaq> list = new ArrayList<CcsFaq>();
		search.setStoreId(SessionUtil.getStoreId());
		list = faqService.selectList(search);
		List<String[]> dataList = new ArrayList<String[]>();

		// 헤더
		String[] headers = { "FAQ번호", "제목", "유형", "전시여부", "우선순위", "등록자", "등록일시", "최종수정자", "최종수정일시" };
		// 리스트 데이터
		for (CcsFaq faq : list) {
			String[] data = new String[headers.length];

			data[0] = faq.getFaqNo().toString();
			data[1] = faq.getTitle();
			data[2] = faq.getFaqTypeName();
			data[3] = faq.getDisplayYn();
			data[4] = faq.getSortNo().toString();
			data[5] = (String) (faq.getSortNo() == null ? faq.getSortNo() : faq.getSortNo().toString());
			data[6] = faq.getInsDt();
			data[7] = faq.getUpdName() == null ? faq.getUpdId() : faq.getUpdId() + "(" + faq.getUpdName() + ")";
			data[8] = faq.getUpdDt();
			dataList.add(data);
		}

		return returnView(headers, dataList);

	}

	/**
	 * 공지사항 엑셀 다운로드
	 * 
	 * @Method Name : getNoticeExcel
	 * @author : roy
	 * @date : 2016. 8. 01.
	 * @description :
	 *
	 * @param CcsNoticeSearch
	 * @throws Exception
	 */

	@RequestMapping(value = "/ccs/notice", method = { RequestMethod.GET })
	public ModelAndView getNoticeExcel(CcsNoticeSearch search) {

		// 목록 조회
		List<CcsNotice> list = new ArrayList<CcsNotice>();
		search.setStoreId(SessionUtil.getStoreId());
		list = noticeService.selectList(search);
		List<String[]> dataList = new ArrayList<String[]>();

			
		// 헤더
		String[] headers = { "공지번호", "제목", "유형", "TOP노출여부", "공지시작일시", "공지마감일시", "전시여부", "조회수", "등록자", "등록일시", "최종수정자", "최종수정일시" };
		// 리스트 데이터
		for (CcsNotice notice : list) {
			String[] data = new String[headers.length];

			data[0] = notice.getNoticeNo().toString();
			data[1] = notice.getTitle();
			data[2] = notice.getNoticeTypeName();
			data[3] = notice.getTopNoticeYn();
			data[4] = notice.getStartDt();
			data[5] = notice.getEndDt();
			data[6] = notice.getDisplayYn();
			data[7] = notice.getReadCnt().toString();
			data[8] = notice.getInsName() == null ? notice.getInsId() : notice.getInsId() + "(" + notice.getInsName() + ")";
			data[9] = notice.getInsDt();
			data[10] = notice.getUpdName() == null ? notice.getUpdId() : notice.getUpdId() + "(" + notice.getUpdName() + ")";
			data[11] = notice.getUpdDt();
			dataList.add(data);

//			data[4] = notice.getUseYn();
//			data[5] = notice.getFixYn();
		}

		return returnView(headers, dataList);

	}

	/**
	 * 팝업 엑셀 다운로드
	 * 
	 * @Method Name : getPopupnoticeExcel
	 * @author : roy
	 * @date : 2016. 8. 01.
	 * @description :
	 *
	 * @param CcsPopupnoticeSearch
	 * @throws Exception
	 */

	@RequestMapping(value = "/ccs/popupnotice", method = { RequestMethod.GET })
	public ModelAndView getPopupnoticeExcel(CcsPopupNoticeSearch search) {

		// 목록 조회
		List<CcsPopup> list = new ArrayList<CcsPopup>();
		search.setStoreId(SessionUtil.getStoreId());
		list = popupService.selectList(search);
		List<String[]> dataList = new ArrayList<String[]>();

		// 헤더
		String[] headers = { "팝업번호", "제목", "팝업 유형", "전시채널", "팝업공지시작 일시", "팝업공지마감 일시", "전시여부", "등록자", "등록일시", "최종수정자", "최종수정일시" };
		// 리스트 데이터
		for (CcsPopup popup : list) {

			if (popup.getPcDisplayYn().equals("Y")) {
				if (popup.getMobileDisplayYn().equals("Y")) {
					popup.setChannelName("PC, MOBILE");
				} else {
					popup.setChannelName("PC");
				}
			} else {
				if (popup.getMobileDisplayYn().equals("Y")) {
					popup.setChannelName("MOBILE");
				}
			}
			String[] data = new String[headers.length];

			data[0] = popup.getPopupNo().toString();
			data[1] = popup.getTitle();
			data[2] = popup.getPopupTypeName();
			data[3] = popup.getChannelName();
			data[4] = popup.getStartDt();
			data[5] = popup.getEndDt();
			data[6] = popup.getDisplayYn();
			data[7] = popup.getInsName() == null ? popup.getInsId() : popup.getInsId() + "(" + popup.getInsName() + ")";
			data[8] = popup.getInsDt();
			data[9] = popup.getUpdName() == null ? popup.getUpdId() : popup.getUpdId() + "(" + popup.getUpdName() + ")";
			data[10] = popup.getUpdDt();
			dataList.add(data);

//			data[4] = notice.getUseYn();
//			data[5] = notice.getFixYn();
		}

		return returnView(headers, dataList);

	}

	/**
	 * @Method Name : getCouponListExcel
	 * @author : ian
	 * @date : 2016. 7. 25.
	 * @description : 쿠폰 목록 엑셀 다운로드
	 *
	 * @param couponSearch
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sps/coupon", method = { RequestMethod.GET })
	public ModelAndView getCouponListExcel(SpsCouponSearch couponSearch) {

		// 쿠폰 목록 조회
		List<SpsCoupon> list = new ArrayList<SpsCoupon>();
		couponSearch.setStoreId(SessionUtil.getStoreId());
		list = couponService.getCouponList(couponSearch);

		List<String[]> dataList = new ArrayList<String[]>();

		// 헤더
		String[] headers = { "쿠폰번호", "쿠폰명", "쿠폰유형", "쿠폰할인", "쿠폰상태", "발급시작일시", "발급종료일시", "등록업체(번호)", "등록자", "등록일", "수정자", "수정일" };

		// 리스트 데이터
		for (SpsCoupon coupon : list) {
			String businessInfo = "";
			if (coupon.getCcsBusiness() != null && !CommonUtil.isEmpty(coupon.getCcsBusiness().getName())
					&& !CommonUtil.isEmpty(coupon.getBusinessId())) {
				businessInfo = coupon.getCcsBusiness().getName() + " (" + coupon.getBusinessId() + ")";
			}

			String[] data = { coupon.getCouponId(), coupon.getName(), coupon.getCouponTypeName(), coupon.getDcValue().toString(),
					coupon.getCouponStateName(), coupon.getIssueStartDt(), coupon.getIssueEndDt(), businessInfo,
					coupon.getInsName() == null ? coupon.getInsId() : coupon.getInsId() + "(" + coupon.getInsName() + ")",
					coupon.getInsDt(), 
					coupon.getUpdName() == null ? coupon.getUpdId() : coupon.getUpdId() + "(" + coupon.getUpdName() + ")", 
					coupon.getUpdDt() };
			dataList.add(data);
		}

		return returnView(headers, dataList);

	}
	
	/**
	 * @Method Name : getCouponIssueListExcel
	 * @author : ian
	 * @date : 2016. 10. 18.
	 * @description : 쿠폰 발급 내역 다운로드
	 *
	 * @param spsCouponIssueSearch
	 * @return
	 */
	@RequestMapping(value = "/sps/coupon/issued", method = { RequestMethod.GET })
	public ModelAndView getCouponIssueListExcel(SpsCouponIssueSearch spsCouponIssueSearch) {
		
		// 쿠폰 목록 조회
		List<SpsCouponissue> list = new ArrayList<SpsCouponissue>();
		spsCouponIssueSearch.setStoreId(SessionUtil.getStoreId());
		list = couponService.getCouponIssuedList(spsCouponIssueSearch);
		
		List<String[]> dataList = new ArrayList<String[]>();
		
		// 헤더
		String[] headers = { "개별인증코드", "발급일시", "발급회원", "사용가능시작일시", "사용가능종료일시", "쿠폰상태", "사용일시", "사용주문번호", "등록자", "등록일", "수정자", "수정일" };
		
		// 리스트 데이터
		for (SpsCouponissue coupon : list) {
			String[] data = { coupon.getPrivateCin() ,coupon.getRegDt() ,coupon.getMemberInfo() ,coupon.getUseStartDt() ,coupon.getUseEndDt() ,
					coupon.getCouponIssueStateName() ,coupon.getUseDt() ,coupon.getOmsOrdercoupon().getOrderId() ,
					coupon.getInsName() == null ? coupon.getInsId() : coupon.getInsId() + "(" + coupon.getInsName() + ")" ,coupon.getInsDt() ,
					coupon.getUpdName() == null ? coupon.getUpdId() : coupon.getUpdId() + "(" + coupon.getUpdName() + ")" ,coupon.getUpdDt() };
			dataList.add(data);
		}
		
		return returnView(headers, dataList);
		
	}

	/**
	 * @Method Name : getPresentPromotionListExcel
	 * @author : ian
	 * @date : 2016. 7. 25.
	 * @description : 사은품 프로모션 목록 엑셀 다운로드
	 *
	 * @param presentSearch
	 * @return
	 */
	@RequestMapping(value = "/sps/present", method = { RequestMethod.GET })
	public ModelAndView getPresentPromotionListExcel(SpsPresentSearch presentSearch) {

		// 사은품 목록 조회
		List<SpsPresent> list = new ArrayList<SpsPresent>();
		presentSearch.setStoreId(SessionUtil.getStoreId());
		list = presentPromotionService.getPresentPromotion(presentSearch);

		List<String[]> dataList = new ArrayList<String[]>();

		// 헤더
		String[] headers = { "프로모션번호", "프로모션명", "사은품유형", "사은품상태", "프로모션시작일시", "프로모션종료일시", "등록자", "등록일", "수정자", "수정일" };

		// 리스트 데이터
		for (SpsPresent present : list) {

			String[] data = { present.getPresentId(), present.getName(), present.getPresentTypeName(),
					present.getPresentStateName(), present.getStartDt(), present.getEndDt(), 
					present.getInsName() == null ? present.getInsId() : present.getInsId() + "(" + present.getInsName() + ")",
					present.getInsDt(), 
					present.getUpdName() == null ? present.getUpdId() : present.getUpdId() + "(" + present.getUpdName() + ")", 
					present.getUpdDt() };
			dataList.add(data);
		}

		return returnView(headers, dataList);

	}

	/**
	 * @Method Name : getPointsaveListExcel
	 * @author : ian
	 * @date : 2016. 7. 25.
	 * @description : 포인트 적립프로모션 목록 엑셀 다운로드
	 *
	 * @param pointSearch
	 * @return
	 */
	@RequestMapping(value = "/sps/point", method = { RequestMethod.GET })
	public ModelAndView getPointsaveListExcel(SpsPointSearch pointSearch) {

		// 포인트 목록 조회
		List<SpsPointsave> list = new ArrayList<SpsPointsave>();
		pointSearch.setStoreId(SessionUtil.getStoreId());
		list = pointService.getPointList(pointSearch);

		List<String[]> dataList = new ArrayList<String[]>();

		// 헤더
		String[] headers = { "프로모션번호", "프로모션명", "프로모션상태", "프로모션시작일시", "프로모션종료일시", "등록자", "등록일", "수정자", "수정일" };

		// 리스트 데이터
		for (SpsPointsave point : list) {

			String[] data = { point.getPointSaveId(), point.getName(), point.getPointSaveStateName(), point.getStartDt(),
					point.getEndDt(), 
					point.getInsName() == null ? point.getInsId() : point.getInsId() + "(" + point.getInsName() + ")", 
					point.getInsDt(), 
					point.getUpdName() == null ? point.getUpdId() : point.getUpdId() + "(" + point.getUpdName() + ")", 
					point.getUpdDt() };
			dataList.add(data);
		}

		return returnView(headers, dataList);

	}

	/**
	 * @Method Name : getCarrotListExcel
	 * @author : ian
	 * @date : 2016. 8. 17.
	 * @description : 당근 관리 목록 엑셀 다운로드
	 *
	 * @param carrotSearch
	 * @return
	 */
	@RequestMapping(value = "/sps/carrot", method = { RequestMethod.GET })
	public ModelAndView getCarrotListExcel(MmsMemberPopupSearch carrotSearch) {

		// 당근 목록 조회
		List<MmsCarrot> list = new ArrayList<MmsCarrot>();
		carrotSearch.setStoreId(SessionUtil.getStoreId());
		list = memberService.getCarrotList(carrotSearch);

		List<String[]> dataList = new ArrayList<String[]>();

		// 헤더
		String[] headers = { "회원번호", "회원명", "조정당근", "보유당근", "유형", "사유", "적용일시", "수정자", "수정일" };

		// 리스트 데이터
		for (MmsCarrot carrot : list) {
			String note = CommonUtil.isEmpty(carrot.getNote()) ? "" : carrot.getNote();

			String[] data = { carrot.getMemberNo().toString(),
					carrot.getMmsMember().getMemberName() + " (" + carrot.getMmsMember().getMemberId() + ")",
					carrot.getCarrot().toString(),
					carrot.getBalanceAmt().toString(), carrot.getCarrotTypeCd(), note, carrot.getInsDt(),
					carrot.getUpdName() == null ? carrot.getUpdId() : carrot.getUpdId() + "(" + carrot.getUpdName() + ")",
					carrot.getUpdDt() };
			dataList.add(data);
		}

		return returnView(headers, dataList);

	}
	
	/**
	 * 
	 * @Method Name : getDeliveryApprovalExcel
	 * @author : brad
	 * @date : 2016. 7. 14.
	 * @description : 배송 승인 목록 엑셀 다운로드
	 * @param omsOrderSearch
	 * @throws Exception 
	 */
	@RequestMapping(value = "/oms/logistics", method = { RequestMethod.GET })
	public ModelAndView getDeliveryApprovalExcel(OmsLogisticsSearch omsLogisticsSearch) throws Exception {

		// 상품 목록 조회
		List<OmsOrderproduct> list = new ArrayList<OmsOrderproduct>();
		omsLogisticsSearch.setStoreId(SessionUtil.getStoreId());
		list = logisticsService.getOrderProductList(omsLogisticsSearch);

		List<String[]> dataList = new ArrayList<String[]>();

		// 헤더
		String[] headers = { "주문일", "주문번호", "주문구분", "사이트", "발송유형", "미출고/승인취소", "주문자명", "주문자휴대폰번호", "수취인명", "수취인전화번호", "수취인휴대폰번호",
				"우편번호", "배송주소", "상품번호", "상품명", "단품명", "배송방법", "이중포장", "배송수량", "로케이션명", "재고수량", "배송승인취소사유", "전송오류유형" };

		String fileName = "배송승인목록_" + DateUtil.getCurrentDate(DateUtil.FORMAT_3);
		// 리스트 데이터
		for (OmsOrderproduct orderProduct : list) {
			String orderProductStateCd = orderProduct.getOrderProductStateCd();
			String orderProductStateName = "ORDER_PRODUCT_STATE_CD.CANCELDELIVERY".equals(orderProductStateCd) ? "미출고" : "ORDER_PRODUCT_STATE_CD.CANCELAPPROCAL".equals(orderProductStateCd) ? "승인취소" : "";
			
			String[] data = {
				orderProduct.getOrderDate(),
				orderProduct.getOrderId(),
				orderProduct.getOrderTypeName(),
				orderProduct.getSiteName(),
				orderProduct.getOrderDeliveryTypeName(),
				orderProductStateName,
				orderProduct.getOrdererNm(),
				orderProduct.getOrdererMobile(),
				orderProduct.getReceiverNm(),
				orderProduct.getReceiverPhone(),
				orderProduct.getReceiverMobile(),
				orderProduct.getZipCd(),
				orderProduct.getDeliveryAddress(),
				orderProduct.getProductId(),
				orderProduct.getProductName(),
				orderProduct.getSaleproductName(),
				orderProduct.getDeliveryMethod(),
				orderProduct.getDualWrapYn(),
				orderProduct.getOutReserveQty() == null ? "" : String.valueOf(orderProduct.getOutReserveQty()),
				orderProduct.getLocationId(),
				orderProduct.getRealStockQty() == null ? "" : String.valueOf(orderProduct.getRealStockQty()),
				orderProduct.getDeliveryCancelReasonName(),
				orderProduct.getSendErrorReasonName()		
			};
			
			dataList.add(data);
		}

		return returnView(null, headers, dataList, fileName);

	}

	/**
	 * @Method Name : getExhibitListExcel
	 * @author : ian
	 * @date : 2016. 7. 25.
	 * @description : 기회전 목록 엑셀 다운로드
	 *
	 * @param DmsExhibitSearch
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dms/exhibit", method = { RequestMethod.GET })
	public ModelAndView getExhibitListExcel(DmsExhibitSearch search) throws Exception {

		// 기획전 목록 조회
		List<DmsExhibit> list = new ArrayList<DmsExhibit>();
		search.setStoreId(SessionUtil.getStoreId());
		list = exhibitService.getExhibitList(search);

		List<String[]> dataList = new ArrayList<String[]>();

		// 헤더
		String[] headers = { "기획전ID", "기획전명", "유형", "상태", "전시여부", "정렬순서", "기획전시작일시", "기획전종료일시", "등록자", "등록일", "수정자", "수정일" };

		// 리스트 데이터
		for (DmsExhibit dmsExhibit : list) {

			String[] data = { dmsExhibit.getExhibitId(), dmsExhibit.getName(), dmsExhibit.getExhibitTypeName(),
					dmsExhibit.getExhibitStateName(),
					dmsExhibit.getDisplayYn(),
					(String) (dmsExhibit.getSortNo() == null ? dmsExhibit.getSortNo() : dmsExhibit.getSortNo().toString()),
					dmsExhibit.getStartDt(), dmsExhibit.getEndDt(),
					dmsExhibit.getInsName() == null ? dmsExhibit.getInsId() : dmsExhibit.getInsId() + "(" + dmsExhibit.getInsName() + ")", 
					dmsExhibit.getInsDt(), 
					dmsExhibit.getUpdName() == null ? dmsExhibit.getUpdId() : dmsExhibit.getUpdId() + "(" + dmsExhibit.getUpdName() + ")", 
					dmsExhibit.getUpdDt() };
			dataList.add(data);
		}

		return returnView(headers, dataList);

	}

	/**
	 * @Method Name : getExhibitDivTitlePoduct
	 * @author : roy
	 * @date : 2016. 10. 11.
	 * @description : 구분 타이틀 상품 목록 엑셀 다운로드
	 *
	 * @param DmsExhibitSearch
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dms/exhibit/divTitlePoduct", method = { RequestMethod.GET })
	public ModelAndView getExhibitDivTitlePoduct(DmsExhibitSearch search) throws Exception {

		// 구분 타이틀 상품 목록 조회
		List<DmsExhibitproduct> list = new ArrayList<DmsExhibitproduct>();
		search.setStoreId(SessionUtil.getStoreId());
		list = exhibitService.getExhibitProductList(search);

		List<String[]> dataList = new ArrayList<String[]>();

		// 헤더
		String[] headers = { "상품번호", "상품명", "우선순위", "전시여부", "상품유형", "판매상태", "판매가", "브랜드", "등록일", "등록자", "최종수정일", "최종수정자" };

		// 리스트 데이터
		for (DmsExhibitproduct dmsExhibitproduct : list) {

			String[] data = { 
					dmsExhibitproduct.getPmsProduct().getProductId(), 
					dmsExhibitproduct.getPmsProduct().getName(), 
					(String) (dmsExhibitproduct.getSortNo() == null ? dmsExhibitproduct.getSortNo() : dmsExhibitproduct.getSortNo().toString()),
					dmsExhibitproduct.getDisplayYn(),
					dmsExhibitproduct.getPmsProduct().getProductTypeName(), 
					dmsExhibitproduct.getPmsProduct().getSaleStateName(),
					(String) (dmsExhibitproduct.getPmsProduct().getSalePrice() == null ? dmsExhibitproduct.getPmsProduct().getSalePrice() : dmsExhibitproduct.getPmsProduct().getSalePrice().toString()),
					dmsExhibitproduct.getPmsProduct().getBrandName(), 
					dmsExhibitproduct.getInsDt(),
					dmsExhibitproduct.getInsName() == null ? dmsExhibitproduct.getInsId() : dmsExhibitproduct.getInsId() + "(" + dmsExhibitproduct.getInsName() + ")",
					dmsExhibitproduct.getUpdDt(),
					dmsExhibitproduct.getUpdName() == null ? dmsExhibitproduct.getUpdId() : dmsExhibitproduct.getUpdId() + "(" + dmsExhibitproduct.getUpdName() + ")"
						
					};
			dataList.add(data);
		}

		return returnView(headers, dataList);

	}
	
	/**
	 * @Method Name : getBusinessInquiryListExcel
	 * @author : roy
	 * @date : 2016. 8. 2.
	 * @description : 입점 신청 목록 엑셀 다운로드
	 *
	 * @param couponSearch
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ccs/businessinquiry", method = { RequestMethod.GET })
	public ModelAndView getBusinessinquiryListExcel(CcsBusinessinquirySearch search) throws Exception {

		// 쿠폰 목록 조회
		List<CcsBusinessinquiry> list = new ArrayList<CcsBusinessinquiry>();
		search.setStoreId(SessionUtil.getStoreId());
		list = businessinquiryService.getBusinessInquiryListInfo(search);

		List<String[]> dataList = new ArrayList<String[]>();

		// 헤더
		String[] headers = { "상담번호", "회사명", "전화번호", "영업담당자이름", "영업담당자전화번호", "영업담당자 E-mail", "담당MD", "신청일시" };

		// 리스트 데이터
		for (CcsBusinessinquiry businessInquiry : list) {
			String[] data = new String[headers.length];

			data[0] = businessInquiry.getBusinessInquiryNo().toString();
			data[1] = businessInquiry.getName();
			data[2] = businessInquiry.getPhone();
			data[3] = businessInquiry.getManagerName();
			data[4] = businessInquiry.getManagerPhone1();
			data[5] = businessInquiry.getManagerEmail();
			data[6] = businessInquiry.getMdInfo();
			data[7] = businessInquiry.getInsDt();

			dataList.add(data);
		}

		return returnView(headers, dataList);

	}
	
	/**
	 * @Method Name : getEventListExcel
	 * @author : stella
	 * @date : 2016. 8. 2.
	 * @description : 이벤트 목록 엑셀 다운로드
	 *
	 * @param eventSearch
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sps/event", method = { RequestMethod.GET })
	public ModelAndView getEventListExcel(SpsEventSearch search) throws Exception {
		
		// 이벤트 목록 조회
		List<SpsEvent> list = new ArrayList<>();
		search.setStoreId(SessionUtil.getStoreId());
		list = eventService.getEventList(search);
		
		List<String[]> dataList = new ArrayList<String[]>();

		// 헤더
		String[] headers = { "이벤트 번호", "이벤트명", "이벤트 유형", "구분", "전시여부", "이벤트시작일시", "이벤트종료일시", "이벤트 상태", "당첨자발표일", "등록자", "등록일", "최종수정자", "최종수정일" };
		
		// 리스트 데이터
		for (SpsEvent event : list) {
			String[] data = { event.getEventId(), event.getName(), event.getEventTypeName(), event.getEventDivName(), event.getDisplayYn(),
					event.getEventStartDt(), event.getEventEndDt(), event.getEventStateName(), event.getWinNoticeDate(), 
					event.getInsName() == null ? event.getInsId() : event.getInsId() + "(" + event.getInsName() + ")", 
					event.getInsDt(), 
					event.getUpdName() == null ? event.getUpdId() : event.getUpdId() + "(" + event.getUpdName() + ")", 
					event.getUpdDt() };
			dataList.add(data);
		}
		
		return returnView(headers, dataList);
	}
	
	/**
	 * @Method Name : getBrandListExcel
	 * @author : stella
	 * @date : 2016. 8. 2.
	 * @description : 브랜드 목록 엑셀 다운로드
	 *
	 * @param brandSearch
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pms/brand", method = { RequestMethod.GET })
	public ModelAndView getBrandListExcel(BrandSearch search) throws Exception {
		
		// 브랜드 목록 조회
		List<PmsBrand> list = new ArrayList<>();
		search.setStoreId(SessionUtil.getStoreId());
		list = brandService.getBrandList(search);
		
		List<String[]> dataList = new ArrayList<String[]>();

		// 헤더
		String[] headers = { "브랜드 번호", "브랜드명", "전시여부", "템플릿 유형", "등록자", "등록일시", "최종수정자", "최종수정일" };
		
		// 리스트 데이터
		for (PmsBrand brand : list) {
			String[] data = { brand.getBrandId(), brand.getName(), 
					brand.getDisplayYn()  == null ? "" : brand.getDisplayYn(),
					brand.getDmsTemplate()  == null ? "" : brand.getDmsTemplate().getTemplateTypeCd(),
					brand.getInsName() == null ? brand.getInsId() : brand.getInsId() + "(" + brand.getInsName() + ")", 
					brand.getInsDt(), 
					brand.getUpdName() == null ? brand.getUpdId() : brand.getUpdId() + "(" + brand.getUpdName() + ")", 
					brand.getUpdDt() };
			dataList.add(data);
		}
		
		return returnView(headers, dataList);
	}

	@RequestMapping(value = "/ccs/user", method = { RequestMethod.GET })
	public ModelAndView getUserListExcel(CcsUserSearch search) throws Exception {

		// 사용자 목록 조회
		List<CcsUser> list = new ArrayList<CcsUser>();
		search.setStoreId(SessionUtil.getStoreId());
		list = userService.getUserList(search);

		List<String[]> dataList = new ArrayList<String[]>();

		// 헤더
		String[] headers = { "Login ID", "사원명", "MD여부", "사용여부", "소속 권한그룹", "E-mail", "등록자", "등록일시", "최종수정자", "최종수정일시" };

		// 리스트 데이터
		for (CcsUser ccsUser : list) {

			String[] data = { ccsUser.getUserId(), ccsUser.getName(), ccsUser.getMdYn(), ccsUser.getUserStateName(),
					ccsUser.getCcsRole().getName(),
					ccsUser.getEmail()
					, ccsUser.getInsName() == null ? ccsUser.getInsId() : ccsUser.getInsId() + "(" + ccsUser.getInsName() + ")"
					, ccsUser.getInsDt()
					, ccsUser.getUpdName() == null ? ccsUser.getUpdId() : ccsUser.getUpdId() + "(" + ccsUser.getUpdName() + ")"
					, ccsUser.getUpdDt() };
			dataList.add(data);
		}

		return returnView(headers, dataList);

	}

	@RequestMapping(value = "/ccs/business", method = { RequestMethod.GET })
	public ModelAndView getBusinessListExcel(CcsBusinessSearch search) throws Exception {

		// 쿠폰 목록 조회
		List<CcsBusiness> list = new ArrayList<CcsBusiness>();
		search.setStoreId(SessionUtil.getStoreId());
		list = businessService.getBusinessListInfo(search);

		List<String[]> dataList = new ArrayList<String[]>();

		// 헤더
		String[] headers = { "업체번호", "업체명", "업체상태", "공급품목", "매입유형", "계약시작일시", "계약만료일시", "등록자", "등록일시", "최종수정자", "최종수정일시" };

		// 리스트 데이터
		for (CcsBusiness ccsBusiness : list) {

			String[] data = { ccsBusiness.getBusinessId(), ccsBusiness.getName(), ccsBusiness.getBusinessStateName(),
					ccsBusiness.getBusinessTypeName(), ccsBusiness.getSaleTypeName(), ccsBusiness.getContractStartDt(), ccsBusiness.getContractEndDt(),
					ccsBusiness.getInsName() == null ? ccsBusiness.getInsId() : ccsBusiness.getInsId() + "(" + ccsBusiness.getInsName() + ")"	
					,ccsBusiness.getInsDt() 
					,ccsBusiness.getUpdName() == null ? ccsBusiness.getUpdId() : ccsBusiness.getUpdId() + "(" + ccsBusiness.getUpdName() + ")"
					,ccsBusiness.getUpdDt() };
			dataList.add(data);
		}

		return returnView(headers, dataList);

	}

	@RequestMapping(value = "/mms/member", method = { RequestMethod.GET })
	public ModelAndView getMemberListExcel(MmsMemberPopupSearch mmsMemberPopupSearch) throws Exception {

		// 회원 목록 조회
		List<MmsMemberZts> list = new ArrayList<MmsMemberZts>();
		mmsMemberPopupSearch.setStoreId(SessionUtil.getStoreId());
		list = memberService.getMemberList(mmsMemberPopupSearch);

		List<String[]> dataList = new ArrayList<String[]>();

		// 헤더
		String[] headers = { "회원번호", "회원명", "생년월일", "성별", "이메일", "회원등급", "임직원여부", "멤버십여부", "다자녀카드 인증여부", "B2E 여부", "프리미엄여부",
				"회원상태", "휴대폰번호", "SMS수신여부", "EMAIL 수신 여부", "PUSH 수신여부", "블래리스트여부", "등록일", "최종수정자", "최종수정일" };

		// 리스트 데이터
		for (MmsMemberZts mmsMemberZts : list) {

			String[] data = { mmsMemberZts.getMemberNo().toString(), mmsMemberZts.getMmsMember().getMemberName(),
					mmsMemberZts.getMmsMember().getBirthday(), mmsMemberZts.getMmsMember().getGenderName(),
					mmsMemberZts.getMmsMember().getEmail(), mmsMemberZts.getMemGradeName(),
					mmsMemberZts.getMmsMember().getEmployeeYn(), mmsMemberZts.getMembershipYn(), mmsMemberZts.getChildrenYn(),
					mmsMemberZts.getB2eYn(), mmsMemberZts.getMmsMember().getPremiumYn(),
					mmsMemberZts.getMmsMember().getMemberStateName(), mmsMemberZts.getMmsMember().getPhone2(),
					mmsMemberZts.getMmsMember().getSmsYn(), mmsMemberZts.getMmsMember().getEmailYn(),
					mmsMemberZts.getMmsMember().getAppPushYn(), mmsMemberZts.getBlacklistYn(),
					mmsMemberZts.getMmsMember().getRegDt(), mmsMemberZts.getUpdId(), mmsMemberZts.getUpdDt() };

			dataList.add(data);
		}

		// 개인정보 조회 이력
		MmsReadhistory mmsReadhistory = new MmsReadhistory();
		mmsReadhistory.setUserId(SessionUtil.getLoginId());
		mmsReadhistory.setDetail("회원리스트 엑셀 다운로드");
		mmsReadhistory.setInsId(SessionUtil.getLoginId());
		mmsReadhistory.setUpdId(SessionUtil.getLoginId());
		historyService.saveReadHistory(mmsReadhistory);

		return returnView(headers, dataList);

	}

	/**
	 * 
	 * @Method Name : getChildrencardListExcel
	 * @author : allen
	 * @date : 2016. 11. 2.
	 * @description : 다자녀 카드 목록 엑셀 다운로드
	 *
	 * @param mmsChildrenSearch
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mms/childrencard", method = { RequestMethod.GET })
	public ModelAndView getChildrencardListExcel(MmsChildrenSearch mmsChildrenSearch) throws Exception {

		// 다자녀 카드 목록 조회
		List<MmsChildrencard> list = new ArrayList<MmsChildrencard>();
		mmsChildrenSearch.setStoreId(SessionUtil.getStoreId());
		list = childrenService.getChildrenCardList(mmsChildrenSearch);

		List<String[]> dataList = new ArrayList<String[]>();

		// 헤더
		String[] headers = { "다자녀카드유형", "이름", "다자녀카드번호", "등록여부", "시작일", "종료일" };

		// 리스트 데이터
		for (MmsChildrencard mmsChildrencard : list) {

			String[] data = { mmsChildrencard.getChildrencardTypeName(), mmsChildrencard.getName(),
					mmsChildrencard.getAccountNo(), mmsChildrencard.getRegYn(), mmsChildrencard.getStartDt(),
					mmsChildrencard.getEndDt() };

			dataList.add(data);
		}

		return returnView(headers, dataList);

	}

	/**
	 * @Method Name : getEventCouponListExcel
	 * @author : stella
	 * @date : 2016. 8. 10.
	 * @description : 이벤트 쿠폰 목록 엑셀 다운로드
	 *
	 * @param eventSearch
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sps/event/popup/coupon", method = { RequestMethod.GET })
	public ModelAndView getEventCouponListExcel(SpsEventSearch search) throws Exception {
		
		// 이벤트 목록 조회
		List<SpsEventcoupon> list = new ArrayList<>();
		search.setStoreId(SessionUtil.getStoreId());
		list = eventService.getEventCouponList(search);
		
		List<String[]> dataList = new ArrayList<String[]>();

		// 헤더
		String[] headers = { "쿠폰번호", "쿠폰명", "쿠폰유형", "쿠폰할인", "쿠폰상태", "쿠폰발급시작일시", "쿠폰발급종료일시", "등록자", "등록일", "최종수정자", "최종수정일" };
		
		// 리스트 데이터
		for (SpsEventcoupon eventCoupon : list) {
			String[] data = { eventCoupon.getCouponId(), eventCoupon.getSpsCoupon().getName(),
					eventCoupon.getSpsCoupon().getCouponTypeName(), eventCoupon.getSpsCoupon().getDcValue().toString(),
					eventCoupon.getSpsCoupon().getIssueStartDt(), eventCoupon.getSpsCoupon().getIssueEndDt(),
					eventCoupon.getInsName() == null ? eventCoupon.getInsId() : eventCoupon.getInsId() + "(" + eventCoupon.getInsName() + ")", 
					eventCoupon.getInsDt(), 
					eventCoupon.getUpdName() == null ? eventCoupon.getUpdId() : eventCoupon.getUpdId() + "(" + eventCoupon.getUpdName() + ")", 
					eventCoupon.getUpdDt() };
			dataList.add(data);
		}
		
		return returnView(headers, dataList);
	}
	
	/**
	 * @Method Name : getEventJoinListExcel
	 * @author : stella
	 * @date : 2016. 8. 11.
	 * @description : 이벤트 응모정보 엑셀 다운로드
	 *
	 * @param eventSearch
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sps/event/popup/join", method = { RequestMethod.GET })
	public ModelAndView getEventJoinListExcel(SpsEventSearch search) throws Exception {
		
		// 이벤트 응모 내역 조회
		List<SpsEventjoin> list = new ArrayList<>();
		search.setStoreId(SessionUtil.getStoreId());
		list = eventService.getEventJoinList(search);
		
		List<String[]> dataList = new ArrayList<String[]>();

		// 헤더
		String[] headers = { "응모번호", "회원번호", "회원명", "전화번호", "핸드폰 번호", "주소", "주문번호", 
				"네이버블로그URL", "인스타그램URL", "페이스북URL", "카카오스토리URL", "기타URL", "응모일시", "당첨여부", "경품명" };
		
		// 리스트 데이터
		for (SpsEventjoin eventJoin : list) {
			String[] data = { eventJoin.getJoinNo().toString(), eventJoin.getMemberNo().toString(), eventJoin.getMemName(), 
					eventJoin.getPhone1(), eventJoin.getPhone2(), eventJoin.getMemAddress(), eventJoin.getOrderId(),
					eventJoin.getNaverblogUrl(), eventJoin.getInstagramUrl(), eventJoin.getFacebookUrl(), eventJoin.getKakaostoryUrl(),
					eventJoin.getUrl(), eventJoin.getInsDt(), eventJoin.getWinYn(), eventJoin.getGiftName() };
			dataList.add(data);
		}
		
		// 개인정보 조회 이력
		MmsReadhistory mmsReadhistory = new MmsReadhistory();
		mmsReadhistory.setUserId(SessionUtil.getLoginId());
		mmsReadhistory.setDetail("이벤트 응모 내역 엑셀 다운로드");
		mmsReadhistory.setInsId(SessionUtil.getLoginId());
		mmsReadhistory.setUpdId(SessionUtil.getLoginId());
		historyService.saveReadHistory(mmsReadhistory);

		return returnView(headers, dataList);
	}
	
	/**
	 * @Method Name : getCardPromotionListExcel
	 * @author : stella
	 * @date : 2016. 8. 11.
	 * @description : 카드사 할인 목록 엑셀 다운로드
	 *
	 * @param eventSearch
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sps/discount/card", method = { RequestMethod.GET })
	public ModelAndView getCardPromotionListExcel(SpsCardPromotionSearch search) throws Exception {
		
		// 카드사 할인 목록 조회
		List<SpsCardpromotion> list = new ArrayList<>();
		search.setStoreId(SessionUtil.getStoreId());
		list = discountService.getCreditCardList(search);
		
		List<String[]> dataList = new ArrayList<String[]>();

		// 헤더
		String[] headers = { "프로모션 번호", "프로모션명", "프로모션 상태", "프로모션 시작일시", "프로모션 종료일시", 
				"등록자", "등록일시", "최종수정자", "최종수정일시" };
		
		// 리스트 데이터
		for (SpsCardpromotion promotion : list) {
			String[] data = { promotion.getCardPromotionNo().toString(), promotion.getName(),
					promotion.getCardPromotionStateName(),
					promotion.getStartDt(), promotion.getEndDt(), 
					promotion.getInsName() == null ? promotion.getInsId() : promotion.getInsId() + "(" + promotion.getInsName() + ")", 
					promotion.getInsDt(),
					promotion.getUpdName() == null ? promotion.getUpdId() : promotion.getUpdId() + "(" + promotion.getUpdName() + ")", 
					promotion.getUpdDt() };
			dataList.add(data);
		}
		
		return returnView(headers, dataList);
	}
	
	/**
	 * @Method Name : getChannelListExcel
	 * @author : roy
	 * @date : 2016. 8. 16.
	 * @description : 채널 목록 엑셀 다운로드
	 *
	 * @param eventSearch
	 * @return
	 * @throws Exception
	 */
	
	@RequestMapping(value = "/ccs/common/channel", method = { RequestMethod.GET })
	public ModelAndView getChannelListExcel(CcsChannelSearch search) throws Exception {
		
		// 카드사 할인 목록 조회
		List<CcsChannel> list = new ArrayList<>();
		search.setStoreId(SessionUtil.getStoreId());
		list = commonService.getChannelList(search);
		
		List<String[]> dataList = new ArrayList<String[]>();

		// 헤더
		String[] headers = { "채널번호", "채널명", "채널유형", "상태", 
				"등록자", "등록일시", "최종수정자", "최종수정일시" };
		
		// 리스트 데이터
		for (CcsChannel channel : list) {
			String[] data = { channel.getChannelId(), channel.getName(), channel.getChannelTypeName(),
					channel.getChannelStateName()
					, channel.getInsName() == null ? channel.getInsId() : channel.getInsId() + "(" + channel.getInsName() + ")"
					, channel.getInsDt()
					, channel.getUpdName() == null ? channel.getUpdId() : channel.getUpdId() + "(" + channel.getUpdName() + ")"
					, channel.getUpdDt() };
			dataList.add(data);
		}
		return returnView(headers, dataList);
	}

	/**
	 * @Method Name : getDepositListExcel
	 * @author : ian
	 * @date : 2016. 8. 17.
	 * @description : 예치금 관리 목록 엑셀 다운로드
	 *
	 * @param depositSearch
	 * @return
	 */
	@RequestMapping(value = "/oms/deposit", method = { RequestMethod.GET })
	public ModelAndView getDepositListExcel(MmsMemberPopupSearch depositSearch) {

		// 쿠폰 목록 조회
		List<MmsDeposit> list = new ArrayList<MmsDeposit>();
		depositSearch.setStoreId(SessionUtil.getStoreId());
		list = memberService.getDepositList(depositSearch);

		List<String[]> dataList = new ArrayList<String[]>();

		// 헤더
		String[] headers = { "회원번호", "회원명", "유형", "조정예치금", "보유당근", "적용일시", "사유", "주문번호", "클레임번호", "최종수정자", "최종수정일시"};

		// 리스트 데이터
		for (MmsDeposit deposit : list) {
			String note = CommonUtil.isEmpty(deposit.getNote()) ? "" : deposit.getNote();
			String orderId = CommonUtil.isEmpty(deposit.getOmsOrder().getOrderId()) ? "" : deposit.getOmsOrder().getOrderId();
			String claimNo = CommonUtil.isEmpty(deposit.getClaimNo().toString()) ? "" : deposit.getClaimNo().toString();

			String[] data = { deposit.getMemberNo().toString(),
					deposit.getMmsMember().getMemberName() + " (" + deposit.getMmsMember().getMemberId() + ")",
					deposit.getDepositTypeCd(), deposit.getDepositAmt().toString(), deposit.getBalanceAmt().toString(),
					note, orderId, claimNo, deposit.getInsDt(),
					deposit.getUpdName() == null ? deposit.getUpdId() : deposit.getUpdId() + "(" + deposit.getUpdName() + ")",
					deposit.getUpdDt() };
			dataList.add(data);
		}

		return returnView(headers, dataList);

	}

	@RequestMapping(value = "/pms/styleShop", method = { RequestMethod.GET })
	public ModelAndView getStyleShopProductListExcel(PmsStyleProductSearch search) {

		List<PmsStyleproduct> list = new ArrayList<PmsStyleproduct>();
		list = styleProductService.getStyleProductList(search);

		List<String[]> dataList = new ArrayList<String[]>();

		// 헤더
		String[] headers = { "스타일번호", "상품번호", "상품명", "브랜드번호", "브랜드", "스타일분류", "사용여부", "등록자", "등록일", "최종수정자", "최종수정일시" };

		// 리스트 데이터
		for (PmsStyleproduct styleproduct : list) {

			String[] data = { styleproduct.getStyleProductNo().toString(),
					styleproduct.getProductId(), styleproduct.getProductName(), styleproduct.getBrandId(),
					styleproduct.getBrandName(), styleproduct.getStyleProductItemName(), styleproduct.getUseYn(),
					styleproduct.getInsName() == null ? styleproduct.getInsId() : styleproduct.getInsId() + "(" + styleproduct.getInsName() + ")", 
					styleproduct.getInsDt(), 
					styleproduct.getUpdName() == null ? styleproduct.getUpdId() : styleproduct.getUpdId() + "(" + styleproduct.getUpdName() + ")", 
					styleproduct.getUpdDt() };
			dataList.add(data);
		}

		return returnView(headers, dataList);

	}
	
	/**
	 * @Method Name : getCatalogListExcel
	 * @author : stella
	 * @date : 2016. 8. 19.
	 * @description : LOOKBOOK 컨텐츠 목록 엑셀 다운로드
	 *
	 * @param search
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dms/catalog", method = { RequestMethod.GET })
	public ModelAndView getCatalogListExcel(DmsCatalogSearch search) throws Exception {
		
		// LOOKBOOK 컨텐츠 목록 조회
		List<DmsCatalog> list = new ArrayList<>();
		search.setStoreId(SessionUtil.getStoreId());
		list = catalogService.getCatalogList(search);
		
		List<String[]> dataList = new ArrayList<String[]>();

		// 헤더
		String[] headers = { "컨텐츠 번호", "컨텐츠명", "브랜드", "유형", "정렬순서", "전시여부", "등록자", "등록일", "최종수정자", "최종수정일" };
		
		// 리스트 데이터
		for (DmsCatalog catalog : list) {
			String[] data = { catalog.getCatalogId(), catalog.getName(), catalog.getPmsBrand().getName(), 
					catalog.getCatalogTypeName(),
					(String) (catalog.getSortNo() == null ? catalog.getSortNo() : catalog.getSortNo().toString()),
					catalog.getDisplayYn(),
					catalog.getInsName() == null ? catalog.getInsId() : catalog.getInsId() + "(" + catalog.getInsName() + ")", 
					catalog.getInsDt(), 
					catalog.getUpdName() == null ? catalog.getUpdId() : catalog.getUpdId() + "(" + catalog.getUpdName() + ")", 
					catalog.getUpdDt() };
			dataList.add(data);
		}
		
		return returnView(headers, dataList);
	}
	
	/**
	 * 
	 * @Method Name : getShippingProcessExcel
	 * @author : brad
	 * @date : 2016. 9. 10.
	 * @description : 출고 완료 처리 리스트 엑셀 다운로드
	 * @param omsOrderSearch
	 * @throws Exception 
	 */
	@RequestMapping(value = "/oms/logistics/shipping/process", method = { RequestMethod.GET })
	public ModelAndView getShippingProcessExcel(OmsLogisticsSearch omsLogisticsSearch) throws Exception {

		// 상품 목록 조회
		List<OmsLogistics> list = new ArrayList<OmsLogistics>();
		omsLogisticsSearch.setStoreId(SessionUtil.getStoreId());
		list = logisticsService.getShippingProcessList(omsLogisticsSearch);

		List<String[]> dataList = new ArrayList<String[]>();
		// 엑셀 업로드를 위해 위에 공백 한줄 넣는다.
		String[] dataType = {"숫자", "날짜", "날짜", "텍스트", "텍스트", "텍스트", "텍스트", "텍스트", "텍스트", "텍스트",
				"텍스트", "텍스트", "텍스트", "텍스트", "텍스트", "텍스트", "텍스트", "텍스트", "숫자", "숫자", "텍스트", "텍스트", "텍스트", "텍스트"};
		// 헤더
		String[] headers = { "배송번호", "배송승인일시", "주문일", "주문구분", "사이트", "주문자명", "주문자휴대폰번호", "수취인명", "수취인전화번호", "수취인휴대폰번호",
				"우편번호", "배송주소", "운송장번호", "상품번호", "상품명", "단품명", "ERP바코드", "이중포장", "배송수량", "실배송수량", "미출고사유", "주문번호", "LP_NO", "주문상품일련번호"};

		String fileName = "출고완료처리_" + DateUtil.getCurrentDate(DateUtil.FORMAT_3);
		// 리스트 데이터
		for (OmsLogistics omsLogistics : list) {
			
			OmsOrderproduct omsOrderProduct = omsLogistics.getOmsOrderproduct();

			String[] data = { 
				omsLogistics.getLogisticsInoutNo() == null ? "" : String.valueOf(omsLogistics.getLogisticsInoutNo()),
				omsLogistics.getInsDt() == null ? "" : omsLogistics.getInsDt(),
				omsOrderProduct.getOrderDate() == null ? "" : omsOrderProduct.getOrderDate(),
				omsOrderProduct.getOmsOrder() == null ? "" : omsOrderProduct.getOmsOrder().getOrderTypeName(),
				omsOrderProduct.getSiteName() == null ? "" : omsOrderProduct.getSiteName(),
				omsOrderProduct.getOmsOrder() == null ? "" : omsOrderProduct.getOmsOrder().getName1(),
				omsOrderProduct.getOmsOrder() == null ? "" : omsOrderProduct.getOmsOrder().getPhone2(),
				omsOrderProduct.getOmsDeliveryaddress() == null ? "" : omsOrderProduct.getOmsDeliveryaddress().getName1(),
				omsOrderProduct.getOmsDeliveryaddress() == null ? "" : omsOrderProduct.getOmsDeliveryaddress().getPhone1(),
				omsOrderProduct.getOmsDeliveryaddress() == null ? "" : omsOrderProduct.getOmsDeliveryaddress().getPhone2(),
				omsOrderProduct.getOmsDeliveryaddress() == null ? "" : omsOrderProduct.getOmsDeliveryaddress().getZipCd(),
				omsOrderProduct.getOmsDeliveryaddress() == null ? "" : omsOrderProduct.getOmsDeliveryaddress().getDeliveryAddress(),
				omsLogistics.getInvoiceNo() == null ? "" : omsLogistics.getInvoiceNo(),
				omsOrderProduct.getProductId() == null ? "" : omsOrderProduct.getProductId(),
				omsOrderProduct.getProductName() == null ? "" : omsOrderProduct.getProductName(),
				omsOrderProduct.getSaleproductName() == null ? "" : omsOrderProduct.getSaleproductName(),
				omsOrderProduct.getErpSaleproductId() == null ? "" : omsOrderProduct.getErpSaleproductId(),
				omsOrderProduct.getDualWrapYn() == null ? "" : omsOrderProduct.getDualWrapYn(),
				omsLogistics.getOutReserveQty() == null ? "" : omsLogistics.getOutReserveQty(),
				omsLogistics.getOutQty() == null ? "" : String.valueOf(omsLogistics.getOutQty()),
				omsLogistics.getCancelDeliveryReasonCd() == null ? "" : omsLogistics.getCancelDeliveryReasonCd(),
				omsOrderProduct.getOrderId() == null ? "" : omsOrderProduct.getOrderId(), 
				omsOrderProduct.getLpNo() == null ? "" : omsOrderProduct.getLpNo(),
				omsOrderProduct.getOrderProductNo() == null ? "" : String.valueOf(omsOrderProduct.getOrderProductNo())
			};
			
			dataList.add(data);
		}
		return returnView(dataType, headers, dataList, fileName);
	}
	
	/**
	 * 
	 * @Method Name : getShippingListExcel
	 * @author : brad
	 * @date : 2016. 9. 19.
	 * @description : 출고/미출고 리스트 엑셀 다운로드
	 * @param omsOrderSearch
	 * @throws Exception 
	 */
	@RequestMapping(value = "/oms/logistics/shipping", method = { RequestMethod.GET })
	public ModelAndView getShippingListExcel(OmsLogisticsSearch omsLogisticsSearch) throws Exception {

		// 상품 목록 조회
		List<OmsLogistics> list = new ArrayList<OmsLogistics>();
		omsLogisticsSearch.setStoreId(SessionUtil.getStoreId());
		list = logisticsService.getShippingList(omsLogisticsSearch);

		List<String[]> dataList = new ArrayList<String[]>();

		// 헤더
		String[] headers = { "주문일시", "출고/미출고처리일", "주문번호", "사이트", "주문자명", "주문자휴대폰번호", "수취인명", "수취인전화번호", "수취인휴대폰번호", "우편번호", "배송주소", "배송방법", "운송장번호", "배송업체", "공급업체",
				"상품번호", "상품명", "단품명", "ERP바코드", "주문수량", "출고수량", "출고여부", "미출고사유", "해외구매대행식별번호", "LP_NO" };

		String fileName = "출고-미출고리스트_" + DateUtil.getCurrentDate(DateUtil.FORMAT_3);
		// 리스트 데이터
		for (OmsLogistics omsLogistics : list) {
			
			OmsOrderproduct omsOrderProduct = omsLogistics.getOmsOrderproduct();
			
			String[] data = { 
				omsOrderProduct.getOrderDate(),
				omsLogistics.getCompleteDt(),
				omsLogistics.getOrderId(),
				omsOrderProduct.getSiteName(),
				omsOrderProduct.getOmsOrder().getName1(),
				omsOrderProduct.getOmsOrder().getPhone2(),
				omsOrderProduct.getOmsDeliveryaddress().getName1(),
				omsOrderProduct.getOmsDeliveryaddress().getPhone1(),
				omsOrderProduct.getOmsDeliveryaddress().getPhone2(),
				omsOrderProduct.getOmsDeliveryaddress().getZipCd(),
				omsOrderProduct.getOmsDeliveryaddress().getDeliveryAddress(),
				omsOrderProduct.getDeliveryMethod(),
				omsLogistics.getInvoiceNo(),
				omsLogistics.getDeliveryServiceName(),
				omsOrderProduct.getBusinessName(),
				omsOrderProduct.getProductId(),
				omsOrderProduct.getProductName(),
				omsOrderProduct.getSaleproductName(),
				omsOrderProduct.getErpSaleproductId(),
				omsLogistics.getOutReserveQty(),
				omsLogistics.getOutQty() == null ? "" : String.valueOf(omsLogistics.getOutQty()),
				BaseConstants.YN_Y.equals(omsLogistics.getShipYn()) ? "출고완료" : "미출고",
				omsLogistics.getCancelDeliveryReasonName(),
				omsOrderProduct.getPersonalCustomsCode(),
				omsOrderProduct.getLpNo()
			};
			
			dataList.add(data);
		}
		return returnView(null, headers, dataList, fileName);
	}
	
	/**
	 * 
	 * @Method Name : getApprovalCancelExcel
	 * @author : brad
	 * @date : 2016. 9. 19.
	 * @description : 배송 승인 취소 리스트 엑셀 다운로드
	 * @param omsOrderSearch
	 * @throws Exception 
	 */
	@RequestMapping(value = "/oms/logistics/cancel", method = { RequestMethod.GET })
	public ModelAndView getApprovalCancelExcel(OmsLogisticsSearch omsLogisticsSearch) throws Exception {

		// 상품 목록 조회
		List<OmsLogistics> list = new ArrayList<OmsLogistics>();
		omsLogisticsSearch.setStoreId(SessionUtil.getStoreId());
		list = logisticsService.getCancelApprovalList(omsLogisticsSearch);

		List<String[]> dataList = new ArrayList<String[]>();

		// 엑셀 업로드를 위해 위에 공백 한줄 넣는다.
		String[] dataType = {"숫자", "날짜", "텍스트", "텍스트", "텍스트", "텍스트", "텍스트", "텍스트", "텍스트", "텍스트", "텍스트",
				"텍스트", "텍스트", "텍스트", "텍스트", "텍스트", "텍스트", "텍스트", "숫자", "텍스트", "텍스트", "숫자"};
		// 헤더
		String[] headers = { "배송번호", "배송승인일시", "배송승인취소사유", "주문번호", "주문구분", "사이트", "주문자명", "주문자휴대폰번호", "수취인명", "수취인전화번호", "수취인휴대폰번호",
				"우편번호", "배송주소", "공급업체", "상품번호", "상품명", "단품명", "배송방법", "배송수량", "제휴주문번호", "주문상품일련번호" };

		String fileName = "배송승인취소_" + DateUtil.getCurrentDate(DateUtil.FORMAT_3);
		// 리스트 데이터
		for (OmsLogistics omsLogistics : list) {
			
			OmsOrderproduct omsOrderProduct = omsLogistics.getOmsOrderproduct();

			String[] data = { 
				omsLogistics.getLogisticsInoutNo() == null ? "" : String.valueOf(omsLogistics.getLogisticsInoutNo()),
				omsLogistics.getInsDt(),
				omsLogistics.getCancelDeliveryReasonCd(),
				omsLogistics.getOrderId(),
				omsOrderProduct.getOmsOrder().getOrderTypeName(),
				omsOrderProduct.getSiteName(),
				omsOrderProduct.getOmsOrder().getName1(),
				omsOrderProduct.getOmsOrder().getPhone2(),
				omsOrderProduct.getOmsDeliveryaddress().getName1(),
				omsOrderProduct.getOmsDeliveryaddress().getPhone1(),
				omsOrderProduct.getOmsDeliveryaddress().getPhone2(),
				omsOrderProduct.getOmsDeliveryaddress().getZipCd(),
				omsOrderProduct.getOmsDeliveryaddress().getDeliveryAddress(),
				omsOrderProduct.getBusinessName(),
				omsOrderProduct.getProductId(),
				omsOrderProduct.getProductName(),
				omsOrderProduct.getSaleproductName(),
				omsOrderProduct.getDeliveryMethod(),
				omsLogistics.getOutReserveQty(),
				omsOrderProduct.getOmsOrder().getSiteOrderId(),
				omsLogistics.getOrderProductNo() == null ? "" : String.valueOf(omsLogistics.getOrderProductNo())
			};
			
			dataList.add(data);
		}
		return returnView(dataType, headers, dataList, fileName);
	}
	
	/**
	 * 
	 * @Method Name : getInvoiceListExcel
	 * @author : brad
	 * @date : 2016. 9. 19.
	 * @description : 운송장 생성 리스트 엑셀 다운로드
	 * @param omsOrderSearch
	 * @throws Exception 
	 */
	@RequestMapping(value = "/oms/logistics/invoice", method = { RequestMethod.GET })
	public ModelAndView getInvoiceListExcel(OmsLogisticsSearch omsLogisticsSearch) throws Exception {

		// 상품 목록 조회
		List<OmsLogistics> list = new ArrayList<OmsLogistics>();
		omsLogisticsSearch.setStoreId(SessionUtil.getStoreId());
		list = logisticsService.getInvoiceList(omsLogisticsSearch);

		List<String[]> dataList = new ArrayList<String[]>();

		// 헤더
		String[] headers = { "배송번호", "배송승인일시", "전송여부", "주문번호", "주문구분", "사이트", "주문자명", "주문자휴대폰번호", "수취인명", "수취인전화번호", "수취인휴대폰번호",
				"우편번호", "배송주소", "상품번호", "상품명", "단품명", "이중포장", "배송수량" };
		// 파일명
		String fileName = "운송장생성_" + DateUtil.getCurrentDate(DateUtil.FORMAT_3);
		// 리스트 데이터
		for (OmsLogistics omsLogistics : list) {
			
			OmsOrderproduct omsOrderProduct = omsLogistics.getOmsOrderproduct();
			String sendYn = BaseConstants.YN_Y.equals(omsLogistics.getSendYn()) ? "전송" : "미전송" ;
			
			String[] data = { 
				omsLogistics.getLogisticsInoutNo() == null ? "" : String.valueOf(omsLogistics.getLogisticsInoutNo()),
				omsLogistics.getInsDt(),
				sendYn,
				omsLogistics.getOrderId(),
				omsOrderProduct.getOmsOrder().getOrderTypeName(),
				omsOrderProduct.getSiteName(),
				omsOrderProduct.getOmsOrder().getName1(),
				omsOrderProduct.getOmsOrder().getPhone2(),
				omsOrderProduct.getOmsDeliveryaddress().getName1(),
				omsOrderProduct.getOmsDeliveryaddress().getPhone1(),
				omsOrderProduct.getOmsDeliveryaddress().getPhone2(),
				omsOrderProduct.getOmsDeliveryaddress().getZipCd(),
				omsOrderProduct.getOmsDeliveryaddress().getDeliveryAddress(),
				omsOrderProduct.getProductId(),
				omsOrderProduct.getProductName(),
				omsOrderProduct.getSaleproductName(),
				omsOrderProduct.getDualWrapYn(),
				omsLogistics.getOutReserveQty()
			};
			
			dataList.add(data);
		}
		return returnView(null, headers, dataList, fileName);
	}
	
	/**
	 * 
	 * @Method Name : getPickingListExcel
	 * @author : brad
	 * @date : 2016. 9. 19.
	 * @description : 피킹 리스트 엑셀 다운로드
	 * @param omsOrderSearch
	 * @throws Exception 
	 */
	@RequestMapping(value = "/oms/logistics/picking", method = { RequestMethod.GET })
	public ModelAndView getPickingListExcel(OmsLogisticsSearch omsLogisticsSearch) throws Exception {

		// 상품 목록 조회
		List<OmsOrderproduct> list = new ArrayList<OmsOrderproduct>();
		omsLogisticsSearch.setStoreId(SessionUtil.getStoreId());
		list = logisticsService.getPickingList(omsLogisticsSearch);

		List<String[]> dataList = new ArrayList<String[]>();

		// 헤더
		String[] headers = { "운송장출력일", "로케이션명", "상품번호", "상품명", "단품명", "내입수량", "Box", "EA", "차수" };

		// 파일명
		String fileName = "피킹리스트_" + DateUtil.getCurrentDate(DateUtil.FORMAT_3);
		// 리스트 데이터
		for (OmsOrderproduct omsOrderproduct : list) {
			
			String[] data = { 
				omsOrderproduct.getInvoiceDt(),
				omsOrderproduct.getLocationId(),
				omsOrderproduct.getProductId(),
				omsOrderproduct.getProductName(),
				omsOrderproduct.getSaleproductName(),
				omsOrderproduct.getBoxUnitQty() == null ? "" : String.valueOf(omsOrderproduct.getBoxUnitQty()),
				CommonUtil.replaceNull(omsOrderproduct.getQuotient()),
				CommonUtil.replaceNull(omsOrderproduct.getRemainder()),
				CommonUtil.replaceNull(omsOrderproduct.getDeliveryOrder())
			};
			
			dataList.add(data);
		}
		return returnView(null, headers, dataList, fileName);
	}
	
	/**
	 * 
	 * @Method Name : getPartnerDeliveryListExcel
	 * @author : brad
	 * @date : 2016. 9. 19.
	 * @description : 배송의뢰서 협력사 리스트 엑셀 다운로드
	 * @param omsOrderSearch
	 * @throws Exception 
	 */
	@RequestMapping(value = "/oms/logistics/partner/delivery", method = { RequestMethod.GET })
	public ModelAndView getPartnerDeliveryListExcel(OmsLogisticsSearch omsLogisticsSearch) throws Exception {

		// 상품 목록 조회
		List<OmsOrderproduct> list = new ArrayList<OmsOrderproduct>();
		omsLogisticsSearch.setStoreId(SessionUtil.getStoreId());
		list = logisticsService.getPartnerDeliveryList(omsLogisticsSearch);

		List<String[]> dataList = new ArrayList<String[]>();

		// 헤더
		String[] headers = { "주문일시", "공급업체", "주문번호", "발송유형", "주문자명", "주문자휴대폰번호", "수취인명", "수취인전화번호", "수취인휴대폰번호",
				"우편번호", "배송주소", "상품번호", "상품명", "단품명", "배송수량", "배송메시지", "텍스트옵션", "해외구매대행식별번호", "배송승인취소사유" };
		// 파일명
		String fileName = "배송의뢰서협력사_" + DateUtil.getCurrentDate(DateUtil.FORMAT_3);
		// 리스트 데이터
		for (OmsOrderproduct orderProduct : list) {
			
			String[] data = {
				orderProduct.getDeliveryReserveDt(),
				orderProduct.getBusinessName(),
				orderProduct.getOrderId(),
				orderProduct.getOrderDeliveryTypeName(),
				orderProduct.getOmsOrder().getName1(),
				orderProduct.getOmsOrder().getPhone2(),
				orderProduct.getOmsDeliveryaddress().getName1(),
				orderProduct.getOmsDeliveryaddress().getPhone1(),
				orderProduct.getOmsDeliveryaddress().getPhone2(),
				orderProduct.getOmsDeliveryaddress().getZipCd(),
				orderProduct.getOmsDeliveryaddress().getDeliveryAddress(),
				orderProduct.getProductId(),
				orderProduct.getProductName(),
				orderProduct.getSaleproductName(),
				orderProduct.getOutReserveQty() == null ? "" : String.valueOf(orderProduct.getOutReserveQty()),
				orderProduct.getOmsDeliveryaddress().getNote(),
				orderProduct.getTextOptionValue(),
				orderProduct.getPersonalCustomsCode(),
				orderProduct.getDeliveryCancelReasonName()		
			};
			
			dataList.add(data);
		}

		return returnView(null, headers, dataList, fileName);
	}
	
	/**
	 * 
	 * @Method Name : getPartnerShippingListExcel
	 * @author : brad
	 * @date : 2016. 9. 19.
	 * @description : 출고 완료 처리 협력사 리스트 엑셀 다운로드
	 * @param omsOrderSearch
	 * @throws Exception 
	 */
	@RequestMapping(value = "/oms/logistics/partner/shipping", method = { RequestMethod.GET })
	public ModelAndView getPartnerShippingListExcel(OmsLogisticsSearch omsLogisticsSearch) throws Exception {

		// 상품 목록 조회
		List<OmsLogistics> list = new ArrayList<OmsLogistics>();
		omsLogisticsSearch.setStoreId(SessionUtil.getStoreId());
		list = logisticsService.getPartnerShippingList(omsLogisticsSearch);

		List<String[]> dataList = new ArrayList<String[]>();
		// 엑셀 업로드를 위해 위에 공백 한줄 넣는다.
		String[] dataType = {"숫자", "날짜", "텍스트", "텍스트", "텍스트", "텍스트", "텍스트", "텍스트", "텍스트", "텍스트", "텍스트", "텍스트", 
				"텍스트", "텍스트", "텍스트", "숫자", "숫자", "텍스트", "텍스트", "숫자" };
		// 헤더
		String[] headers = { "배송번호", "배송승인일시", "주문자명", "주문자휴대폰번호", "수취인명", "수취인전화번호", "수취인휴대폰번호", "우편번호", "배송주소", "운송장번호", "배송업체", "공급업체",
				"상품번호", "상품명", "단품명", "배송수량", "실배송수량", "미출고사유", "주문번호", "주문상품일련번호" };

		String fileName = "출고완료처리협력사_" + DateUtil.getCurrentDate(DateUtil.FORMAT_3);
		// 리스트 데이터
		for (OmsLogistics omsLogistics : list) {
			
			OmsOrderproduct omsOrderProduct = omsLogistics.getOmsOrderproduct();
			
			String[] data = { 
				omsLogistics.getLogisticsInoutNo() == null ? "" : String.valueOf(omsLogistics.getLogisticsInoutNo()),
				omsLogistics.getInsDt(),
				omsOrderProduct.getOmsOrder().getName1(),
				omsOrderProduct.getOmsOrder().getPhone2(),
				omsOrderProduct.getOmsDeliveryaddress().getName1(),
				omsOrderProduct.getOmsDeliveryaddress().getPhone1(),
				omsOrderProduct.getOmsDeliveryaddress().getPhone2(),
				omsOrderProduct.getOmsDeliveryaddress().getZipCd(),
				omsOrderProduct.getOmsDeliveryaddress().getDeliveryAddress(),
				omsLogistics.getInvoiceNo(),
				omsLogistics.getDeliveryServiceCd(),
				omsOrderProduct.getBusinessName(),
				omsOrderProduct.getProductId(),
				omsOrderProduct.getProductName(),
				omsOrderProduct.getSaleproductName(),
				omsLogistics.getOutReserveQty(),
				omsLogistics.getOutQty() == null ? "" : String.valueOf(omsLogistics.getOutQty()),
				omsLogistics.getCancelDeliveryReasonCd(),
				omsLogistics.getOrderId(),
				omsLogistics.getOrderProductNo() == null ? "" : String.valueOf(omsLogistics.getOrderProductNo())
			};
			
			dataList.add(data);
		}
		return returnView(dataType, headers, dataList, fileName);
	}
	
	/**
	 * 
	 * @Method Name : getReturnConfirmListExcel
	 * @author : brad
	 * @date : 2016. 9. 19.
	 * @description : 반품확인 리스트 엑셀 다운로드
	 * @param omsOrderSearch
	 * @throws Exception 
	 */
	@RequestMapping(value = "/oms/logistics/return", method = { RequestMethod.GET })
	public ModelAndView getReturnConfirmListExcel(OmsLogisticsSearch omsLogisticsSearch) throws Exception {

		// 상품 목록 조회
		List<OmsLogistics> list = new ArrayList<OmsLogistics>();
		omsLogisticsSearch.setStoreId(SessionUtil.getStoreId());
		list = logisticsService.getReturnConfirmList(omsLogisticsSearch);

		List<String[]> dataList = new ArrayList<String[]>();

		// 헤더
		String[] headers = { "주문일", "주문번호", "클레임접수일", "클레임번호", "사이트", "주문자명", "주문자휴대폰번호", "발송인명", "발송인전화번호", "발송인휴대폰번호", "우편번호", "발송주소",
				"상품번호", "상품명", "단품번호", "단품명", "반품수량", "로케이션명", "입고상태", "정상수량", "불량수량", "가상입고수량", "제휴주문번호", "LP_NO" };
		// 파일명
		String fileName = "반품확인_" + DateUtil.getCurrentDate(DateUtil.FORMAT_3);
		// 리스트 데이터
		for (OmsLogistics omsLogistics : list) {
			
			OmsOrderproduct omsOrderProduct = omsLogistics.getOmsOrderproduct();
			OmsClaimproduct omsClaimproduct = omsLogistics.getOmsClaimproduct();
			
			String[] data = { 
				omsOrderProduct.getOrderDate(),
				omsLogistics.getOrderId(),
				omsClaimproduct.getOmsClaim().getAcceptDt(),
				omsLogistics.getClaimNo() == null ? "" : String.valueOf(omsLogistics.getClaimNo()),
				omsOrderProduct.getSiteName(),
				omsOrderProduct.getOmsOrder().getName1(),
				omsOrderProduct.getOmsOrder().getPhone2(),
				omsClaimproduct.getOmsClaim().getOmsClaimdelivery().getReturnName(),
				omsClaimproduct.getOmsClaim().getOmsClaimdelivery().getReturnPhone1(),
				omsClaimproduct.getOmsClaim().getOmsClaimdelivery().getReturnPhone2(),
				omsClaimproduct.getOmsClaim().getOmsClaimdelivery().getReturnZipCd(),
				omsClaimproduct.getOmsClaim().getOmsClaimdelivery().getDeliveryAddress(),
				omsOrderProduct.getProductId(),
				omsOrderProduct.getProductName(),
				omsOrderProduct.getSaleproductId(),
				omsOrderProduct.getSaleproductName(),
				omsLogistics.getInReserveQty(),
				omsOrderProduct.getLocationId(),
				omsLogistics.getLogisticsStateName(),
				omsLogistics.getGoodInQty() == null ? "" : String.valueOf(omsLogistics.getGoodInQty()),
				omsLogistics.getBadInQty() == null ? "" : String.valueOf(omsLogistics.getBadInQty()),
				omsLogistics.getVirtualInQty() == null ? "" : String.valueOf(omsLogistics.getVirtualInQty()),
				omsOrderProduct.getOmsOrder().getSiteOrderId(),
				omsOrderProduct.getLpNo()
			};
			
			dataList.add(data);
		}
		return returnView(null, headers, dataList, fileName);
	}
	
	/**
	 * 
	 * @Method Name : getPartnerReturnConfirmListExcel
	 * @author : brad
	 * @date : 2016. 9. 19.
	 * @description : 반품확인_협력사 리스트 엑셀 다운로드
	 * @param omsOrderSearch
	 * @throws Exception 
	 */
	@RequestMapping(value = "/oms/logistics/partner/return", method = { RequestMethod.GET })
	public ModelAndView getPartnerReturnConfirmListExcel(OmsLogisticsSearch omsLogisticsSearch) throws Exception {

		// 상품 목록 조회
		List<OmsLogistics> list = new ArrayList<OmsLogistics>();
		omsLogisticsSearch.setStoreId(SessionUtil.getStoreId());
		list = logisticsService.getReturnConfirmList(omsLogisticsSearch);

		List<String[]> dataList = new ArrayList<String[]>();

		// 헤더
		String[] headers = { "주문일", "주문번호", "클레임접수일", "클레임번호", "주문자명", "주문자휴대폰번호", "발송인명", "발송인전화번호", "발송인휴대폰번호", "우편번호", "발송주소",
				"상품번호", "상품명", "단품명", "반품예정수량", "입고수량", "클레임사유" };
		// 파일명
		String fileName = "반품확인협력사_" + DateUtil.getCurrentDate(DateUtil.FORMAT_3);
		// 리스트 데이터
		for (OmsLogistics omsLogistics : list) {
			
			OmsOrderproduct omsOrderProduct = omsLogistics.getOmsOrderproduct();
			OmsClaimproduct omsClaimproduct = omsLogistics.getOmsClaimproduct();
			
			String[] data = { 
				omsOrderProduct.getOrderDate(),
				omsLogistics.getOrderId(),
				omsClaimproduct.getOmsClaim().getAcceptDt(),
				omsLogistics.getClaimNo() == null ? "" : String.valueOf(omsLogistics.getClaimNo()),
				omsOrderProduct.getOmsOrder().getName1(),
				omsOrderProduct.getOmsOrder().getPhone2(),
				omsClaimproduct.getOmsClaim().getOmsClaimdelivery().getReturnName(),
				omsClaimproduct.getOmsClaim().getOmsClaimdelivery().getReturnPhone1(),
				omsClaimproduct.getOmsClaim().getOmsClaimdelivery().getReturnPhone2(),
				omsClaimproduct.getOmsClaim().getOmsClaimdelivery().getReturnZipCd(),
				omsClaimproduct.getOmsClaim().getOmsClaimdelivery().getDeliveryAddress(),
				omsOrderProduct.getProductId(),
				omsOrderProduct.getProductName(),
				omsOrderProduct.getSaleproductName(),
				omsLogistics.getInReserveQty(),
				omsLogistics.getGoodInQty() == null ? "" : String.valueOf(omsLogistics.getGoodInQty()),
				omsClaimproduct.getClaimReasonName()
			};
			
			dataList.add(data);
		}
		return returnView(null ,headers, dataList, fileName);
	}
	
	/**
	 * 
	 * @Method Name : getLocationListExcel
	 * @author : brad
	 * @date : 2016. 9. 19.
	 * @description : 로케이션관리 엑셀 다운로드
	 * @param omsOrderSearch
	 * @throws Exception 
	 */
	@RequestMapping(value = "/oms/logistics/location", method = { RequestMethod.GET })
	public ModelAndView getLocationListExcel(OmsLogisticsSearch omsLogisticsSearch) throws Exception {

		// 상품 목록 조회
		List<PmsWarehouselocation> list = new ArrayList<PmsWarehouselocation>();
		omsLogisticsSearch.setStoreId(SessionUtil.getStoreId());
		list = logisticsService.getLocationList(omsLogisticsSearch);

		List<String[]> dataList = new ArrayList<String[]>();

		// 헤더
		String[] headers = { "창고명", "로케이션명", "로케이션사용여부", "등록자", "등록일시", "최종수정자", "최종수정일시" };
		// 파일명
		String fileName = "로케이션관리_" + DateUtil.getCurrentDate(DateUtil.FORMAT_3);
		// 리스트 데이터
		for (PmsWarehouselocation pmsWarehouselocation : list) {
			
			String locationUseYn = BaseConstants.YN_Y.equals(pmsWarehouselocation.getLocationUseYn()) ? "사용" : "미사용";
			
			String[] data = { 
				pmsWarehouselocation.getWarehouseName(),
				pmsWarehouselocation.getLocationId(),
				locationUseYn,
				pmsWarehouselocation.getInsId(),
				pmsWarehouselocation.getInsDt(),
				pmsWarehouselocation.getUpdId(),
				pmsWarehouselocation.getUpdDt()
			};
			
			dataList.add(data);
		}
		return returnView(null, headers, dataList, fileName);
	}
	
	/**
	 * 
	 * @Method Name : getLocationListExcel
	 * @author : brad
	 * @date : 2016. 9. 19.
	 * @description : 상품 로케이션 엑셀 다운로드
	 * @param omsOrderSearch
	 * @throws Exception 
	 */
	@RequestMapping(value = "/oms/logistics/location/mapping", method = { RequestMethod.GET })
	public ModelAndView getLocationMappingListExcel(OmsLogisticsSearch omsLogisticsSearch) throws Exception {

		// 상품 목록 조회
		List<PmsSaleproduct> list = new ArrayList<PmsSaleproduct>();
		omsLogisticsSearch.setStoreId(SessionUtil.getStoreId());
		list = logisticsService.getLocationMappingList(omsLogisticsSearch);

		List<String[]> dataList = new ArrayList<String[]>();

		// 헤더
		String[] headers = { "공급업체", "상품번호", "상품명", "단품명", "ERP바코드", "로케이션명", "로케이션사용여부", "배송제한수량", "등록자", "등록일시", "최종수정자", "최종수정일시" };

		// 파일명
		String fileName = "상품로케이션매핑_" + DateUtil.getCurrentDate(DateUtil.FORMAT_3);
		// 리스트 데이터
		for (PmsSaleproduct pmsSaleproduct : list) {
			
			String locationUseYn = "";
			if(CommonUtil.isNotEmpty(pmsSaleproduct.getPmsWarehouselocation())){
				locationUseYn = BaseConstants.YN_Y.equals(pmsSaleproduct.getPmsWarehouselocation().getLocationUseYn()) ? "사용" : "미사용";
			}
			
			String[] data = { 
				pmsSaleproduct.getBusinessName(),
				pmsSaleproduct.getPmsProduct().getProductId(),
				pmsSaleproduct.getPmsProduct().getName(),
				pmsSaleproduct.getName(),
				pmsSaleproduct.getErpSaleproductId(),
				pmsSaleproduct.getLocationId(),
				locationUseYn,
				pmsSaleproduct.getDeliveryTogetherQty() == null ? "" : String.valueOf(pmsSaleproduct.getDeliveryTogetherQty()),
				pmsSaleproduct.getInsId(),
				pmsSaleproduct.getInsDt(),
				pmsSaleproduct.getUpdId(),
				pmsSaleproduct.getUpdDt()
			};
			
			dataList.add(data);
		}
		return returnView(null, headers, dataList, fileName);
	}
	
	/**
	 * 
	 * @Method Name : getPoProductListExcel
	 * @author : roy
	 * @date : 2016. 10. 16.
	 * @description : 품절 임박 상품 목록  엑셀 다운로드
	 * @param pmsProductSearch
	 * @throws Exception 
	 */
	@RequestMapping(value = "/ccs/main/product", method = { RequestMethod.GET })
	public ModelAndView getPoProductListExcel(PmsProductSearch pmsProductSearch) throws Exception {

		// 상품 목록 조회
		
		pmsProductSearch.setStoreId(SessionUtil.getStoreId());
		if (CommonUtil.isNotEmpty(BoSessionUtil.getBusinessId())) {
			pmsProductSearch.setBusinessId(BoSessionUtil.getBusinessId());
		}
		List<PmsProduct> list = mainService.getProductList(pmsProductSearch);
		
		List<String[]> dataList = new ArrayList<String[]>();

		// 헤더
		String[] headers = { "상품번호", "상품명", "단품명", "표준카테고리", "재고수량" };

		// 파일명
		String fileName = "품절임박상품_" + DateUtil.getCurrentDate(DateUtil.FORMAT_3);
         
		// 리스트 데이터
		for (PmsProduct pmsProduct : list) {
			
			String[] data = { 
					pmsProduct.getProductId(), pmsProduct.getName(), pmsProduct.getPmsSaleproduct().getName(),
					pmsProduct.getPmsCategory().getName(), pmsProduct.getPmsSaleproduct().getRealStockQty() == null ? ""
							: String.valueOf(pmsProduct.getPmsSaleproduct().getRealStockQty())
			};
			
			dataList.add(data);
		}
		return returnView(null, headers, dataList, fileName);
	}

	@RequestMapping(value = "/pms/attribute", method = { RequestMethod.GET })
	public ModelAndView getAttributeListExcel(PmsAttributeSearch search) throws Exception {
		//외부비노출정보 조회
		search.setStoreId(SessionUtil.getStoreId());
		List<PmsAttribute> attrList = attrService.getAttributeList(search);

		//엑셀저장 데이터
		List<String[]> dataList = new ArrayList<String[]>();

		//헤더
		String[] headers = { "속성번호", "속성유형", "속성명", "사용여부", "등록자", "등록일", "최종수정자", "최종수정일" };

		//파일명
		String fileName = "속성목록_" + DateUtil.getCurrentDate(DateUtil.FORMAT_3);

		//데이터처리
		for (PmsAttribute attr : attrList) {
			String useYn = BaseConstants.YN_Y.equals(attr.getUseYn()) ? "사용" : "미사용";
			String[] data = { attr.getAttributeId(), attr.getAttributeTypeName(), attr.getName(), useYn, attr.getInsName(),
					attr.getInsDt(), attr.getUpdName(), attr.getUpdDt() };
			dataList.add(data);
		}

		return returnView(null, headers, dataList, fileName);
	}

	@RequestMapping(value = "/pms/epexcproduct", method = { RequestMethod.GET })
	public ModelAndView getEpexcproductListExcel(PmsEpexcproductSearch search) throws Exception {
		//외부비노출정보 조회
		search.setStoreId(SessionUtil.getStoreId());
		List<PmsEpexcproduct> epexcList = epexcService.getEpexcproductList(search);

		//엑셀저장 데이터
		List<String[]> dataList = new ArrayList<String[]>();

		//헤더
		String[] headers = { "구분", "공급업체", "상품번호", "상품명", "판매상태", "상품유형", "등록자", "등록일", "최종수정자", "최종수정일" };

		//파일명
		String fileName = "외부비노출목록_" + DateUtil.getCurrentDate(DateUtil.FORMAT_3);

		//데이터처리
		for (PmsEpexcproduct epexc : epexcList) {
			String[] data = { epexc.getExcProductTypeName(), epexc.getBusinessInfo(), epexc.getProductId(),
					epexc.getPmsProduct().getName(), epexc.getPmsProduct().getSaleStateName(),
					epexc.getPmsProduct().getProductTypeName(), epexc.getInsName(), epexc.getInsDt(), epexc.getUpdName(),
					epexc.getUpdDt() };
			dataList.add(data);
		}

		return returnView(null, headers, dataList, fileName);
	}

	@RequestMapping(value = "/pms/sendgoods", method = { RequestMethod.GET })
	public ModelAndView getSendgoodsListExcel(PmsSendgoodsSearch search) throws Exception {
		//사방넷 등록용 상품 조회
		search.setStoreId(SessionUtil.getStoreId());
		List<PmsProduct> sendgoodsList = sendgoodsService.getSendgoodsList(search);

		//엑셀저장 데이터
		List<String[]> dataList = new ArrayList<String[]>();

		//헤더
		String[] headers = { "상품번호", "상품명", "업체정보", "판매상태", "상품유형", "전송여부", "상품등록일", "상품전송일", "제조사명", "세금구분", "ERP상품ID", "정상가",
				"판매가", "공급가", "등록자", "등록일", "최종수정자", "최종수정일" };

		//파일명
		String fileName = "사방넷상품목록_" + DateUtil.getCurrentDate(DateUtil.FORMAT_3);

		//데이터처리
		for (PmsProduct goods : sendgoodsList) {
			String sendYn = BaseConstants.YN_Y.equals(goods.getOutSendYn()) ? "전송" : "미전송";
			String[] data = { goods.getProductId(), goods.getName(), goods.getBusinessInfo(), goods.getSaleStateName(),
					goods.getProductTypeName(), sendYn, goods.getInsDt(), goods.getOutSendDt(), goods.getMaker(),
					goods.getTaxTypeName(), goods.getErpProductId(), goods.getListPrice().toString(),
					goods.getSalePrice().toString(), goods.getSupplyPrice().toString(), goods.getInsName(), goods.getInsDt(),
					goods.getUpdName(), goods.getUpdDt() };
			dataList.add(data);
		}

		return returnView(null, headers, dataList, fileName);
	}

	@RequestMapping(value = "/oms/termorder", method = { RequestMethod.GET })
	public ModelAndView getTermorderListExcel(OmsTermorderSearch search) throws Exception {
		//사방넷 주문미처리내역 조회
		search.setStoreId(SessionUtil.getStoreId());
		List<OmsReceiveordermapping> orderList = termOrderService.getUnHandleReceiveOrderList(search);

		//엑셀저장 데이터
		List<String[]> dataList = new ArrayList<String[]>();

		//헤더
		String[] headers = { "사이트주문번호", "사이트주문일시", "사이트", "주문자명", "수취인명", "수취인전화번호", "수취인휴대폰번호", "수취인우편번호", "수취인주소", "사이트상품명",
				"사이트옵션명", "고객사상품코드", "단품번호", "배송메시지", "사은품코드", "BO_YN", "오류메시지" };

		//파일명
		String fileName = "사방넷주문미처리_" + DateUtil.getCurrentDate(DateUtil.FORMAT_3);

		//데이터처리
		for (OmsReceiveordermapping order : orderList) {
			String[] data = { order.getOrderId(), order.getOrderDate(), order.getSiteName(), order.getUserName(),
					order.getReceiveName(), order.getReceiveTel(), order.getReceiveCel(), order.getReceiveZipcode(),
					order.getReceiveAddr(), order.getProductName(), order.getSkuValue(), order.getProductId(),
					order.getSkuAlias(), order.getDelvMsg(), order.getOrderEtc2(), order.getBoYn(), order.getBoMsg() };
			dataList.add(data);
		}

		return returnView(null, headers, dataList, fileName);
	}

	@RequestMapping(value = "/oms/settle", method = { RequestMethod.GET })
	public ModelAndView getSettleListExcel(OmsSettleSearch search) throws Exception {
		//정산목록 조회
		List<OmsSettle> settleList = settleService.getSettleList(search);

		//엑셀저장 데이터
		List<String[]> dataList = new ArrayList<String[]>();

		String[] dataType = { "텍스트", "텍스트", "텍스트", "숫자", "텍스트", "텍스트", "숫자", "텍스트", "숫자", "텍스트", "숫자", "숫자", "숫자", "숫자", "퍼센트",
				"숫자", "숫자", "숫자", "숫자", "숫자", "숫자", "숫자", "숫자", "숫자", "숫자", "텍스트", "텍스트", "텍스트", "텍스트", "텍스트", "텍스트", "텍스트",
				"텍스트" };

		//헤더
		String[] headers = { "주문구분", "매출기준일", "주문일", "주문번호", "정산방법", "공급사명", "상품번호", "상품명", "단품번호", "단품명", "공급단가", "판매단가", "공급금액",
				"판매금액", "수수료율", "고객결제금액", "사입회계매출", "위탁회계매출", "매일포인트", "예치금사용액", "자사쿠폰금액", "업체쿠폰금액", "플러스쿠폰금액", "장바구니쿠폰금액",
				"수량", "사이트명", "표준카테고리", "과세구분", "클레임유형명", "클레임번호", "출고완료일", "입고완료일", "클레임완료일" };

		//파일명
		String fileName = "매출원장_" + DateUtil.getCurrentDate(DateUtil.FORMAT_3);

		//데이터처리
		for (OmsSettle settle : settleList) {
			String[] data = { settle.getOrderType(), settle.getSaleStandardDt(), settle.getOrderDt(), settle.getOrderId(),
					settle.getSettleType(), settle.getBusinessName(), settle.getProductId(), settle.getProductName(),
					settle.getSaleproductId(), settle.getSaleproductName(),
					bigDecimalToString(settle.getSupplyPrice()), bigDecimalToString(settle.getSalePrice()),
					bigDecimalToString(settle.getSupplyAmt()), bigDecimalToString(settle.getSaleAmt()),
					settle.getStrCommissionRate(), bigDecimalToString(settle.getPaymentAmt()),
					bigDecimalToString(settle.getPurchaseSales()), bigDecimalToString(settle.getConsignSales()),
					bigDecimalToString(settle.getMaeilPoint()), bigDecimalToString(settle.getDepositAmt()),
					bigDecimalToString(settle.getOwnCouponAmt()), bigDecimalToString(settle.getBizCouponAmt()),
					bigDecimalToString(settle.getPlusCouponAmt()),
					String.valueOf(settle.getBasketCouponAmt()), String.valueOf(settle.getQty()), settle.getSiteName(),
					settle.getCategoryName(), settle.getTaxTypeName(), settle.getClaimTypeName(), settle.getClaimNo(),
					settle.getShipDt(), settle.getReturnDt(), settle.getClaimDt() };
			dataList.add(data);
		}

		return returnView(dataType, headers, dataList, fileName);
	}

	@RequestMapping(value = "/oms/settlebiz", method = { RequestMethod.GET })
	public ModelAndView getSettleBizListExcel(OmsSettleSearch search) throws Exception {
		//정산목록 조회
		List<OmsSettleBiz> bizList = settleBizService.getSettleBizList(search);

		//엑셀저장 데이터
		List<String[]> dataList = new ArrayList<String[]>();

		//헤더
		String[] headers = { "공급업체명", "최종판매액(합계)", "최종판매액(신용카드)", "최종판매액(현금)", "최종판매액(휴대폰)", "최종판매액(기타)", "자사쿠폰금액", "업체쿠폰금액",
				"위탁판매수수료", "최종지급액" };

		//파일명
		String fileName = "업체정산내역_" + DateUtil.getCurrentDate(DateUtil.FORMAT_3);

		//데이터처리
		for (OmsSettleBiz bizOne : bizList) {
			String[] data = { bizOne.getBusinessName(), CommonUtil.formatMoney(bizOne.getFinalSaleAmt().intValue()),
					CommonUtil.formatMoney(bizOne.getFinalCardAmt().intValue()),
					CommonUtil.formatMoney(bizOne.getFinalCashAmt().intValue()),
					CommonUtil.formatMoney(bizOne.getFinalMobileAmt().intValue()),
					CommonUtil.formatMoney(bizOne.getFinalEtcAmt().intValue()),
					CommonUtil.formatMoney(bizOne.getFinalOwnCouponAmt().intValue()),
					CommonUtil.formatMoney(bizOne.getFinalBizCouponAmt().intValue()),
					CommonUtil.formatMoney(bizOne.getFinalSaleChargeAmt().intValue()),
					CommonUtil.formatMoney(bizOne.getFinalSupportAmt().intValue()) };
			dataList.add(data);
		}

		return returnView(null, headers, dataList, fileName);
	}

	@RequestMapping(value = "/oms/settlebiz/detail", method = { RequestMethod.GET })
	public ModelAndView getSettleBizDetailListExcel(OmsSettleSearch search) throws Exception {
		//정산목록 조회
		List<OmsSettle> bizDetaillist = settleBizService.getSettleBizDetailList(search);

		//엑셀저장 데이터
		List<String[]> dataList = new ArrayList<String[]>();

		//헤더
		String[] headers = { "매출기준일", "주문일", "주문번호", "공급사명", "상품번호", "상품명", "단품번호", "단품명", "공급단가", "판매단가", "공급금액", "판매금액", "수수료율",
				"수수료", "결제수단", "고객결제금액", "기타", "자사쿠폰금액", "업체쿠폰금액", "지급액", "수량", "표준카테고리", "과세구분", "클레임유형명", "클레임번호" };

		//파일명
		String fileName = "업체정산내역상세_" + DateUtil.getCurrentDate(DateUtil.FORMAT_3);

		//데이터처리
		for (OmsSettle settle : bizDetaillist) {
			String[] data = { settle.getSaleStandardDt(), settle.getOrderDt(), settle.getOrderId(), settle.getBusinessName(),
					settle.getProductId(), settle.getProductName(), settle.getSaleproductId(), settle.getSaleproductName(),
					CommonUtil.formatMoney(settle.getSupplyPrice().intValue()),
					CommonUtil.formatMoney(settle.getSalePrice().intValue()),
					CommonUtil.formatMoney(settle.getSupplyAmt().intValue()),
					CommonUtil.formatMoney(settle.getSaleAmt().intValue()), settle.getStrCommissionRate(),
					CommonUtil.formatMoney(settle.getChargeAmt().intValue()), settle.getPaymentMethodName(),
					CommonUtil.formatMoney(settle.getPaymentAmt().intValue()),
					CommonUtil.formatMoney(settle.getEtcAmt().intValue()),
					CommonUtil.formatMoney(settle.getOwnCouponAmt().intValue()),
					CommonUtil.formatMoney(settle.getBizCouponAmt().intValue()),
					CommonUtil.formatMoney(settle.getSupportAmt().intValue()), CommonUtil.formatMoney(settle.getQty().intValue()),
					settle.getCategoryName(),
					settle.getTaxTypeName(), settle.getClaimTypeName(), settle.getClaimNo() };
			dataList.add(data);
		}

		return returnView(null, headers, dataList, fileName);
	}

	@RequestMapping(value = "/oms/pgsettle", method = { RequestMethod.GET })
	public ModelAndView getPgSettleListExcel(OmsPgSettleSearch search) throws Exception {
		//정산목록 조회
		List<OmsPaymentsettle> pgSettleList = pgSettleService.getPgSettleList(search);

		//엑셀저장 데이터
		List<String[]> dataList = new ArrayList<String[]>();

		//헤더
		String[] headers = { "PG사", "승인번호", "승인일시", "주문번호", "주문일시", "PG 상점ID", "결제수단", "결제금액", "PG사 매입금액", "PG사 입금예정금액",
				"PG사 입금예정일", "오차금액" };

		//파일명
		String fileName = "PG사승인대사_" + DateUtil.getCurrentDate(DateUtil.FORMAT_3);

		//데이터처리
		for (OmsPaymentsettle pgOne : pgSettleList) {
			String[] data = { pgOne.getPgCompany(), pgOne.getPgApprovalNo(), pgOne.getPaymentDt(), pgOne.getOrderId(),
					pgOne.getOrderDt(), pgOne.getPgShopId(), pgOne.getPaymentMethodName(),
					CommonUtil.formatMoney(pgOne.getApprovalAmt().intValue()),
					CommonUtil.formatMoney(pgOne.getPaymentAmt().intValue()),
					CommonUtil.formatMoney(pgOne.getDepositAmt().intValue()), pgOne.getSettleDt(),
					CommonUtil.formatMoney(pgOne.getErrorAmt().intValue()) };
			dataList.add(data);
		}

		return returnView(null, headers, dataList, fileName);
	}

	@RequestMapping(value = "/mms/history", method = { RequestMethod.GET })
	public ModelAndView getReadHistoryListExcel(MmsMemberSearch mmsMemberSearch) throws Exception {
		// 기획전 목록 조회
		List<MmsReadhistory> list = new ArrayList<MmsReadhistory>();
		mmsMemberSearch.setStoreId(SessionUtil.getStoreId());
		list = historyService.getReadHistorylist(mmsMemberSearch);

		List<String[]> dataList = new ArrayList<String[]>();

		// 헤더
		String[] headers = { "사용자명", "조회내용", "조회일자" };

		// 리스트 데이터
		for (MmsReadhistory mmsReadhistory : list) {

			String[] data = { mmsReadhistory.getUserId(), mmsReadhistory.getDetail(), mmsReadhistory.getInsDt()

			};

			dataList.add(data);
		}

		return returnView(headers, dataList);
	}

}