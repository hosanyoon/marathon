package com.ssafy.marathon.service.user;

import com.ssafy.marathon.controller.user.UserSignController;
import com.ssafy.marathon.db.entity.user.User;
import com.ssafy.marathon.db.repository.UserRepository;
import com.ssafy.marathon.dto.request.user.IdEmailDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl {

    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;
    private final Logger LOGGER = LoggerFactory.getLogger(UserSignController.class);

    @Transactional(readOnly = true)
    public void sendEmailToFindId(String email) {
        User user = (User) userRepository.findByEmail(email).get();
        String id = user.getId();
        LOGGER.info("[sendEmailToFindId] 검색된 id : {} ", id);
        LOGGER.info("[sendEmailToFindId] 이메일 전송 시작");
        String subject = "[Marathon] 아이디 찾기";
        String text = user.getName() + " 회원님 아이디는" + id + " 입니다. ";
        System.out.println(text);
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject(subject);
        msg.setText(text);
        javaMailSender.send(msg);
        LOGGER.info("[sendEmail] 이메일 전송 완료");
    }

    @Transactional
    public void sendEmailToFindPassword(IdEmailDto idEmailDto) throws Exception {
        LOGGER.info("[sendEmailToFindPassword] 검색할 아이디 : {}, 이메일 : {}",idEmailDto.getId(), idEmailDto.getEmail());
        User user = userRepository.getById(idEmailDto.getId());
        if(!idEmailDto.getEmail().equals(user.getEmail())) {
            LOGGER.info("[sendEmailToFindPassword] 아이디, 이메일 불일치");
            throw new Exception();
        }
        LOGGER.info("[sendEmailToFindPassword] 아이디, 이메일 일치");
        LOGGER.info("[sendEmailToFindPassword] 임시 비밀번호 생성 시작");
        String NewPassword = createPassword();
        String str = passwordEncoder.encode(NewPassword);
        user.updateUserPassword(passwordEncoder.encode(NewPassword));
        LOGGER.info("[sendEmailToFindId] 임시 비밀번호 생성 완료 pw : {}", NewPassword);
        LOGGER.info("[sendEmailToFindId] 임시 비밀번호 생성 완료 pw : {}", str);
        LOGGER.info("[sendEmailToFindId] 이메일 전송 시작");
        String subject = "[Marathon] 임시 비밀번호";
        String text = user.getName() + "회원님의 임시 비밀번호는 " + NewPassword + " 입니다. ";
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(idEmailDto.getEmail());
        msg.setSubject(subject);
        msg.setText(text);
        javaMailSender.send(msg);
        LOGGER.info("[sendEmail] 이메일 전송 완료");
    }

    public String createPassword(){
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
            'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        String str = "";

        // 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 구문을 작성함
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
    }
}