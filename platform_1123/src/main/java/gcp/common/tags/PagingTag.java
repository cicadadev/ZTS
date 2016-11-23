package gcp.common.tags;

import javax.servlet.jsp.JspWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

import lombok.Data;

@Data
public class PagingTag extends RequestContextAwareTag {
	private final Log logger = LogFactory.getLog(getClass());
	private static final long serialVersionUID = 7595181584158469126L;

	// 파라메터 영역
	private int currentPage; // 현재 페이지
	private int pageSize; // 한페이지당 아이템 갯수
	private int total; // 아이템 총 갯수

	private String formId; // [필수] formsubmit 대상ID
	private String url; // [필수] : 페이지URL
	private String type; // [옵션] : ajax
	private String callback; // [옵션] : type이 ajax일때 필수

	// local 변수
	private int maxPage; // 페이지 전체의 마지막페이지
	private int startPage; // 시작페이지
	private int endPage; // 페이지 단위의 마지막페이지
	private int perPageNavi = 10; // 페이지 단위

	public String getOptionStr() {
		return "pageSize:" + pageSize + ",formId:" + formId + ",url:" + url + ",type:" + type + ",callback:" + callback;
	}

	@Override
	protected int doStartTagInternal() throws Exception {

		StringBuilder sb = new StringBuilder();

		if (total > 0) {
			maxPage = total / pageSize;

			if (total % pageSize != 0) {
				maxPage++;
			}

			if (currentPage % perPageNavi == 1) {
				startPage = currentPage;
			} else if (currentPage % perPageNavi == 0) {
				startPage = ((int) (currentPage / perPageNavi) - 1) * perPageNavi + 1;
			} else {
				startPage = ((int) (currentPage / perPageNavi)) * perPageNavi + 1;
			}

			endPage = startPage + perPageNavi - 1;
			if (endPage > maxPage) {
				endPage = maxPage;
			}

			logger.debug("maxPage:" + maxPage);
			logger.debug("startPage:" + startPage);
			logger.debug("endPage:" + endPage);
			logger.debug("currentPage:" + currentPage);
			logger.debug("total:" + total);
			logger.debug("pageSize:" + pageSize);

			sb.append(getFirstLink());
			sb.append(" ");
			sb.append(getPrevLink());
			sb.append("<div>");

			for (int i = startPage; i <= endPage; i++) {

				if (currentPage != i) {
					sb.append("<a href=\"javascript:void(0);\" onclick=\"common.goPage('currentPage:" + i + "," + getOptionStr() + "');\">");
					sb.append(i);
					sb.append("</a>");
				} else {
					sb.append("<strong>");
					sb.append(i);
					sb.append("</strong>");
				}
			}
			sb.append("</div>");

			sb.append(getNextLink());
			sb.append(" ");
			sb.append(getLastLink());
		}

		JspWriter out = pageContext.getOut();
		out.write(sb.toString());

		return EVAL_BODY_INCLUDE;
	}

	private String getFirstLink() {
		return "<a href=\"javascript:void(0);\" onclick=\"common.goPage('currentPage:1," + getOptionStr() + "');\" class=\"btn first\">처음</a>";
//		if (currentPage > 1) {
//			return "<a href=\"javascript:void(0);\" onclick=\"common.goPage('currentPage:1," + getOptionStr() + "');\" class=\"btn first\">처음</a>";
//		} else {
//			// return "<a href=\"javascript:void(0);\" class=\"btn first\">처음</a>";
//			return "";
//		}
	}

	private String getPrevLink() {
		String button = "";
		if (currentPage > perPageNavi) {
			button = "<a href=\"javascript:void(0);\" onclick=\"common.goPage('currentPage:" + (startPage - 10) + "," + getOptionStr()
					+ "');\" class=\"btn prev\">이전</a>";
		} else if (currentPage <= perPageNavi) {
			 button = "<a href=\"javascript:void(0);\" class=\"btn prev\">이전</a>";
		}
		return button;
	}

	private String getNextLink() {
		String button = "";
		int std = (int) Math.ceil(currentPage - ((maxPage / perPageNavi) * perPageNavi));
		if (std < 1) {
			button = "<a href=\"javascript:void(0);\" onclick=\"common.goPage('currentPage:" + (endPage + 1) + "," + getOptionStr()
					+ "');\" class=\"btn next\">다음</a>";
		} else if (std >= 1) {
			button =  "<a href=\"javascript:void(0);\" class=\"btn next\">다음</a>";
		}
		return button;
	}

	private String getLastLink() {
		return "<a href=\"javascript:void(0);\" onclick=\"common.goPage('currentPage:" + maxPage + "," + getOptionStr() + "');\" class=\"btn last\">마지막</a>";
//		if (currentPage < maxPage) {
//			return "<a href=\"javascript:void(0);\" onclick=\"common.goPage('currentPage:" + maxPage + "," + getOptionStr() + "');\" class=\"btn last\">마지막</a>";
//		} else {
//			// return "<a href=\"javascript:void(0);\" class=\"btn last\">마지막</a>";
//			return "";
//		}
	}
}
