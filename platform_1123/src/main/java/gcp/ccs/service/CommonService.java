package gcp.ccs.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import gcp.ccs.model.CcsApply;
import gcp.ccs.model.CcsApplytarget;
import gcp.ccs.model.CcsBusiness;
import gcp.ccs.model.CcsChannel;
import gcp.ccs.model.CcsControl;
import gcp.ccs.model.CcsControlchannel;
import gcp.ccs.model.CcsControldevice;
import gcp.ccs.model.CcsControlmembergrade;
import gcp.ccs.model.CcsControlmembertype;
import gcp.ccs.model.CcsExcproduct;
import gcp.ccs.model.base.BaseCcsApply;
import gcp.ccs.model.base.BaseCcsApplytarget;
import gcp.ccs.model.base.BaseCcsControl;
import gcp.ccs.model.base.BaseCcsControlchannel;
import gcp.ccs.model.base.BaseCcsControldevice;
import gcp.ccs.model.base.BaseCcsControlmembergrade;
import gcp.ccs.model.base.BaseCcsControlmembertype;
import gcp.ccs.model.search.CcsApplySearch;
import gcp.ccs.model.search.CcsChannelSearch;
import gcp.ccs.model.search.CcsControlSearch;
import gcp.common.util.FoSessionUtil;
import gcp.dms.service.CatalogService;
import gcp.mms.service.MemberService;
import gcp.pms.model.PmsProduct;
import gcp.sps.model.search.SpsDealSearch;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.FileUploadUtil;
import intune.gsf.common.utils.MessageUtil;
import intune.gsf.common.utils.SessionUtil;
import intune.gsf.model.FileInfo;
import intune.gsf.model.FileMeta;

/**
 * 
 * @Pagckage Name : gcp.ccs.service
 * @FileName : CommonService.java
 * @author : steve
 * @date : May 3, 2016
 * @description :
 */
@Service
public class CommonService extends BaseService {
	
	private static final Logger	logger	= LoggerFactory.getLogger(CommonService.class);

	@Autowired
	private MemberService		memberService;

	@Autowired
	private CatalogService		catalogService;

	public List<CcsChannel> getChannelList(CcsChannelSearch channelSearch) throws Exception {
		return (List<CcsChannel>) dao.selectList("ccs.common.getChannelList", channelSearch);
	}

	public List<CcsChannel> getSimpleChannelList(String storeId) throws Exception {
		return (List<CcsChannel>) dao.selectList("ccs.common.getSimpleChannelList", storeId);
	}

	public CcsChannel getChannelDetail(String channelId) throws Exception {
		return (CcsChannel) dao.selectOne("ccs.common.getChannelDetail", channelId);
	}
	public List<CcsBusiness> getChannelBusinessList(String storeId) throws Exception {
		return (List<CcsBusiness>) dao.selectList("ccs.common.getChannelBusinessList", storeId);
	}
	
	public void createChannel(CcsChannel ccsChannel) throws Exception {
		dao.insertOneTable(ccsChannel);
	}
	
	public void updateChannel(List<CcsChannel> ccsChannels) throws Exception {
		for (CcsChannel ccsChannel : ccsChannels) {
			ccsChannel.setUpdId(SessionUtil.getLoginId());
			ccsChannel.setStoreId(SessionUtil.getStoreId());
			dao.updateOneTable(ccsChannel);
		}
	}
	
	public void deleteChannel(List<CcsChannel> ccsChannels) throws Exception {

		for (CcsChannel ccsChannel : ccsChannels) {
			
			CcsControlchannel controlChannel = new CcsControlchannel();
			controlChannel.setStoreId(SessionUtil.getStoreId());
			controlChannel.setChannelId(ccsChannel.getChannelId());
			dao.delete("ccs.common.deleteControlChannel", controlChannel);
		
			ccsChannel.setStoreId(SessionUtil.getStoreId());
			dao.deleteOneTable(ccsChannel);
		}
	}

	public void updateChannelStatus(CcsChannel ccsChannel) {
		ccsChannel.setStoreId(SessionUtil.getStoreId());
		ccsChannel.setUpdId(SessionUtil.getLoginId());
		dao.update("ccs.common.updateChannelStatus", ccsChannel);
	}

	/**
	 * @Method Name : getControlInfo
	 * @author : peter
	 * @date : 2016. 5. 20.
	 * @description : 제어 정보
	 *
	 * @param cctl
	 * @return
	 * @throws ServiceException
	 */
	public BaseCcsControl getControlInfo(CcsControl cctl) throws ServiceException {
		return (BaseCcsControl) dao.selectOne("ccs.common.getControlInfo", cctl);
	}

	/**
	 * @Method Name : getControlMemberList
	 * @author : peter
	 * @date : 2016. 5. 24.
	 * @description : 회원 제어정보
	 *
	 * @param ccm
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<BaseCcsControlmembertype> getControlMemberTypeList(CcsControlmembertype ccm) throws ServiceException {
		return (List<BaseCcsControlmembertype>) dao.selectList("ccs.common.getControlMemberTypeList", ccm);
	}

	@SuppressWarnings("unchecked")
	public List<BaseCcsControlmembergrade> getControlMemberGradeList(CcsControlmembergrade ccm) throws ServiceException {
		return (List<BaseCcsControlmembergrade>) dao.selectList("ccs.common.getControlMemberGradeList", ccm);
	}

	/**
	 * @Method Name : getControlChannelList
	 * @author : peter
	 * @date : 2016. 5. 24.
	 * @description : 채널 제어정보
	 *
	 * @param ccc
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<BaseCcsControlchannel> getControlChannelList(CcsControlchannel ccc) throws ServiceException {
		return (List<BaseCcsControlchannel>) dao.selectList("ccs.common.getControlChannelList", ccc);
	}

	/**
	 * @Method Name : getControlDeviceList
	 * @author : peter
	 * @date : 2016. 5. 24.
	 * @description : 디바이스 제어정보
	 *
	 * @param ccd
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<BaseCcsControldevice> getControlDeviceList(CcsControldevice ccd) throws ServiceException {
		return (List<BaseCcsControldevice>) dao.selectList("ccs.common.getControlDeviceList", ccd);
	}

	public int insertApply(CcsApply apply) throws Exception {
		return dao.insertOneTable(apply);
	}

	public void deleteApplytargetByApplyNoNewTx(CcsApplytarget ccsApplytarget) {
		dao.delete("ccs.common.deleteApplytargetByApplyNo", ccsApplytarget);
	}

	public int insertApplyTarget(BaseCcsApplytarget applytarget) throws Exception {
		return dao.insert("ccs.common.insertApplyTarget", applytarget);
	}

	public int updateApply(BaseCcsApply apply) throws Exception {
		apply.setUpdId(SessionUtil.getLoginId());
		apply.setStoreId(SessionUtil.getStoreId());
		return dao.updateOneTable(apply);
	}

	public int updateApplyTarget(BaseCcsApplytarget applytarget) throws Exception {
		applytarget.setUpdId(SessionUtil.getLoginId());
		applytarget.setStoreId(SessionUtil.getStoreId());
		return dao.updateOneTable(applytarget);
	}

	public int updateApplyTarget(CcsApplytarget ccsApplytarget) throws Exception {
		ccsApplytarget.setUpdId(SessionUtil.getLoginId());
		ccsApplytarget.setStoreId(SessionUtil.getStoreId());
		return dao.updateOneTable(ccsApplytarget);
	}

	public int updateExcproduct(CcsExcproduct ccsExcproduct) throws Exception {
		ccsExcproduct.setUpdId(SessionUtil.getLoginId());
		ccsExcproduct.setStoreId(SessionUtil.getStoreId());
		return dao.updateOneTable(ccsExcproduct);
	}

	public int deleteApply(BaseCcsApply apply) throws Exception {
		return dao.deleteOneTable(apply);
	}

	public int deleteApplyTarget(BaseCcsApplytarget applytarget) throws Exception {
		applytarget.setUpdId(SessionUtil.getLoginId());
		applytarget.setStoreId(SessionUtil.getStoreId());
		return dao.deleteOneTable(applytarget);
	}

	public int insertApplyTarget(CcsApplytarget ccsApplytarget) throws Exception {
		return dao.insert("ccs.common.insertApplyTarget", ccsApplytarget);
	}

	public int insertExcProduct(CcsExcproduct ccsExcproduct) throws Exception {
		return dao.insert("ccs.common.insertExcProduct", ccsExcproduct);
	}

	public void deleteExcproductByApplyNoNewTx(CcsExcproduct ccsExcproduct) {
		dao.delete("ccs.common.deleteExcproductByApplyNo", ccsExcproduct);
	}

	/**
	 * @Method Name : insertControl
	 * @author : paul
	 * @date : 2016. 5. 16.
	 * @Description : 제약테이블 마스터 입력
	 *
	 * @param control
	 * @return
	 */
	public int insertControl(BaseCcsControl control) throws Exception {
		return dao.insert("ccs.common.insertControl", control);
	}

	/**
	 * 이미지 파일 단건 업로드
	 * 
	 * @Method Name : imgFileUpload
	 * @author : peter
	 * @date : 2016. 5. 26.
	 * @description : 업로드된 경로를 리턴한다
	 *
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public String imgFileUpload(MultipartHttpServletRequest req, FileInfo fileInfo) throws Exception {
		LinkedList<FileMeta> list = FileUploadUtil.uploadImages(req, fileInfo);

		return list.get(0).getUploadedFilePath();
	}

	/**
	 * 압축 ZIP 파일 단건 업로드
	 * 
	 * @Method Name : zipFileUpload
	 * @author : roy
	 * @date : 2016. 11. 02.
	 * @description : String을 리턴한다
	 *
	 * @param req
	 * @return
	 * @throws Throwable
	 */
	public List<FileInfo> zipFileUpload(MultipartHttpServletRequest req, FileInfo fileInfo) throws Throwable {
		List<FileInfo> list = FileUploadUtil.uploadZip(req, fileInfo);

//		FileUploadUtil.parseReferer(fileInfo);
//		try {
////			상품
//			if ("pms.product".equals(fileInfo.getSystem() + "." + fileInfo.getBiz())) {
//
//			}
////			카탈로그
//			else if ("dms.catalog".equals(fileInfo.getSystem() + "." + fileInfo.getBiz())) {
//				String catalogId = req.getParameter("pKey");
//				String sortNo = null;
//				for (FileInfo info : list) {
//					String img2 = info.getFileName();
//					if (CommonUtil.isNotEmpty(info.getFileName()) && info.getFileName().matches("(.*)pc(.*)")) {
//						img2 = CommonUtil.replace(info.getFileName(), "pc", "mo");
//						String[] splitTemp = null;
//						splitTemp = info.getFileName().split("_");
//						sortNo = splitTemp[1];
//					} else {
//						continue;
//					}
//					DmsCatalogimg catalogImg = new DmsCatalogimg();
//					catalogImg.setCatalogId(catalogId);
//					catalogImg.setName("타이틀을 입력해주세요.");
//					catalogImg.setSortNo(new BigDecimal(sortNo));
//					catalogImg.setDisplayYn("N");
//					catalogImg.setImg1(info.getFullPath() + "/" + info.getFileName());
//					catalogImg.setImg2(info.getFullPath() + "/" + img2);
//					catalogService.insertCatalogImg(catalogImg);
//				}
//			}
//		} catch (Exception e) {
//			return result;
//		}

		return list;
	}

	/**
	 * 이미지 파일 단건 업로드
	 * 
	 * @Method Name : imgFileUpload
	 * @author : peter
	 * @date : 2016. 5. 26.
	 * @description : FileMeta 를 리턴한다
	 *
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public FileMeta imgFileUpload2(MultipartHttpServletRequest req, FileInfo fileInfo) throws Exception {
		LinkedList<FileMeta> list = FileUploadUtil.uploadImages(req, fileInfo);

		return list.get(0);
	}


	public int countCheckIdTarget(CcsApplytarget ccsApplytarget) throws Exception {

		return dao.selectCount("ccs.common.countCheckIdTarget", ccsApplytarget);
	}

	public int countCheckIdExcpro(CcsExcproduct ccsExcproduct) throws Exception {

		return dao.selectCount("ccs.common.countCheckIdExcpro", ccsExcproduct);
	}

	/**
	 * 
	 * @Method Name : getCcsExcproductByApplyNo
	 * @author : dennis
	 * @date : 2016. 6. 16.
	 * @description : apply no로 제외대상 조회
	 *
	 * @param applyNo
	 * @return
	 */
	public List<CcsExcproduct> getCcsExcproductByApplyNo(BigDecimal applyNo) {
		CcsExcproduct ccsExcproduct = new CcsExcproduct();
		ccsExcproduct.setApplyNo(applyNo);
		return (List<CcsExcproduct>) dao.selectList("ccs.common.getCcsExcproduct", ccsExcproduct);
	}

	/**
	 * 
	 * @Method Name : getCcsApplytargetPrByApplyNo
	 * @author : dennis
	 * @date : 2016. 6. 16.
	 * @description : apply no로 적용대상 조회 (상품)
	 *
	 * @param applyNo
	 * @return
	 */
	public List<CcsApplytarget> getCcsApplytargetPrByApplyNo(BigDecimal applyNo) {
		CcsApplytarget ccsApplytarget = new CcsApplytarget();
		ccsApplytarget.setApplyNo(applyNo);
		return (List<CcsApplytarget>) dao.selectList("ccs.common.getCcsApplytargetPr", ccsApplytarget);
	}

	/**
	 * 
	 * @Method Name : getCcsApplytargetCaByApplyNo
	 * @author : dennis
	 * @date : 2016. 6. 16.
	 * @description : apply no로 적용대상 조회 (전시카테고리)
	 *
	 * @param applyNo
	 * @return
	 */
	public List<CcsApplytarget> getCcsApplytargetCaByApplyNo(BigDecimal applyNo) {
		CcsApplytarget ccsApplytarget = new CcsApplytarget();
		ccsApplytarget.setApplyNo(applyNo);
		return (List<CcsApplytarget>) dao.selectList("ccs.common.getCcsApplytargetCa", ccsApplytarget);
	}

	/**
	 * 
	 * @Method Name : getCcsApplytargetBrByApplyNo
	 * @author : dennis
	 * @date : 2016. 6. 16.
	 * @description : apply no로 적용대상 조회 (브랜드)
	 *
	 * @param applyNo
	 * @return
	 */
	public List<CcsApplytarget> getCcsApplytargetBrByApplyNo(BigDecimal applyNo) {
		CcsApplytarget ccsApplytarget = new CcsApplytarget();
		ccsApplytarget.setApplyNo(applyNo);
		return (List<CcsApplytarget>) dao.selectList("ccs.common.getCcsApplytargetBr", ccsApplytarget);
	}

	

	/**
	 * 
	 * @Method Name : saveCcsApply
	 * @author : dennis
	 * @date : 2016. 6. 16.
	 * @description : 적용대상 저장
	 *
	 * @param ccsApply
	 * @return
	 * @throws Exception
	 */
	public BigDecimal saveCcsApply(CcsApply ccsApply) throws Exception {

		String storeId = ccsApply.getStoreId();
		BigDecimal applyNo = null;

		if (ccsApply != null) {
			ccsApply.setStoreId(storeId);
			if (CommonUtil.isEmpty(ccsApply.getApplyNo())) {
//				this.insertApply(ccsApply);
				dao.insertOneTable(ccsApply);
				applyNo = ccsApply.getApplyNo();
			} else {
				ccsApply.setUpdId(SessionUtil.getLoginId());
				ccsApply.setStoreId(SessionUtil.getStoreId());
				dao.updateOneTable(ccsApply);
				applyNo = ccsApply.getApplyNo();
			}

			String targetTypeCd = ccsApply.getTargetTypeCd();

			CcsApplytarget ccsApplytarget = new CcsApplytarget();
			ccsApplytarget.setStoreId(storeId);
			ccsApplytarget.setApplyNo(applyNo);
			dao.delete("ccs.common.deleteApplytargetByApplyNo", ccsApplytarget);
//			((CommonService) this.getThis()).deleteApplytargetByApplyNoNewTx(ccsApplytarget);

			List<CcsApplytarget> ccsApplytargets = ccsApply.getCcsApplytargets();
			if (ccsApplytargets != null && ccsApplytargets.size() > 0) {
				for (CcsApplytarget ct : ccsApplytargets) {
					ct.setStoreId(storeId);
					ct.setApplyNo(applyNo);
					if (BaseConstants.TARGET_TYPE_CD_PRODUCT.equals(targetTypeCd)) {
						ct.setTargetId(ct.getPmsProduct().getProductId());
					} else if (BaseConstants.TARGET_TYPE_CD_CATEGORY.equals(targetTypeCd)) {
						ct.setTargetId(ct.getDmsDisplaycategory().getDisplayCategoryId());
					} else if (BaseConstants.TARGET_TYPE_CD_BRAND.equals(targetTypeCd)) {
						ct.setTargetId(ct.getPmsBrand().getBrandId());
					}
					dao.insertOneTable(ct);

//					try {
//						((CommonService) this.getThis()).insertOneTableNewTx(ct);
//					} catch (DuplicateKeyException de) {
//						logger.error(de.getMessage());
//					}
				}
			}

			CcsExcproduct ccsExcproduct = new CcsExcproduct();
			ccsExcproduct.setStoreId(storeId);
			ccsExcproduct.setApplyNo(applyNo);
			((CommonService) this.getThis()).deleteExcproductByApplyNoNewTx(ccsExcproduct);

			List<CcsExcproduct> ccsExcproducts = ccsApply.getCcsExcproducts();
			if (ccsExcproducts != null && ccsExcproducts.size() > 0) {
				for (CcsExcproduct ce : ccsExcproducts) {
					ce.setStoreId(storeId);
					ce.setApplyNo(applyNo);
					ce.setProductId(ce.getPmsProduct().getProductId());

					dao.insertOneTable(ce);
//					try {
//						((CommonService) this.getThis()).insertOneTableNewTx(ce);
//					} catch (DuplicateKeyException de) {
//						logger.error(de.getMessage());
//					}

				}
			}

		}
		return applyNo;

	}

	public void insertOneTableNewTx(Object obj) throws Exception {
		dao.insertOneTable(obj);
	}

	/**
	 * @Method Name : getExistsList
	 * @author : ian
	 * @date : 2016. 6. 24.
	 * @description : 적용대상 중복 확인
	 *
	 * @param search
	 * @return
	 */
	public List<String> getExistsList(CcsApplySearch search) {
		search.setStoreId(SessionUtil.getStoreId());
		List<String> list = new ArrayList<String>();
		String type = search.getApplyType();

		if ("ec".equals(type)) {
			list = (ArrayList<String>) dao.selectList("ccs.common.getExistsExcproductList", search);
		} else {
			list = (ArrayList<String>) dao.selectList("ccs.common.getExistsTargetList", search);
		}

		return list;
	}

	/**
	 * 공통 허용 설정저장
	 * 
	 * @Method Name : updateControl
	 * @author : eddie
	 * @date : 2016. 7. 7.
	 * @description : 1. controlNo가 없고, control이 있으면 신규 등록 ( insert ) <br/>
	 *              2. controlNo가 있고, control 이 없으면 허용정보 삭제( delete )<br/>
	 *              3. controlNo가 있고, control 이 있으면 신규or수정 ( delete & insert )
	 * @param control
	 * @return
	 * @throws Exception
	 */
	public BigDecimal updateControl(BigDecimal controlNo, CcsControl control) throws Exception {

//		logger.debug("####### controlNo : " + controlNo);
//		logger.debug("####### control : " + control);

		BigDecimal newControlNo = null;

		// 둘다 없으면 return
		if (controlNo == null && control == null) {
			return null;
		}


		// 마스터 수정
		if (controlNo != null && control != null) {
			control.setStoreId(SessionUtil.getStoreId());
			control.setControlNo(controlNo);
			control.setUpdId(SessionUtil.getLoginId());
			dao.updateOneTable(control);
		}

//		control.setStoreId(SessionUtil.getStoreId());

		// 설정삭제 or 수정을 위한 삭제
		if (controlNo != null) {
			
			// ccs_controlmembertype 삭제
			CcsControlmembertype controlMemType = new CcsControlmembertype();
			controlMemType.setStoreId(SessionUtil.getStoreId());
			controlMemType.setControlNo(controlNo);
			dao.delete("ccs.common.deleteControlMemberType", controlMemType);

			// ccs_controlmembergrade 삭제
			CcsControlmembergrade controlMemGrade = new CcsControlmembergrade();
			controlMemGrade.setStoreId(SessionUtil.getStoreId());
			controlMemGrade.setControlNo(controlNo);
			dao.delete("ccs.common.deleteControlMemberGrade", controlMemGrade);

			// ccs_controldevice 삭제
			CcsControldevice controlDevice = new CcsControldevice();
			controlDevice.setStoreId(SessionUtil.getStoreId());
			controlDevice.setControlNo(controlNo);
			dao.delete("ccs.common.deleteControlDevice", controlDevice);

			// ccs_controlchannel 삭제
			CcsControlchannel controlChannel = new CcsControlchannel();
			controlChannel.setStoreId(SessionUtil.getStoreId());
			controlChannel.setControlNo(controlNo);
			dao.delete("ccs.common.deleteControlChannel", controlChannel);
		}

		// 설정 삭제
		if (controlNo != null && control == null) {
			// ccs_control 삭제
			CcsControl del = new CcsControl();
			del.setStoreId(SessionUtil.getStoreId());
			del.setControlNo(controlNo);
			dao.deleteOneTable(del);
		}

		// 신규등록
		if (controlNo == null && control != null) {
			control.setStoreId(SessionUtil.getStoreId());
			control.setControlNo(null);
			if (StringUtils.isEmpty(control.getChannelControlCd())) {
				control.setChannelControlCd("CHANNEL_CONTROL_CD.ALL");
			}
			dao.insertOneTable(control);
			newControlNo = control.getControlNo();
		}

		// 신규등록 or 수정을 위한 insert
		if (control != null) {
			// 채널
			if ("CHANNEL_CONTROL_CD.CHANNEL".equals(control.getChannelControlCd())) {
				for (String channelId : control.getChannelIdArr()) {
					if (StringUtils.isNotEmpty(channelId)) {
						CcsControlchannel channel = new CcsControlchannel();
						channel.setStoreId(SessionUtil.getStoreId());
						channel.setControlNo(control.getControlNo());
						channel.setChannelId(channelId);
						dao.insertOneTable(channel);
					}
				}
			}

			// 디바이스 
			if (control.getDeviceTypeArr() != null) {
				for (String deviceCd : control.getDeviceTypeArr()) {
					if (StringUtils.isNotEmpty(deviceCd)) {
						CcsControldevice device = new CcsControldevice();
						device.setStoreId(SessionUtil.getStoreId());
						device.setControlNo(control.getControlNo());
						device.setDeviceTypeCd(deviceCd);
						dao.insertOneTable(device);
					}
				}
			}

			// 회원유형
			if (control.getMemberTypeArr() != null) {
				for (String memTypeCd : control.getMemberTypeArr()) {
					if (StringUtils.isNotEmpty(memTypeCd)) {
						CcsControlmembertype memType = new CcsControlmembertype();
						memType.setStoreId(SessionUtil.getStoreId());
						memType.setControlNo(control.getControlNo());
						memType.setMemberTypeCd(memTypeCd);
						dao.insertOneTable(memType);
					}
				}
			}

			// 회원등급
			if (control.getMemGradeArr() != null) {
				for (String memGradeCd : control.getMemGradeArr()) {
					if (StringUtils.isNotEmpty(memGradeCd)) {
						CcsControlmembergrade memGrade = new CcsControlmembergrade();
						memGrade.setStoreId(SessionUtil.getStoreId());
						memGrade.setControlNo(control.getControlNo());
						memGrade.setMemGradeCd(memGradeCd);
						dao.insertOneTable(memGrade);
					}
				}
			}
		}
		return newControlNo;
	}

	/**
	 * @Method Name : deleteChannelByControlNo, deleteDeviceByControlNo,
	 *         deleteMemberGradeByControlNo, deleteMemberTypeByControlNo
	 * @author : ian
	 * @date : 2016. 8. 4.
	 * @description : 제한설정 sub 테이블 삭제
	 *
	 * @param channel
	 */
	public void deleteChannelByControlNo(CcsControlchannel channel) {
		dao.delete("ccs.common.deleteControlChannel", channel);
	}

	public void deleteDeviceByControlNo(CcsControldevice device) {
		dao.delete("ccs.common.deleteControlDevice", device);
	}

	public void deleteMemberGradeByControlNo(CcsControlmembergrade grade) {
		dao.delete("ccs.common.deleteControlMemberGrade", grade);
	}

	public void deleteMemberTypeByControlNo(CcsControlmembertype type) {
		dao.delete("ccs.common.deleteControlMemberType", type);
	}

	public boolean checkControl(CcsControlSearch search) {

		boolean result = true;
		String resultMsg = "";

		// 회원번호만 설정하고 등급, 유형정보가 없을경우 조회
		if (search.getMemberNo() != null && CommonUtil.isEmpty(search.getMemGradeCd()) && search.getMemberTypeCds() == null
				|| (search.getMemberTypeCds() != null && search.getMemberTypeCds().size() == 0)) {

			List<String> memberTypeCds = new ArrayList();
			memberService.getMemberTypeInfo(memberTypeCds);

			String memGradeCd = FoSessionUtil.getMemGradeCd();	//회원등급

			search.setMemberTypeCds(memberTypeCds);
			search.setMemGradeCd(memGradeCd);
		} else {
			logger.debug("non member !!!!!!!!!!!!!");
		}


		CcsControl ccsControl = (CcsControl) dao.selectOne("ccs.common.getControlInfo", search);

		List<String> memberTypeCds = search.getMemberTypeCds();
		String memGradeCd = search.getMemGradeCd();
		String deviceTypeCd = search.getDeviceTypeCd();
		String channelId = search.getChannelId();

		logger.debug("channelId : " + channelId);
		logger.debug("deviceTypeCd : " + deviceTypeCd);
		logger.debug("memGradeCd : " + memGradeCd);
		logger.debug("memTypeCds : " + memberTypeCds);

		boolean channelFlag = false;
		boolean memGradeFlag = false;
		boolean memberTypeFlag = false;
		boolean deviceTypeFlag = false;

		//check 하지 않는것 true;
		String controlType = search.getControlType();
		if ("PRODUCT".equals(controlType)) {					//상품
			channelFlag = true;
			deviceTypeFlag = true;
		} else if ("DEAL".equals(controlType)) {				//딜
			channelFlag = true;
			deviceTypeFlag = true;
		} else if ("COUPON".equals(controlType)) {				//쿠폰(상품다운로드)
			memGradeFlag = true;
			memberTypeFlag = true;
			channelFlag = true;
		} else if ("PRESENT".equals(controlType)) {				//사은품
			channelFlag = true;
			deviceTypeFlag = true;
		} else if ("COUPON_BO".equals(controlType)) {			//쿠폰 수동발급 ( 어드민, 배치 등..)
			channelFlag = true;
			deviceTypeFlag = true;
		} else if ("COUPON_ORDER".equals(controlType)) {		//쿠폰사용
			memGradeFlag = true;
			memberTypeFlag = true;
		}

		if (CommonUtil.isNotEmpty(ccsControl)) {
			//허용채널
			List<CcsControlchannel> applyChannels = ccsControl.getCcsControlchannels();
			if (applyChannels != null && applyChannels.size() > 0) {	//허용채널이 없으면 전체
				for (CcsControlchannel channel : applyChannels) {
					String applyChannelId = channel.getChannelId();
					if (CommonUtil.isNotEmpty(applyChannelId) && CommonUtil.isNotEmpty(channelId)
							&& applyChannelId.equals(channelId)) {
						channelFlag = true;
					}
				}
			} else {
				channelFlag = true;
			}

			// 회원 체크
//			if (CommonUtil.isNotEmpty(search.getMemberNo())) {//TODO 비회원일경우 처리
				//허용회원유형
				List<CcsControlmembertype> applyMemberType = ccsControl.getCcsControlmembertypes();
				if (applyMemberType != null && applyMemberType.size() > 0) {

					for (CcsControlmembertype memberType : applyMemberType) {
						for (String memberTypeCd : memberTypeCds) {
							if (CommonUtil.isNotEmpty(memberType.getMemberTypeCd())
									&& memberType.getMemberTypeCd().equals(memberTypeCd)) {
								memberTypeFlag = true;
							}
						}
					}
				} else {
					memberTypeFlag = true;
				}

				//허용회원등급
				List<CcsControlmembergrade> applyMemberGrade = ccsControl.getCcsControlmembergrades();
				if (applyMemberGrade != null && applyMemberGrade.size() > 0) {

					for (CcsControlmembergrade memberGrade : applyMemberGrade) {
						if (CommonUtil.isNotEmpty(memberGrade.getMemGradeCd())
								&& memberGrade.getMemGradeCd().equals(memGradeCd)) {
							memGradeFlag = true;
						}
					}
				} else {
					memGradeFlag = true;
				}
//			} else {
//				memberTypeFlag = true;
//				memGradeFlag = true;
//			}

			//허용device
			List<CcsControldevice> applyDevice = ccsControl.getCcsControldevices();
			if (applyDevice != null && applyDevice.size() > 0) {
				for (CcsControldevice device : applyDevice) {
					String controlDeviceTypeCd = device.getDeviceTypeCd();
					if (CommonUtil.isNotEmpty(controlDeviceTypeCd)) {
						if (controlDeviceTypeCd.equals(deviceTypeCd)) {
							deviceTypeFlag = true;
						}
					}
				}
			} else {
				deviceTypeFlag = true;
			}

			String channelControlCd = ccsControl.getChannelControlCd();

			String applyObjectName = search.getApplyObjectName();
			String[] args = new String[2];
			if (CommonUtil.isNotEmpty(applyObjectName)) {
				args = applyObjectName.split(",");
			}

			if ("CHANNEL_CONTROL_CD.ALL".equals(channelControlCd)) {
//				if (!memGradeFlag) {
//					resultMsg = "ccs.control.applyMemGrade";
//					result = false;
//				}
//				if (!memberTypeFlag) {
//					resultMsg = "ccs.control.applyMemberType";
//					result = false;
//				}
//				if (!deviceTypeFlag) {
//					resultMsg = "ccs.control.applyDevice";
//					result = false;
//				}

				if (!memGradeFlag || !memberTypeFlag || !deviceTypeFlag) {
					resultMsg = "ccs.control.notApply";
					result = false;
				}

			} else if ("CHANNEL_CONTROL_CD.DIRECT".equals(channelControlCd)) {
//				if (!memGradeFlag) {
//					resultMsg = "ccs.control.applyMemGrade";
//					result = false;
//				}
//				if (!memberTypeFlag) {
//					resultMsg = "ccs.control.applyMemberType";
//					result = false;
//				}
//				if (!deviceTypeFlag) {
//					resultMsg = "ccs.control.applyDevice";
//					result = false;
//				}
				if (!memGradeFlag || !memberTypeFlag || !deviceTypeFlag) {
					resultMsg = "ccs.control.notApply";
					result = false;
				}
			} else if ("CHANNEL_CONTROL_CD.CHANNEL".equals(channelControlCd)) {
//				if (!channelFlag) {
//					resultMsg = "ccs.control.applyChannel";
//					result = false;
//				}
//				if (!memGradeFlag) {
//					resultMsg = "ccs.control.applyMemGrade";
//					result = false;
//				}
//				if (!memberTypeFlag) {
//					resultMsg = "ccs.control.applyMemberType";
//					result = false;
//				}
//				if (!deviceTypeFlag) {
//					resultMsg = "ccs.control.applyDevice";
//					result = false;
//				}
				if (!channelFlag || !memGradeFlag || !memberTypeFlag || !deviceTypeFlag) {
					resultMsg = "ccs.control.notApply";
					result = false;
				}
			}

			if (!result && search.isExceptionFlag()) {
				throw new ServiceException(resultMsg, args);
			}

			if (!result) {
				logger.error(MessageUtil.getMessage(resultMsg, args));
			}

		}
		return result;
	}

	/**
	 * @Method Name : getDealProductLowestPriceList
	 * @author : ian
	 * @date : 2016. 9. 28.
	 * @description : 딜 상품 최저가
	 *
	 * @param storeId
	 * @param memGradeCd 
	 * @param dealTypeCd
	 * @param upperDealGroupNo 1depth 상품
	 * @param dealGroupNo 2depth 상품
	 * @param oneItem - 딜당 상품 하나씩
	 * @param sortType  
	 * default 인기상품순
	 *  
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<PmsProduct> getDealProductLowestPriceList(SpsDealSearch dealSearch) {
		return (List<PmsProduct>) dao.selectList("ccs.common.getDealProductLowestPriceList", dealSearch);
	}

	/**
	 * @Method Name : updateControlByCopyReg
	 * @author : ian
	 * @date : 2016. 10. 23.
	 * @description : 복사 등록시 허용설정 저장
	 *
	 * @param ccsControl
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public BigDecimal updateControlByCopyReg(CcsControl ccsControl) throws Exception {

		logger.debug("\t old control No >> " + ccsControl.getControlNo());
		
		List<CcsControlchannel> channel = (List<CcsControlchannel>) dao.selectList("ccs.common.getChannelByCopyReg", ccsControl);
		List<CcsControldevice> device = (List<CcsControldevice>) dao.selectList("ccs.common.getDeviceByCopyReg", ccsControl);
		List<CcsControlmembergrade> grade = (List<CcsControlmembergrade>) dao.selectList("ccs.common.getGradeByCopyReg", ccsControl);
		List<CcsControlmembertype> type = (List<CcsControlmembertype>) dao.selectList("ccs.common.getTypeByCopyReg", ccsControl);

		dao.insert("ccs.common.insertCcsControlByCopyReg", ccsControl);
		
		BigDecimal newControlNo = (BigDecimal) dao.selectOne("ccs.common.getControlNo", ccsControl);
		logger.debug("\t new control No >> " + newControlNo);
		
		
		// 채널
		if(channel.size() > 0) {
			if ("CHANNEL_CONTROL_CD.CHANNEL".equals(ccsControl.getChannelControlCd())) {
				for (CcsControlchannel c : channel) {
					c.setControlNo(newControlNo);
					dao.insertOneTable(c);
				}
			}
		}
		
		// 디바이스 
		if (device.size() > 0) {
			for (CcsControldevice d : device) {
				d.setControlNo(newControlNo);
				dao.insertOneTable(d);
			}
		}
		
		// 회원유형
		if (type.size() > 0) {
			for (CcsControlmembertype t : type) {
				t.setControlNo(newControlNo);
				dao.insertOneTable(t);
			}
		}
		
		// 회원등급
		if (grade.size() > 0) {
			for (CcsControlmembergrade g : grade) {
				g.setControlNo(newControlNo);
				dao.insertOneTable(g);
			}
		}
		
		return newControlNo;
	}
	
}

