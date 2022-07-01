package com.company.service;

import com.company.dto.*;
import com.company.dto.profile.ProfileDTO;
import com.company.entity.EmailHistoryEntity;
import com.company.entity.ProfileEntity;
import com.company.entity.SmsEntity;
import com.company.enums.ProfileRole;
import com.company.enums.ProfileStatus;
import com.company.exp.BadRequestException;
import com.company.exp.ItemNotFoundException;
import com.company.repository.EmailHistoryRepository;
import com.company.repository.ProfileRepository;
import com.company.repository.SmsRepository;
import com.company.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class AuthService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private SmsService smsService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private SmsRepository smsRepository;
    @Autowired
    private EmailHistoryRepository emailHistoryRepository;


    public ProfileDTO login(AuthDTO dto) {
        Optional<ProfileEntity> optional = profileRepository.findByEmail(dto.getEmail());
        if (optional.isEmpty()) {
            log.warn("User not found by email (" + dto.getEmail() + ") when logged in. Date:");
            throw new BadRequestException("User not found by email (" + dto.getEmail() + ")");
        }
        ProfileEntity profileEntity = optional.get();
        if (!profileEntity.getPassword().equals(dto.getPassword())) {
            log.warn("User entered incorrect password (" + dto.getPassword() + ") when logged in.");
            throw new BadRequestException("User password incorrect (" + dto.getPassword() + ") ");
        }

        if (!profileEntity.getStatus().equals(ProfileStatus.ACTIVE)) {
            log.warn(profileEntity.getName() + " is not active user.");
            throw new BadRequestException("Not active user");
        }

        ProfileDTO profileDto = new ProfileDTO();
        profileDto.setName(profileEntity.getName());
        profileDto.setSurname(profileEntity.getSurname());
        profileDto.setJwt(JwtUtil.encode(profileEntity.getId(), profileEntity.getRole()));

        return profileDto;
    }

    public String registration(RegistrationDTO dto) {
        Optional<ProfileEntity> optional = profileRepository.findByEmail(dto.getEmail());
        if (optional.isPresent()) {
            log.warn("User already exists by email (" + dto.getEmail() + ") or phone(" + dto.getPhone() + ") ");
            throw new BadRequestException("User already exists");
        }

        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setPassword(dto.getPassword());
        entity.setStatus(ProfileStatus.NOT_ACTIVE);
        entity.setRole(ProfileRole.USER);
        profileRepository.save(entity);

//        smsService.sendRegistrationSms(dto.getPhone());
        emailService.sendRegistrationEmail(entity.getEmail(), entity.getId());

        return "Activation code was sent to " + dto.getEmail();
    }

    public String verification(VerificationDTO dto) {
        Optional<SmsEntity> optional = smsRepository.findTopByPhoneOrderByCreatedDateDesc(dto.getPhone());
        if (optional.isEmpty()) {
            log.warn("Phone(" + dto.getPhone() + ") not found in verification");
            return "Phone Not Found";
        }
        SmsEntity sms = optional.get();
        LocalDateTime validDate = sms.getCreatedDate().plusMinutes(1);

        if (!sms.getCode().equals(dto.getCode())) {
            log.warn("Phone (" + dto.getPhone() + ") did not match with code (" + dto.getCode() + ") in verification");
            return "Code Invalid";
        }
        if (validDate.isBefore(LocalDateTime.now())) {
            log.warn("Time is out with phone(" + dto.getPhone() + ")");
            return "Time is out";
        }
        profileRepository.updateStatusByPhone(dto.getPhone(), ProfileStatus.ACTIVE);
        return "Verification Done";
    }

    public ResponseInfoDTO resendSms(String phone) {
        Long count = smsService.smsCount(phone);
        if (count >= 4) {
            log.warn("Too many attempts with phone("+phone+")");
            return new ResponseInfoDTO(-1, "Too many attempts. Try later");
        }

        smsService.sendRegistrationSms(phone);
        return new ResponseInfoDTO(1);
    }

    public String emailVerification(Integer id) {
        Optional<ProfileEntity> profile = profileRepository.findById(id);
        if (profile.isEmpty()) {
            log.warn("User not found by token");
            return "<h1>User Not Found</h1>";
        }

        ProfileEntity profileEntity = profile.get();
        Optional<EmailHistoryEntity> emailHistory = emailHistoryRepository.findTopByEmailOrderByCreatedDateDesc(profileEntity.getEmail());
        if (emailHistory.isEmpty()) {
            return "Email Not Found";
        }

        EmailHistoryEntity email = emailHistory.get();
        LocalDateTime validDate = email.getCreatedDate().plusMinutes(1);

        if (validDate.isBefore(LocalDateTime.now())) {
            return "<h1 style='align-text:center'>Time is out.</h1>";
        }

        profileEntity.setStatus(ProfileStatus.ACTIVE);
        profileRepository.save(profileEntity);
        return "<h1 style='align-text:center'>Success. Tabriklaymiz.</h1>";
    }

    public ResponseInfoDTO resendEmail(String email) {


        Optional<ProfileEntity> byEmail = profileRepository.findByEmail(email);
        if (byEmail.isEmpty()) {
            throw new ItemNotFoundException("User not found");
        }

        ProfileEntity entity = byEmail.get();
        if (entity.getStatus().equals(ProfileStatus.ACTIVE)) {
            throw new BadRequestException("User has already verified by email");
        }
        Long count = emailService.getCountByEmail(email);
        if (count >= 3) {
            throw new BadRequestException("Too many attempts. Try later");
        }
        emailService.sendRegistrationEmail(email, entity.getId());
        return new ResponseInfoDTO(1, "Link was sent to " + email);
    }

}
