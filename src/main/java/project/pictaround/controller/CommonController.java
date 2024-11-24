package project.pictaround.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.pictaround.dto.request.CategoryRequestDto;
import project.pictaround.dto.response.CategoryResponseDto;
import project.pictaround.dto.response.SuccessResponse;
import project.pictaround.service.CommonService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    private final CommonService commonService;

    public CommonController(CommonService commonService) {
        this.commonService = commonService;
    }

    @PostMapping("/categories")
    public ResponseEntity<SuccessResponse> saveCategory(@RequestBody CategoryRequestDto requestDto) {
        commonService.saveCategory(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse(true));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponseDto>> findCategories() {
        log.info("Find categories");
        return ResponseEntity.ok(commonService.findCategories());
    }
}
