package telemarketing.web.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import telemarketing.model.Business_type;

@Controller
@RequestMapping("/businessType")
public class BusinessTypeController extends BaseController {
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody List<Business_type> getState() {
		 
		return busineMapper.selectAll();
	}

}
