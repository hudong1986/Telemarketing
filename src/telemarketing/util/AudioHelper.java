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

	// �ļ�·����������
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
			return (int) (clip.getMicrosecondLength() / 1000000);// ��ȡ��Ƶ�ļ�ʱ��
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
		return (int) (clip.getMicrosecondLength() / 1000000);// ��ȡ��Ƶ�ļ�ʱ�� second
		}
		finally{
			ais.close();
		}
	
	}

	/**
	 * ���ϴ���¼��תΪmp3��ʽ
	 * 
	 * @param webroot
	 *            ��Ŀ�ĸ�Ŀ¼
	 * @param sourcePath
	 *            �ļ�����Ե�ַ
	 */
	public static void toAudioWithFFmpeg(String ffmpegPath, String sourcePath, String targetPath) {
		File file = new File(sourcePath);
		Runtime run = null;
		try {
			run = Runtime.getRuntime();
			long start = System.currentTimeMillis();
			Process p = run.exec(ffmpegPath + "/ffmpeg -i " + sourcePath + " -acodec libmp3lame " + targetPath);// ִ��ffmpeg.exe,ǰ����ffmpeg.exe�ĵ�ַ���м�����Ҫת�����ļ���ַ��������ת������ļ���ַ��-i��ת����ʽ����˼�ǿɱ�����룬mp3���뷽ʽ���õ���libmp3lame
			// �ͷŽ���
			p.getOutputStream().close();
			p.getInputStream().close();
			p.getErrorStream().close();
			p.waitFor();
			long end = System.currentTimeMillis();
			System.out.println(sourcePath + " convert success, costs:" + (end - start) + "ms");
			// ɾ��ԭ�����ļ�
			if (file.exists()) {
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// run����lame����������ͷ��ڴ�
			run.freeMemory();
		}
	}

}
