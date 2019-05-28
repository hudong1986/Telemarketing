package telemarketing.repository;

import java.util.List;

import telemarketing.model.AllowIp;

public interface AllowIpMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AllowIp record);

    int insertSelective(AllowIp record);

    AllowIp selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AllowIp record);

    int updateByPrimaryKey(AllowIp record);
    
    List<AllowIp> selectAll();
}