package gcp.external.model.coupon;

import java.util.List;

import lombok.Data;

@Data
public class CampaignCoupon {

	private List<TargetMember>	targetMembers;
	private String				campaignId;
	private String				couponId;
	private String				storeId;
	private String				campaignOrder;
	private String				useStartDt;
	private String				useEndDt;
}
