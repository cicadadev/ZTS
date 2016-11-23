package gcp.sps.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.sps.model.SpsEventjoin;
import gcp.sps.model.SpsEvent;
import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseSpsEventgift extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String eventId; //이벤트ID		[primary key, primary key, primary key, not null]
	private BigDecimal giftNo; //경품번호		[primary key, primary key, primary key, not null]
	private String name; //경품명		[null]
	private BigDecimal winnerNumber; //당첨인원		[null]

	private List<SpsEventjoin> spsEventjoins;
	private SpsEvent spsEvent;
}