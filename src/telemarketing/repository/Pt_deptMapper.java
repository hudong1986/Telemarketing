package telemarketing.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import telemarketing.model.Pt_dept;

public interface Pt_deptMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Pt_dept record);

    int insertSelective(Pt_dept record);

    Pt_dept selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Pt_dept record);

    int updateByPrimaryKey(Pt_dept record);
    
    List<Pt_dept> selectALL();
    
    List<Pt_dept> searchMyDept(@Param("upDownId") String upDownId);
}