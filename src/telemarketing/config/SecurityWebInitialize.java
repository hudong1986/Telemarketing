package telemarketing.config;

import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * ���ཫ�ᱻspring���ֲ���������ע��SelegateingFilterProxy
 * @author huchengdong
 *
 */
@Order(2)
public class SecurityWebInitialize extends AbstractSecurityWebApplicationInitializer {

}
