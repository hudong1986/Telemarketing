package telemarketing.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import telemarketing.model.Customer;

public interface CustomerMapper {
    int deleteByPrimaryKey(Integer id);
    int deleteByWhoUse(@Param("who_use") String who_use);
    int deleteAllCommon();
    
    int insert(Customer record);
    int insertToBackup(Customer record);
    Customer selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(Customer record);
    
    List<Customer> searchBySql(Map<String, String> map);
    
    int updateState(Map<String, Object> map);
    
    int updateWhoUse(Map<String, Object> map);
    
    int updateToCommon(Map<String, Object> map);
    
    int updateUpdownId(@Param("who_up_down_id") String who_up_down_id, @Param("who_use") String who_use);
    
    Customer selectByPhone(@Param("phone") String phone);
    Customer selectNext(@Param("id") int id,@Param("who_use") String who_use);
    
    int countBySql(@Param("sql")String sql);
    
    int recoveryCustomer(@Param("list") List<Integer> list,@Param("who_use")String who_use,
    		@Param("who_use_name")String who_use_name,@Param("who_get_time")Date who_get_time,
    		@Param("who_up_down_id")String who_up_down_id);

}