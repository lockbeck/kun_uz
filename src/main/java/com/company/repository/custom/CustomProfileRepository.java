package com.company.repository.custom;

import com.company.dto.profile.ProfileFilterDTO;
import com.company.entity.ProfileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public class CustomProfileRepository {
    @Autowired
    private EntityManager entityManager;
    public List<ProfileEntity> filter(ProfileFilterDTO dto, int page,int size) {

        StringBuilder builder = new StringBuilder();
        builder.append(" SELECT p FROM ProfileEntity p ");
        builder.append(" where visible = true ");

        if (dto.getId() != null) {
            builder.append(" and p.id = '" + dto.getId() + "' ");
        }

        if (dto.getName() != null) {
            builder.append(" and p.name like '%" + dto.getName() + "%' ");
        }
        // Select a from ArticleEntity a where title = 'asdasd'; delete from sms-- fdsdfsdfs'
        if (dto.getStatus() != null) {
            builder.append(" and p.status = '" + dto.getStatus() + "' ");
        }

        if (dto.getSurname() != null) {
            builder.append(" and p.surname like '%" + dto.getSurname() + "%' ");
        }

        if (dto.getEmail() != null) {
            builder.append(" and p.email='" + dto.getEmail() + "' ");
        }

        if (dto.getPhone() != null) {
            builder.append(" and p.phone= '" + dto.getPhone() + "' ");
        }

        if (dto.getPhotoId() != null) {
            builder.append(" and p.photo.id= '" + dto.getPhotoId() + "' ");
        }
        if (dto.getRole() != null) {
            builder.append(" and p.role= '" + dto.getRole() + "' ");
        }

        if (dto.getCreatedDateFrom() != null && dto.getCreatedDateTo() == null) {
            // builder.append(" and a.publishDate = '" + dto.getPublishedDateFrom() + "' ");

            LocalDate localDate = LocalDate.parse(LocalDateTime.parse(dto.getCreatedDateFrom()).toString());
            LocalDateTime fromTime = LocalDateTime.of(localDate, LocalTime.MIN); // yyyy-MM-dd 00:00:00
            LocalDateTime toTime = LocalDateTime.of(localDate, LocalTime.MAX); // yyyy-MM-dd 23:59:59
            builder.append(" and p.createdDate between '" + fromTime + "' and '" + toTime + "' ");
        }else if (dto.getCreatedDateFrom() != null && dto.getCreatedDateTo() != null) {
            builder.append(" and p.createdDate between '" + dto.getCreatedDateFrom() + "' and '" + dto.getCreatedDateTo() + "' ");
        }

        Query query = entityManager.createQuery(builder.toString());
        query.setFirstResult((page-1) * size);
        query.setMaxResults(size);
        List<ProfileEntity> profileList = query.getResultList();

        return profileList;
    }

}
