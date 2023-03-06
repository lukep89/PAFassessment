package ibf2022.PAFassessment.repo;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;

import ibf2022.PAFassessment.model.Account;

@Repository
public class AccountsRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static final String SQL_GET_ACCOUNTS = """
            select *
            from accounts
            """;

    public static final String SQL_GET_ACCOUNT_BY_ID = """
            select * from accounts
            where account_id = ?
            """;

    public static final String SQL_UPDATE_BALANCE_BY_ID = """
            update accounts
            set balance = ?
            where account_id = ?
            """;

    public List<Account> getListOfAccounts() {
        return jdbcTemplate.query(SQL_GET_ACCOUNTS, BeanPropertyRowMapper.newInstance(Account.class));

    }

    public Optional<Account> getAccountById(String id) {

        Account result = jdbcTemplate.queryForObject(SQL_GET_ACCOUNT_BY_ID,
                BeanPropertyRowMapper.newInstance(Account.class), id);

        if (result == null) {
            return Optional.empty();
        }

        return Optional.of(result);
    }

    public Boolean updateBalanceById(Float amount, String id) {

        Integer result = jdbcTemplate.update(SQL_UPDATE_BALANCE_BY_ID, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setFloat(1, amount);
                ps.setString(2, id);
            }

        });

        System.out.println("======== @7 Repo updateBlance: " + getAccountById(id).get().toString() + "\n\n");

        return result > 0;
    }

}
