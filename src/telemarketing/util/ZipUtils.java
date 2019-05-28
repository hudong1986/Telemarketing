package telemarketing.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 压缩文件工具类
 */
public class ZipUtils {

	//  一层目录或者单个文件
	public static void doCompress(String srcFile, String zipFile) throws Exception {
		doCompress(new File(srcFile), new File(zipFile));
	}

	/**
	 * 文件压缩
	 * 
	 * @param srcFile
	 *            一层目录或者单个文件
	 * @param destFile
	 *            压缩后的ZIP文件
	 */
	public static void doCompress(File srcFile, File destFile) throws Exception {
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(destFile));
		if (srcFile.isDirectory()) {
			File[] files = srcFile.listFiles();
			for (File file : files) {
				doCompress(file, out);
			}
		} else {
			doCompress(srcFile, out);
		}
	}

	public static void doCompress(String pathname, ZipOutputStream out) throws IOException {
		doCompress(new File(pathname), out);
	}

	public static void doCompress(File file, ZipOutputStream out) throws IOException {
		if (file.exists()) {
			byte[] buffer = new byte[1024];
			FileInputStream fis = new FileInputStream(file);
			out.putNextEntry(new ZipEntry(file.getName()));
			int len = 0;
			// 读取文件的内容,打包到zip文件
			while ((len = fis.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
			out.flush();
			out.closeEntry();
			fis.close();
		}
	}

	//多级目录
	public static void zip(File fileSrc, File dectFile) throws IOException {
		ZipOutputStream zipOutputStream = new ZipOutputStream(
				new CheckedOutputStream(new FileOutputStream(dectFile), new CRC32()));
		String name = fileSrc.getName();
		zip(zipOutputStream, name, fileSrc);
		zipOutputStream.flush();
		zipOutputStream.close();
	}

	private static void zip(ZipOutputStream zipOutputStream, String name, File fileSrc) throws IOException {
		if (fileSrc.isDirectory()) {
			File[] files = fileSrc.listFiles();
			zipOutputStream.putNextEntry(new ZipEntry(name + "/")); // 建一个文件夹
			name = name + "/";
			for (File f : files) {
				zip(zipOutputStream, name + f.getName(), f);
			}
		} else {

			zipOutputStream.putNextEntry(new ZipEntry(name));
			FileInputStream input = new FileInputStream(fileSrc);
			byte[] buf = new byte[1024];
			int len = -1;
			while ((len = input.read(buf)) != -1) {
				zipOutputStream.write(buf, 0, len);
			}
			zipOutputStream.flush();
			input.close();
		}
	}

	public static void unzip(String zipFile, String destDir) throws IOException {
		
		unzip(new File(zipFile),destDir);
	}
	
	//多级目录
	public static void unzip(File zipFile, String destDir) throws IOException {
		ZipInputStream zipInputStream = new ZipInputStream(
				new CheckedInputStream(new FileInputStream(zipFile), new CRC32()));
		ZipEntry zipEntry;
		while ((zipEntry = zipInputStream.getNextEntry()) != null) {
			//System.out.println(zipEntry.getName());
			File f = new File(destDir + zipEntry.getName());
			if (zipEntry.getName().endsWith("/")) {
				f.mkdirs();
			} else {
				// f.createNewFile();
				FileOutputStream fileOutputStream = new FileOutputStream(f);
				byte[] buf = new byte[1024];
				int len = -1;
				while ((len = zipInputStream.read(buf)) != -1) { // 直到读到该条目的结尾
					fileOutputStream.write(buf, 0, len);
				}
				fileOutputStream.flush();
				fileOutputStream.close();
			}
			zipInputStream.closeEntry(); // 关闭该条目
		}
		zipInputStream.close();
	}

}
