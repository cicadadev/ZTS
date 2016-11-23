/*
 * code https://github.com/jittagornp/excel-object-mapping
 */
package intune.gsf.common.excel.converter;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author redcrow
 */
public class StringTypeConverter implements TypeConverter<String> {

    @Override
    public String convert(Object value, String... pattern) {
        if (value instanceof String) {
            return ((String) value).trim();
        }
        
        if (value instanceof Integer) {
            return (String.valueOf(value)).trim();
        }
        
        if (value instanceof BigDecimal) {
            return (String.valueOf(((BigDecimal) value).setScale(0, RoundingMode.HALF_DOWN))).trim();
        }
        
        return null;
    }

}
