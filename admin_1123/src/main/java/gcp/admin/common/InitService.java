/**
 * 
 */
package gcp.admin.common;

import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import gcp.ccs.model.CcsCode;
import gcp.ccs.model.base.BaseCcsCodegroup;
import gcp.ccs.model.base.BaseCcsField;
import gcp.ccs.service.BaseService;
import gcp.common.util.CodeUtil;
import gcp.common.util.ValidationUtil;
import intune.gsf.common.utils.MessageDBUtil;
import intune.gsf.model.Message;

/**
 * 
 * @Pagckage Name : gcp.admin.common
 * @FileName : InitService.java
 * @author : victor
 * @date : 2016. 4. 20.
 * @description :
 */
@Service
public class InitService extends BaseService {

	protected final Log		logger	= LogFactory.getLog(getClass());


	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() throws Exception {

		try {

			// 코드정보 조회
			List<BaseCcsCodegroup> codeGroupList = (List<BaseCcsCodegroup>) dao.selectList("ccs.common.getCodeListAll",
					null);
			HashMap<String, List<CcsCode>> codeMap = new HashMap<String, List<CcsCode>>();
			for (BaseCcsCodegroup codeGroup : codeGroupList) {
				String groupCd = codeGroup.getCdGroupCd();
				codeMap.put(groupCd,codeGroup.getCcsCodes());

			}
			CodeUtil.setCodeListMap(codeMap);

			// 메세지 정보 조회
			List<Message> ccsMessages = (List<Message>) dao.selectList("ccs.common.getMessageAllInfo", null);
			MessageDBUtil.setMessageListAll(ccsMessages);

			// 필드정보 조회
			List<BaseCcsField> ccsFields = (List<BaseCcsField>) dao.selectList("ccs.common.getCcsField", null);
			ValidationUtil.setCcsFields(ccsFields);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
