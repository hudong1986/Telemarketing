package telemarketing.config;

import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * 此类将会被spring发现并在容器中注册SelegateingFilterProxy
 * @author huchengdong
 *
 */
@Order(2)
public class SecurityWebInitialize extends AbstractSecurityWebApplicationInitializer {

}
