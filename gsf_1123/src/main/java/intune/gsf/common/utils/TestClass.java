package intune.gsf.common.utils;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Data
public class TestClass{
	private String msgCd;
	private String tableName;
	private String columnName;
	private String nullYn;
	private String langYn;
	private Timestamp insDt;
	private String insId;
	private Timestamp updDt;
	private String updId;
	private String msg;
	private String pkYn;
	private BigDecimal length;
	private String dataType;
}