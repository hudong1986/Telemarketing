package telemarketing.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import telemarketing.model.BusiReport;
import telemarketing.model.Customer;
import telemarketing.model.LoginRecord;
import telemarketing.model.Pager;
import telemarketing.model.Pt_user;
import telemarketing.model.Remind;
import telemarketing.model.SoundRecord;
import telemarketing.model.SoundStatics;
import telemarketing.model.TrackRecord;
import telemarketing.repository.CustomerMapper;
import telemarketing.repository.LoginRecordMapper;
import telemarketing.repository.Pt_userMapper;
import telemarketing.repository.RemindMapper;
import telemarketing.repository.ReportMapper;
import telemarketing.repository.SoundRecordMapper;
import telemarketing.repository.TrackRecordMapper;
import telemarketing.util.TxtLogger;
import telemarketing.util.TxtLogger.LogFileCreateType;
import telemarketing.util.TxtLogger.LogTye;

@Service
public class PagerServiceAdapter {

	@Value("#{sysProperties['page_size']}")
	private int pageSize = 20;

	@Autowired
	private CustomerMapper customerMapper;

	@Autowired
	Pt_userMapper pt_userMapper;

	@Autowired
	SoundRecordMapper soundRecordMapper;

	@Autowired
	RemindMapper remindMapper;

	@Autowired
	TrackRecordMapper trackRecordMapper;

	@Autowired
	ReportMapper reportMapper;

	@Autowired
	LoginRecordMapper loginRecordMapper;

	// 查询客户表信息 search1:查询公共池 2查询自己的 3查询下属的
	public Pager searchCustom(String who_use, String phone, String cus_name, String id_person, String who_use_name,
			String time_beg, String time_end, int state, String business_name, String who_put_common_name,
			int has_recovery, String orderField, String orderType, String get_time_beg, String get_time_end,
			int pageNum, int searchType, int currentPageSize, String up_down_id, String dataFrom) throws SQLException {

		if (currentPageSize == 0) {
			currentPageSize = pageSize;
		}

		Pt_user user = pt_userMapper.selectByPhone(who_use);

		phone = phone.trim();
		cus_name = cus_name.trim();
		id_person = id_person.trim();

		Pager pager = new Pager();
		String select = "select * from customer where is_common=0 ";

		if (searchType == 1) { // 公共池
			select = "select * from customer where is_common=1 ";
		} else if (searchType == 2) { // 自己的
			select = String.format(
					"select * from customer where  is_common=0 and (who_use='%s' or share_use like'%%%s%%')", who_use,
					who_use);
		} else if (searchType == 3) { // 下属的
			select = "select * from customer where is_common=0 and  who_up_down_id like '%"
					+ user.getDeptId().getUpDownId() + "%' and who_use !=" + who_use;
		} else if (searchType == 4) { // 贷后部
			select = "select * from customer where 1=1 ";
		}

		StringBuilder sWhere = new StringBuilder();

		if (StringUtils.isNotBlank(who_put_common_name)) {
			sWhere.append(" and who_put_common_name like '%" + who_put_common_name + "%'");
		}

		//查看自己的客户里不需考虑部门
		if (StringUtils.isNotBlank(up_down_id) && !up_down_id.equals("all") && searchType != 2) {
			sWhere.append(" and who_up_down_id like '%" + up_down_id + "%'");
		}

		if (StringUtils.isNotBlank(phone)) {
			sWhere.append(" and phone like '%" + phone + "%'");
		}

		if (StringUtils.isNotBlank(cus_name)) {
			sWhere.append(" and cus_name like '%" + cus_name + "%'");
		}

		if (StringUtils.isNotBlank(dataFrom)) {
			sWhere.append(" and data_from like '%" + dataFrom + "%'");
		}

		if (StringUtils.isNotBlank(id_person)) {
			sWhere.append(" and id_person like '%" + id_person + "%'");
		}

		if (StringUtils.isNotBlank(who_use_name)) {
			if (has_recovery != 1) { //非回收
				sWhere.append(" and who_use_name like '%" + who_use_name + "%'");
			}
			else{ //回收的情况下 直接去匹配回收人
				sWhere.append(" and recovery_from like '%" + who_use_name + "%'");
			}
		}

		if (StringUtils.isNotBlank(time_beg)) {
			sWhere.append(String.format(" and update_state_time >='%s' ", time_beg));
		}

		if (StringUtils.isNotBlank(time_end)) {
			sWhere.append(String.format(" and update_state_time <='%s' ", time_end));
		}

		if (StringUtils.isNotBlank(get_time_beg)) {
			sWhere.append(String.format(" and who_get_time >='%s' ", get_time_beg));
		}

		if (StringUtils.isNotBlank(get_time_end)) {
			sWhere.append(String.format(" and who_get_time <='%s' ", get_time_end));
		}

		if (state != -1) {
			sWhere.append(" and state = " + state);
		}

		if (has_recovery != -1) {
			sWhere.append(" and has_recovery = " + has_recovery);
		}

		if (!business_name.equals("全部")) {
			sWhere.append(" and business_name ='" + business_name + "'");
		}

		String orderString = "";
		if (StringUtils.isNotBlank(orderField)) {
			orderString = " order by " + orderField + " " + orderType + " ";
		} else {
			orderField = "id";
			orderType = "asc";
			orderString = " order by id asc "; // 默认按id升序排序
		}

		String countStr = String.format("%s %s", select.replaceFirst("\\*", "count(id) as count"), sWhere.toString());
		int totalCount = getCount(countStr); // 总共的数量
		if (totalCount == 0) {
			return pager;
		}

		int theLastShowItemCounts = pageNum * currentPageSize; // 想要查询的最后一条展示结果序号
		String excuteSql;
		if (theLastShowItemCounts < 50000) { // 小于这个数量时查询时间影响不大
			excuteSql = select + sWhere.toString() + orderString + " limit " + (theLastShowItemCounts - currentPageSize)
					+ "," + currentPageSize;
		} else {
			if (orderType.equalsIgnoreCase("asc")) {
				excuteSql = select + sWhere.toString() + " and " + orderField + " >= ( select " + orderField
						+ " from customer where 1=1 " + sWhere.toString() + orderString + " limit "
						+ (theLastShowItemCounts - currentPageSize) + ",1)" + orderString + " limit " + currentPageSize;

			} else {
				excuteSql = select + sWhere.toString() + " and " + orderField + " <= ( select " + orderField
						+ " from customer where 1=1 " + sWhere.toString() + orderString + " limit "
						+ (theLastShowItemCounts - currentPageSize) + ",1)" + orderString + " limit " + currentPageSize;
			}
		}

		pager.setTotalCount(totalCount);
		pager.setPageCounts((totalCount + currentPageSize - 1) / currentPageSize);
		Map<String, String> map = new HashMap<String, String>();
		map.put("sql", excuteSql);
		List<Customer> list = customerMapper.searchBySql(map);
		// 设置客户状态名
		if (list != null && list.size() > 0) {
			for (Customer customer : list) {
				customer.setStateName(StaticService.getCustomStateName(customer.getState()));
			}
		}

		pager.setData(list);
		pager.setCurrentPageNum(pageNum);
		pager.setPageSize(list != null ? list.size() : 0);
		String nextPage = String.format(
				"phone=%s&cus_name=%s&id_person=%s&state=%d&business_name=%s&who_put_common_name=%s&has_recovery=%d&"
						+ "orderField=%s&orderType=%s&who_use=%s&who_use_name=%s&dataFrom=%s"
						+ "&add_time_beg=%s&add_time_end=%s&get_time_beg=%s&get_time_end=%s&"
						+ "searchType=%d&currentPageSize=%d&up_down_id=%s&pageNum=%d",
				phone, cus_name, id_person, state, business_name, who_put_common_name, has_recovery, orderField,
				orderType, who_use, who_use_name, dataFrom, time_beg, time_end, get_time_beg, get_time_end, searchType,
				currentPageSize, up_down_id, pageNum == pager.getPageCounts() ? pageNum : pageNum + 1);
		String prePage = String.format(
				"phone=%s&cus_name=%s&id_person=%s&state=%d&business_name=%s&who_put_common_name=%s&has_recovery=%d&"
						+ "orderField=%s&orderType=%s&who_use=%s&who_use_name=%s&dataFrom=%s"
						+ "&add_time_beg=%s&add_time_end=%s&get_time_beg=%s&get_time_end=%s&"
						+ "searchType=%d&currentPageSize=%d&up_down_id=%s&pageNum=%d",
				phone, cus_name, id_person, state, business_name, who_put_common_name, has_recovery, orderField,
				orderType, who_use, who_use_name, dataFrom, time_beg, time_end, get_time_beg, get_time_end, searchType,
				currentPageSize, up_down_id, pageNum == 1 ? 1 : pageNum - 1);

		pager.setNextPageString(nextPage);
		pager.setPrePageString(prePage);
		int lastIndex = pager.getNextPageString().lastIndexOf('&');
		pager.setFirstPageString(pager.getNextPageString().substring(0, lastIndex) + "&pageNum=1");
		pager.setEndPageString(pager.getNextPageString().substring(0, lastIndex) + "&pageNum=" + pager.getPageCounts());

		// 设置排序
		String basicOrderString = String.format(
				"phone=%s&cus_name=%s&id_person=%s&state=%d&business_name=%s&who_put_common_name=%s&has_recovery=%d&"
						+ "orderField=%s&orderType=%s&who_use=%s&who_use_name=%s&dataFrom=%s"
						+ "&add_time_beg=%s&add_time_end=%s&get_time_beg=%s&get_time_end=%s&"
						+ "searchType=%d&currentPageSize=%d&up_down_id=%s&pageNum=%d",
				phone, cus_name, id_person, state, business_name, who_put_common_name, has_recovery, "id", "asc",
				who_use, who_use_name, dataFrom, time_beg, time_end, get_time_beg, get_time_end, searchType,
				currentPageSize, up_down_id, 1);
		String tempOrderString;

		// 设置排序

		if (orderField.equalsIgnoreCase("id")) {
			tempOrderString = basicOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString1(tempOrderString);
		} else {
			pager.setOrderString1(basicOrderString);
		}

		if (orderField.equalsIgnoreCase("state")) {
			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=state");
			tempOrderString = tempOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString2(tempOrderString);
		} else {

			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=state");
			pager.setOrderString2(tempOrderString);
		}

		if (orderField.equalsIgnoreCase("update_state_time")) {
			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=update_state_time");
			tempOrderString = tempOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString3(tempOrderString);
		} else {

			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=update_state_time");
			pager.setOrderString3(tempOrderString);
		}

		if (orderField.equalsIgnoreCase("data_from")) {
			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=data_from");
			tempOrderString = tempOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString4(tempOrderString);
		} else {

			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=data_from");
			pager.setOrderString4(tempOrderString);
		}

		if (orderField.equalsIgnoreCase("business_name")) {
			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=business_name");
			tempOrderString = tempOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString5(tempOrderString);
		} else {

			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=business_name");
			pager.setOrderString5(tempOrderString);
		}

		if (orderField.equalsIgnoreCase("who_get_time")) {
			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=who_get_time");
			tempOrderString = tempOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString6(tempOrderString);
		} else {

			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=who_get_time");
			pager.setOrderString6(tempOrderString);
		}

		if (orderField.equalsIgnoreCase("add_time")) {
			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=add_time");
			tempOrderString = tempOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString7(tempOrderString);
		} else {

			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=add_time");
			pager.setOrderString7(tempOrderString);
		}

		return pager;
	}

	// 查询用户表信息
	public Pager searchEmployee(String phone, String real_name, int deptId, int state, String up_down_id,
			String orderField, String orderType, int pageNum) throws SQLException {

		phone = phone.trim();

		Pager pager = new Pager();
		String select = "select pt_user.*,pt_dept.dept_name,pt_role.role_name from pt_user inner join pt_dept on "
				+ "pt_user.dept_id=pt_dept.id INNER JOIN pt_role ON pt_role.role_code=pt_user.role_code";

		StringBuilder sWhere = new StringBuilder();
		if (StringUtils.isNotBlank(phone)) {
			sWhere.append(" and phone like '%" + phone + "%'");
		}

		if (StringUtils.isNotBlank(real_name)) {
			sWhere.append(" and real_name like '%" + real_name + "%'");
		}

		if (StringUtils.isNotBlank(up_down_id)) {
			sWhere.append(" and pt_user.up_down_id like '%" + up_down_id + "%'");
		}

		if (state != -1) {
			sWhere.append(" and state = " + state);
		}

		if (deptId != -1) {
			sWhere.append(" and dept_id = " + deptId);
		}

		String orderString = "";
		if (StringUtils.isNotBlank(orderField)) {
			orderString = " order by " + orderField + " " + orderType + " ";
		} else {
			orderField = "id";
			orderType = "asc";
			orderString = " order by id asc "; // 默认按id升序排序
		}

		String countStr = String.format("select count(id) as count from pt_user where 1=1 %s", sWhere.toString());
		int totalCount = getCount(countStr); // 总共的数量
		if (totalCount == 0) {
			return pager;
		}

		int theLastShowItemCounts = pageNum * pageSize; // 想要查询的最后一条展示结果序号
		String excuteSql;
		if (theLastShowItemCounts < 50000) { // 小于这个数量时查询时间影响不大
			excuteSql = select + sWhere.toString() + orderString + " limit " + (theLastShowItemCounts - pageSize) + ","
					+ pageSize;
		} else {
			if (orderType.equalsIgnoreCase("asc")) {
				excuteSql = select + sWhere.toString() + " and " + orderField + " >= ( select " + orderField
						+ " from pt_user where 1=1 " + sWhere.toString() + orderString + " limit "
						+ (theLastShowItemCounts - pageSize) + ",1)" + orderString + " limit " + pageSize;

			} else {
				excuteSql = select + sWhere.toString() + " and " + orderField + " <= ( select " + orderField
						+ " from pt_user where 1=1 " + sWhere.toString() + orderString + " limit "
						+ (theLastShowItemCounts - pageSize) + ",1)" + orderString + " limit " + pageSize;
			}
		}

		pager.setTotalCount(totalCount);
		pager.setPageCounts((totalCount + pageSize - 1) / pageSize);
		Map<String, String> map = new HashMap<String, String>();
		map.put("sql", excuteSql);
		List<Pt_user> list = pt_userMapper.searchBySql(excuteSql);
		pager.setData(list);
		pager.setCurrentPageNum(pageNum);
		pager.setPageSize(list != null ? list.size() : 0);
		String nextPage = String.format(
				"phone=%s&real_name=%s&deptId=%d&state=%d&up_down_id=%s&" + "orderField=%s&orderType=%s&pageNum=%d",
				phone, real_name, deptId, state, up_down_id, orderField, orderType,
				pageNum == pager.getPageCounts() ? pageNum : pageNum + 1);
		String prePage = String.format(
				"phone=%s&real_name=%s&deptId=%d&state=%d&up_down_id=%s&" + "orderField=%s&orderType=%s&pageNum=%d",
				phone, real_name, deptId, state, up_down_id, orderField, orderType, pageNum == 1 ? 1 : pageNum - 1);

		pager.setNextPageString(nextPage);
		pager.setPrePageString(prePage);
		// 设置排序

		return pager;
	}

	// 查询提醒表信息
	public Pager searchRemind(String user_id, String up_down_id, String time_date, String orderField, String orderType,
			int pageNum) throws SQLException {

		Pager pager = new Pager();
		String select = "select * from remind where 1=1 ";

		StringBuilder sWhere = new StringBuilder();
		if (StringUtils.isNotBlank(user_id)) {
			sWhere.append(
					" and ((remind.user_id='" + user_id + "' and remind.remind_type=0) or (LOCATE(remind.up_down_id,'"
							+ up_down_id + "')>0 and" + " remind.remind_type=1))");
		}

		if (StringUtils.isNotBlank(time_date)) {
			sWhere.append(" and time_beg >= '" + time_date + "' and time_beg <= '" + time_date + " 23:59:59' ");
		}

		String orderString = "";
		if (StringUtils.isNotBlank(orderField)) {
			orderString = " order by " + orderField + " " + orderType + " ";
		} else {
			orderField = "id";
			orderType = "desc";
			orderString = " order by id desc "; // 默认按id升序排序
		}

		String countStr = String.format("select count(id) as count from remind where 1=1 %s ", sWhere.toString());
		int totalCount = getCount(countStr); // 总共的数量
		if (totalCount == 0) {
			return pager;
		}

		int theLastShowItemCounts = pageNum * pageSize; // 想要查询的最后一条展示结果序号
		String excuteSql;
		if (theLastShowItemCounts < 50000) { // 小于这个数量时查询时间影响不大
			excuteSql = select + sWhere.toString() + orderString + " limit " + (theLastShowItemCounts - pageSize) + ","
					+ pageSize;
		} else {
			if (orderType.equalsIgnoreCase("asc")) {
				excuteSql = select + sWhere.toString() + " and " + orderField + " >= ( select " + orderField
						+ " from sound_record where 1=1 " + sWhere.toString() + orderString + " limit "
						+ (theLastShowItemCounts - pageSize) + ",1)" + orderString + " limit " + pageSize;

			} else {
				excuteSql = select + sWhere.toString() + " and " + orderField + " <= ( select " + orderField
						+ " from sound_record where 1=1 " + sWhere.toString() + orderString + " limit "
						+ (theLastShowItemCounts - pageSize) + ",1)" + orderString + " limit " + pageSize;
			}
		}

		pager.setTotalCount(totalCount);
		pager.setPageCounts((totalCount + pageSize - 1) / pageSize);
		List<Remind> list = remindMapper.searchBySql(excuteSql);
		pager.setData(list);
		pager.setCurrentPageNum(pageNum);
		pager.setPageSize(list != null ? list.size() : 0);
		String nextPage = String.format(
				"user_id=%s&" + "up_down_id=%s&time_date=%s&" + "orderField=%s&orderType=%s&pageNum=%d", user_id,
				up_down_id, time_date, orderField, orderType, pageNum == pager.getPageCounts() ? pageNum : pageNum + 1);
		String prePage = String.format(
				"user_id=%s&" + "up_down_id=%s&time_date=%s&" + "orderField=%s&orderType=%s&pageNum=%d", user_id,
				up_down_id, time_date, orderField, orderType, pageNum == 1 ? 1 : pageNum - 1);

		pager.setNextPageString(nextPage);
		pager.setPrePageString(prePage);
		// 设置排序
		String basicOrderString = String.format(
				"user_id=%s&" + "up_down_id=%s&time_date=%s&" + "orderField=%s&orderType=%s&pageNum=%d", user_id,
				up_down_id, time_date, "id", "asc", 1);

		String tempOrderString;
		if (orderField.equalsIgnoreCase("id")) {
			tempOrderString = basicOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString1(tempOrderString);
		} else {
			pager.setOrderString1(basicOrderString);
		}

		if (orderField.equalsIgnoreCase("add_time")) {
			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=add_time");
			tempOrderString = tempOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString2(tempOrderString);
		} else {

			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=add_time");
			pager.setOrderString2(tempOrderString);
		}

		return pager;
	}

	public Pager searchSoundGroupData(String customer_phone, String local_phone, String user_id, String user_name,
			String seconds, String up_down_id, String sound_time_beg, String sound_time_end, String add_time_beg,
			String add_time_end, String orderField, String orderType, int pageNum) throws SQLException {

		Pager pager = new Pager();
		String select = "SELECT * from soundreport where 1=1 ";

		StringBuilder sWhere = new StringBuilder();

		if (StringUtils.isNotBlank(user_id)) {
			sWhere.append(" and user_phone = '" + user_id + "'");
		}

		if (StringUtils.isNotBlank(user_name)) {
			sWhere.append(" and user_name like '%" + user_name + "%'");
		}

		if (StringUtils.isNotBlank(up_down_id)) {
			sWhere.append(" and up_down_id like '%" + up_down_id + "%'");
		}

		if (StringUtils.isNotBlank(sound_time_beg)) {
			sWhere.append(" and dateStr >= '" + sound_time_beg + "'");
		}

		if (StringUtils.isNotBlank(sound_time_end)) {
			sWhere.append(" and dateStr <= '" + sound_time_end + "'");
		}

		String groupBy = "";
		String orderString = "";
		if (StringUtils.isNotBlank(orderField) && StringUtils.isNotBlank(orderType)) {
			orderString = " order by " + orderField + " " + orderType + " ";
		} else {
			orderField = "dateStr";
			orderType = "desc";
			orderString = " order by dateStr desc "; // 默认按id升序排序
		}

		String countStr = String.format("select count(*) as count from (%s) as t ",
				select + sWhere.toString() + groupBy);
		int totalCount = getCount(countStr); // 总共的数量
		if (totalCount == 0) {
			return pager;
		}

		int theLastShowItemCounts = pageNum * pageSize; // 想要查询的最后一条展示结果序号
		String excuteSql;

		excuteSql = select + sWhere.toString() + groupBy + orderString + " limit " + (theLastShowItemCounts - pageSize)
				+ "," + pageSize;

		pager.setTotalCount(totalCount);
		pager.setPageCounts((totalCount + pageSize - 1) / pageSize);
		List<SoundStatics> list = reportMapper.searchSoundBySql(excuteSql);
		pager.setData(list);
		pager.setCurrentPageNum(pageNum > pager.getPageCounts() ? pager.getPageCounts() : pageNum);
		pager.setPageSize(list != null ? list.size() : 0);
		String nextPage = String.format(
				"customer_phone=%s&local_phone=%s&user_id=%s&user_name=%s&seconds=%s&"
						+ "upDownId=%s&sound_time_beg=%s&sound_time_end=%s&" + "add_time_beg=%s&add_time_end=%s&"
						+ "orderField=%s&orderType=%s&pageNum=%d",
				customer_phone, local_phone, user_id, user_name, seconds, up_down_id, sound_time_beg, sound_time_end,
				add_time_beg, add_time_end, orderField, orderType,
				pageNum == pager.getPageCounts() ? pageNum : pageNum + 1);
		String prePage = String.format(
				"customer_phone=%s&local_phone=%s&user_id=%s&user_name=%s&seconds=%s&"
						+ "upDownId=%s&sound_time_beg=%s&sound_time_end=%s&" + "add_time_beg=%s&add_time_end=%s&"
						+ "orderField=%s&orderType=%s&pageNum=%d",
				customer_phone, local_phone, user_id, user_name, seconds, up_down_id, sound_time_beg, sound_time_end,
				add_time_beg, add_time_end, orderField, orderType, pageNum == 1 ? 1 : pageNum - 1);

		pager.setNextPageString(nextPage);
		pager.setPrePageString(prePage);
		int lastIndex = pager.getNextPageString().lastIndexOf('&');
		pager.setFirstPageString(pager.getNextPageString().substring(0, lastIndex) + "&pageNum=1");
		pager.setEndPageString(pager.getNextPageString().substring(0, lastIndex) + "&pageNum=" + pager.getPageCounts());
		// 设置排序
		String basicOrderString = String.format(
				"customer_phone=%s&local_phone=%s&user_id=%s&user_name=%s&seconds=%s&"
						+ "upDownId=%s&sound_time_beg=%s&sound_time_end=%s&" + "add_time_beg=%s&add_time_end=%s&"
						+ "orderField=%s&orderType=%s&pageNum=%d",
				customer_phone, local_phone, user_id, user_name, seconds, up_down_id, sound_time_beg, sound_time_end,
				add_time_beg, add_time_end, "id", "asc", 1);
		String tempOrderString;
		if (orderField.equalsIgnoreCase("id")) {
			tempOrderString = basicOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString1(tempOrderString);
		} else {
			pager.setOrderString1(basicOrderString);
		}

		if (orderField.equalsIgnoreCase("up_down_id")) {
			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=up_down_id");
			tempOrderString = tempOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString2(tempOrderString);
		} else {

			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=up_down_id");
			pager.setOrderString2(tempOrderString);
		}

		if (orderField.equalsIgnoreCase("user_phone")) {
			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=user_phone");
			tempOrderString = tempOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString3(tempOrderString);
		} else {

			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=user_phone");
			pager.setOrderString3(tempOrderString);
		}

		if (orderField.equalsIgnoreCase("dateStr")) {
			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=dateStr");
			tempOrderString = tempOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString4(tempOrderString);
		} else {

			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=dateStr");
			pager.setOrderString4(tempOrderString);
		}

		return pager;
	}

	// 查询录音表信息
	public Pager searchSound(String customer_phone, String local_phone, String user_id, String user_name,
			String seconds, String maxseconds, String up_down_id, String sound_time_beg, String sound_time_end,
			String add_time_beg, String add_time_end, String orderField, String orderType, int pageNum,
			int currentPageSize) throws SQLException {

		if (currentPageSize == 0) {
			currentPageSize = pageSize;
		}

		Pager pager = new Pager();
		String select = "select * from sound_record where 1=1 ";

		StringBuilder sWhere = new StringBuilder();
		if (StringUtils.isNotBlank(customer_phone)) {
			sWhere.append(" and customer_phone like '%" + customer_phone + "%'");
		}

		if (StringUtils.isNotBlank(local_phone)) {
			sWhere.append(" and local_phone like '%" + local_phone + "%'");
		}

		if (StringUtils.isNotBlank(user_id)) {
			sWhere.append(" and user_id = '" + user_id + "'");
		}

		if (StringUtils.isNotBlank(user_name)) {
			sWhere.append(" and user_name like '%" + user_name + "%'");
		}

		if (StringUtils.isNotBlank(seconds)) {
			sWhere.append(" and sound_length >=" + seconds);
		}

		if (StringUtils.isNotBlank(maxseconds)) {
			sWhere.append(" and sound_length <=" + maxseconds);
		}

		if (StringUtils.isNotBlank(up_down_id)) {
			sWhere.append(" and up_down_id like '%" + up_down_id + "%'");
		}

		if (StringUtils.isNotBlank(sound_time_beg)) {
			sWhere.append(" and sound_time >= '" + sound_time_beg + "'");
		}

		if (StringUtils.isNotBlank(sound_time_end)) {
			sWhere.append(" and sound_time <= '" + sound_time_end + "'");
		}
		if (StringUtils.isNotBlank(add_time_beg)) {
			sWhere.append(" and add_time >= '" + add_time_beg + "'");
		}
		if (StringUtils.isNotBlank(add_time_end)) {
			sWhere.append(" and add_time <= '" + add_time_end + "'");
		}

		String orderString = "";
		if (StringUtils.isNotBlank(orderField)) {
			orderString = " order by " + orderField + " " + orderType + " ";
		} else {
			orderField = "id";
			orderType = "asc";
			orderString = " order by id asc "; // 默认按id升序排序
		}

		String countStr = String.format("select count(id) as count from sound_record where 1=1 %s ", sWhere.toString());
		int totalCount = getCount(countStr); // 总共的数量
		if (totalCount == 0) {
			return pager;
		}

		int theLastShowItemCounts = pageNum * currentPageSize; // 想要查询的最后一条展示结果序号
		String excuteSql;
		if (theLastShowItemCounts < 50000) { // 小于这个数量时查询时间影响不大
			excuteSql = select + sWhere.toString() + orderString + " limit " + (theLastShowItemCounts - currentPageSize)
					+ "," + currentPageSize;
		} else {
			if (orderType.equalsIgnoreCase("asc")) {
				excuteSql = select + sWhere.toString() + " and " + orderField + " >= ( select " + orderField
						+ " from sound_record where 1=1 " + sWhere.toString() + orderString + " limit "
						+ (theLastShowItemCounts - currentPageSize) + ",1)" + orderString + " limit " + currentPageSize;

			} else {
				excuteSql = select + sWhere.toString() + " and " + orderField + " <= ( select " + orderField
						+ " from sound_record where 1=1 " + sWhere.toString() + orderString + " limit "
						+ (theLastShowItemCounts - currentPageSize) + ",1)" + orderString + " limit " + currentPageSize;
			}
		}

		pager.setTotalCount(totalCount);
		pager.setPageCounts((totalCount + currentPageSize - 1) / currentPageSize);
		Map<String, String> map = new HashMap<String, String>();
		map.put("sql", excuteSql);
		List<SoundRecord> list = soundRecordMapper.searchBySql(excuteSql);
		pager.setData(list);
		pager.setCurrentPageNum(pageNum);
		pager.setPageSize(list != null ? list.size() : 0);
		String nextPage = String.format(
				"customer_phone=%s&local_phone=%s&user_id=%s&user_name=%s&seconds=%s&maxseconds=%s&currentPageSize=%d&"
						+ "upDownId=%s&sound_time_beg=%s&sound_time_end=%s&" + "add_time_beg=%s&add_time_end=%s&"
						+ "orderField=%s&orderType=%s&pageNum=%d",
				customer_phone, local_phone, user_id, user_name, seconds, maxseconds, currentPageSize, up_down_id,
				sound_time_beg, sound_time_end, add_time_beg, add_time_end, orderField, orderType,
				pageNum == pager.getPageCounts() ? pageNum : pageNum + 1);
		String prePage = String.format(
				"customer_phone=%s&local_phone=%s&user_id=%s&user_name=%s&seconds=%s&maxseconds=%s&currentPageSize=%d&"
						+ "upDownId=%s&sound_time_beg=%s&sound_time_end=%s&" + "add_time_beg=%s&add_time_end=%s&"
						+ "orderField=%s&orderType=%s&pageNum=%d",
				customer_phone, local_phone, user_id, user_name, seconds, maxseconds, currentPageSize, up_down_id,
				sound_time_beg, sound_time_end, add_time_beg, add_time_end, orderField, orderType,
				pageNum == 1 ? 1 : pageNum - 1);

		pager.setNextPageString(nextPage);
		pager.setPrePageString(prePage);
		int lastIndex = pager.getNextPageString().lastIndexOf('&');
		pager.setFirstPageString(pager.getNextPageString().substring(0, lastIndex) + "&pageNum=1");
		pager.setEndPageString(pager.getNextPageString().substring(0, lastIndex) + "&pageNum=" + pager.getPageCounts());

		// 设置排序
		String basicOrderString = String.format(
				"customer_phone=%s&local_phone=%s&user_id=%s&user_name=%s&seconds=%s&maxseconds=%s&currentPageSize=%d&"
						+ "upDownId=%s&sound_time_beg=%s&sound_time_end=%s&" + "add_time_beg=%s&add_time_end=%s&"
						+ "orderField=%s&orderType=%s&pageNum=%d",
				customer_phone, local_phone, user_id, user_name, seconds, maxseconds, currentPageSize, up_down_id,
				sound_time_beg, sound_time_end, add_time_beg, add_time_end, "id", "asc", 1);
		String tempOrderString;
		if (orderField.equalsIgnoreCase("id")) {
			tempOrderString = basicOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString1(tempOrderString);
		} else {
			pager.setOrderString1(basicOrderString);
		}

		if (orderField.equalsIgnoreCase("sound_time")) {
			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=sound_time");
			tempOrderString = tempOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString2(tempOrderString);
		} else {

			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=sound_time");
			pager.setOrderString2(tempOrderString);
		}

		if (orderField.equalsIgnoreCase("add_time")) {
			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=add_time");
			tempOrderString = tempOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString3(tempOrderString);
		} else {

			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=add_time");
			pager.setOrderString3(tempOrderString);
		}

		if (orderField.equalsIgnoreCase("sound_length")) {
			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=sound_length");
			tempOrderString = tempOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString4(tempOrderString);
		} else {

			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=sound_length");
			pager.setOrderString4(tempOrderString);
		}

		return pager;
	}

	// 查询录音表信息
	public Pager searchTrackRecord(String customer_phone, String customer_name, String user_phone, String user_name,
			String up_down_id, String add_time_beg, String add_time_end, String orderField, String orderType,
			int pageNum) throws SQLException {

		Pager pager = new Pager();
		String select = "SELECT track_record.*,pt_user.up_down_id,customer.phone as cus_phone,customer.cus_name "
				+ "from track_record INNER JOIN pt_user ON " + "track_record.user_id=pt_user.phone "
				+ "INNER JOIN customer on track_record.customer_id=customer.id ";

		StringBuilder sWhere = new StringBuilder();
		if (StringUtils.isNotBlank(customer_phone)) {
			sWhere.append(" and customer.phone = '" + customer_phone + "'");
		}

		if (StringUtils.isNotBlank(customer_name)) {
			sWhere.append(" and customer.cus_name like '%" + customer_name + "%'");
		}

		if (StringUtils.isNotBlank(user_phone)) {
			sWhere.append(" and track_record.user_id = '" + user_phone + "'");
		}

		if (StringUtils.isNotBlank(user_name)) {
			sWhere.append(" and track_record.user_name like '%" + user_name + "%'");
		}

		if (StringUtils.isNotBlank(up_down_id)) {
			sWhere.append(" and pt_user.up_down_id like '%" + up_down_id + "%'");
		}

		if (StringUtils.isNotBlank(add_time_beg)) {
			sWhere.append(" and track_record.add_time >= '" + add_time_beg + "'");
		}
		if (StringUtils.isNotBlank(add_time_end)) {
			sWhere.append(" and track_record.add_time <= '" + add_time_end + "'");
		}

		String orderString = "";
		if (StringUtils.isNotBlank(orderField)) {
			orderString = " order by " + orderField + " " + orderType + " ";
		} else {
			orderField = "track_record.id";
			orderType = "desc";
			orderString = " order by track_record.id desc "; // 默认按id升序排序
		}

		String countStr = "SELECT count(*) as count " + "from track_record INNER JOIN pt_user ON "
				+ "track_record.user_id=pt_user.phone " + "INNER JOIN customer on track_record.customer_id=customer.id "
				+ sWhere.toString();
		int totalCount = getCount(countStr); // 总共的数量
		if (totalCount == 0) {
			return pager;
		}

		int theLastShowItemCounts = pageNum * pageSize; // 想要查询的最后一条展示结果序号
		String excuteSql;
		excuteSql = select + sWhere.toString() + orderString + " limit " + (theLastShowItemCounts - pageSize) + ","
				+ pageSize;

		pager.setTotalCount(totalCount);
		pager.setPageCounts((totalCount + pageSize - 1) / pageSize);
		Map<String, String> map = new HashMap<String, String>();
		map.put("sql", excuteSql);
		List<TrackRecord> list = trackRecordMapper.searchBySql(excuteSql);
		pager.setData(list);
		pager.setCurrentPageNum(pageNum);
		pager.setPageSize(list != null ? list.size() : 0);
		String nextPage = String.format(
				"customer_phone=%s&customer_name=%s&user_phone=%s&user_name=%s&"
						+ "up_down_id=%s&add_time_beg=%s&add_time_end=%s&" + "orderField=%s&orderType=%s&pageNum=%d",
				customer_phone, customer_name, user_phone, user_name, up_down_id, add_time_beg, add_time_end,
				orderField, orderType, pageNum == pager.getPageCounts() ? pageNum : pageNum + 1);
		String prePage = String.format(
				"customer_phone=%s&customer_name=%s&user_phone=%s&user_name=%s&"
						+ "up_down_id=%s&add_time_beg=%s&add_time_end=%s&" + "orderField=%s&orderType=%s&pageNum=%d",
				customer_phone, customer_name, user_phone, user_name, up_down_id, add_time_beg, add_time_end,
				orderField, orderType, pageNum == 1 ? 1 : pageNum - 1);

		pager.setNextPageString(nextPage);
		pager.setPrePageString(prePage);
		// 设置排序
		String basicOrderString = String.format(
				"customer_phone=%s&customer_name=%s&user_phone=%s&user_name=%s&"
						+ "up_down_id=%s&add_time_beg=%s&add_time_end=%s&" + "orderField=%s&orderType=%s&pageNum=%d",
				customer_phone, customer_name, user_phone, user_name, up_down_id, add_time_beg, add_time_end,
				"track_record.id", "asc", 1);
		String tempOrderString;
		if (orderField.equalsIgnoreCase("track_record.id")) {
			tempOrderString = basicOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString1(tempOrderString);
		} else {
			pager.setOrderString1(basicOrderString);
		}

		if (orderField.equalsIgnoreCase("track_record.add_time")) {
			tempOrderString = basicOrderString.replaceFirst("orderField=track_record.id",
					"orderField=track_record.add_time");
			tempOrderString = tempOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString2(tempOrderString);
		} else {

			tempOrderString = basicOrderString.replaceFirst("orderField=track_record.id",
					"orderField=track_record.add_time");
			pager.setOrderString2(tempOrderString);
		}

		if (orderField.equalsIgnoreCase("track_record.user_id")) {
			tempOrderString = basicOrderString.replaceFirst("orderField=track_record.id",
					"orderField=track_record.user_id");
			tempOrderString = tempOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString3(tempOrderString);
		} else {

			tempOrderString = basicOrderString.replaceFirst("orderField=track_record.id",
					"orderField=track_record.user_id");
			pager.setOrderString3(tempOrderString);
		}

		return pager;
	}

	public Pager searchHistoryDaiKuan(String upDownId, String user_phone, String user_name, String date_beg,
			String date_end, String busiName, String state, String orderField, String orderType, int pageNum,
			int tempPageSize, boolean isOnlyCountTotal) throws SQLException {

		if (tempPageSize == 0) {
			tempPageSize = pageSize;
		}

		Pager pager = new Pager();
		String select = "select * from busireport where 1=1 ";

		StringBuilder sWhere = new StringBuilder();
		if (StringUtils.isNotBlank(upDownId)) {
			sWhere.append(" and up_down_id like '%" + upDownId + "%'");
		}

		if (StringUtils.isNotBlank(user_phone)) {
			sWhere.append(" and user_phone like '%" + user_phone + "%'");
		}

		if (StringUtils.isNotBlank(user_name)) {
			sWhere.append(" and user_name like '%" + user_name + "%'");
		}

		if (StringUtils.isNotBlank(date_beg)) {
			sWhere.append(" and dateStr >= '" + date_beg + "'");
		}

		if (StringUtils.isNotBlank(date_end)) {
			sWhere.append(" and dateStr <= '" + date_end + "'");
		}

		if (!StringUtils.equalsIgnoreCase(busiName, "-1")) {
			sWhere.append(" and business_name = '" + busiName + "'");
		}

		if (!StringUtils.equalsIgnoreCase(state, "-1")) {
			sWhere.append(" and state =" + state + "");
		}

		String orderString = "";
		if (StringUtils.isNotBlank(orderField)) {
			orderString = " order by " + orderField + " " + orderType + " ";
		} else {
			orderField = "id";
			orderType = "asc";
			orderString = " order by id asc "; // 默认按id升序排序
		}

		String countStr = String.format("select count(id) as count from busireport where 1=1 %s ", sWhere.toString());
		int totalCount = getCount(countStr); // 总共的数量
		pager.setTotalCount(totalCount);
		if (totalCount == 0 || isOnlyCountTotal) {
			return pager;
		}

		int theLastShowItemCounts = pageNum * tempPageSize; // 想要查询的最后一条展示结果序号
		String excuteSql;
		if (theLastShowItemCounts < 50000) { // 小于这个数量时查询时间影响不大
			excuteSql = select + sWhere.toString() + orderString + " limit " + (theLastShowItemCounts - tempPageSize)
					+ "," + tempPageSize;
		} else {
			if (orderType.equalsIgnoreCase("asc")) {
				excuteSql = select + sWhere.toString() + " and " + orderField + " >= ( select " + orderField
						+ " from sound_record where 1=1 " + sWhere.toString() + orderString + " limit "
						+ (theLastShowItemCounts - tempPageSize) + ",1)" + orderString + " limit " + tempPageSize;

			} else {
				excuteSql = select + sWhere.toString() + " and " + orderField + " <= ( select " + orderField
						+ " from sound_record where 1=1 " + sWhere.toString() + orderString + " limit "
						+ (theLastShowItemCounts - tempPageSize) + ",1)" + orderString + " limit " + tempPageSize;
			}
		}

		pager.setPageCounts((totalCount + tempPageSize - 1) / tempPageSize);
		Map<String, String> map = new HashMap<String, String>();
		map.put("sql", excuteSql);
		List<BusiReport> list = reportMapper.searchBySql(excuteSql);
		// 设置客户状态名
		if (list != null && list.size() > 0) {
			for (BusiReport busiReport : list) {
				busiReport.setStateName(StaticService.getCustomStateName(busiReport.getState()));
			}
		}
		pager.setData(list);
		pager.setCurrentPageNum(pageNum);
		pager.setPageSize(list != null ? list.size() : 0);
		String nextPage = String.format(
				"upDownId=%s&user_phone=%s&user_name=%s&date_beg=%s&date_end=%s&busiName=%s&" + "state=%s&"
						+ "orderField=%s&orderType=%s&pageNum=%d",
				upDownId, user_phone, user_name, date_beg, date_end, busiName, state, orderField, orderType,
				pageNum == pager.getPageCounts() ? pageNum : pageNum + 1);
		String prePage = String.format(
				"upDownId=%s&user_phone=%s&user_name=%s&date_beg=%s&date_end=%s&busiName=%s&" + "state=%s&"
						+ "orderField=%s&orderType=%s&pageNum=%d",
				upDownId, user_phone, user_name, date_beg, date_end, busiName, state, orderField, orderType,
				pageNum == 1 ? 1 : pageNum - 1);

		pager.setNextPageString(nextPage);
		pager.setPrePageString(prePage);
		int lastIndex = pager.getNextPageString().lastIndexOf('&');
		pager.setFirstPageString(pager.getNextPageString().substring(0, lastIndex) + "&pageNum=1");
		pager.setEndPageString(pager.getNextPageString().substring(0, lastIndex) + "&pageNum=" + pager.getPageCounts());
		// 设置排序
		String basicOrderString = String.format(
				"upDownId=%s&user_phone=%s&user_name=%s&date_beg=%s&date_end=%s&busiName=%s&" + "state=%s&"
						+ "orderField=%s&orderType=%s&pageNum=%d",
				upDownId, user_phone, user_name, date_beg, date_end, busiName, state, "id", "asc", 1);
		String tempOrderString;
		if (orderField.equalsIgnoreCase("id")) {
			tempOrderString = basicOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString1(tempOrderString);
		} else {
			pager.setOrderString1(basicOrderString);
		}

		if (orderField.equalsIgnoreCase("up_down_id")) {
			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=up_down_id");
			tempOrderString = tempOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString2(tempOrderString);
		} else {

			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=up_down_id");
			pager.setOrderString2(tempOrderString);
		}

		if (orderField.equalsIgnoreCase("business_name")) {
			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=business_name");
			tempOrderString = tempOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString3(tempOrderString);
		} else {

			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=business_name");
			pager.setOrderString3(tempOrderString);
		}

		if (orderField.equalsIgnoreCase("state")) {
			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=state");
			tempOrderString = tempOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString4(tempOrderString);
		} else {

			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=state");
			pager.setOrderString4(tempOrderString);
		}

		if (orderField.equalsIgnoreCase("user_phone")) {
			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=user_phone");
			tempOrderString = tempOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString5(tempOrderString);
		} else {

			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=user_phone");
			pager.setOrderString5(tempOrderString);
		}

		return pager;
	}

	// 查询用户登录记录
	public Pager searchLoginRecord(String user_phone, String user_name, String up_down_id, String beg_time,
			String end_time, String orderField, String orderType, int pageNum) throws SQLException {
		Pager pager = new Pager();
		String select = "select * from login_record where 1=1 ";

		StringBuilder sWhere = new StringBuilder();
		if (StringUtils.isNotBlank(user_phone)) {
			sWhere.append(" and user_phone like '%" + user_phone + "%'");
		}

		if (StringUtils.isNotBlank(user_name)) {
			sWhere.append(" and user_name like '%" + user_name + "%'");
		}

		if (StringUtils.isNotBlank(up_down_id) && !up_down_id.equals("all")) {
			sWhere.append(" and up_down_id = '" + up_down_id + "'");
		}

		if (StringUtils.isNotBlank(beg_time)) {
			sWhere.append(" and login_time >= '" + beg_time + "'");
		}
		if (StringUtils.isNotBlank(end_time)) {
			sWhere.append(" and login_time <= '" + end_time + "'");
		}

		String orderString = "";
		if (StringUtils.isNotBlank(orderField)) {
			orderString = " order by " + orderField + " " + orderType + " ";
		} else {
			orderField = "id";
			orderType = "desc";
			orderString = " order by id desc "; // 默认按id升序排序
		}

		String countStr = String.format("select count(id) as count from login_record where 1=1 %s", sWhere.toString());
		int totalCount = getCount(countStr); // 总共的数量
		if (totalCount == 0) {
			return pager;
		}

		int theLastShowItemCounts = pageNum * pageSize; // 想要查询的最后一条展示结果序号
		String excuteSql;
		if (theLastShowItemCounts < 50000) { // 小于这个数量时查询时间影响不大
			excuteSql = select + sWhere.toString() + orderString + " limit " + (theLastShowItemCounts - pageSize) + ","
					+ pageSize;
		} else {
			if (orderType.equalsIgnoreCase("asc")) {
				excuteSql = select + sWhere.toString() + " and " + orderField + " >= ( select " + orderField
						+ " from login_record where 1=1 " + sWhere.toString() + orderString + " limit "
						+ (theLastShowItemCounts - pageSize) + ",1)" + orderString + " limit " + pageSize;

			} else {
				excuteSql = select + sWhere.toString() + " and " + orderField + " <= ( select " + orderField
						+ " from login_record where 1=1 " + sWhere.toString() + orderString + " limit "
						+ (theLastShowItemCounts - pageSize) + ",1)" + orderString + " limit " + pageSize;
			}
		}

		pager.setTotalCount(totalCount);
		pager.setPageCounts((totalCount + pageSize - 1) / pageSize);
		List<LoginRecord> list = loginRecordMapper.searchBySql(excuteSql);
		pager.setData(list);
		pager.setCurrentPageNum(pageNum);
		pager.setPageSize(list != null ? list.size() : 0);
		String nextPage = String.format(
				"user_phone=%s&user_name=%s&up_down_id=%s&beg_time=%s&end_time=%s&"
						+ "orderField=%s&orderType=%s&pageNum=%d",
				user_phone, user_name, up_down_id, beg_time, end_time, orderField, orderType,
				pageNum == pager.getPageCounts() ? pageNum : pageNum + 1);
		String prePage = String.format(
				"user_phone=%s&user_name=%s&up_down_id=%s&beg_time=%s&end_time=%s&"
						+ "orderField=%s&orderType=%s&pageNum=%d",
				user_phone, user_name, up_down_id, beg_time, end_time, orderField, orderType,
				pageNum == 1 ? 1 : pageNum - 1);

		pager.setNextPageString(nextPage);
		pager.setPrePageString(prePage);
		int lastIndex = pager.getNextPageString().lastIndexOf('&');
		pager.setFirstPageString(pager.getNextPageString().substring(0, lastIndex) + "&pageNum=1");
		pager.setEndPageString(pager.getNextPageString().substring(0, lastIndex) + "&pageNum=" + pager.getPageCounts());
		// 设置排序
		String basicOrderString = String.format(
				"user_phone=%s&user_name=%s&up_down_id=%s&beg_time=%s&end_time=%s&"
						+ "orderField=%s&orderType=%s&pageNum=%d",
				user_phone, user_name, up_down_id, beg_time, end_time, "id", "asc", 1);
		String tempOrderString;
		if (orderField.equalsIgnoreCase("id")) {
			tempOrderString = basicOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString1(tempOrderString);
		} else {
			pager.setOrderString1(basicOrderString);
		}

		if (orderField.equalsIgnoreCase("up_down_id")) {
			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=up_down_id");
			tempOrderString = tempOrderString.replaceFirst("orderType=asc",
					"orderType=" + (orderType.equalsIgnoreCase("asc") ? "desc" : "asc"));
			pager.setOrderString2(tempOrderString);
		} else {

			tempOrderString = basicOrderString.replaceFirst("orderField=id", "orderField=up_down_id");
			pager.setOrderString2(tempOrderString);
		}

		return pager;
	}

	public int getCount(String sql) throws SQLException {
		int count = 0;
		try {
			count = customerMapper.countBySql(sql);

		} catch (Exception ex) {

			TxtLogger.log(ex.toString(), LogTye.ERROR, LogFileCreateType.OneFileAnHour, "");
			ex.printStackTrace();
		}
		return count;
	}

}
