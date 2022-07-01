package com.company.service;

import com.company.dto.*;
import com.company.dto.article.ArticleCreateDTO;
import com.company.dto.article.ArticleDTO;
import com.company.dto.article.ArticleFilterDTO;
import com.company.dto.article.ArticleFullInfoDTO;
import com.company.entity.*;
import com.company.enums.ArticleStatus;
import com.company.enums.LangEnum;
import com.company.exp.BadRequestException;
import com.company.exp.ItemNotFoundException;
import com.company.mapper.ArticleShortInfoByCategory;
import com.company.repository.ArticleRepository;
import com.company.repository.custom.CustomArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {
    @Value("${server.url}")
    private String serverUrl;

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private TagService tagService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private RegionService regionService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ArticleTypeService articleTypeService;
    @Autowired
    private ArticleTagService articleTagService;
    @Autowired
    private AttachService attachService;
    @Autowired
    private ArticleLikeService articleLikeService;
    @Autowired
    private CustomArticleRepository customArticleRepository;

    public ArticleDTO create(ArticleCreateDTO dto, Integer profileId){
        ArticleEntity entity = new ArticleEntity();
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setDescription(dto.getDescription());

        entity.setAttach(attachService.get(dto.getImageId()));
        entity.setRegion(regionService.get(dto.getRegionId()));
        entity.setCategory(categoryService.get(dto.getCategoryId()));

        ProfileEntity moderator = new ProfileEntity();
        moderator.setId(profileId);
        entity.setModerator(moderator);

        entity.setAttach(new AttachEntity(dto.getImageId()));

        entity.setStatus(ArticleStatus.NOT_PUBLISHED);

        articleRepository.save(entity);

        articleTagService.create(entity,dto.getTagList());
        articleTypeService.create(entity,dto.getTypesList());

        return toDto(entity);


    }

    public void update(String id , ArticleCreateDTO dto){
        ArticleEntity entity = get(id);

        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setContent(dto.getContent());

        RegionEntity region = regionService.get(dto.getRegionId());
        CategoryEntity category = categoryService.get(dto.getCategoryId());
        entity.setRegion(region);
        entity.setCategory(category);


        entity.setAttach(new AttachEntity(dto.getImageId()));
        entity.setStatus(ArticleStatus.NOT_PUBLISHED);
        if (entity.getAttach() == null && dto.getImageId() != null) {
            entity.setAttach(new AttachEntity(dto.getImageId()));
        } else if (entity.getAttach() != null && dto.getImageId()== null) {
            articleRepository.updatePhotoById(id,dto.getImageId());
            attachService.delete(entity.getAttach().getId());
            entity.setAttach(null);
        } else if (entity.getAttach() != null && dto.getImageId() != null &&
                !entity.getAttach().getId().equals(dto.getImageId())) {
            articleRepository.updatePhotoById(id,dto.getImageId());
            attachService.delete(entity.getAttach().getId());
            entity.setAttach(new AttachEntity(dto.getImageId()));
        }

        articleRepository.save(entity);
        articleTagService.delete(id);
        articleTagService.create(entity,dto.getTagList());
        articleTypeService.delete(id);
        articleTypeService.create(entity,dto.getTypesList());

    }

    public  void delete(String id){
        ArticleEntity entity = get(id);
        entity.setVisible(Boolean.FALSE);
        articleRepository.save(entity);

    }

    public  void changeStatus(Integer profileId , String id){
        profileService.get(profileId);
        ArticleEntity entity = get(id);
        if(entity.getStatus().equals(ArticleStatus.NOT_PUBLISHED)){

//            entity.setStatus(ArticleStatus.PUBLISHED);
//            entity.setPublisher(profile.get());
//            entity.setPublishedDate(LocalDateTime.now());
            articleRepository.changeStatusToPublish(id,profileId, ArticleStatus.PUBLISHED,LocalDateTime.now());
        }
        else {
//            entity.setStatus(ArticleStatus.NOT_PUBLISHED);
            articleRepository.changeStatusNotPublish(id,ArticleStatus.NOT_PUBLISHED);

        }
//        articleRepository.save(entity);

    }

    public List<ArticleDTO> getLast5ArticleByType(String typeKey) {
        Pageable pageable = PageRequest.of(0, 5);
        Page<ArticleEntity> articlePage = articleRepository.findLast5ByType(
                typeKey, pageable);

        List<ArticleDTO> dtoList = new LinkedList<>();
        articlePage.getContent().forEach(article -> {
            dtoList.add(shortInfo(article));
        });

        return dtoList;
    }

    public List<ArticleDTO> getLast3ArticleByType(String typeKey) {
        Pageable pageable = PageRequest.of(0, 3);
        Page<ArticleEntity> articlePage = articleRepository.findLast3ByType(
                typeKey, pageable);

        List<ArticleDTO> dtoList = new LinkedList<>();
        articlePage.getContent().forEach(article -> {
            dtoList.add(shortInfo(article));
        });

        return dtoList;
    }

    public List<ArticleDTO> getLast8NotInList(List<String> idList) {

        Pageable pageable = PageRequest.of(0, 8);
        Page<ArticleEntity> page = articleRepository.findLast8NotIn(idList, pageable);
        List<ArticleDTO> dtoList = new LinkedList<>();

        page.getContent().forEach(entity -> {
            dtoList.add(shortInfo(entity));
        });
        return dtoList;
    }

    public ArticleFullInfoDTO getById(String id ,LangEnum language){
        ArticleEntity entity = get(id);
        if (entity.getVisible().equals(Boolean.FALSE)) {
            throw new BadRequestException("Article not visible");
        }
        if (entity.getStatus().equals(ArticleStatus.NOT_PUBLISHED)) {
            throw new BadRequestException("Article is not accessible");
        }
        articleRepository.increaseViewCount(entity.getId());

       return fullInfo(entity, language);
    }

    public List<ArticleDTO> getLast4ByTypeNotInList(String typeKey , List<String> idList){
        Pageable pageable = PageRequest.of(0, 4);

        Page<ArticleEntity> articlePage = articleRepository.findLast4NotInListByType(idList, typeKey, pageable);
        List<ArticleDTO> dtoList = new LinkedList<>();
        articlePage.getContent().forEach(article -> {
            dtoList.add(shortInfo(article));
        });

        return dtoList;
    }

    public List<ArticleDTO> mostRead4(String id) {
        Pageable pageable = PageRequest.of(0, 4);
        Page<ArticleEntity> articlePage = articleRepository.mostRead4(id ,pageable);

        List<ArticleDTO> dtoList = new LinkedList<>();
        articlePage.getContent().forEach(article -> {
            dtoList.add(shortInfo(article));
        });

        return dtoList;
    }

    public List<ArticleDTO> getLast4ByTagNameNotId(String tagName, String id) {
        Pageable pageable = PageRequest.of(0, 4);

        Page<ArticleEntity> articlePage = articleRepository.getLast4ByTagNameNotId(id, tagName, pageable);
        List<ArticleDTO> dtoList = new LinkedList<>();
        articlePage.getContent().forEach(article -> {
            dtoList.add(shortInfo(article));
        });

        return dtoList;
    }

    private ArticleFullInfoDTO fullInfo(ArticleEntity entity, LangEnum langEnum) {
        ArticleFullInfoDTO articleDTO = new ArticleFullInfoDTO();

        articleDTO.setId(entity.getId());
        articleDTO.setTitle(entity.getTitle());
        articleDTO.setDescription(entity.getDescription());
        articleDTO.setContent(entity.getContent());

        RegionEntity region = regionService.get(entity.getRegion().getId());
        RegionDTO regionDTO = regionService.getArticleKeyAndName(region, langEnum);
        articleDTO.setRegion(regionDTO);

        CategoryEntity category = categoryService.get(entity.getCategory().getId());
        CategoryDTO categoryDTO = categoryService.getArticleKeyAndName(category, langEnum);
        articleDTO.setCategory(categoryDTO);

        articleDTO.setPublishedDate(entity.getPublishedDate());

        articleDTO.setViewCount(entity.getViewCount());
        articleDTO.setSharedCount(entity.getSharedCount());

        //Integer numberLike = articleLikeService.getNumberOfLikeByArticleId(entity.getId());
        //articleDTO.setLikeCount(numberLike);

        List<String> stringList = tagService.getTagNameByArticleId(entity.getId());
        articleDTO.setTagList(stringList);
        return articleDTO;
    }
    public ArticleEntity get(String id){
        Optional<ArticleEntity> byId = articleRepository.findById(id);
        if(byId.isEmpty()){
            throw new ItemNotFoundException("Article not found");
        }
        return byId.get();

    }

    public List<ArticleDTO> getAll() {
        Iterable<ArticleEntity> entities = articleRepository.findAll();
        List<ArticleDTO> dtoList = new LinkedList<>();
        entities.forEach(articleEntity -> {
            ArticleDTO dto = new ArticleDTO();
             toDto(dto,articleEntity);
            dtoList.add(dto);
        });
        return dtoList;

    }

    public PageImpl pagination(int page, int size) {
        // page = 1
    /*    Iterable<TypesEntity> all = typesRepository.pagination(size, size * (page - 1));
        long totalAmount = typesRepository.countAllBy();*/
        Sort sort = Sort.by(Sort.Direction.ASC, "createdDate");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ArticleEntity> all = articleRepository.findAll(pageable);

//        long totalAmount = all.getTotalElements();
//        int totalPages = all.getTotalPages();
        List<ArticleEntity> list = all.getContent();

        List<ArticleDTO> dtoList = new LinkedList<>();

        list.forEach(article -> {
            if(article.getStatus().equals(ArticleStatus.PUBLISHED)) {
                ArticleDTO dto = new ArticleDTO();
                dto.setContent(article.getContent());
                dto.setTitle(article.getTitle());
                dto.setDescription(article.getDescription());
                dto.setSharedCount(article.getSharedCount());
                dto.setViewCount(article.getViewCount());
                dtoList.add(dto);
            }
        });

//        TypesPaginationDTO paginationDTO = new TypesPaginationDTO(totalAmount, dtoList);
//        return paginationDTO;
        return new PageImpl(dtoList,pageable, all.getTotalElements());
    }

    public List<ArticleDTO> getLast5ArticleByCategoryKey(String key){
        CategoryEntity categoryEntity = categoryService.get(key);
        List<ArticleDTO> dtoList = new LinkedList<>();
        List<ArticleEntity> articleEntityList = articleRepository.findTopByCategoryAndStatusAndVisibleTrueOrderByCreatedDateDesc(categoryEntity, ArticleStatus.PUBLISHED);
        articleEntityList.forEach(entity -> {
            ArticleDTO articleDTO = shortInfo(entity);
            dtoList.add(articleDTO);
        });
        return dtoList;
    }

    public List<ArticleDTO> getLast5ArticleByCategoryKey2(String categoryKey) {
        Pageable pageable = PageRequest.of(0, 5);
        Page<ArticleEntity> articlePage = articleRepository.findLast5ByCategory(
                categoryKey, ArticleStatus.PUBLISHED, pageable);
        int n =  articlePage.getTotalPages();

        List<ArticleDTO> dtoList = new LinkedList<>();
        articlePage.getContent().forEach(article -> {
            dtoList.add(shortInfo(article));
        });
        return dtoList;
    }

    public List<ArticleDTO> getLast5ArticleByCategory3(String categoryKey) {
        List<ArticleShortInfoByCategory> articleList = articleRepository.findTop5ByArticleByCategory3(categoryKey);

        List<ArticleDTO> dtoList = new LinkedList<>();
        articleList.forEach(article -> {
            dtoList.add(shortInfo(article));
        });
        return dtoList;
    }

    public ArticleDTO shortInfo(ArticleShortInfoByCategory entity){
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(entity.getId());
        articleDTO.setTitle(entity.getTitle());
        articleDTO.setDescription(entity.getDescription());
        articleDTO.setPublishedDate(entity.getPublishedDate());
        return articleDTO;
    }

    public ArticleDTO shortInfo(ArticleEntity entity){
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(entity.getId());
        articleDTO.setTitle(entity.getTitle());
        articleDTO.setDescription(entity.getDescription());
        articleDTO.setPublishedDate(entity.getPublishedDate());
        return articleDTO;
    }

    public ArticleDTO toDto(ArticleEntity entity){
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setTitle(entity.getTitle());
        articleDTO.setDescription(entity.getDescription());
        articleDTO.setContent(entity.getContent());

        articleDTO.setCreatedDate(entity.getCreatedDate());
        articleDTO.setPublishedDate(entity.getPublishedDate());
        articleDTO.setViewCount(entity.getViewCount());
        articleDTO.setSharedCount(entity.getSharedCount());

        AttachDTO attachDTO = new AttachDTO();
        attachDTO.setId(entity.getAttach().getId());
        attachDTO.setUrl(serverUrl+"attach/open/"+entity.getAttach().getId());
        articleDTO.setAttach(attachDTO);
        return articleDTO;
    }
    public void toDto(ArticleDTO dto , ArticleEntity entity){
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setContent(entity.getContent());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setPublishedDate(entity.getPublishedDate());
        dto.setViewCount(entity.getViewCount());
        dto.setSharedCount(entity.getSharedCount());
    }

    public ArticleEntity toEntity(ArticleDTO dto){
        ArticleEntity entity = new ArticleEntity();
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setContent(dto.getContent());
        entity.setCreatedDate(dto.getCreatedDate());
        entity.setPublishedDate(dto.getPublishedDate());
        entity.setViewCount(dto.getViewCount());
        entity.setSharedCount(dto.getSharedCount());
        return entity;
    }

    public List<ArticleDTO> getLast5ByTypeAndRegionNotId(String id, String regionKey, String typeKey) {
        Pageable pageable = PageRequest.of(0, 5);

        Page<ArticleEntity> articlePage = articleRepository.getLast5ByTypeAndRegionNotId(typeKey, regionKey, pageable);
        List<ArticleDTO> dtoList = new LinkedList<>();
        articlePage.getContent().forEach(article -> {
            dtoList.add(shortInfo(article));
        });

        return dtoList;
    }

    public List<ArticleDTO> filter(ArticleFilterDTO dto, int page, int size){
//        Sort sort = Sort.by(Sort.Direction.ASC, "createdDate");
//        Pageable pageable = PageRequest.of(page, size, sort);
        List<ArticleEntity> filter = customArticleRepository.filter(dto,page,size);

        List<ArticleDTO> dtoList= new LinkedList<>();

        filter.forEach(article -> {
            ArticleDTO articleDTO = new ArticleDTO();
            articleDTO.setTitle(article.getTitle());
            articleDTO.setContent(article.getContent());
            articleDTO.setDescription(article.getDescription());
            articleDTO.setCreatedDate(article.getCreatedDate());
            articleDTO.setId(article.getId());
            if(article.getAttach()!=null){
                articleDTO.setAttach(new AttachDTO(article.getAttach().getId()));
            }

            dtoList.add(articleDTO);
        });
        return dtoList;
    }
}

