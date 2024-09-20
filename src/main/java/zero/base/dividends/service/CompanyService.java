package zero.base.dividends.service;

import lombok.AllArgsConstructor;
import org.apache.commons.collections4.Trie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import zero.base.dividends.dto.CompanyDto;
import zero.base.dividends.dto.ScrapedResult;
import zero.base.dividends.repository.CompanyRepository;
import zero.base.dividends.repository.DividendRepository;
import zero.base.dividends.domain.CompanyEntity;
import zero.base.dividends.domain.DividendEntity;
import zero.base.dividends.scraper.Scraper;

import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class CompanyService {
    private final Trie trie;
    private final Scraper yahooFinanceScraper;
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    //회사가 있는지 확인하는 로직 & db에 저장되어 있는지 확인
    public CompanyDto save(String ticker) {
        boolean exists = this.companyRepository.existsByTicker(ticker);
        if (exists) {
            throw new RuntimeException("already exists ticker -> " + ticker);
        }
        return this.storeCompanyAndDividend(ticker);
    }

    //회사 조회
    public Page<CompanyEntity> getAllCompany(Pageable pageable) {
        return this.companyRepository.findAll(pageable);
    }

    //회사가 없다면 ? 실행하는 로직
    private CompanyDto storeCompanyAndDividend(String ticker) {
        //ticker를 기준으로 회사를 스크래핑
        CompanyDto companyDto = this.yahooFinanceScraper.scrapCompanyByTicker(ticker);
        if (ObjectUtils.isEmpty(companyDto)) {
            throw new RuntimeException("failed to scrap ticker -> " + ticker);
        }
        //해당 회사가 존재할 경우, 회사의 배당금 정보를 스크래핑
        ScrapedResult scrapedResult = this.yahooFinanceScraper.scrap(companyDto);

        System.out.println("Scraped Result: 여기까지 데이터 정상 수량으로 들어오고있음 " + scrapedResult);

        //스크래핑 결과
        CompanyEntity companyEntity = this.companyRepository.save(new CompanyEntity(companyDto));
        System.out.println("회사 === " + companyEntity);

        List<DividendEntity> dividendEntityList = scrapedResult.getDividendDtos()
                .stream()
                .map(e -> new DividendEntity(companyEntity.getId(), e))
                .collect(Collectors.toList());
        List<DividendEntity> savedDividends = this.dividendRepository.saveAll(dividendEntityList);
        System.out.println("Saved Dividends, 최종 데이터 수: " + savedDividends.size());
        return companyDto;
    }

    //like를 활용한 자동저장
    public List<String> getCompanyNamesByKeyword(String keyword) {
        //한번에 10개씩 나오게
        Pageable limit = PageRequest.of(0,10);

        Page<CompanyEntity> companyEntities = this.companyRepository.findByNameStartingWithIgnoreCase(keyword,limit);
        return companyEntities.stream()
                .map(e -> e.getName())
                .collect(Collectors.toList());
    }

    //회사명 저장 후 자동저장 로직(trie)
    public void addAutoCompleteKeyword(String keyword) {
        this.trie.put(keyword, null);
    }

    //회사명을 조회 후 자동저장 로직(trie)
    public List<String> autoComplete(String keyword) {
        return (List<String>) this.trie.prefixMap(keyword).keySet()
                .stream().collect(Collectors.toList());
    }

    //trie(자동저장)된 데이터 삭제 로직
    public void deleteAutoCompleteKeyword(String keyword) {
        this.trie.remove(keyword);
    }
}
