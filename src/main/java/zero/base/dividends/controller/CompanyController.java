package zero.base.dividends.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import zero.base.dividends.dto.Company;
import zero.base.dividends.service.CompanyService;

@RestController
@RequestMapping("/company")
@AllArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    //배당금 검색 자동완성
    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam(name = "keyword") String keyword){
        return null;
    }

    //회사 리스트 조회
    @GetMapping
    public ResponseEntity<?> searchCompany(){
        return null;
    }

    // 회사 저장
    @PostMapping
    public ResponseEntity<?> addCompany(@RequestBody Company request){
        String ticker = request.getTicker().trim();
        if (ObjectUtils.isEmpty(ticker)){
            throw  new RuntimeException("ticker is empty");
        }
        Company company = this.companyService.save(ticker);

        return ResponseEntity.ok(company);
    }

    //회사 삭제
    @DeleteMapping
    public ResponseEntity<?> deleteCompany(){
        return null;
    }
}
