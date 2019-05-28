package telemarketing.repository;

import java.util.List;

import telemarketing.model.Business_type;

public interface Business_typeMapper {
    int deleteByPrimaryKey(Byte id);

    int insert(Business_type record);

    int insertSelective(Business_type record);

    Business_type selectByPrimaryKey(Byte id);

    int updateByPrimaryKeySelective(Business_type record);

    int updateByPrimaryKey(Business_type record);
    
    List<Business_type> selectAll();
}