package zero.base.dividends.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ScrapedResult {
    private CompanyDto companyDto;
    private List<DividendDto> dividendDtos;

    public ScrapedResult(){this.dividendDtos = new ArrayList<>();}
}
