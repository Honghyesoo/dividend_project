package zero.base.dividends.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import zero.base.dividends.dto.CompanyDto;
import zero.base.dividends.persist.entity.CompanyEntity;
import zero.base.dividends.service.CompanyService;

@RestController
@RequestMapping("/company")
@AllArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    //배당금 검색 자동완성
    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam(name = "keyword") String keyword){
        var result = this.companyService.getCompanyNamesByKeyword(keyword);
        return ResponseEntity.ok(result);
    }

    //회사 리스트 조회
    @GetMapping
    public ResponseEntity<?> searchCompany(final Pageable pageable){
        Page<CompanyEntity> companies = this.companyService.getAllCompany(pageable);
        return ResponseEntity.ok(companies);
    }

    // 회사 저장
    @PostMapping
    public ResponseEntity<?> addCompany(@RequestBody CompanyDto request){
        String ticker = request.getTicker().trim();
        if (ObjectUtils.isEmpty(ticker)){
            throw  new RuntimeException("ticker is empty");
        }
        CompanyDto companyDto = this.companyService.save(ticker);
        // 자동 저장
        this.companyService.addAutoCompleteKeyword(companyDto.getName());

        return ResponseEntity.ok(companyDto);
    }

    //회사 삭제
    @DeleteMapping
    public ResponseEntity<?> deleteCompany(){
        return null;
    }
}
