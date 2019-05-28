package telemarketing.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;

public class AudioHelper {

	public static int getMp3Seconds2(String filePath) {
		int t = 0;
		try {
			MP3File f = (MP3File) AudioFileIO.read(new File(filePath));
			MP3AudioHeader audioHeader = (MP3AudioHeader) f.getAudioHeader();
			t = audioHeader.getTrackLength();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

	// 文件路径与码特率
	public static int getMP3Seconds(String filePath, int kbps) {

		File file = new File(filePath);
		return (int) (file.length() * 8 / (kbps * 1000));
	}

	public static int getWavSeconds(String filePath)
			throws LineUnavailableException, UnsupportedAudioFileException, IOException {

		File file = new File(filePath);
		Clip clip = AudioSystem.getClip();
		AudioInputStream ais = AudioSystem.getAudioInputStream(file);
		try {
			clip.open(ais);
			return (int) (clip.getMicrosecondLength() / 1000000);// 获取音频文件时长
																	// second
		} finally {
			ais.close();
		}
	}

	public static int getWavSeconds(InputStream stream)
			throws LineUnavailableException, UnsupportedAudioFileException, IOException {
		Clip clip = AudioSystem.getClip();
		AudioInputStream ais = AudioSystem.getAudioInputStream(stream);
		try {
		clip.open(ais);
		return (int) (clip.getMicrosecondLength() / 1000000);// 获取音频文件时长 second
		}
		finally{
			ais.close();
		}
	
	}

	/**
	 * 将上传的录音转为mp3格式
	 * 
	 * @param webroot
	 *            项目的根目录
	 * @param sourcePath
	 *            文件的相对地址
	 */
	public static void toAudioWithFFmpeg(String ffmpegPath, String sourcePath, String targetPath) {
		File file = new File(sourcePath);
		Runtime run = null;
		try {
			run = Runtime.getRuntime();
			long start = System.currentTimeMillis();
			Process p = run.exec(ffmpegPath + "/ffmpeg -i " + sourcePath + " -acodec libmp3lame " + targetPath);// 执行ffmpeg.exe,前面是ffmpeg.exe的地址，中间是需要转换的文件地址，后面是转换后的文件地址。-i是转换方式，意思是可编码解码，mp3编码方式采用的是libmp3lame
			// 释放进程
			p.getOutputStream().close();
			p.getInputStream().close();
			p.getErrorStream().close();
			p.waitFor();
			long end = System.currentTimeMillis();
			System.out.println(sourcePath + " convert success, costs:" + (end - start) + "ms");
			// 删除原来的文件
			if (file.exists()) {
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// run调用lame解码器最后释放内存
			run.freeMemory();
		}
	}

}
