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

//处理ZIP文件的上传
@Service
public class UploadZipService {

	@Autowired
	SoundRecordMapper soundRecordMapper;

	@Autowired
	SystemProperty systemProperty;

	public void handle(Pt_user user, String rootDir, String subDir, String zipName, String remark) {
		Thread thread = new Thread(() -> {
			String zipPath = rootDir + "/" + subDir + "/" + zipName;
			// 解压到批定目录
			String unzipDir = rootDir + "/" + subDir + "/" + UUID.randomUUID().toString() + "/";
			try {
				new File(unzipDir).mkdir();
				ZipUtils.unzip(zipPath, unzipDir);
				// 遍历文件进行处理
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
					soundRecord.setSoundTime(new Date()); // 这部分要修改，要根据文件来解析时间
					soundRecord.setDirection("呼出");
					String filename = originalFilename;
					String[] temp = filename.split("_");
					if (temp.length == 3) {
						if (temp[0].equalsIgnoreCase("O")) {
							soundRecord.setDirection("呼出");
						} else {
							soundRecord.setDirection("呼入");
						}
						soundRecord.setCustomerPhone(temp[1]);
						String soundTime = temp[2].replace(".wav", "");
						soundRecord.setSoundTime(DateUtil.parse(soundTime.length()==14 ?  soundTime : "20"+soundTime, "yyyyMMddHHmmss"));
					} else if (temp.length == 2) {
						soundRecord.setDirection("呼出");
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

				TxtLogger.log("成功处理文件:" + zipPath+" "+user.getPhone()+" "+user.getRealName(), LogTye.INFO, LogFileCreateType.OneFileEveryDay, "UploadZip");
			} catch (Exception ex) {
				TxtLogger.log(ex, LogFileCreateType.OneFileEveryDay, "UploadZip");
			} finally {
				// 删除压纹文件及角标后的目录
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
