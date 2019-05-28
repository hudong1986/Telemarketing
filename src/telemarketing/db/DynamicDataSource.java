package telemarketing.db;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

//数据库路由
public class DynamicDataSource extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		return DataSourceContextHolder.getDataSourceType();

	}

}
