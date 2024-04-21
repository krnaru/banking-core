package org.example.corebanking.repository;

import org.apache.ibatis.annotations.*;
import org.example.corebanking.domain.response.Account;

@Mapper
public interface AccountMapper {
    @Select("SELECT * FROM account WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "customerId", column = "customer_id"),
            @Result(property = "country", column = "country")
    })
    Account getAccount(Long id);

    @Insert("INSERT INTO account(customer_id, country) VALUES(#{customerId}, #{country})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertAccount(Account account);
}
