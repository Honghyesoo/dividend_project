package zero.base.dividends;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zero.base.dividends.dto.Company;
import zero.base.dividends.scraper.Scraper;
import zero.base.dividends.scraper.YahooFinanceScraper;

@SpringBootApplication
public class DividendsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DividendsApplication.class, args);
	}
}
