package telemarketing.repository;

import java.util.List;

import telemarketing.model.Pt_role;

public interface Pt_roleMapper {
    int deleteByPrimaryKey(String roleCode);

    int insert(Pt_role record);

    int insertSelective(Pt_role record);

    Pt_role selectByPrimaryKey(String roleCode);

    int updateByPrimaryKeySelective(Pt_role record);

    int updateByPrimaryKey(Pt_role record);
    
    List<Pt_role> selectAll();
}