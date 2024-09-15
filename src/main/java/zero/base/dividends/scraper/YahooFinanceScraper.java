package zero.base.dividends.scraper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import zero.base.dividends.dto.Company;
import zero.base.dividends.dto.Dividend;
import zero.base.dividends.dto.ScrapedResult;
import zero.base.dividends.dto.constants.Month;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class YahooFinanceScraper implements Scraper{
    private static final String STATISTICS_URL = "https://finance.yahoo.com/quote/%s/history/?frequency=1mo&period1=%d&period2=%d";
    private static final String SUMMARY_URL = "http://finance.yahoo.com/quote/%s?p=%s";
    private static final long START_TIME = 86400;  // 60 * 60 * 24 -> 1일


    @Override
    public ScrapedResult scrap(Company company) {
        var scrapResult = new ScrapedResult();
        scrapResult.setCompany(company);

        try {
            long now = System.currentTimeMillis() / 1000;

            String url = String.format(STATISTICS_URL, company.getTicker(), START_TIME, now);
            // Jsoup을 사용하여 Yahoo Finance 페이지에 연결
            Connection connection = Jsoup.connect(url);
            Document document = connection.get(); // HTML 문서 가져오기

            // 클래스 이름이 맞는지 확인 필요
            Elements parsingDivs = document.getElementsByAttributeValue("class", "table yf-ewueuo noDl");
            if (parsingDivs.isEmpty()) {
                throw new RuntimeException("No table elements found with the specified class.");
            }

            Element tableEle = parsingDivs.get(0); // table 전체 가져오기
            Element tbody = tableEle.children().get(1); // tbody 가져오기

            List<Dividend> dividends = new ArrayList<>();
            int count = 0;
            for (Element e : tbody.children()) {
                String txt = e.text();
                if (!txt.endsWith("Dividend")) {
                    continue;
                }

                String[] splits = txt.split(" ");
                //System.out.println("2nkjqwndjkqwnkjdasda=============" + Arrays.toString(splits));
                int month = Month.strToNumber(splits[0]);
                //System.out.println("wqjkdnqkjwndkj" + month);
                int day = Integer.parseInt(splits[1].replace(",", ""));
                //System.out.println("day ========= " + day);
                int year = Integer.parseInt(splits[2]);
                //System.out.println("year ========= " + year);
                String dividend = splits[3];
                //System.out.println("dividend ========= " + dividend);

                if (month < 0) {
                    throw new RuntimeException("Unexpected Month enum value - > " + splits[0]);
                }

                dividends.add(Dividend.builder()
                        .date(LocalDateTime.of(year, month, day, 0, 0))
                        .dividend(dividend)
                        .build());

                count++;
            }
            System.out.println("총 " + count + "개의 배당금 데이터가 파싱되었습니다.");

            scrapResult.setDividends(dividends);
        } catch (IOException e) {
            System.err.println("Failed to fetch data from Yahoo Finance: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }

        return scrapResult;
    }

    //회사명 가져오는 로직
    @Override
    public Company scrapCompanyByTicker(String ticker){
        String url = String.format(SUMMARY_URL,ticker,ticker);
        try {
            Document document = Jsoup.connect(url).get();
            Element titleEle = document.getElementsByTag("h1").get(1);
            String title = titleEle.text().split("\\(")[0].trim();
            // ex) abc - def - xzy -> "-"기준으로 쪼갬
            return Company.builder()
                    .ticker(ticker)
                    .name(title)
                    .build();
        }catch (IOException e ){
            e.printStackTrace();
        }
        return null;
    }
}
