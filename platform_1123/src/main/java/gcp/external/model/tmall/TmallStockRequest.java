package gcp.external.model.tmall;

import lombok.Data;

@Data
public class TmallStockRequest {
	//단품ID
	private String	item_vdr_code;
	//재고수량
	private String	stk_num;
	//재고연동방식: 전체수량(1), 증가수량(2)
	private String	type;
}
