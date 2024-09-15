package zero.base.dividends.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import zero.base.dividends.dto.Company;
import zero.base.dividends.dto.ScrapedResult;
import zero.base.dividends.persist.CompanyRepository;
import zero.base.dividends.persist.DividendRepository;
import zero.base.dividends.persist.entity.CompanyEntity;
import zero.base.dividends.persist.entity.DividendEntity;
import zero.base.dividends.scraper.Scraper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompanyService {
    private final Scraper yahooFinanceScraper;
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    //회사가 있는지 확인하는 로직 & db에 저장되어 있는지 확인
    public Company save(String ticker) {
        boolean exists = this.companyRepository.existsByTicker(ticker);
        if (exists){
            throw new RuntimeException("already exists ticker -> " + ticker);
        }
        return this.storeCompanyAndDividend(ticker);
    }

    //회사가 없다면 ? 실행하는 로직
    private Company storeCompanyAndDividend(String ticker) {
        //ticker를 기준으로 회사를 스크래핑
        Company company = this.yahooFinanceScraper.scrapCompanyByTicker(ticker);
        if (ObjectUtils.isEmpty(company)) {
            throw new RuntimeException("failed to scrap ticker -> " + ticker);
        }
        //해당 회사가 존재할 경우, 회사의 배당금 정보를 스크래핑
        ScrapedResult scrapedResult = this.yahooFinanceScraper.scrap(company);

        System.out.println("Scraped Result: 여기까지 데이터 정상 수량으로 들어오고있음 " + scrapedResult);

        //스크래핑 결과
        CompanyEntity companyEntity = this.companyRepository.save(new CompanyEntity(company));
        System.out.println("회사 === " + companyEntity);

        List<DividendEntity> dividendEntityList = scrapedResult.getDividends()
                .stream()
                .map(e -> new DividendEntity(companyEntity.getId(), e))
                .collect(Collectors.toList());
        List<DividendEntity> savedDividends = this.dividendRepository.saveAll(dividendEntityList);
        System.out.println("Saved Dividends, 최종 데이터 수: " + savedDividends.size());
        return company;
    }
}
