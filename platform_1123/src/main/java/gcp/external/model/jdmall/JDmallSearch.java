package gcp.external.model.jdmall;

import java.util.List;

import lombok.Data;

@Data
public class JDmallSearch {
	//오더총갯수
	private int					order_total;
	//오더리스트
	private List<JDmallOrder>	order_info_list;
}
