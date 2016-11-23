package gcp.external.model.tmall;

import java.util.List;

import lombok.Data;

@Data
public class TmallResponse {
	//총주문수
	private String				total_count;
	//총페이지수
	private String				total_page;
	//주문정보
	private List<TmallOrder>	orders;
}
