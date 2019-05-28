package telemarketing.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;

import telemarketing.util.TxtLogger;


@Order(1)
public class MyWebApplicationInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		//设置日志目录
		TxtLogger.SetRootDir(servletContext.getContextPath());
		CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
		encodingFilter.setEncoding("UTF-8");
		encodingFilter.setForceEncoding(true);
		
		 
		servletContext.addFilter("characterEncodingFilter", encodingFilter)
		.addMappingForUrlPatterns(null , false, "/*");
		servletContext.addFilter("hiddenMethodFilter", HiddenHttpMethodFilter.class)
		.addMappingForUrlPatterns(null , false, "/*");

	}

}
