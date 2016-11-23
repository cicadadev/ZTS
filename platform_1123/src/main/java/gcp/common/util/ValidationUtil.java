package gcp.common.util;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SmartValidator;

import com.google.common.collect.Maps;

import gcp.ccs.model.base.BaseCcsField;
import intune.gsf.common.excel.annotation.Excel;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.LocaleUtil;
import intune.gsf.common.utils.MessageUtil;
import intune.gsf.model.FieldVaidation;

/**
 * 
 * @Pagckage Name : intune.gsf.common.utils
 * @FileName : ValidationUtil.java
 * @author : eddie
 * @date : 2016. 4. 20.
 * @description : Validation Util
 */
public class ValidationUtil {
	
	public static interface Insert {
	};

	public static interface Update {
	};

	public static interface Delete {
	};	

	private static List<BaseCcsField> ccsFields;

	public static List<BaseCcsField> getCcsFields() {
		return ccsFields;
	}

	public static void setCcsFields(List<BaseCcsField> ccsFields) {
		ValidationUtil.ccsFields = ccsFields;
	}

	/**
	 * 
	 * @Method Name : isValid
	 * @author : Administrator
	 * @date : 2016. 4. 20.
	 * @description :
	 *
	 * @return
	 */
	public static void isValid(FieldVaidation fieldValue) {

		List<BaseCcsField> fields = getCcsFields();

		Object value = fieldValue.getValues();

		fieldValue.setValid(true);

		for (BaseCcsField field : fields) {
			if (fieldValue.getFieldCd().equals(field.getFieldCd())) {
				if ("N".equals(field.getRequiredYn())) { // Null체크
					if (value == null || "".equals(CommonUtil.trimToEmpty(String.valueOf(value)))) {
						fieldValue.setMessage(MessageUtil.getMessage("ccs.common.errormsg.empty"));
						fieldValue.setValid(false);
					}
				}

				// if ("Y".equals(field.getPkYn())) { // PK체크
				// if ("".equals(CommonUtil.trimToEmpty(String.valueOf(value))))
				// {
				// fieldValue.setMessage(MessageUtil.getMessage("CCS.common.msg.savefail"));
				// fieldValue.setValid(false);
				// }
				// }

				int length = CommonUtil.parseInt(field.getLength(), 0);
				if (length != 0) {
					if (length < String.valueOf(value).length()) { // Column
																	// Size
						fieldValue.setMessage(MessageUtil.getMessage("ccs.common.errormsg.size"));
						fieldValue.setValid(false);
					}
				}
			}
		}
	}

	public static boolean isValid(String value) {

		List<BaseCcsField> fields = getCcsFields();

		for (BaseCcsField field : fields) {

			if (CommonUtil.parseInt(field.getLength(), 0) < value.length()) { // Column
																				// Size
				return false;
			}
		}
		return true;
	}

	public static BaseCcsField getCcsField(String fieldName) {

		List<BaseCcsField> fields = getCcsFields();

		for (BaseCcsField field : fields) {
			if (field.getFieldCd().equals(fieldName)) {
				return field;
			}
		}
		return null;
	}
}
