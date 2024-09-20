package zero.base.dividends.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import zero.base.dividends.domain.DividendEntity;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DividendRepository extends JpaRepository<DividendEntity,Long> {
    List<DividendEntity>findAllByCompanyId(Long companyId);
    @Transactional
    void deleteAllByCompanyId(Long id);

    boolean existsByCompanyIdAndDate(Long companyId, LocalDateTime dateTime);
}
