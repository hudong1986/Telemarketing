package telemarketing.repository;

import telemarketing.model.Phone_record;

public interface Phone_recordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Phone_record record);

    int insertSelective(Phone_record record);

    Phone_record selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Phone_record record);

    int updateByPrimaryKey(Phone_record record);
}