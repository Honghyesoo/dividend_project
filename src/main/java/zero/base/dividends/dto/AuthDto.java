package zero.base.dividends.dto;

import lombok.Data;
import zero.base.dividends.domain.MemberEntity;

import java.util.List;

public class AuthDto {

    @Data
    public static class SignIn {
        private String username;
        private String password;
    }

    @Data
    public static class SignUp {
        private String username;
        private String password;
        private List<String> roles;

        public MemberEntity toEntity() {
            return MemberEntity.builder()
                    .username(this.username)
                    .password(this.password)
                    .roles(this.roles)
                    .build();
        }
    }
}
