package zero.base.dividends.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zero.base.dividends.domain.CompanyEntity;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity,Long> {
    boolean existsByTicker(String ticker);

    Optional<CompanyEntity> findByName(String name);

    //like를 활용한 자동완성
    Page <CompanyEntity> findByNameStartingWithIgnoreCase(String s, Pageable pageable);
}
