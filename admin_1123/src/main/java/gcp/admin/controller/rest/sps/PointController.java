package gcp.admin.controller.rest.sps;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.sps.model.SpsPointsave;
import gcp.sps.model.search.SpsPointSearch;
import gcp.sps.service.PointService;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.SessionUtil;

@RestController
@RequestMapping("api/sps/point")
public class PointController {

	@Autowired
	private PointService		pointService;

	/**
	 * 
	 * @Method Name : list
	 * @author : dennis
	 * @date : 2016. 6. 14.
	 * @description : 포인트 조회
	 *
	 * @param spsPointSearch
	 * @return List<SpsPointsave>
	 */
	@RequestMapping(value="/list",method = { RequestMethod.GET, RequestMethod.POST })
	public List<SpsPointsave> list(@RequestBody SpsPointSearch spsPointSearch) {
		return pointService.getPointList(spsPointSearch);
	}

	/**
	 * @Method Name : deletePointPromotion
	 * @author : ian
	 * @date : 2016. 8. 4.
	 * @description : 포인트 대기 상태 삭제
	 *
	 * @param point
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void deletePointPromotion(@RequestBody List<SpsPointsave> point) throws Exception {
		pointService.deletePoint(point);
	}

	/**
	 * @Method Name : save
	 * @author : ian
	 * @date : 2016. 8. 4.
	 * @description : 포인트 프로모션 등록, 수정
	 *
	 * @param spsPointsave
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/save", method = { RequestMethod.POST })
	public String save(@RequestBody SpsPointsave spsPointsave) throws Exception {

		spsPointsave.setInsId(SessionUtil.getLoginId());
		spsPointsave.setUpdId(SessionUtil.getLoginId());

		String id = null;
		if (CommonUtil.isEmpty(spsPointsave.getPointSaveId())) {
			id = pointService.insertPoint(spsPointsave);
		} else {
			id = pointService.updatePoint(spsPointsave);
		}
		return id;
	}

	/**
	 * @Method Name : updatePointState
	 * @author : ian
	 * @date : 2016. 8. 22.
	 * @description : 포인트 상태 변경 (진행, 중지)
	 *
	 * @param spsPointsave
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updatePointState", method = RequestMethod.POST)
	public String updatePointState(@RequestBody SpsPointsave spsPointsave) throws Exception {
		return pointService.updatePointState(spsPointsave);
	}

	@RequestMapping(value = "/{pointSaveId}", method = { RequestMethod.GET, RequestMethod.POST })
	public SpsPointsave detail(@PathVariable("pointSaveId") String pointSaveId) throws Exception {
		SpsPointSearch spsPointSearch = new SpsPointSearch();
		spsPointSearch.setStoreId(SessionUtil.getStoreId());
		spsPointSearch.setPointSaveId(pointSaveId);
		return (SpsPointsave) pointService.getPointDetail(spsPointSearch);
	}
}
