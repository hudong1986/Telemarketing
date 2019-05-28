package telemarketing.web.controller;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import telemarketing.model.Customer;
import telemarketing.model.Pt_user;
import telemarketing.model.TrackRecord;
import telemarketing.repository.CustomerMapper;
import telemarketing.repository.Pt_userMapper;
import telemarketing.repository.TrackRecordMapper;
import telemarketing.service.StaticService;
import telemarketing.util.StringHelper;

@Controller
@RequestMapping("/trackrecord")
public class TrackRecordController extends BaseController {

	@RequestMapping(method = RequestMethod.POST)
	public String add(@RequestParam(required = false, defaultValue = "") String updateStateSelect,
			@RequestParam(required = false, defaultValue = "") String updateBusinessSelect,
			@RequestParam int customer_id, @RequestParam String content, @AuthenticationPrincipal User user1) {

		StringBuilder sBuilder = new StringBuilder();
		if (StringUtils.isNotBlank(updateStateSelect) && StringUtils.isNotBlank(updateBusinessSelect)) {
			Customer old = customerMapper.selectByPrimaryKey(customer_id);
			int state = Byte.parseByte(updateStateSelect);

			// 必须是发生改变的状态或者业务才是真正的有效动作，需要改变更新时间
			if ((old.getState() != state) || !updateBusinessSelect.equals(old.getBusiness_name())) {
				sBuilder.append(String.format("原业务:%s,原状态:%s\n", old.getBusiness_name(), StaticService.getCustomStateName(old.getState())));
				old.setUpdate_state_time(new Date());
				old.setState(Byte.parseByte(updateStateSelect));
				old.setBusiness_name(updateBusinessSelect);
				customerMapper.updateByPrimaryKey(old);
				sBuilder.append(String.format("修改后业务:%s,修改后状态:%s\n", old.getBusiness_name(), StaticService.getCustomStateName(old.getState())));
			}

		}

		if (StringUtils.isNotBlank(content) || sBuilder.length() > 0) {
			Pt_user user = ptUserMapper.selectByPhone(user1.getUsername());
			TrackRecord record = new TrackRecord();
			record.setAddTime(new Date());
			record.setContent(StringHelper.turn(sBuilder.toString() + content));
			record.setCustomerId(customer_id);
			record.setUserId(user.getPhone());
			record.setUserName(user.getRealName());
			trackRecordMapper.insert(record);
		}

		return "redirect:/customer/track_record/" + customer_id;
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	public @ResponseBody int delete(@PathVariable int id,@AuthenticationPrincipal User user1) {
		 TrackRecord trackRecord = trackMapper.selectByPrimaryKey(id);
		 addTrackRecord(user1.getUsername(), trackRecord.getCustomerId(), "删除跟踪记录:"+trackRecord.getContent());
		 trackMapper.deleteByPrimaryKey(id);
		 return 1;
	}

}
