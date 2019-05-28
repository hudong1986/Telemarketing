package telemarketing.repository;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import telemarketing.model.Pt_user;

public interface Pt_userMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Pt_user record);

    int insertSelective(Pt_user record);

    Pt_user selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Pt_user record);

    int updateByPrimaryKey(Pt_user record);
    
    Pt_user selectByPhone(String phone);
    
    List<Pt_user> selectByDeptAndRole(@Param("up_down_id") String up_down_id,@Param("role") String role);
    
    List<Pt_user> selectByUpDownId(@Param("up_down_id") String up_down_id);
    
    List<Pt_user> selectByDeptId(@Param("deptId") int deptId);
    
    List<Pt_user> searchBySql(@Param("sql") String sql);
    
    int updateLeaveMore(@Param("list") List<Integer> list);
    //批量重新为在职
    int updateOnWorkMore(@Param("list") List<Integer> list);
    
    int updateRestPwdMore(@Param("list") List<Integer> list,@Param("pwd") String pwd);
    
    int leaveMoreAndBackCustomer(@Param("list") List<String> list,@Param("who_use")String who_use,
    		@Param("who_use_name")String who_use_name,@Param("who_get_time")Date who_get_time,
    		@Param("who_up_down_id")String who_up_down_id
    		);
 }