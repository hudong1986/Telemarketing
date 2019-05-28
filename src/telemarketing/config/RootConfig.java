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
 * һЩ��WEB������ص�����ĵ��������� ��������Դ��
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
	 * ������Դ��Ҫ���ڶ�д����
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
							: environment.getProperty("cypt_db.c3p0_write.url")); // cyptǰ׺��˵���Ǽӹ��ܵ�
			dataSource.setUser(
					db_is_cypt == 1 ? SecretHelper.decodeBase64(environment.getProperty("cypt_db.c3p0.username"))
							: environment.getProperty("cypt_db.c3p0.username"));
			dataSource.setPassword(
					db_is_cypt == 1 ? SecretHelper.decodeBase64(environment.getProperty("cypt_db.c3p0.password"))
							: environment.getProperty("cypt_db.c3p0.password"));
			// �����ӳ���û�п��ÿ�������ʱÿ�ο���������������
			dataSource.setAcquireIncrement(environment.getProperty("c3p0.acquireIncrement", int.class, 5));
			// ���ӳس�ʼ������
			dataSource.setInitialPoolSize(environment.getProperty("c3p0.initialPoolSize", int.class, 5));
			// ���ӳؿɳ��е����������
			dataSource.setMaxPoolSize(environment.getProperty("c3p0.maxPoolSize", int.class, 200));
			// ���ӳؿɳ��е���С������
			dataSource.setMinPoolSize(environment.getProperty("c3p0.minPoolSize", int.class, 5));
			// ���ӳ��е�����ʧЧ�ķ�ֵ(�����δ��ʹ��ʱ��)
			dataSource.setMaxIdleTime(environment.getProperty("c3p0.maxIdleSize", int.class, 1800));
			// ��MaxIdleTime���ʹ�ã�����С��MaxIdleTime��ֵ�����ڼ������ӳ��е�����
			dataSource.setMaxIdleTimeExcessConnections(
					environment.getProperty("c3p0.maxIdleTimeExcessConnections", int.class, 1200));
			// ���������ʱ�䣬�������ʱ�佫���Ͽ�������ʹ�õ�������ʹ����Ϻ󱻶Ͽ�
			dataSource.setMaxConnectionAge(environment.getProperty("c3p0.maxConnectionAge", int.class, 1000));
			// ���п������Ӳ��Ե�SQL
			// dataSource.setPreferredTestQuery(environment.getProperty("c3p0.preferredTestQuery",
			// "select 1 from dual"));
			// ���п������Ӳ��Ե�ʱ����
			dataSource.setIdleConnectionTestPeriod(
					environment.getProperty("c3p0.idleConnectionTestPeriod", int.class, 120));
			return dataSource;
		} catch (Exception ex) {
			TxtLogger.log(ex, LogFileCreateType.OneFileAnHour, "");
			return null;
		}
	}

	/**
	 * ������Դ��Ҫ����ֻ������
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
							: environment.getProperty("cypt_db.c3p0_read.url")); // cyptǰ׺��˵���Ǽӹ��ܵ�
			dataSource.setUser(
					db_is_cypt == 1 ? SecretHelper.decodeBase64(environment.getProperty("cypt_db.c3p0.username"))
							: environment.getProperty("cypt_db.c3p0.username"));
			dataSource.setPassword(
					db_is_cypt == 1 ? SecretHelper.decodeBase64(environment.getProperty("cypt_db.c3p0.password"))
							: environment.getProperty("cypt_db.c3p0.username"));
			// �����ӳ���û�п��ÿ�������ʱÿ�ο���������������
			dataSource.setAcquireIncrement(environment.getProperty("c3p0.acquireIncrement", int.class, 5));
			// ���ӳس�ʼ������
			dataSource.setInitialPoolSize(environment.getProperty("c3p0.initialPoolSize", int.class, 5));
			// ���ӳؿɳ��е����������
			dataSource.setMaxPoolSize(environment.getProperty("c3p0.maxPoolSize", int.class, 200));
			// ���ӳؿɳ��е���С������
			dataSource.setMinPoolSize(environment.getProperty("c3p0.minPoolSize", int.class, 5));
			// ���ӳ��е�����ʧЧ�ķ�ֵ(�����δ��ʹ��ʱ��)
			dataSource.setMaxIdleTime(environment.getProperty("c3p0.maxIdleSize", int.class, 1800));
			// ��MaxIdleTime���ʹ�ã�����С��MaxIdleTime��ֵ�����ڼ������ӳ��е�����
			dataSource.setMaxIdleTimeExcessConnections(
					environment.getProperty("c3p0.maxIdleTimeExcessConnections", int.class, 1200));
			// ���������ʱ�䣬�������ʱ�佫���Ͽ�������ʹ�õ�������ʹ����Ϻ󱻶Ͽ�
			dataSource.setMaxConnectionAge(environment.getProperty("c3p0.maxConnectionAge", int.class, 1000));
			// ���п������Ӳ��Ե�SQL
			// dataSource.setPreferredTestQuery(environment.getProperty("c3p0.preferredTestQuery",
			// "select 1 from dual"));
			// ���п������Ӳ��Ե�ʱ����
			dataSource.setIdleConnectionTestPeriod(
					environment.getProperty("c3p0.idleConnectionTestPeriod", int.class, 120));
			return dataSource;
		} catch (Exception ex) {
			TxtLogger.log(ex, LogFileCreateType.OneFileAnHour, "");
			return null;
		}
	}

	// ���¶���һ����̬ѡ�������Դ
	@Bean
	public DataSource dataSource() {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("dataSource_write", dataSourceWrite());
		map.put("dataSource_read", dataSourceRead());
		DynamicDataSource dataSource = new DynamicDataSource();
		dataSource.setTargetDataSources(map);
		dataSource.setDefaultTargetDataSource(dataSourceWrite()); // Ĭ��д��
		return dataSource;
	}

}
