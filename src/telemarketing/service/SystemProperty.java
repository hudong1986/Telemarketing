package telemarketing.service;

import java.io.File;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import telemarketing.model.CustomerState;

@Service
public class SystemProperty {

	@Value("#{sysProperties['page_size']}")
	private int pageSize = 20;
	
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getHeader_pic_path() {
		return header_pic_path;
	}

	public void setHeader_pic_path(String header_pic_path) {
		this.header_pic_path = header_pic_path;
	}

	public String getSound_record_path() {
		return sound_record_path;
	}

	public void setSound_record_path(String sound_record_path) {
		this.sound_record_path = sound_record_path;
	}

	@Value("#{sysProperties['header_pic_path']}")
	private String header_pic_path;
	
	@Value("#{sysProperties['sound_record_path']}")
	private String sound_record_path;
	
	@Value("#{sysProperties['ffmpeg_path']}")
	private String ffmpeg_path;
	
	@Value("#{sysProperties['allow_ip']}")
	private String allow_ip;
	
	

	private boolean isStopService = false;
	
	
	public String getAllow_ip() {
		return allow_ip;
	}

	public void setAllow_ip(String allow_ip) {
		this.allow_ip = allow_ip;
	}

	@PostConstruct
	void init(){
		try {
			File file = new File(header_pic_path);
			if(!file.exists()){
				file.mkdirs();
			}
			
			file = new File(sound_record_path);
			if(!file.exists()){
				file.mkdirs();
			}
			
			//¹¹Ôì×´Ì¬×Öµä
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public String getFfmpeg_path() {
		return ffmpeg_path;
	}

	public void setFfmpeg_path(String ffmpeg_path) {
		this.ffmpeg_path = ffmpeg_path;
	}

	public boolean isStopService() {
		return isStopService;
	}

	public void setStopService(boolean isStopService) {
		this.isStopService = isStopService;
	}
}
