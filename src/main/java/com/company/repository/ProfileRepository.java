package com.company.repository;

import com.company.entity.ProfileEntity;
import com.company.enums.ProfileStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.Optional;

public interface ProfileRepository extends CrudRepository<ProfileEntity,Integer> {

    Optional<ProfileEntity> findByEmail(String email);

    @Modifying
    @Transactional
    @Query("update ProfileEntity as p set p.status =?2 where p.phone =?1")
    void updateStatusByPhone(String phone, ProfileStatus active);

}
