package telemarketing.test;

import org.junit.Assert;
import org.junit.Test;

import telemarketing.util.SecretHelper;

public class TestBase64 {

	@Test
	public void testEncode(){
		String url = SecretHelper.encodeBase64("jdbc:mysql://localhost:3306/telemarketingdb?useUnicode=true&characterEncoding=utf-8");
		String root = SecretHelper.encodeBase64("root");
		String pwd=SecretHelper.encodeBase64("123");
		System.out.println(url);
		System.out.println(root);
		System.out.println(pwd);
		
		String url2= SecretHelper.decodeBase64("amRiYzpteXNxbDovL2xvY2FsaG9zdDozMzA2L3RlbGVtYXJrZXRpbmdkYj91c2VVbmljb2RlPXRydWUmY2hhcmFjdGVyRW5jb2Rpbmc9dXRmLTg=");
	    System.out.println(url2);
		 
	}
	
	@Test
	public void testMd5(){
		String md5=SecretHelper.parseStrToMd5U32("123456");
		System.out.println(md5);
		
	}
}
