package telemarketing.db;

import java.lang.reflect.Method;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;

//定论一个针对数据源选择的切面通知
public class MyDataSourceAspect implements MethodBeforeAdvice, AfterReturningAdvice {

	@Override
	public void afterReturning(Object arg0, Method arg1, Object[] arg2, Object arg3) throws Throwable {
	 
		DataSourceContextHolder.clearDataSourceType();
	}

	@Override
	public void before(Method arg0, Object[] arg1, Object arg2) throws Throwable {
		 if(arg0.isAnnotationPresent(MyDataSource.class)){
			 MyDataSource source =  arg0.getAnnotation(MyDataSource.class);
			 DataSourceContextHolder.setDataSourceType(source.name());
			 
		 }
		 else
		 {
			 DataSourceContextHolder.setDataSourceType(MyDataSource.write);
		 }

	}

}
