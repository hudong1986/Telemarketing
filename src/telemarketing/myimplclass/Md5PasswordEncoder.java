package telemarketing.myimplclass;

import org.springframework.security.crypto.password.PasswordEncoder;

import telemarketing.util.SecretHelper;

//自己来实现MD5加密用于spring security 的登录验证
public class Md5PasswordEncoder implements PasswordEncoder {

	@Override
	public String encode(CharSequence arg0) {

		String md5=SecretHelper.parseStrToMd5U32(arg0.toString());
		return md5;
	}

	@Override
	public boolean matches(CharSequence arg0, String arg1) {
		String md5=SecretHelper.parseStrToMd5U32(arg0.toString());
		return md5.equals(arg1);
	}

}
