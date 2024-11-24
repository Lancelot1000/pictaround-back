package project.pictaround.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.pictaround.dto.response.MyFavoriteDto;
import project.pictaround.dto.response.SuccessResponse;
import project.pictaround.service.FavoriteService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;


    @PostMapping("/favorite/{id}")
    public ResponseEntity<SuccessResponse> setFavorite(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
        favoriteService.setFavorite(id, request, response);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse(true));
    }

    @GetMapping("/favorite/{id}")
    public ResponseEntity<Integer> getFavoriteCount(@PathVariable Long id) {
        Integer favoriteCount = favoriteService.getFavoriteCount(id);
        return ResponseEntity.status(HttpStatus.OK).body(favoriteCount);
    }

    @GetMapping("/favorite")
    public ResponseEntity<MyFavoriteDto> findFavorite(HttpServletRequest request, HttpServletResponse response) {
        MyFavoriteDto favorite = favoriteService.findFavorite(request, response);
        return ResponseEntity.status(HttpStatus.OK).body(favorite);
    }
}
