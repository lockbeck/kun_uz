package com.company.controller;

import com.company.dto.article.*;
import com.company.enums.LangEnum;
import com.company.enums.ProfileRole;
import com.company.service.ArticleService;
import com.company.util.HttpHeaderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    // 1. Moderator creates article
    @PostMapping("/adm/create")
    public ResponseEntity<ArticleDTO> create(@RequestBody @Valid ArticleCreateDTO dto, HttpServletRequest request) {
        log.info("Request for create article {}", dto);
        Integer pId = HttpHeaderUtil.getId(request, ProfileRole.MODERATOR);
        ArticleDTO articleDTO = articleService.create(dto, pId);
        return ResponseEntity.ok(articleDTO);
    }

    // 2. Moderator updates article
    @PutMapping("/adm/{id}")
    public ResponseEntity<String> update(@RequestBody ArticleCreateDTO dto,
                                         @PathVariable("id") String id,
                                         HttpServletRequest request) {
        Integer pId = HttpHeaderUtil.getId(request, ProfileRole.MODERATOR);
        log.info("Request for update article {}", dto);
        articleService.update(id, dto);
        return ResponseEntity.ok("Successful");
    }

    // 3. Moderator deletes article
    @DeleteMapping("/adm/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") String id,
                                         HttpServletRequest request) {
        log.info("Request for delete article by id {}", id);
        HttpHeaderUtil.getId(request, ProfileRole.MODERATOR);
        articleService.delete(id);
        return ResponseEntity.ok("Successful");

    }

    // 4. Publisher changes status of article

    @PutMapping("/adm/changeStatus/{id}")
    public ResponseEntity<String> changeStatus(@PathVariable("id") String id,
                                               HttpServletRequest request) {
        log.info("Request for change status of article by id {}", id);
        Integer decode = HttpHeaderUtil.getId(request, ProfileRole.PUBLISHER);
        articleService.changeStatus(decode, id);
        return ResponseEntity.ok("Successful");
    }

    // 5. Get last 5 by type order by created_date for PUBLIC
    @GetMapping("/get5/{typeKey}")
    public ResponseEntity<?> getLast5ByType(@PathVariable("typeKey") String typeKey) {
        log.info("Request for get last 5 by type key {}", typeKey);
        List<ArticleDTO> all = articleService.getLast5ArticleByType(typeKey);
        return ResponseEntity.ok().body(all);
    }

    // 6. Get last 3 by type order by created_date for PUBLIC
    @GetMapping("/get3/{typeKey}")
    public ResponseEntity<?> getLast3ByType(@PathVariable("typeKey") String typeKey) {
        log.info("Request for get last 3 by type key {}", typeKey);
        List<ArticleDTO> all = articleService.getLast3ArticleByType(typeKey);
        return ResponseEntity.ok().body(all);
    }

    // 7. Get last 8 not include given id
    @PostMapping("/last8")
    public ResponseEntity<List<ArticleDTO>> getLast8NotIn(@RequestBody ArticleRequestDTO dto) {
        log.info("Request for get last 8 not in given list id {}", dto);
        List<ArticleDTO> response = articleService.getLast8NotInList(dto.getIdList());
        return ResponseEntity.ok().body(response);
    }

    // 8 Get Article By id FULL INFO for PUBLIC
    @GetMapping("/{id}")
    public ResponseEntity<ArticleFullInfoDTO> show(@PathVariable("id") String id,
                                                   @RequestHeader(value = "Accept-language", defaultValue = "uz") LangEnum language) {
        log.info("Request for article full info by id {}", id);
        ArticleFullInfoDTO dto = articleService.getById(id, language);
        return ResponseEntity.ok(dto);

    }

    //9. get last 4 by type not in given List
    @PostMapping("/last4/{typeKey}")
    public ResponseEntity<List<ArticleDTO>> getLast4ByTypeNotIn(@RequestBody ArticleRequestDTO dto,
                                                                @PathVariable String typeKey) {
        log.info("Request for get last 4 by type key {} not in given list id  {}", typeKey, dto);
        List<ArticleDTO> response = articleService.getLast4ByTypeNotInList(typeKey, dto.getIdList());
        return ResponseEntity.ok(response);

    }

    // 10. Get 4 most read articles   ArticleShortInfo
    @GetMapping("/mostread4/{id}")
    public ResponseEntity<List<ArticleDTO>> mostRead4(@PathVariable("id") String id) {
        log.info("Request for 4 most read not given id {}", id);
        List<ArticleDTO> dtoList = articleService.mostRead4(id);
        return ResponseEntity.ok(dtoList);

    }

    // 11. get last 4 by tagName not in given articleId
    @PostMapping("/last4/{tagName}")
    public ResponseEntity<List<ArticleDTO>> getLast4ByTagNameNotId(@RequestBody ArticleDTO dto,
                                                                   @PathVariable("tagName") String typeKey) {
        log.info("Request for get last 4 by tag name {} not id {}", typeKey, dto.getId());
        List<ArticleDTO> response = articleService.getLast4ByTagNameNotId(typeKey, dto.getId());
        return ResponseEntity.ok(response);

    }

    // 12. get last 4 by types and region not in given articleId
    @PostMapping("/last5/by_region_and_type/{id}")
    public ResponseEntity<List<ArticleDTO>> getLast5ByTypeAndRegionKeyNotId(@RequestBody ArticleRequestDTO dto,
                                                                            @PathVariable("id") String id) {
        log.info("Request for get last 5 by type key {} and region key {} not id {}", dto.getTypeKey(), dto.getRegionKey(), id);
        List<ArticleDTO> response = articleService.getLast5ByTypeAndRegionNotId(id, dto.getRegionKey(), dto.getTypeKey());
        return ResponseEntity.ok(response);

    }

    // 13. Get Article list by Region Key (Pagination)  ArticleShortInfo
    @GetMapping("/listByRegion/{regionKey}")
    public ResponseEntity<List<ArticleDTO>> getArticleListByRegionKeyPagination(@PathVariable("regionKey") String regionKey,
                                                                                @RequestParam(value = "page", defaultValue = "1") int page,
                                                                                @RequestParam(value = "size", defaultValue = "5") int size) {
        log.info("Request to get article list pagination by region key {}", regionKey);
        List<ArticleDTO> response = articleService.getArticleListPaginationByRegionKey(regionKey, page, size);
        return ResponseEntity.ok(response);

    }

    // 14. Get last 5 by type order by created_date for PUBLIC
    @GetMapping("/getByCategory/{categoryKey}")
    public ResponseEntity<?> getLast5ByCategory(@PathVariable("categoryKey") String categoryKey) {
        log.info("Request for get last 5 by category key {}", categoryKey);
        List<ArticleDTO> all = articleService.getLast5ArticleByCategory3(categoryKey);
        return ResponseEntity.ok().body(all);
    }

    // 15. Get Article By Category Key (Pagination) ArticleShortInfo
    @GetMapping("/listByCategory/{categoryKey}")
    public ResponseEntity<List<ArticleDTO>> getArticleListByCategoryKeyPagination(@PathVariable("categoryKey") String categoryKey,
                                                                                  @RequestParam(value = "page", defaultValue = "1") int page,
                                                                                  @RequestParam(value = "size", defaultValue = "5") int size) {
        log.info("Request to get article list pagination by category key {}", categoryKey);
        List<ArticleDTO> response = articleService.getLast5ArticleByCategory3(categoryKey);
        return ResponseEntity.ok(response);

    }









    @GetMapping("/adm/listArticle")
    public ResponseEntity<?> getAll(HttpServletRequest request) {
        log.info("Request for get all for ADMIN");
        HttpHeaderUtil.getId(request, ProfileRole.ADMIN);
        List<ArticleDTO> all = articleService.getAll();
        return ResponseEntity.ok().body(all);
    }

    @GetMapping("/get1/{categoryKey}")
    public ResponseEntity<?> getLast5ByCategoryKey(@PathVariable("categoryKey") String categoryKey) {

        List<ArticleDTO> all = articleService.getLast5ArticleByCategoryKey(categoryKey);
        return ResponseEntity.ok().body(all);
    }

    @GetMapping("/get2/{categoryKey}")
    public ResponseEntity<?> getLast5ByCategoryKey2(@PathVariable("categoryKey") String categoryKey) {

        List<ArticleDTO> all = articleService.getLast5ArticleByCategoryKey2(categoryKey);
        return ResponseEntity.ok().body(all);
    }

    @GetMapping("/get3/{categoryKey}")
    public ResponseEntity<?> getLast5ByCategoryKey3(@PathVariable("categoryKey") String categoryKey) {

        List<ArticleDTO> all = articleService.getLast5ArticleByCategory3(categoryKey);
        return ResponseEntity.ok().body(all);
    }


//    @GetMapping("/adm/listArticle")
//    public ResponseEntity<?> getLastFiveArticlesByTypes(HttpServletRequest request){
//        HttpHeaderUtil.getId(request ,ProfileRole.MODERATOR);
//        List<ArticleDTO> all = articleService.getAll();
//        return ResponseEntity.ok().body(all);
//    }

    @GetMapping("/pagination")
    public ResponseEntity<PageImpl> getPagination(@RequestParam(value = "page", defaultValue = "1") int page,
                                                  @RequestParam(value = "size", defaultValue = "5") int size) {
        PageImpl response = articleService.pagination(page, size);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/filter")
    public ResponseEntity<List<ArticleDTO>> filter(@RequestParam(value = "page", defaultValue = "1") int page,
                                                   @RequestParam(value = "size", defaultValue = "5") int size,
                                                   @RequestBody ArticleFilterDTO dto) {
        List<ArticleDTO> response = articleService.filter(dto, page, size);
        return ResponseEntity.ok().body(response);
    }


}
