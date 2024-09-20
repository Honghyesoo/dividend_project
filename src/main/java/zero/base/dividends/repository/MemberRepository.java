package zero.base.dividends.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zero.base.dividends.domain.MemberEntity;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity,Long> {
    Optional<MemberEntity> findByUsername(String username);
    boolean existsByUsername(String username); // 회원가입할때 이미 존재된 아이디인지 확인

}
