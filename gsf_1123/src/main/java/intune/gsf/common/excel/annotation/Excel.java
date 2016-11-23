/*
 * code https://github.com/jittagornp/excel-object-mapping
 */
package intune.gsf.common.excel.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author redcrow
 */
@Target(ElementType.FIELD)
@Retention(RUNTIME)
public @interface Excel {

//	int MODEL_DEPTH_1 = 1;
//	int MODEL_DEPTH_2_OBJECT = 2;
//	int MODEL_DEPTH_2_LIST = 3;
//	int MODEL_DEPTH_3_OBJECT = 4;
//	int MODEL_DEPTH_3_LIST = 5;

	String name() default "";

	String pattern() default "";

	String target() default "";
	
	String codegroup() default "";

//	int type() default 1;

//	boolean pk() default false;

//	String[] join() default {};
}
