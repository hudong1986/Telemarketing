package telemarketing.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import telemarketing.model.Customer;
import telemarketing.repository.CustomerMapper;
import telemarketing.util.StringHelper;
import telemarketing.util.TxtLogger;
import telemarketing.util.TxtLogger.LogFileCreateType;
import telemarketing.util.TxtLogger.LogTye;

@Service
public class CustomerService {

	@Autowired
	private CustomerMapper customerMapper;

	public boolean updateState(List<Integer> list, int state, String busiName) {

		for (Integer integer : list) {
			Customer old = customerMapper.selectByPrimaryKey(integer);
			old.setUpdate_state_time(new Date());
			old.setState((byte) state);
			old.setBusiness_name(busiName);
			customerMapper.updateByPrimaryKey(old);
		}

		return true;
	}

	public boolean updateWhoUse(List<Integer> list, String who_use, String who_use_name, String who_up_down_id) {

		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("list", list);
		param.put("who_use", who_use);
		param.put("who_use_name", who_use_name);
		param.put("who_up_down_id", who_up_down_id);

		int ret = customerMapper.updateWhoUse(param);

		return ret > 0 ? true : false;
	}

	public boolean updateToCommon(List<Integer> list, String who_put_common, String who_put_common_name) {

		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("list", list);
		param.put("who_put_common", who_put_common);
		param.put("who_put_common_name", who_put_common_name);

		int ret = customerMapper.updateToCommon(param);

		return ret > 0 ? true : false;
	}

	public boolean updateShareUse(List<Integer> list, String who_use, String who_use_name) {

		for (Integer integer : list) {
			Customer old = customerMapper.selectByPrimaryKey(integer);
			String old_shareUser = old.getShareUse();
			String old_shareName = old.getShareUseName();
			String[] userids = who_use.split(",");
			String[] usernames = who_use_name.split(",");
			for (int i = 0; i < userids.length; i++) {
				if (!old_shareUser.contains(userids[i])) {
					old_shareUser += "," + userids[i];
					old_shareName += "," + usernames[i];
				}
			}
			old.setShareGetTime(new Date());
			old.setShareUse(StringHelper.trim(old_shareUser, ','));
			old.setShareUseName(StringHelper.trim(old_shareName, ','));
			customerMapper.updateByPrimaryKey(old);
		}

		return true;
	}

	// @Transactional
	public String importData(List<Customer> list) {
		int success = 0;
		int failed = 0;
		StringBuilder sBuilder = new StringBuilder();
		for (Customer customer : list) {
			if (customer.getPhone().length() != 11 || StringUtils.isBlank(customer.getPhone())) {
				failed++;
				sBuilder.append(String.format("%s-%s\r", customer.getPhone(), customer.getCusName()));
				continue;
			}

			Customer old = customerMapper.selectByPhone(customer.getPhone());
			if (old == null) {
				try {
					customerMapper.insert(customer);
					success++;
				} catch (Exception ex) {
					TxtLogger.log(ex.toString(), LogTye.ERROR, LogFileCreateType.OneFileAnHour, "");
					failed++;
					sBuilder.append(String.format("%s-%s\r", customer.getPhone(), customer.getCusName()));
				}
			} else {
				failed++;
				sBuilder.append(String.format("%s-%s\r", customer.getPhone(), customer.getCusName()));
			}
		}

		if (failed == 0) {
			return "成功导入数据" + success + "条";
		} else {
			return "成功导入数据" + success + "条,由于数据库已存在或者格式不对导致未导入" + failed + "条，以下为未导入信息：\r" + sBuilder.toString();
		}
	}

}
