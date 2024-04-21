package org.example.corebanking.repository;

import org.example.corebanking.domain.response.Balance;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface BalanceMapper {

    @Insert("INSERT INTO balance(account_id, available_amount, currency) VALUES(#{accountId}, #{availableAmount}, #{currency})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertBalance(Balance balance);

    @Update("UPDATE balance SET available_amount = #{newAmount} WHERE id = #{id}")
    void updateBalance(@Param("id") Long id, @Param("newAmount") BigDecimal newAmount);

    @Select("SELECT id, account_id, available_amount, currency FROM balance WHERE account_id = #{accountId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "accountId", column = "account_id"),
            @Result(property = "availableAmount", column = "available_amount"),
            @Result(property = "currency", column = "currency")
    })
    List<Balance> getBalancesByAccountId(Long accountId);

    @Select("SELECT * FROM balance WHERE account_id = #{accountId} AND currency = #{currency}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "accountId", column = "account_id"),
            @Result(property = "availableAmount", column = "available_amount"),
            @Result(property = "currency", column = "currency")
    })
    Balance getBalanceByAccountIdAndCurrency(@Param("accountId") Long accountId, @Param("currency") String currency);

}