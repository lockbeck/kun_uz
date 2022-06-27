package com.company.service;

import com.company.dto.RegistrationDTO;
import com.company.dto.integration.EmailDTO;
import com.company.entity.EmailHistoryEntity;
import com.company.enums.ProfileRole;
import com.company.repository.EmailHistoryRepository;
import com.company.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.LinkedList;
import java.util.List;

@Service
public class EmailService {
    @Autowired
    EmailHistoryRepository emailHistoryRepository;
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String fromAccount;
    @Value("${server.url}")
    private String serverUrl;

    public void sendRegistrationEmail( String toAccount, Integer id) {
//        String message = "Your Activation lin: adsdasdasdasda";
//        sendSimpleEmail(toAccount, "Registration", message);
        String token = JwtUtil.encode(id, ProfileRole.USER);
        String url = String.format("<a href='%s/auth/email/verification/%s'>Verification Link</a>", serverUrl, token);

        StringBuilder builder = new StringBuilder();
        builder.append("<h1 style='align-text:center'>Hi man </h1>");
        builder.append("<b>Kun.uz saytiga verifikatsiya kodi  </b>");
        builder.append("<p>");
        builder.append(url);
        builder.append("</p>");
        System.out.println(builder);
        sendEmail(toAccount, "Registration", builder.toString());
    }

    private void sendEmail(String toAccount, String subject, String text) {
        try {
            MimeMessage msg = javaMailSender.createMimeMessage();
            msg.setFrom(fromAccount);
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            helper.setTo(toAccount);
            helper.setSubject(subject);
            helper.setText(text, true);
            javaMailSender.send(msg);
            EmailHistoryEntity entity = new EmailHistoryEntity();
            entity.setEmail(toAccount);
            emailHistoryRepository.save(entity);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendSimpleEmail(String toAccount, String subject, String text) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(toAccount);
        msg.setSubject(subject);
        msg.setText(text);
        msg.setFrom(fromAccount);
        javaMailSender.send(msg);
    }

    public PageImpl pagination(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.ASC, "createdDate");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<EmailHistoryEntity> all = emailHistoryRepository.findAll(pageable);

        List<EmailHistoryEntity> list = all.getContent();
        System.out.println(list);

        List<EmailDTO> dtoList = new LinkedList<>();

        list.forEach(email -> {
                EmailDTO dto = new EmailDTO();
                dto.setId(email.getId());
                dto.setEmail(email.getEmail());
                dto.setCreatedDate(email.getCreatedDate());
                dtoList.add(dto);

        });
        return new PageImpl(dtoList,pageable, all.getTotalElements());
    }
    public Long getCountByEmail(String email) {
        return emailHistoryRepository.getCountByEmail(email);
    }
}
