package project.pictaround.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.pictaround.dto.request.CreateReviewRequestDto;
import project.pictaround.dto.request.FindLocationsRequestDto;
import project.pictaround.dto.request.PageRequestDto;
import project.pictaround.dto.response.*;
import project.pictaround.service.LocationService;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class LocationController {

    LocationService locationService;


    @PostMapping("/review")
    public ResponseEntity<SuccessResponse> createReview(
            @RequestBody CreateReviewRequestDto reviewRequestDto,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        locationService.createReview(reviewRequestDto, request, response);

        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse(true));
    }

    @GetMapping("/keyword")
    public ResponseEntity<List<KeywordResponseDto>> findSearchKeyword(@RequestParam String query) {
        List<KeywordResponseDto> searchKeywords = locationService.findSearchKeyword(query);

        return ResponseEntity.status(HttpStatus.OK).body(searchKeywords);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<LocationListDto> findLocations(@ModelAttribute FindLocationsRequestDto requestDto) {
        LocationListDto response = locationService.findLocations(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/search/{locationId}")
    public ResponseEntity<SingleLocationDto> findLocation(@PathVariable Long locationId) {
        SingleLocationDto location = locationService.findLocation(locationId);
        return ResponseEntity.status(HttpStatus.OK).body(location);
    }

    @GetMapping(value = "/reviews/{locationId}")
    public ResponseEntity<ReviewListDto> findReviews(@PathVariable Long locationId, @ModelAttribute PageRequestDto pageDto) {
        ReviewListDto reviews = locationService.findReviews(locationId, pageDto);
        return ResponseEntity.status(HttpStatus.OK).body(reviews);
    }
}
