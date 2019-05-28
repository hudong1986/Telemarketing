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

	// ���̴߳���ÿ��ͳ��ͷһ���ҵ������
	@PostConstruct
	private void backWork() {

		// ͳ�ƴ�����ʷ
		Thread thread = new Thread(() -> {

			while (true) {
				try {
					Calendar calendar = Calendar.getInstance();
					if (calendar.get(Calendar.HOUR_OF_DAY) < 1 || calendar.get(Calendar.HOUR_OF_DAY) > 5) {
						continue;
					}

					Date endDate = DateUtil.parse(DateUtil.format(DateUtil.addDay(new Date(), -1)));

					// ��ȡ��ǰͳ�����ݱ����������
					Date lastDateInDB = reportMapper.selectMaxDate();
					if (lastDateInDB == null) {
						lastDateInDB = DateUtil.parse(DateUtil.format(DateUtil.addDay(new Date(), -120)));
					} else {
						lastDateInDB = DateUtil.addDay(lastDateInDB, 1);
					}

					while (lastDateInDB.getTime() <= endDate.getTime()) {
						reportMapper.insertByDate(DateUtil.format(lastDateInDB));
						// ��һ��
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

		// ͳ�Ƶ绰¼����ʷ
		Thread thread2 = new Thread(() -> {

			while (true) {
				try {

					Calendar calendar = Calendar.getInstance();
					if (calendar.get(Calendar.HOUR_OF_DAY) < 1 || calendar.get(Calendar.HOUR_OF_DAY) > 5) {
						continue;
					}

					Date endDate = DateUtil.parse(DateUtil.format(DateUtil.addDay(new Date(), -1)));

					// ��ȡ��ǰͳ�����ݱ����������
					Date lastDateInDB = reportMapper.selectSoundMaxDate();
					if (lastDateInDB == null) {
						lastDateInDB = DateUtil.parse(DateUtil.format(DateUtil.addDay(new Date(), -120)));
					} else {
						lastDateInDB = DateUtil.addDay(lastDateInDB, 1);
					}

					while (lastDateInDB.getTime() <= endDate.getTime()) {
						reportMapper.insertSoundByDate(DateUtil.format(lastDateInDB));
						// ͳ��һ�ξ�ɾ������Ҫ���ļ�

						// ��һ��
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

		// ɾ��ʱ��С��45��ĵ绰¼��
		Thread thread3 = new Thread(() -> {

			while (true) {
				try {

					// ɾ��2��ǰ��
					String dateStr = DateUtil.format(DateUtil.addDay(new Date(), -1));
					String sql = String.format("select * from sound_record where sound_length<=45 "
							+ " and add_time < '%s' limit 2000", dateStr);
					List<SoundRecord> list = soundRecordMapper.searchBySql(sql);
					if (list != null && list.size() > 0) {
						for (SoundRecord soundRecord : list) {
							new File(systemProperty.getSound_record_path()+"/"+soundRecord.getFileName()).delete();
							soundRecordMapper.deleteByPrimaryKey(soundRecord.getId());
						}
						
						TxtLogger.log("�ɹ�ɾ��ָ������ǰ��¼���ļ�,ָ������:" + dateStr+" ����:"+list.size(), LogTye.INFO, LogFileCreateType.OneFileAnHour,
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
