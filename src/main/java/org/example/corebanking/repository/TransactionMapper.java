package org.example.corebanking.repository;

import org.example.corebanking.domain.response.Transaction;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TransactionMapper {

    @Select("SELECT * FROM transaction WHERE account_id = #{accountId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "accountId", column = "account_id"),
            @Result(property = "amount", column = "amount"),
            @Result(property = "currency", column = "currency"),
            @Result(property = "direction", column = "direction"),
            @Result(property = "description", column = "description")
    })
    List<Transaction> getTransactionsByAccountId(Long accountId);

    @Insert("INSERT INTO transaction(account_id, amount, currency, direction, description, balance_after_transaction)" +
            " VALUES(#{accountId}, #{amount}, #{currency}, #{direction}, #{description}, #{balanceAfterTransaction})")
    void insertTransaction(Transaction transaction);
}