package gcp.admin.controller.view;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * @Pagckage Name : gcp.admin.controller.view
 * @FileName : SampleViewController.java
 * @author : dennis
 * @date : 2016. 4. 19.
 * @description : Sample page를 load하기위한 controller
 */
@Controller
@RequestMapping("/samplePage")
public class SampleViewController {

	private final Log logger = LogFactory.getLog(getClass());

	/**
	 * 
	 * @Method Name : samplePage
	 * @author : dennis
	 * @date : 2016. 4. 19.
	 * @Description : Sample page loading
	 *
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView samplePage() {
		return new ModelAndView("sample/sample2");
	}

	/**
	 * 
	 * @Method Name : samplePage
	 * @author : dennis
	 * @date : 2016. 4. 19.
	 * @Description : Sample page loading
	 *
	 * @return
	 */
	@RequestMapping(value = "/message", method = RequestMethod.GET)
	public ModelAndView message() {
		return new ModelAndView("sample/message");
	}

	/**
	 * @Method Name : testFile
	 * @author : ian
	 * @date : 2016. 4. 26.
	 * @description : File Uplaod ( img, zip, etc...)
	 *
	 * @return
	 */
	@RequestMapping(value = "/fileUpload", method = RequestMethod.GET)
	public ModelAndView testFile() {
		return new ModelAndView("sample/fileUpload");
	}

	/**
	 * @Method Name : testExcelGrid
	 * @author : ian
	 * @date : 2016. 4. 26.
	 * @description : Excel Upload / Download (xls, xlsx)
	 *
	 * @return
	 */
	@RequestMapping(value = "/excelGrid", method = RequestMethod.GET)
	public ModelAndView testExcelGrid() {
		return new ModelAndView("sample/excelGrid");
	}


	/**
	 * 
	 * @Method Name : guide
	 * @author : dennis
	 * @date : 2016. 4. 19.
	 * @Description : Sample page loading
	 *
	 * @return
	 */
	@RequestMapping(value = "/guide", method = RequestMethod.GET)
	public ModelAndView guideDoc(HttpServletRequest req) {
		if (req.getParameter("param") == null) {
			return new ModelAndView("sample/guide/guideMain");
		} else {
			return new ModelAndView("sample/guide/" + req.getParameter("param"));
		}

	}

}
