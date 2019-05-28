package telemarketing.db;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

//���ݿ�·��
public class DynamicDataSource extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		return DataSourceContextHolder.getDataSourceType();

	}

}
