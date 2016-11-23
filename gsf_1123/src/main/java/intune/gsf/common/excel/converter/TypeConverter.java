/*
 * code https://github.com/jittagornp/excel-object-mapping
 */
package intune.gsf.common.excel.converter;

/**
 * @author redcrow
 */
public interface TypeConverter<T> {

    T convert(Object value, String... pattern);
}
