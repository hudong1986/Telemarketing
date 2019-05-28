package telemarketing.myimplclass;

import org.springframework.security.crypto.password.PasswordEncoder;

import telemarketing.util.SecretHelper;

//�Լ���ʵ��MD5��������spring security �ĵ�¼��֤
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
