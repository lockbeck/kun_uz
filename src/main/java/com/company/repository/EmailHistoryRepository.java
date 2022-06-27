package com.company.repository;

import com.company.entity.EmailHistoryEntity;
import com.company.entity.SmsEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmailHistoryRepository extends PagingAndSortingRepository<EmailHistoryEntity,Integer> {

    @Query(value = "select count(*) from email where email =:email and created_date > now() - INTERVAL '1 MINUTE' ",
            nativeQuery = true)
    Long getCountByEmail(@Param("email") String email);

    Optional<EmailHistoryEntity> findTopByEmailOrderByCreatedDateDesc(String email);
}
