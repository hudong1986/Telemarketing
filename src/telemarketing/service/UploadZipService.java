package telemarketing.service;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.NativeWebRequest;

import telemarketing.model.Pt_user;
import telemarketing.model.SoundRecord;
import telemarketing.repository.SoundRecordMapper;
import telemarketing.util.AppendFile;
import telemarketing.util.AudioHelper;
import telemarketing.util.DateUtil;
import telemarketing.util.TxtLogger;
import telemarketing.util.TxtLogger.LogFileCreateType;
import telemarketing.util.TxtLogger.LogTye;
import telemarketing.util.ZipUtils;

//����ZIP�ļ����ϴ�
@Service
public class UploadZipService {

	@Autowired
	SoundRecordMapper soundRecordMapper;

	@Autowired
	SystemProperty systemProperty;

	public void handle(Pt_user user, String rootDir, String subDir, String zipName, String remark) {
		Thread thread = new Thread(() -> {
			String zipPath = rootDir + "/" + subDir + "/" + zipName;
			// ��ѹ������Ŀ¼
			String unzipDir = rootDir + "/" + subDir + "/" + UUID.randomUUID().toString() + "/";
			try {
				new File(unzipDir).mkdir();
				ZipUtils.unzip(zipPath, unzipDir);
				// �����ļ����д���
				List<File> files = AppendFile.getAllFiles(unzipDir);
				for (File file : files) {
					if(file.isDirectory()){
						continue;
					}
					String originalFilename = file.getName();
					SoundRecord soundRecord = new SoundRecord();
					soundRecord.setAddTime(new Date());
					soundRecord.setCustomerPhone("");
					soundRecord.setLocalPhone("");
					soundRecord.setRemark(remark);
					soundRecord.setUserId(user.getPhone());
					soundRecord.setUserName(user.getRealName());
					soundRecord.setUpDownId(user.getDeptId().getUpDownId());
					soundRecord.setSoundTime(new Date()); // �ⲿ��Ҫ�޸ģ�Ҫ�����ļ�������ʱ��
					soundRecord.setDirection("����");
					String filename = originalFilename;
					String[] temp = filename.split("_");
					if (temp.length == 3) {
						if (temp[0].equalsIgnoreCase("O")) {
							soundRecord.setDirection("����");
						} else {
							soundRecord.setDirection("����");
						}
						soundRecord.setCustomerPhone(temp[1]);
						String soundTime = temp[2].replace(".wav", "");
						soundRecord.setSoundTime(DateUtil.parse(soundTime.length()==14 ?  soundTime : "20"+soundTime, "yyyyMMddHHmmss"));
					} else if (temp.length == 2) {
						soundRecord.setDirection("����");
						soundRecord.setCustomerPhone(temp[0]);
						String soundTime = temp[1].replace(".wav", "");
						soundRecord.setSoundTime(DateUtil.parse(soundTime.length()==14 ?  soundTime : "20"+soundTime, "yyyyMMddHHmmss"));

					} else {
						filename = DateUtil.format(new Date(), "yyyyMMddHHmmssSSS") + "-" + user.getPhone()
								+ originalFilename.substring(originalFilename.lastIndexOf('.'));
					}

					if (soundRecord.getCustomerPhone().length() > 11) {
						continue;
					}

					if (originalFilename.contains(".mp3")) {
						soundRecord.setSoundLength(AudioHelper.getMp3Seconds2(file.getPath()));
						file.renameTo(new File(rootDir + "/" + subDir + "/" + filename));
						soundRecord.setFileName(subDir + "/" + filename);
					} else {
						String targetName = rootDir + "/" + subDir + "/"
								+ filename.substring(0, filename.lastIndexOf('.')) + ".mp3";
						AudioHelper.toAudioWithFFmpeg(systemProperty.getFfmpeg_path(), file.getPath(), targetName);
						soundRecord.setSoundLength(AudioHelper.getMp3Seconds2(targetName));
						soundRecord
								.setFileName(subDir + "/" + filename.substring(0, filename.lastIndexOf('.')) + ".mp3");
					}

					soundRecordMapper.insert(soundRecord);
				}

				TxtLogger.log("�ɹ������ļ�:" + zipPath+" "+user.getPhone()+" "+user.getRealName(), LogTye.INFO, LogFileCreateType.OneFileEveryDay, "UploadZip");
			} catch (Exception ex) {
				TxtLogger.log(ex, LogFileCreateType.OneFileEveryDay, "UploadZip");
			} finally {
				// ɾ��ѹ���ļ����Ǳ���Ŀ¼
				try {
					AppendFile.deleteDir(new File(zipPath));
					AppendFile.deleteDir(new File(unzipDir));
				} catch (Exception ex) {

				}
			}
		});

		thread.setDaemon(true);
		thread.start();
	}

}
