package telemarketing.repository;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import telemarketing.model.BusiReport;
import telemarketing.model.SoundStatics;
import telemarketing.model.TrackReport;

public interface ReportMapper {

	List<BusiReport> searchBusiByDate(@Param("dateStr") String date,@Param("up_down_id") String up_down_id,@Param("user_name")String user_name);
	
	List<BusiReport> searchBySql(@Param("sql")String sql);
	
	List<SoundStatics> searchSoundBySql(@Param("sql")String sql);
	
	List<TrackReport> searchTrackReportByDate(@Param("dateStr") String date,@Param("up_down_id") String up_down_id,
			@Param("user_name")String user_name);
	
	List<BusiReport> searchGroupByDate(@Param("date1") String date1,@Param("date2") String date2,@Param("up_down_id") String up_down_id,
			@Param("business_name") String business_name,@Param("state") int state,@Param("user_name")String user_name);
	
	List<BusiReport> searchGroupByMonth(@Param("date1") String date1,@Param("date2") String date2,@Param("up_down_id") String up_down_id,
			@Param("business_name") String business_name,@Param("state") int state,@Param("user_name")String user_name);
	
	int insert(BusiReport report);
	
	Date selectMaxDate();
	Date selectSoundMaxDate();
	
	int insertByDate(@Param("dateStr") String dateStr);
	
	int insertSoundByDate(@Param("dateStr") String dateStr);
	
}
