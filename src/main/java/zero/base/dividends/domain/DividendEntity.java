package zero.base.dividends.domain;

import jakarta.persistence.*;
import lombok.*;
import zero.base.dividends.dto.DividendDto;

import java.time.LocalDateTime;

@Entity(name = "DIVIDEND")
@Getter
@NoArgsConstructor
@ToString
@Table(
        uniqueConstraints = {
                @UniqueConstraint( //중복 데이터 저장을 방지하는 제약 조건
                        columnNames = {"companyId","date"}
                )
        }
)
public class DividendEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long companyId;
    private LocalDateTime date;
    private String dividend;

    public DividendEntity (Long companyId , DividendDto dividendDto){
        this.companyId = companyId;
        this.date = dividendDto.getDate();
        this.dividend = dividendDto.getDividend();

    }


}
