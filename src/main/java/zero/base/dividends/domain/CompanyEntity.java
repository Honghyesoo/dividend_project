package zero.base.dividends.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import zero.base.dividends.dto.CompanyDto;

@Entity(name = "COMPANY")
@Getter
@ToString
@NoArgsConstructor
public class CompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String ticker;
    private String name;

    public CompanyEntity(CompanyDto companyDto){
        this.ticker = companyDto.getTicker();
        this.name = companyDto.getName();
    }
}
