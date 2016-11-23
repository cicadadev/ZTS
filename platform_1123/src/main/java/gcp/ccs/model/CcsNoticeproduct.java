package gcp.ccs.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

import gcp.ccs.model.base.BaseCcsNoticeproduct;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class CcsNoticeproduct extends BaseCcsNoticeproduct {
	private List<String> productIdList;
}