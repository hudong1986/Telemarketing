package telemarketing.repository;

import java.util.List;

import telemarketing.model.CustomerState;

public interface CustomerStateMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomerState record);

    int insertSelective(CustomerState record);

    CustomerState selectByPrimaryKey(Integer id);
    List<CustomerState> selectAll();

    int updateByPrimaryKeySelective(CustomerState record);

    int updateByPrimaryKey(CustomerState record);
}