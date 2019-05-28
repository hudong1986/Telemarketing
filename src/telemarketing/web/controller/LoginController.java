package telemarketing.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(HttpServletRequest request) {
		 
		request.getSession(true).removeAttribute("update_login_time");
		request.getSession(true).removeAttribute("LoginUser");
		return "login";
	}
}
