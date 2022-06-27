package com.company.service;

import com.company.dto.profile.ProfileDTO;
import com.company.dto.profile.ProfileFilterDTO;
import com.company.entity.AttachEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.ProfileStatus;
import com.company.exp.BadRequestException;
import com.company.exp.ItemNotFoundException;
import com.company.repository.ProfileRepository;
import com.company.repository.custom.CustomProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private AttachService attachService;
    @Autowired
    private CustomProfileRepository customProfileRepository;
    // admin
    public ProfileDTO create(ProfileDTO dto) {
        // name; surname; email; password;
        isValid(dto);

        Optional<ProfileEntity> optional = profileRepository.findByEmail(dto.getEmail());
        if (optional.isPresent()) {
            throw new BadRequestException("User already exists");
        }

        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        entity.setPhone(dto.getPhone());
        entity.setRole(dto.getRole());
        entity.setStatus(ProfileStatus.ACTIVE);

        profileRepository.save(entity);
        dto.setPassword(null);
        dto.setId(entity.getId());
        return dto;
    }

    public void update(Integer profileId, ProfileDTO dto) {
        ProfileEntity entity = get(profileId);

        isValid(dto);

        if (entity.getPhoto() == null && dto.getPhotoId() != null) {
            entity.setPhoto(new AttachEntity(dto.getPhotoId()));
        } else if (entity.getPhoto() != null && dto.getPhotoId() == null) {
            attachService.delete(entity.getPhoto().getId());
            entity.setPhoto(null);
        } else if (entity.getPhoto() != null && dto.getPhotoId() != null &&
                !entity.getPhoto().getId().equals(dto.getPhotoId())) {
            attachService.delete(entity.getPhoto().getId());
            entity.setPhoto(new AttachEntity(dto.getPhotoId()));
        }

        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setPassword(dto.getPassword());

        profileRepository.save(entity);
    }

    public List<ProfileDTO> getAll() {
        Iterable<ProfileEntity> entities = profileRepository.findAll();
        List<ProfileDTO> dtoList = new LinkedList<>();
        entities.forEach(profileEntity -> {
            ProfileDTO dto = new ProfileDTO();
            dto.setId(profileEntity.getId());
            dto.setName(profileEntity.getName());
            dto.setSurname(profileEntity.getSurname());
            dto.setEmail(profileEntity.getEmail());
            dto.setPhone(profileEntity.getPhone());
            dto.setStatus(profileEntity.getStatus());
            dto.setRole(profileEntity.getRole());
            dto.setCreatedDate(profileEntity.getCreatedDate());
            if(profileEntity.getPhoto()!=null){
                dto.setPhotoId(profileEntity.getPhoto().getId());
            }
            dtoList.add(dto);
        });
        return dtoList;

    }

    public void delete(Integer id) {
        ProfileEntity entity = get(id);
        entity.setVisible(Boolean.FALSE);
        profileRepository.save(entity);

    }


    public String setPhoto(Integer userId, String photoId) {
        ProfileEntity entity = get(userId);
        attachService.get(photoId);

        if (entity.getPhoto() == null) {
            entity.setPhoto(new AttachEntity(photoId));
        } else {
            attachService.delete(entity.getPhoto().getId());
            entity.setPhoto(new AttachEntity(photoId));
        }
        profileRepository.save(entity);

        return "Photo set";
    }

    public ProfileEntity get(Integer id) {
        return profileRepository.findById(id).orElseThrow(() -> {
                    throw new ItemNotFoundException("Profile Not found");
                }
        );
    }
    public void isValid(ProfileDTO dto) {
        if (dto.getName().isEmpty()) {
            throw new BadRequestException("Name is required");
        }
        if (dto.getSurname().isEmpty()) {
            throw new BadRequestException("Surname is required");
        }
        if (dto.getEmail().isEmpty()) {
            throw new BadRequestException("Email is required");
        }
        if (dto.getPassword().isEmpty()) {
            throw new BadRequestException("Password is required");
        }
    }

    public List<ProfileDTO> filter(ProfileFilterDTO dto, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.ASC, "createdDate");
        Pageable pageable = PageRequest.of(page, size, sort);

        List<ProfileEntity> filter = customProfileRepository.filter(dto,page,size);
        List<ProfileDTO> dtoList= new LinkedList<>();

        filter.forEach(profileEntity -> {
            ProfileDTO profileDTO = new ProfileDTO();
            profileDTO.setName(profileEntity.getName());
            profileDTO.setSurname(profileEntity.getSurname());
            profileDTO.setEmail(profileEntity.getEmail());
            profileDTO.setCreatedDate(profileEntity.getCreatedDate());
            profileDTO.setRole(profileEntity.getRole());
            profileDTO.setStatus(profileEntity.getStatus());
            profileDTO.setId(profileEntity.getId());
            if(profileEntity.getPhoto()!=null){
                profileDTO.setPhotoId(profileEntity.getPhoto().getId());
            }

            dtoList.add(profileDTO);
        });
        return dtoList;
    }
}
