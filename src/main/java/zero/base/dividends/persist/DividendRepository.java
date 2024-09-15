package zero.base.dividends.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zero.base.dividends.persist.entity.DividendEntity;

@Repository
public interface DividendRepository extends JpaRepository<DividendEntity,Long> {
}
