package zero.base.dividends.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import zero.base.dividends.dto.Company;
import zero.base.dividends.dto.ScrapedResult;
import zero.base.dividends.persist.CompanyRepository;
import zero.base.dividends.persist.DividendRepository;
import zero.base.dividends.persist.entity.CompanyEntity;
import zero.base.dividends.persist.entity.DividendEntity;
import zero.base.dividends.scraper.Scraper;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class ScraperScheduler { //주기적으로 배당금 데이터를 가져오는 로직
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;
    private final Scraper yahooFinanceScraper;

    @Scheduled(fixedDelay = 1000)
    public void test1() throws InterruptedException{
        Thread.sleep(10000); // 10초간 일시정지
        System.out.println(Thread.currentThread().getName() + " -> 테스트 1 : " + LocalDateTime.now());
    }

    @Scheduled(fixedDelay = 1000)
    public void test2() throws InterruptedException{
        System.out.println(Thread.currentThread().getName() + " -> 테스트 2 : " + LocalDateTime.now());
    }



    // 일정 주기 마다 실행
//    @Scheduled(cron = "${scheduler.scrap.yahoo}")
    public void yahooFinanceScheduling() {
        log.info("scraping scheduler is started"); //스케줄러가 정상적으로 움직이는지 확인
        //저장된 회사 목록 조회
        List<CompanyEntity> companies = this.companyRepository.findAll();

        //회사마다 배당금 정보를 새로 스크래핑
        ScrapedResult scrapedResult = null;
        for (var company : companies) {
            log.info("scraping scheduler is started -> " + company.getName() ); //어느 회사 배당금이 새로 저장이 됬는지 확인
            scrapedResult = this.yahooFinanceScraper.scrap(Company.builder()
                    .name(company.getName())
                    .ticker(company.getTicker())
                    .build());


            //스크래핑한 배당금 정보 중 데이터베이스에 없는 값은 저장
            scrapedResult.getDividends().stream()
                    //디비든 모델을 디비든 엔티티로 매핑
                    .map(e -> new DividendEntity(company.getId(), e))
                    // 엘리먼트를 하나씩 디비든 레파지토리에 삽입
                    .forEach(e -> {
                        boolean exists = this.dividendRepository.existsByCompanyIdAndDate(e.getCompanyId(), e.getDate());
                        if (!exists) {
                            this.dividendRepository.save(e);
                        }
                    });

            //연속적으로 스크래핑 대상 사이트서버에 요청을 날리지 않도록 일시정지
            try {
                Thread.sleep(3000); //3초간 정지
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }

        }


    }
}
