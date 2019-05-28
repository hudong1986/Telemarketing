package telemarketing.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import telemarketing.model.SoundRecord;
import telemarketing.model.SoundStatics;

public interface SoundRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SoundRecord record);

    int insertSelective(SoundRecord record);

    SoundRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SoundRecord record);

    int updateByPrimaryKey(SoundRecord record);
    
    List<SoundRecord> searchBySql(@Param("sql") String sql);
    
    List<SoundStatics> searchGroupBySql(@Param("sql")String sql);
}