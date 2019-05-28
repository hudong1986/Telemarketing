package telemarketing.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import telemarketing.model.TrackRecord;

public interface TrackRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByCustomer(@Param("id") int id);
    
    int deleteByWhoUse(@Param("who_use") String who_use);
    
    int deleteAllCommonTrack();
    
    int insert(TrackRecord record);

    int insertSelective(TrackRecord record);

    TrackRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TrackRecord record);

    int updateByPrimaryKey(TrackRecord record);
    
    List<TrackRecord> selectByCustomerId(@Param("id") int id);
    
    List<TrackRecord> searchBySql(@Param("sql") String sql);
}