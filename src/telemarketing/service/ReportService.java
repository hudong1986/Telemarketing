package telemarketing.service;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;
import org.springframework.stereotype.Service;

import com.sun.org.apache.xpath.internal.operations.And;

import telemarketing.model.SoundRecord;
import telemarketing.repository.ReportMapper;
import telemarketing.repository.SoundRecordMapper;
import telemarketing.util.DateUtil;
import telemarketing.util.TxtLogger;
import telemarketing.util.TxtLogger.LogFileCreateType;
import telemarketing.util.TxtLogger.LogTye;

@Service
public class ReportService {

	@Autowired
	ReportMapper reportMapper;

	@Autowired
	SoundRecordMapper soundRecordMapper;
	
	@Autowired
	SystemProperty systemProperty;

	// 在线程处理，每天统计头一天的业务数据
	@PostConstruct
	private void backWork() {

		// 统计贷款历史
		Thread thread = new Thread(() -> {

			while (true) {
				try {
					Calendar calendar = Calendar.getInstance();
					if (calendar.get(Calendar.HOUR_OF_DAY) < 1 || calendar.get(Calendar.HOUR_OF_DAY) > 5) {
						continue;
					}

					Date endDate = DateUtil.parse(DateUtil.format(DateUtil.addDay(new Date(), -1)));

					// 获取当前统计数据表中最大日期
					Date lastDateInDB = reportMapper.selectMaxDate();
					if (lastDateInDB == null) {
						lastDateInDB = DateUtil.parse(DateUtil.format(DateUtil.addDay(new Date(), -120)));
					} else {
						lastDateInDB = DateUtil.addDay(lastDateInDB, 1);
					}

					while (lastDateInDB.getTime() <= endDate.getTime()) {
						reportMapper.insertByDate(DateUtil.format(lastDateInDB));
						// 下一天
						lastDateInDB = DateUtil.addDay(lastDateInDB, 1);
					}

				} catch (Exception ex) {
					TxtLogger.log(ex.toString(), LogTye.ERROR, LogFileCreateType.OneFileAnHour, "Statistics");
					ex.printStackTrace();
				} finally {
					try {
						Thread.sleep(60000);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		thread.setDaemon(true);
		thread.start();

		// 统计电话录音历史
		Thread thread2 = new Thread(() -> {

			while (true) {
				try {

					Calendar calendar = Calendar.getInstance();
					if (calendar.get(Calendar.HOUR_OF_DAY) < 1 || calendar.get(Calendar.HOUR_OF_DAY) > 5) {
						continue;
					}

					Date endDate = DateUtil.parse(DateUtil.format(DateUtil.addDay(new Date(), -1)));

					// 获取当前统计数据表中最大日期
					Date lastDateInDB = reportMapper.selectSoundMaxDate();
					if (lastDateInDB == null) {
						lastDateInDB = DateUtil.parse(DateUtil.format(DateUtil.addDay(new Date(), -120)));
					} else {
						lastDateInDB = DateUtil.addDay(lastDateInDB, 1);
					}

					while (lastDateInDB.getTime() <= endDate.getTime()) {
						reportMapper.insertSoundByDate(DateUtil.format(lastDateInDB));
						// 统计一次就删除不必要的文件

						// 下一天
						lastDateInDB = DateUtil.addDay(lastDateInDB, 1);
					}

				} catch (Exception ex) {
					TxtLogger.log(ex.toString(), LogTye.ERROR, LogFileCreateType.OneFileAnHour, "Statistics_sound");
					ex.printStackTrace();
				} finally {
					try {
						Thread.sleep(60000);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		thread2.setDaemon(true);
		thread2.start();

		// 删除时长小于45秒的电话录音
		Thread thread3 = new Thread(() -> {

			while (true) {
				try {

					// 删除2天前的
					String dateStr = DateUtil.format(DateUtil.addDay(new Date(), -1));
					String sql = String.format("select * from sound_record where sound_length<=45 "
							+ " and add_time < '%s' limit 2000", dateStr);
					List<SoundRecord> list = soundRecordMapper.searchBySql(sql);
					if (list != null && list.size() > 0) {
						for (SoundRecord soundRecord : list) {
							new File(systemProperty.getSound_record_path()+"/"+soundRecord.getFileName()).delete();
							soundRecordMapper.deleteByPrimaryKey(soundRecord.getId());
						}
						
						TxtLogger.log("成功删除指定日期前的录音文件,指定日期:" + dateStr+" 数量:"+list.size(), LogTye.INFO, LogFileCreateType.OneFileAnHour,
								"del_sound");
					}
				} catch (Exception ex) {
					TxtLogger.log(ex.toString(), LogTye.ERROR, LogFileCreateType.OneFileAnHour, "del_sound");
					ex.printStackTrace();
				} finally {
					try {
						Thread.sleep(5000);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		thread3.setDaemon(true);
		thread3.start();

	}

}
