package telemarketing.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import telemarketing.model.LoginRecord;

public interface LoginRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LoginRecord record);

    int insertSelective(LoginRecord record);

    LoginRecord selectByPrimaryKey(Integer id);
    
    List<LoginRecord> searchBySql(@Param("sql")String sql);

    int updateByPrimaryKeySelective(LoginRecord record);

    int updateByPrimaryKey(LoginRecord record);
}