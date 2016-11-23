
package gcp.admin.controller.rest;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import gcp.ccs.model.CcsCode;
import gcp.ccs.model.CcsCodegroup;
import gcp.ccs.model.CcsField;
import gcp.ccs.model.base.BaseCcsField;
import gcp.ccs.service.BaseService;
import gcp.dms.model.DmsDisplay;
import gcp.dms.model.search.DmsDisplaySearch;
import gcp.mms.model.base.BaseMmsMember;
import gcp.pms.model.PmsBrand;
import gcp.sample.model.Sample;
import gcp.sample.service.SampleService;
import gcp.sps.model.base.BaseSpsCouponissue;
import gcp.table.model.TableSample;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.controller.BaseController;
import intune.gsf.model.BaseEntity;

/**
 * 
 * @Pagckage Name : gcp.admin.controller
 * @FileName : SampleController.java
 * @author : dennis
 * @date : 2016. 4. 19.
 * @description : Sample Controller
 */
//@RestController
@Controller
@RequestMapping("/api/sample")
public class SampleController extends BaseController{

	private final Log		logger	= LogFactory.getLog(getClass());

	@Autowired
	private SampleService sampleService;

	@Autowired
	private BaseService		baseService;

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String test() throws Exception {
//		List<Sample> list = sampleService.getSampleList();
		return "test";
	}

	/**
	 * 
	 * @Method Name : listAllSamples
	 * @author : dennis
	 * @date : 2016. 4. 19.
	 * @description : Sample 목록 조회
	 *
	 * @return List<Sample>
	 * @throws Exception
	 */
	@RequestMapping(value = "/sample", method = RequestMethod.GET)
	public List<Sample> listAllSamples() throws Exception {
		List<Sample> list = sampleService.getSampleList();
		return list;
	}

	/**
	 * 
	 * @Method Name : getSample
	 * @author : dennis
	 * @date : 2016. 4. 19.
	 * @description : Sample id에 해당하는 Sample 조회
	 *
	 * @param id
	 * @return Sample
	 * @throws Exception
	 */
	@RequestMapping(value = "/sample/{id}", method = RequestMethod.GET)
	public TableSample getSample(@PathVariable("id") String id) {


		TableSample sample = null;
		try {

			sample = sampleService.getSample(id);

		} catch (ServiceException se) {
			sample = new TableSample();
			sample.setSuccess(false);
			sample.setResultCode(se.getMessageCd());
			sample.setResultMessage(se.getMessage());
		}
		return sample;
	}

	/**
	 * 
	 * @Method Name : getSample
	 * @author : dennis
	 * @date : 2016. 4. 19.
	 * @description : Sample id에 해당하는 Sample 조회
	 *
	 * @param id
	 * @return Sample
	 * @throws Exception
	 */
	@RequestMapping(value = "/sample2/{id}", method = RequestMethod.GET)
	public TableSample getSample2(@PathVariable("id") String id) {

		TableSample sample = sampleService.getSample2(id);

		return sample;
	}
	
	/**
	 * 
	 * @Method Name : getSample
	 * @author : dennis
	 * @date : 2016. 4. 19.
	 * @description : Sample id에 해당하는 Sample 조회
	 *
	 * @param id
	 * @return Sample
	 * @throws Exception
	 */
	@RequestMapping(value = "/sample3/{id}", method = RequestMethod.GET)
	public BaseEntity getSample3(@PathVariable("id") String id) {

		BaseEntity base = null;
		try {

			sampleService.getSample(id);

		} catch (ServiceException se) {
			base = new TableSample();
			base.setSuccess(false);
			base.setResultCode(se.getMessageCd());
			base.setResultMessage(se.getMessage());
		}
		return base;
	}

	/**
	 * 
	 * @Method Name : createSample
	 * @author : dennis
	 * @date : 2016. 4. 19.
	 * @description : Sample 등록
	 *
	 * @param sample
	 * @throws Exception
	 */
	@RequestMapping(value = "/sample", method = RequestMethod.POST, produces = "text/plain")
	public String createSample(@RequestBody TableSample sample) throws Exception {


//		display.setStoreId(BaseConstants.DEFAULT_STORE_ID);

		sampleService.createSample(sample);
		//baseService.insertOneTable(sample);
		//sampleService.createSample(sample);
//		baseService.insertOneTable(t);

		//sampleService.createSample(sample);		
		baseService.insertOneTable(sample);

		
		return "success";
	}

//	@RequestMapping(value = "/corner", method = { RequestMethod.POST, RequestMethod.GET }, produces = "text/plain")
//	public String displayPost(@RequestBody BaseDmsDisplay display) throws Exception {
//
//		display.setStoreId(BaseConstants.DEFAULT_STORE_ID);
//		baseService.insert("display.insertCorner", display);
//		return display.getDisplayId();
//	}

	/**
	 * 
	 * @Method Name : updateSample
	 * @author : dennis
	 * @date : 2016. 4. 19.
	 * @description : Sample 수정
	 *
	 * @param id
	 * @param sample
	 * @throws Exception
	 */
	@RequestMapping(value = "/sample/{id}", method = RequestMethod.PUT)
	public void updateSample(@PathVariable("id") String id, @RequestBody TableSample sample) throws Exception {
//		sampleService.updateSample(sample);

		baseService.updateOneTable(sample);
	}

	/**
	 * 
	 * @Method Name : deleteSample
	 * @author : dennis
	 * @date : 2016. 4. 19.
	 * @description : Sample 삭제
	 *
	 * @param id
	 * @throws Exception
	 */
	@RequestMapping(value = "/sample/{id}", method = RequestMethod.DELETE)
	public void deleteSample(@PathVariable("id") String id) throws Exception {
		TableSample sample = new TableSample();
		sample.setSampleId(id);
		baseService.deleteOneTable(sample);
	}

	public static void main(String args[]) {
		int price = 11112222;

		int price2 = 500 / 100 * 100;
		System.out.println(price2);
	}


	@RequestMapping(value = "/table/productinsert", method = RequestMethod.GET)
	public void productInsert() throws Exception {

		// 상품 등록 : 상품, 단품, 단품가격
		//295 * 7 
		for (int i = 15015; i < 16000; i++) {
			sampleService.insertProduct(i);
		}
	}

	@RequestMapping(value = "/table/cornerinsert", method = RequestMethod.GET)
	public void cornerinsert() throws Exception {

		DmsDisplay dms = new DmsDisplay();
		//dms.setUpperDisplayId("root");		
		dms.setDisplayTypeCd("DISPLAY_TYPE_CD.COMMON");
		dms.setDisplayItemTypeCd("DISPLAY_ITEM_TYPE_CD.PRODUCT");
		dms.setDisplayId("root");
		dms.setName("root");
		dms.setLeafYn("N");
		dms.setDisplayYn("N");
		dms.setUseYn("Y");
		baseService.insertOneTable(dms);


		dms.setUpperDisplayId("root");
		dms.setDisplayId("0001");
		dms.setName("대카_1");
		dms.setLeafYn("N");
		dms.setDisplayYn("Y");
		baseService.insertOneTable(dms);

		dms.setUpperDisplayId("root");
		dms.setDisplayId("0002");
		dms.setName("대카_2");
		dms.setLeafYn("Y");
		baseService.insertOneTable(dms);

		dms.setUpperDisplayId("0001");
		dms.setDisplayId("0003");
		dms.setName("중카_1_1");
		dms.setLeafYn("N");
		baseService.insertOneTable(dms);

		dms.setUpperDisplayId("0003");
		dms.setDisplayId("0004");
		dms.setName("소카_1_1_1");
		dms.setLeafYn("Y");
		baseService.insertOneTable(dms);

		dms.setUpperDisplayId("0003");
		dms.setDisplayId("0005");
		dms.setName("소카_1_1_2");
		dms.setLeafYn("Y");
		baseService.insertOneTable(dms);

		dms.setUpperDisplayId("0001");
		dms.setDisplayId("0006");
		dms.setName("중카_1_2");
		dms.setLeafYn("Y");
		baseService.insertOneTable(dms);

		dms.setUpperDisplayId("0001");
		dms.setDisplayId("0007");
		dms.setName("중카_1_3");
		dms.setLeafYn("Y");
		baseService.insertOneTable(dms);
	}

	/**
	 * 
	 * @Method Name : selectTest
	 * @author : Administrator
	 * @date : 2016. 4. 21.
	 * @description :
	 *
	 * @throws Exception
	 */
	@RequestMapping(value = "/table/insert", method = RequestMethod.GET)
	public void tableInsert() throws Exception {
//
//		for (int i = 2; i < 500; i++) {
//			inseretProduct(i);
//		}

//		BasePmsCategory cate = new BasePmsCategory();
//		cate.setStoreId(BaseConstants.DEFAULT_STORE_ID);
//		cate.setCategoryId("00001");
//		cate.setName("test category 1");
//		cate.setLeafYn("Y");
//		cate.setUseYn("Y");
//		baseService.insertOneTable(cate);


		CcsField cf = new CcsField();
		try {

			try {
				//CCS_FIELD INSERT ( extends 객체로 등록, pk 입력)

				cf.setFieldCd("pmsBrand.sortNo");
				cf.setTableName("PMS_BRAND");
				cf.setColumnName("SORT_NO");
				cf.setFieldTypeCd("01");
				cf.setDataType("01");
				cf.setRequiredYn("Y");
				cf.setLength(BigDecimal.valueOf(3));
				cf.setFormat("/[0-9]/");
				cf.setPkYn("N");
				baseService.insertOneTable(cf);

			} catch (Exception e) {
				throw new ServiceException("SPS.COUPON.REG.INFO", e);
			}
		} catch (ServiceException se) {
			cf.setResultCode(se.getMessageCd());
			cf.setResultMessage(se.getMessage());

//			logger.error("error message : " + MessageUtil.getMessage("SPS.COUPON.REG.INFO"));

//			logger.error("ResultModel : " + cf.toString());
		}

		
//
//		//MMS_QNA INSERT ( base객체로 등록, pk QNA_NO MAX+1 자동생성)
//		BaseMmsQna qna = new BaseMmsQna();
//		qna.setStoreId("1001");//pk
//		//qna.setQnaNo(qnaNo);//pk, 자동생성
//		qna.setDisplayYn("Y");
//		qna.setMemId("eddie");
//		qna.setQnaTypeCd("01");
//		qna.setQnaStateCd("01");
//		qna.setTitle("title");
//		qna.setExpl("expl");
//		qna.setQnaDt(DateUtil.getCurrnetTimestamp());
//		baseService.insertOneTable(qna);
//
//		//SPS_COUPONISSUE INSERT (날짜 세팅)
//		BaseSpsCouponissue bsp = new BaseSpsCouponissue();
//		bsp.setStoreId("1002");//pk1
//		bsp.setCouponId("test1236");//pk2
//		bsp.setCouponIssueStateCd("01");
//		bsp.setUseEndDt(DateUtil.convertStringToTimestamp("2012-06-03", DateUtil.FORMAT_2));
//		bsp.setMemId("1001");
//		bsp.setUseStartDt(DateUtil.getCurrnetTimestamp());
//		baseService.insertOneTable(bsp);
//
//		CcsChannel cl = new CcsChannel();
//		cl.setChannelId("1002");
//		cl.setStoreId("1001");
//		cl.setChannelTypeCd("CHANNEL_TYPE_CD");
//		cl.setName("name");
//		cl.setUseYn("Y");
//		baseService.insertOneTable(cl);
//
//		BaseMmsMember mb = new BaseMmsMember();
//		mb.setChannelId("1001");
//		mb.setStoreId("1001");
//		mb.setMemId("eddie2");
//		mb.setMemGradeCd("01");
//		mb.setAddress1("address1");
//		mb.setAddress2("address2");
//		mb.setBirth("19790722");
//		mb.setCloseDt(DateUtil.convertStringToTimestamp("2012-06-03", DateUtil.FORMAT_2));
//		mb.setCountryNo("KOR");
//		mb.setDmYn("Y");
//		mb.setEmail("eddie@intune.co.kr");
//		mb.setEmailYn("Y");
//		mb.setGender("M");
//		mb.setMemStateCd("01");
//		mb.setName1("kang eddie2");
//		mb.setRegDt(DateUtil.getCurrnetTimestamp());
//		mb.setPwd("1234");
//		mb.setSmsYn("Y");
//		baseService.insertOneTable(mb);

	}

	@RequestMapping(value = "/table/select", method = RequestMethod.GET)
	public void tableSelect() throws Exception {

		//CCS_FIELD SELECT(base 객체)
		BaseCcsField selectCf1 = new BaseCcsField();
		selectCf1.setFieldCd("");
		BaseCcsField baseCcsField = (BaseCcsField) baseService.selectOneTable(selectCf1);
		logger.debug("BaseCcsField :: " + baseCcsField.toString());

		//CCS_FIELD SELECT(extends 객체)
		CcsField selectCf2 = new CcsField();
//		selectCf2.setMsgCd("12");
		CcsField ccsField = (CcsField) baseService.selectOneTable(selectCf2);
		logger.debug("CcsField :: " + ccsField.toString());

	}

	@RequestMapping(value = "/table/update", method = RequestMethod.GET)
	public void tableUpdate() throws Exception {

		BaseMmsMember mb = new BaseMmsMember();

//		mb.setStoreId("1001");//pk 필수
//		mb.setMemId("eddie2");//pk 필수
//		mb.setChannelId("1001");
//		mb.setMemGradeCd("01");
//		mb.setAddress1(null);			// case1 : update 안됨
//		mb.setAddress2("");				// case2 :  null 로 update
//		//mb.setAddress3("address3");	// case3 :  update 안됨
//		mb.setBirth("19790722");		// case4 : 세팅값으로 update
//		//mb.setCloseDt(DateUtil.convertStringToTimestamp("2012-06-03", DateUtil.FORMAT_2));
//		mb.setCountryNo("USA");
//		mb.setDmYn("Y");
//		mb.setEmail("eddie@intune.co.kr");
//		mb.setEmailYn("Y");
//		mb.setGender("M");
//		mb.setMemStateCd("01");
//		mb.setName1("kang eddie2");
//		mb.setRegDt(DateUtil.getCurrentDate(DateUtil.FORMAT_1));
//		mb.setPwd("1234");
//		mb.setSmsYn("Y");
		baseService.updateOneTable(mb);

	}

	@RequestMapping(value = "/table/delete", method = RequestMethod.GET)
	public void tableDelete() throws Exception {

		//SPS_COUPONISSUE INSERT (날짜 세팅)
		BaseSpsCouponissue bsp = new BaseSpsCouponissue();
		bsp.setStoreId("1002");//pk1
		bsp.setCouponId("test1236");//pk2
		bsp.setCouponIssueNo(BigDecimal.valueOf(1));
		baseService.deleteOneTable(bsp);

	}

//	@RequestMapping(value = "/sample", method = RequestMethod.DELETE)
//	public void deleteAllSamples() {
//		System.out.println("Deleting All Users");
//
//		sampleList = new ArrayList<Sample>();
//
//	}e

	@RequestMapping(value = "/paging", method = RequestMethod.POST)
	public List<DmsDisplaySearch> paging(@RequestBody DmsDisplaySearch dds) throws Exception {

		List<DmsDisplaySearch> list = sampleService.getPagingList(dds);

		return list;
	}

	@RequestMapping(value = "/pagingTotalCnt", method = RequestMethod.POST, produces = "text/plain")
	public String totalCnt(@RequestBody DmsDisplaySearch dds) throws Exception {

		String a = String.valueOf(sampleService.getPageCnt(dds));
		return a;
	}
//	@RequestMapping(value = "/pagingTotalCnt", method = RequestMethod.GET, produces = { "text/plain", "application/*" })
//	public int totalCnt() throws Exception {
//		return sampleService.getPageCnt();
//	}

	@RequestMapping(value = "/table/brandinsert", method = RequestMethod.GET)
	public void brandinsert() throws Exception {
	
		PmsBrand brand = new PmsBrand();
		
		brand.setBrandId("TEST_BRAND_ID_001");
		brand.setName("TEST_BRAND_001");
		brand.setStoreId("10001");
//		brand.setLeafYn("Y");
//		brand.setUseYn("Y");
		baseService.insertOneTable(brand);

	}
	/**
	 * 
	 * @Method Name : selectTest
	 * @author : Administrator
	 * @date : 2016. 4. 21.
	 * @description :
	 *
	 * @throws Exception
	 */
	@RequestMapping(value = "/table/codeinsert", method = RequestMethod.GET)
	public void codeInsert() throws Exception {

		CcsCodegroup cg = new CcsCodegroup();
		cg.setCdGroupCd("SALE_STATE_CD");
		cg.setName("판매상태코드");
		cg.setCdGroupTypeCd("01");
		baseService.insertOneTable(cg);

		CcsCode cd = new CcsCode();
		cd.setCdGroupCd("SALE_STATE_CD");
		cd.setCd("SALE_STATE_CD.SALE");
		cd.setName("판매중");
		cd.setUseYn("Y");
		baseService.insertOneTable(cd);

		cd = new CcsCode();
		cd.setCdGroupCd("SALE_STATE_CD");
		cd.setCd("SALE_STATE_CD.STOP");
		cd.setName("판매중지");
		cd.setUseYn("Y");
		baseService.insertOneTable(cd);

		cd.setCdGroupCd("SALE_STATE_CD");
		cd.setCd("SALE_STATE_CD.SOLDOUT");
		cd.setName("품절");
		cd.setUseYn("Y");
		baseService.insertOneTable(cd);

		cd.setCdGroupCd("SALE_STATE_CD");
		cd.setCd("SALE_STATE_CD.END");
		cd.setName("판매종료");
		cd.setUseYn("Y");
		baseService.insertOneTable(cd);

//상품 유형
		cg = new CcsCodegroup();
		cg.setCdGroupCd("PRODUCT_TYPE_CD");
		cg.setName("상품유형코드");
		cg.setCdGroupTypeCd("01");
		baseService.insertOneTable(cg);

		cd = new CcsCode();
		cd.setCdGroupCd("PRODUCT_TYPE_CD");
		cd.setCd("PRODUCT_TYPE_CD.COM");
		cd.setName("일반상품");
		cd.setUseYn("Y");
		baseService.insertOneTable(cd);

		cd = new CcsCode();
		cd.setCdGroupCd("PRODUCT_TYPE_CD");
		cd.setCd("PRODUCT_TYPE_CD.RSV");
		cd.setName("예약상품");
		cd.setUseYn("Y");
		baseService.insertOneTable(cd);

		cd.setCdGroupCd("PRODUCT_TYPE_CD");
		cd.setCd("PRODUCT_TYPE_CD.SET");
		cd.setName("세트상품");
		cd.setUseYn("Y");
		baseService.insertOneTable(cd);

		cd.setCdGroupCd("PRODUCT_TYPE_CD");
		cd.setCd("PRODUCT_TYPE_CD.DGT");
		cd.setName("디지털상품");
		cd.setUseYn("Y");
		baseService.insertOneTable(cd);

		cd.setCdGroupCd("PRODUCT_TYPE_CD");
		cd.setCd("PRODUCT_TYPE_CD.ADD");
		cd.setName("추가구성상품");
		cd.setUseYn("Y");
		baseService.insertOneTable(cd);

		cd.setCdGroupCd("PRODUCT_TYPE_CD");
		cd.setCd("PRODUCT_TYPE_CD.GFT");
		cd.setName("사은품");
		cd.setUseYn("Y");
		baseService.insertOneTable(cd);
	}

}
