package hckt.respalhex.global.config;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CleanUp {
    private final JdbcTemplate jdbcTemplate;

    public void all() {
        // 제약조건 무효화 -> 데이터 초기화 -> 제약조건 재설정
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE MEMBER");
        jdbcTemplate.execute("TRUNCATE TABLE OAUTH");
        jdbcTemplate.execute("TRUNCATE TABLE REFRESH_TOKEN");
        jdbcTemplate.execute("TRUNCATE TABLE USER_ACCOUNT");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }
}
