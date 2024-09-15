package zero.base.dividends.scraper;

import zero.base.dividends.dto.Company;
import zero.base.dividends.dto.ScrapedResult;

public interface Scraper {
    Company scrapCompanyByTicker(String ticker);
    ScrapedResult scrap(Company company);
}
