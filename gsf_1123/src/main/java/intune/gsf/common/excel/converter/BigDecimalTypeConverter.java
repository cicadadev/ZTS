/*
 * code https://github.com/jittagornp/excel-object-mapping
 */
package intune.gsf.common.excel.converter;

import java.math.BigDecimal;

/**
 * @author redcrow
 */
public class BigDecimalTypeConverter implements TypeConverter<BigDecimal> {

	@Override
	public BigDecimal convert(Object value, String... pattern) {
		if (value == null) {
			return null;
		}

		// return ((BigDecimal) value);
		return new BigDecimal(String.valueOf(value));
	}

}
