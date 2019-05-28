package telemarketing.config;

import java.util.Base64;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import telemarketing.db.DynamicDataSource;
import telemarketing.util.SecretHelper;
import telemarketing.util.TxtLogger;
import telemarketing.util.TxtLogger.LogFileCreateType;

/**
 * 一些除WEB核心相关的以外的第三方配置 比如数据源等
 * 
 * @author huchengdong
 *
 */
@ImportResource({ "/WEB-INF/spring_root.xml" })
@Configuration
@ComponentScan(basePackages = { "telemarketing" }, excludeFilters = {
		@Filter(type = FilterType.ANNOTATION, value = EnableWebMvc.class) })
@PropertySource("classpath:db.properties")
public class RootConfig {

	@Autowired
	Environment environment;

	/**
	 * 此数据源主要用于读写操作
	 * 
	 * @return
	 */
	public DataSource dataSourceWrite() {
		try {
			int db_is_cypt = Integer.parseInt(environment.getProperty("db_is_cypt"));
			ComboPooledDataSource dataSource = new ComboPooledDataSource();
			dataSource.setDriverClass(environment.getProperty("db.c3p0.driverClassName"));
			dataSource.setJdbcUrl(
					db_is_cypt == 1 ? SecretHelper.decodeBase64(environment.getProperty("cypt_db.c3p0_write.url"))
							: environment.getProperty("cypt_db.c3p0_write.url")); // cypt前缀的说明是加过密的
			dataSource.setUser(
					db_is_cypt == 1 ? SecretHelper.decodeBase64(environment.getProperty("cypt_db.c3p0.username"))
							: environment.getProperty("cypt_db.c3p0.username"));
			dataSource.setPassword(
					db_is_cypt == 1 ? SecretHelper.decodeBase64(environment.getProperty("cypt_db.c3p0.password"))
							: environment.getProperty("cypt_db.c3p0.password"));
			// 当连接池在没有可用空闲连接时每次可以新增的连接数
			dataSource.setAcquireIncrement(environment.getProperty("c3p0.acquireIncrement", int.class, 5));
			// 连接池初始连接数
			dataSource.setInitialPoolSize(environment.getProperty("c3p0.initialPoolSize", int.class, 5));
			// 连接池可持有的最大连接数
			dataSource.setMaxPoolSize(environment.getProperty("c3p0.maxPoolSize", int.class, 200));
			// 连接池可持有的最小连接数
			dataSource.setMinPoolSize(environment.getProperty("c3p0.minPoolSize", int.class, 5));
			// 连接池中的连接失效的阀值(即最大未被使用时长)
			dataSource.setMaxIdleTime(environment.getProperty("c3p0.maxIdleSize", int.class, 1800));
			// 与MaxIdleTime配合使用，必须小于MaxIdleTime的值，用于减少连接池中的连接
			dataSource.setMaxIdleTimeExcessConnections(
					environment.getProperty("c3p0.maxIdleTimeExcessConnections", int.class, 1200));
			// 连接最大存活时间，超过这个时间将被断开，正在使用的连接在使用完毕后被断开
			dataSource.setMaxConnectionAge(environment.getProperty("c3p0.maxConnectionAge", int.class, 1000));
			// 进行空闲连接测试的SQL
			// dataSource.setPreferredTestQuery(environment.getProperty("c3p0.preferredTestQuery",
			// "select 1 from dual"));
			// 进行空闲连接测试的时间间隔
			dataSource.setIdleConnectionTestPeriod(
					environment.getProperty("c3p0.idleConnectionTestPeriod", int.class, 120));
			return dataSource;
		} catch (Exception ex) {
			TxtLogger.log(ex, LogFileCreateType.OneFileAnHour, "");
			return null;
		}
	}

	/**
	 * 此数据源主要用于只读操作
	 * 
	 * @return
	 */
	public DataSource dataSourceRead() {
		try {
			int db_is_cypt = Integer.parseInt(environment.getProperty("db_is_cypt"));
			ComboPooledDataSource dataSource = new ComboPooledDataSource();
			dataSource.setDriverClass(environment.getProperty("db.c3p0.driverClassName"));
			dataSource.setJdbcUrl(
					db_is_cypt == 1 ? SecretHelper.decodeBase64(environment.getProperty("cypt_db.c3p0_read.url"))
							: environment.getProperty("cypt_db.c3p0_read.url")); // cypt前缀的说明是加过密的
			dataSource.setUser(
					db_is_cypt == 1 ? SecretHelper.decodeBase64(environment.getProperty("cypt_db.c3p0.username"))
							: environment.getProperty("cypt_db.c3p0.username"));
			dataSource.setPassword(
					db_is_cypt == 1 ? SecretHelper.decodeBase64(environment.getProperty("cypt_db.c3p0.password"))
							: environment.getProperty("cypt_db.c3p0.username"));
			// 当连接池在没有可用空闲连接时每次可以新增的连接数
			dataSource.setAcquireIncrement(environment.getProperty("c3p0.acquireIncrement", int.class, 5));
			// 连接池初始连接数
			dataSource.setInitialPoolSize(environment.getProperty("c3p0.initialPoolSize", int.class, 5));
			// 连接池可持有的最大连接数
			dataSource.setMaxPoolSize(environment.getProperty("c3p0.maxPoolSize", int.class, 200));
			// 连接池可持有的最小连接数
			dataSource.setMinPoolSize(environment.getProperty("c3p0.minPoolSize", int.class, 5));
			// 连接池中的连接失效的阀值(即最大未被使用时长)
			dataSource.setMaxIdleTime(environment.getProperty("c3p0.maxIdleSize", int.class, 1800));
			// 与MaxIdleTime配合使用，必须小于MaxIdleTime的值，用于减少连接池中的连接
			dataSource.setMaxIdleTimeExcessConnections(
					environment.getProperty("c3p0.maxIdleTimeExcessConnections", int.class, 1200));
			// 连接最大存活时间，超过这个时间将被断开，正在使用的连接在使用完毕后被断开
			dataSource.setMaxConnectionAge(environment.getProperty("c3p0.maxConnectionAge", int.class, 1000));
			// 进行空闲连接测试的SQL
			// dataSource.setPreferredTestQuery(environment.getProperty("c3p0.preferredTestQuery",
			// "select 1 from dual"));
			// 进行空闲连接测试的时间间隔
			dataSource.setIdleConnectionTestPeriod(
					environment.getProperty("c3p0.idleConnectionTestPeriod", int.class, 120));
			return dataSource;
		} catch (Exception ex) {
			TxtLogger.log(ex, LogFileCreateType.OneFileAnHour, "");
			return null;
		}
	}

	// 以下定义一个动态选择的数据源
	@Bean
	public DataSource dataSource() {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("dataSource_write", dataSourceWrite());
		map.put("dataSource_read", dataSourceRead());
		DynamicDataSource dataSource = new DynamicDataSource();
		dataSource.setTargetDataSources(map);
		dataSource.setDefaultTargetDataSource(dataSourceWrite()); // 默认写库
		return dataSource;
	}

}
