package com.example.blogapi.service;

import com.example.blogapi.dto.request.EmailDetails;
import com.example.blogapi.entity.EmailNewMember;
import com.example.blogapi.repository.EmailNewMemberRepo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@EnableKafka
public class MyConsumer {

    private final JavaMailSender mailSender;
    private final EmailNewMemberRepo emailNewMemberRepo;

    public MyConsumer(JavaMailSender mailSender, EmailNewMemberRepo emailNewMemberRepo) {
        this.mailSender = mailSender;
        this.emailNewMemberRepo = emailNewMemberRepo;
    }

    @KafkaListener(topics = "emails", groupId = "myGroup")
    public void listen(EmailDetails email) throws MessagingException {
        System.out.println("Received message: " + email);
        if (email != null) {
            if (true) {
                EmailNewMember emailNewMember = new EmailNewMember();
                emailNewMember.setEmail(email.getEmailTo());
                emailNewMember.setIp(email.getIp());
                EmailNewMember emailNewMemberSaved = emailNewMemberRepo.save(emailNewMember);
                if(emailNewMemberSaved !=null){
                    MimeMessage message = mailSender.createMimeMessage();
                    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
                    boolean html = true;
                    helper.setTo(email.getEmailTo());
                    helper.setSubject("BLOG DEBUG CHÀO MỪNG THÀNH VIÊN MỚI!");
                    helper.setText("\n" +
                            "\t<body>\n" +
                            "<h3>Xin chào,</h3> \n<p>" +
                            " \n" +
                            "Đội ngũ Blog Debug cảm ơn vì bạn đã đăng ký nhận thông báo sớm nhất với những bài viết trên blog của chúng tôi! Chúng tôi rất vui mừng khi có thêm thành viên mới và hy vọng bạn sẽ thấy những bài viết trên blog của chúng tôi hữu ích. <br><br>\n" +
                            " \n" +
                            "Nếu bạn có bất kỳ câu hỏi hoặc góp ý nào, xin đừng ngần ngại liên hệ với chúng tôi qua email này. Chúng tôi sẵn sàng hỗ trợ bạn bất cứ lúc nào. <br>Đừng quên truy cập Blog: <a href='fb.com'>debug.com.vn</a> của chúng tôi để xem những bài viết mới nhất.<br>\n" +
                            " \n" +
                            "Một lần nữa, cảm ơn bạn đã tham gia vào cộng đồng của chúng tôi!<br><br> \n" +
                            " \n" +
                            "Trân trọng,<br> \n" +
                            "Đội ngũ quản trị viên Blog Debug.<br></p>\n" +
                            "\t</body>", html);
                    mailSender.send(message);
                }
            }

        }
    }
}