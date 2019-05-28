package telemarketing.db;

public class DataSourceContextHolder {
	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();  
    /** 
     * @Description: ��������Դ���� 
     * @param dataSourceType  ���ݿ����� 
     * @return void 
     * @throws 
     */  
    public static void setDataSourceType(String dataSourceType) {  
        contextHolder.set(dataSourceType);  
    }  
      
    /** 
     * @Description: ��ȡ����Դ���� 
     * @param  
     * @return String 
     * @throws 
     */  
    public static String getDataSourceType() {  
        return contextHolder.get();  
    }  
      
    /** 
     * @Description: �������Դ���� 
     * @param  
     * @return void 
     * @throws 
     */  
    public static void clearDataSourceType() {  
        contextHolder.remove();  
    }  
}
