package telemarketing.db;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//定义一个关于数据源选择的注解
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyDataSource {
	String name() default MyDataSource.write;

	public static String write = "dataSource_write";

	public static String read = "dataSource_read";
}
