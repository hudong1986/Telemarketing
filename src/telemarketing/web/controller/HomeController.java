package telemarketing.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import telemarketing.model.AjaxReturn;
import telemarketing.model.Pt_user;
import telemarketing.util.TxtLogger;
import telemarketing.util.TxtLogger.LogFileCreateType;
import telemarketing.util.TxtLogger.LogTye;



@Controller
public class HomeController extends BaseController{
	
	@RequestMapping(value = { "/", "/home" }, method = RequestMethod.GET)
	public String home( ) {

		return "home";
	}

	/**
	 * 语言切换
	 */
	@RequestMapping("language")
	public ModelAndView language(HttpServletRequest request, HttpServletResponse response, @RequestParam String lng) {

		//不知为什么这里无法注入localresolver ,改为通过配置的LocaleChangeInterceptor实现
		
		/*language = language.toLowerCase();
		if (language == null || language.equals("")) {
			return new ModelAndView("redirect:/");
		} else {
			if (language.equals("zh_cn")) {
				localeResolver.setLocale(request, response, Locale.CHINA);
			} else if (language.equals("en")) {
				localeResolver.setLocale(request, response, Locale.ENGLISH);
			} else {
				localeResolver.setLocale(request, response, Locale.CHINA);
			}
		}*/

		return new ModelAndView("redirect:/");
	} 
	
	
	@RequestMapping("/copy_record")
	public @ResponseBody AjaxReturn copyRecord(
			@RequestParam(value="txt",defaultValue="") String txt,
			@RequestParam(value="url",defaultValue="") String url,
			@AuthenticationPrincipal User user1
			){
		Pt_user user = ptUserMapper.selectByPhone(user1.getUsername());
		AjaxReturn ret = new AjaxReturn();
		try{
			TxtLogger.log(url+" txt:"+txt, LogTye.INFO, LogFileCreateType.OneFileEveryDay,
					"CopyRecord/"+user.getPhone()+"-"+user.getRealName());
			
			ret.setCode(1);
		}
		catch(Exception ex){
			ret.setCode(0);
		}
		
		return ret;
	}

}
