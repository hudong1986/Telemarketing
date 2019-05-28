/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telemarketing.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author HCD
 */
public class AppendFile {
    public static void method1(String file, String conent) {     
        BufferedWriter out = null;     
        try {     
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true),"utf-8"));     
            out.write(conent);     
            out.newLine();
        } catch (Exception e) {     
            e.printStackTrace();     
        } finally {     
            try {     
                if(out != null){  
                    out.close();     
                }  
            } catch (IOException e) {     
                e.printStackTrace();     
            }     
        }     
    }     
    
    /**   
     * 杩藉姞鏂囦欢锛氫娇鐢‵ileWriter   
     *    
     * @param fileName   
     * @param content   
     */    
    public static void method2(String fileName, String content) {   
        FileWriter writer = null;  
        try {     
            // 鎵撳紑涓�涓啓鏂囦欢鍣紝鏋勯�犲嚱鏁颁腑鐨勭浜屼釜鍙傛暟true琛ㄧず浠ヨ拷鍔犲舰寮忓啓鏂囦欢     
            writer = new FileWriter(fileName, true);     
            writer.write(content);       
            
        } catch (IOException e) {     
            e.printStackTrace();     
        } finally {     
            try {     
                if(writer != null){  
                    writer.close();     
                }  
            } catch (IOException e) {     
                e.printStackTrace();     
            }     
        }   
    }     
    
    /**   
     * 杩藉姞鏂囦欢锛氫娇鐢≧andomAccessFile   
     *    
     * @param fileName 鏂囦欢鍚�   
     * @param content 杩藉姞鐨勫唴瀹�   
     */    
    public static void method3(String fileName, String content) {   
        RandomAccessFile randomFile = null;  
        try {     
            // 鎵撳紑涓�涓殢鏈鸿闂枃浠舵祦锛屾寜璇诲啓鏂瑰紡     
            randomFile = new RandomAccessFile(fileName, "rw");     
            // 鏂囦欢闀垮害锛屽瓧鑺傛暟     
            long fileLength = randomFile.length();     
            // 灏嗗啓鏂囦欢鎸囬拡绉诲埌鏂囦欢灏俱��     
            randomFile.seek(fileLength);     
            randomFile.writeBytes(content);      
        } catch (IOException e) {     
            e.printStackTrace();     
        } finally{  
            if(randomFile != null){  
                try {  
                    randomFile.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
    }  
    
    
    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    public  static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
         //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
    
    public static List<File> getAllFiles(String fileDir) {  
        List<File> fileList = new ArrayList<File>();  
        File file = new File(fileDir);  
        File[] files = file.listFiles();// 获取目录下的所有文件或文件夹  
        if (files == null) {// 如果目录为空，直接退出  
            return null;  
        }  
        // 遍历，目录下的所有文件  
        for (File f : files) {  
            if (f.isFile()) {  
                fileList.add(f);  
            } else if (f.isDirectory()) {  
            	List<File> temp=  getAllFiles(f.getAbsolutePath());  
            	if(temp.size()>0){
            		fileList.addAll(temp);
            	}
            }  
        }  
        
        return fileList;
    }  
}
