package gcp.mms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.mms.model.MmsWishlist;
import gcp.mms.model.MmsMemberZts;
import gcp.mms.model.MmsWishlist;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseMmsWishlist extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private BigDecimal memberNo; //회원번호		[primary key, primary key, primary key, not null]
	private BigDecimal wishlistNo; //위시리스트번호		[primary key, primary key, primary key, not null]
	private BigDecimal upperWishlistNo; //상위위시리스트번호		[null]
	private String wishlistProductTypeCd; //위시리스트상품유형코드		[not null]
	private String storeId; //상점ID		[null]
	private String productId; //상품ID		[not null]
	private String saleproductId; //단품ID		[null]

	private List<MmsWishlist> mmsWishlists;
	private MmsMemberZts mmsMemberZts;
	private MmsWishlist mmsWishlist;

	public String getWishlistProductTypeName(){
			return CodeUtil.getCodeName("WISHLIST_PRODUCT_TYPE_CD", getWishlistProductTypeCd());
	}
}