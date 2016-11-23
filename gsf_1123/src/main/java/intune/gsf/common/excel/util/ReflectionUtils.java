/*
 * code https://github.com/jittagornp/excel-object-mapping
 */
package intune.gsf.common.excel.util;

import static intune.gsf.common.excel.util.CollectionUtils.isEmpty;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import intune.gsf.common.excel.annotation.Excel;
import intune.gsf.common.excel.converter.TypeConverter;
import intune.gsf.common.excel.converter.TypeConverters;

/**
 * @author redcrow
 */
public class ReflectionUtils {

	// private static final Logger LOG =
	// LoggerFactory.getLogger(ReflectionUtils.class);

	private static String toUpperCaseFirstCharacter(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void setValueOnField(Object instance, Field field, Object cellValue) throws Exception {
		Class clazz = instance.getClass();
		String setMethodName = "set" + toUpperCaseFirstCharacter(field.getName());

		for (Map.Entry<Class, TypeConverter> entry : TypeConverters.getConverter().entrySet()) {
			if (field.getType().equals(entry.getKey())) {
				Method method = clazz.getDeclaredMethod(setMethodName, entry.getKey());

				Excel excel = field.getAnnotation(Excel.class);
				if (excel != null && excel.codegroup().length() > 0) {
					cellValue = excel.codegroup() + "." + cellValue;
				}
//				method.invoke(instance, entry.getValue().convert(cellValue, excel == null ? null : excel.pattern()));
				method.invoke(instance, entry.getValue().convert(cellValue, null));
			}
		}
	}
	
	public static void eachFields(Class clazz, EachFieldCallback callback) throws Exception {
		Field[] fields = clazz.getDeclaredFields();
		if (!isEmpty(fields)) {
			for (Field field : fields) {
				callback.each(field, field.isAnnotationPresent(Excel.class) ? field.getAnnotation(Excel.class).name() : field.getName());
			}
		}
	}
}
