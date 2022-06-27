package com.company.service;

import com.company.dto.article.ArticleDTO;
import com.company.dto.integration.SmsDTO;
import com.company.dto.integration.SmsRequestDTO;
import com.company.dto.integration.SmsResponseDTO;
import com.company.entity.ArticleEntity;
import com.company.entity.SmsEntity;
import com.company.enums.ArticleStatus;
import com.company.exp.BadRequestException;
import com.company.repository.SmsRepository;
import com.company.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class SmsService {
    @Autowired
    private SmsRepository smsRepository;


    @Value("${sms.key}")
    private String key;

    @Value("${sms.url}")
    private String smsUrl;

    public void sendRegistrationSms(String phone) {
//        Pageable pageable = PageRequest.of(0,4);
//        List<SmsEntity> smsEntityList = smsRepository.findByPhone(phone,pageable);
//
//        if(!smsEntityList.isEmpty()&&smsEntityList.size()>3){
//
//            if (smsEntityList.get(3).getCreatedDate().plusMinutes(1).isBefore(LocalDateTime.now())) {
//                throw new BadRequestException("Too many attempts. Please try later");
//            }
//        }
        String code = RandomUtil.getRandomSmsCode();
        String message = "Kun.uz partali uchun\n registratsiya kodi: " + code;

        SmsResponseDTO responseDTO = send(phone, message);

        SmsEntity entity = new SmsEntity();
        entity.setPhone(phone);
        entity.setCode(code);
        entity.setStatus(responseDTO.getSuccess());

        smsRepository.save(entity);
    }

    public boolean verifySms(String phone, String code) {
        Optional<SmsEntity> optional = smsRepository.findTopByPhoneOrderByCreatedDateDesc(phone);
        if (optional.isEmpty()) {
            return false;
        }
        SmsEntity sms = optional.get();
        LocalDateTime validDate = sms.getCreatedDate().plusMinutes(1);

        if (sms.getCode().equals(code) && validDate.isAfter(LocalDateTime.now())) {
            return true;
        }
        return false;
    }

    private SmsResponseDTO send(String phone, String message) {
        SmsRequestDTO requestDTO = new SmsRequestDTO();
        requestDTO.setKey(key);
        requestDTO.setPhone(phone);
        requestDTO.setMessage(message);
        System.out.println("Sms Request: message " + message);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SmsRequestDTO> entity = new HttpEntity<SmsRequestDTO>(requestDTO, headers);

        RestTemplate restTemplate = new RestTemplate();
        SmsResponseDTO response = restTemplate.postForObject(smsUrl, entity, SmsResponseDTO.class);
        System.out.println("Sms Response  " + response);
        return response;
    }

    public PageImpl pagination(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.ASC, "createdDate");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<SmsEntity> all = smsRepository.findAll(pageable);

        List<SmsEntity> list = all.getContent();

        List<SmsDTO> dtoList = new LinkedList<>();

        list.forEach(sms -> {
            if(sms.getStatus().equals(Boolean.TRUE)) {
                SmsDTO dto = new SmsDTO();
                dto.setId(sms.getId());
                dto.setPhone(sms.getPhone());
                dto.setCode(sms.getCode());
                dto.setStatus(sms.getStatus());
                dto.setCreatedDate(sms.getCreatedDate());
                dtoList.add(dto);
            }
        });
        return new PageImpl(dtoList,pageable, all.getTotalElements());
    }
    public Long smsCount(String phone){
       return smsRepository.getSmsCount(phone);
    }
}
