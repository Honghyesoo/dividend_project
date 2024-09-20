package zero.base.dividends.service;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import zero.base.dividends.dto.CompanyDto;
import zero.base.dividends.dto.DividendDto;
import zero.base.dividends.dto.ScrapedResult;
import zero.base.dividends.dto.constants.CacheKey;
import zero.base.dividends.persist.CompanyRepository;
import zero.base.dividends.persist.DividendRepository;
import zero.base.dividends.persist.entity.CompanyEntity;
import zero.base.dividends.persist.entity.DividendEntity;


import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FinanceService {
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    @Cacheable(key = "#p0", value = CacheKey.KEY_FINANCE)
    public ScrapedResult getDividendByCompanyName(String companyName) {

        //1. 회사명을 기준으로 회사 정보를 조회
        CompanyEntity company = this.companyRepository.findByName(companyName)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회사명 입니다"));

        //2. 조회된 회사 ID로 배당금을 정보 조회
        List<DividendEntity> dividendEntities = this.dividendRepository.findAllByCompanyId(company.getId());

        //3. 결과 조합 반환
        List<DividendDto> dividendDtos = new ArrayList<>();
        for (DividendEntity entity: dividendEntities) {
            dividendDtos.add(new DividendDto(entity.getDate(), entity.getDividend()));
        }

        return new ScrapedResult(new CompanyDto(company.getTicker(),company.getName()),
                dividendDtos);
    }

}
