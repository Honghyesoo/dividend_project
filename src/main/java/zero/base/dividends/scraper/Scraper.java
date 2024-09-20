package zero.base.dividends.scraper;

import zero.base.dividends.dto.CompanyDto;
import zero.base.dividends.dto.ScrapedResult;

public interface Scraper {
    CompanyDto scrapCompanyByTicker(String ticker);
    ScrapedResult scrap(CompanyDto companyDto);
}
