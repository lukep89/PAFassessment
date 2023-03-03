package ibf2022.PAFassessment.repo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ibf2022.PAFassessment.model.Account;

@Repository
public class AccountRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static final String SQL_GET_ACCOUNTS = """
            select *
            from accounts
            """;

    public List<Account> getListOfAccounts() {
        return jdbcTemplate.query(SQL_GET_ACCOUNTS, BeanPropertyRowMapper.newInstance(Account.class));

    }

}
