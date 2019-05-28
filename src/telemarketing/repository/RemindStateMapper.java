package telemarketing.repository;

import org.apache.ibatis.annotations.Param;

import telemarketing.model.RemindState;

public interface RemindStateMapper {
    int deleteByPrimaryKey(Integer id);
    
    int deleteByRemindId(Integer id);

    int insert(RemindState record);

    int insertSelective(RemindState record);

    RemindState selectByPrimaryKey(Integer id);
    
    RemindState selectByUserIdRemindId(@Param("remind_id") int remind_id,
    		@Param("user_id") String user_id);

    int updateByPrimaryKeySelective(RemindState record);

    int updateByPrimaryKey(RemindState record);
}