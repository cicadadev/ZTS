package gcp.frontpc.controller.rest.dms;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gcp.mms.model.MmsStyle;
import gcp.mms.model.MmsStylelike;
import gcp.mms.service.MemberService;
import gcp.mms.service.StyleService;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.SessionUtil;

@RestController
@RequestMapping("api/dms/display")
public class DisplayController {
	
	@Autowired
	private StyleService					styleService;

	@Autowired
	private MemberService	memberService;

	private final Log	logger	= LogFactory.getLog(getClass());
	
	

	/**
	 * 
	 * @Method Name : updateLikeCnt
	 * @author : allen
	 * @date : 2016. 10. 9.
	 * @description : 스타일 좋아요 업데이트
	 *
	 * @param request
	 * @param style
	 * @throws Exception
	 */
	@RequestMapping(value = "/style/updateLikeCnt")
	public void updateLikeCnt(HttpServletRequest request, @RequestBody MmsStyle style) throws Exception {

		logger.debug(style);

		MmsStylelike stylelike = new MmsStylelike();
		stylelike.setStyleNo(style.getStyleNo());
		stylelike.setMemberNo(SessionUtil.getMemberNo());
		stylelike.setInsId(SessionUtil.getLoginId());
		stylelike.setUpdId(SessionUtil.getLoginId());

		if (BaseConstants.YN_Y.equals(style.getLikeYn())) {
			styleService.insertStyleLike(stylelike);
		} else {
			styleService.deleteStyleLike(stylelike);
		}
	}
}
