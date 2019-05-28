package telemarketing.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import telemarketing.model.Remind;

public interface RemindMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Remind record);

    int insertSelective(Remind record);

    Remind selectByPrimaryKey(Integer id);
    
    List<Remind> selectUnRead(@Param("userId") String userId,@Param("upDownId") String upDownId);
    
    List<Remind> searchBySql(@Param("sql") String sql);
 
    int updateByPrimaryKeySelective(Remind record);

    int updateByPrimaryKey(Remind record);
}