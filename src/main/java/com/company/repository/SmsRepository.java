package com.company.repository;

import com.company.entity.SmsEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface  SmsRepository extends PagingAndSortingRepository<SmsEntity,Integer> {

    Optional<SmsEntity> findTopByPhoneOrderByCreatedDateDesc(String phone);

    @Query("from SmsEntity where phone =?1 order by createdDate desc")
    List<SmsEntity> findByPhone(String phone, Pageable pageable);

    @Query(value = "select count(*) from sms where phone =:phone and created_date > now() - INTERVAL '1 MINUTE' ",
            nativeQuery = true)
    Long getSmsCount(@Param("phone") String phone);

}
