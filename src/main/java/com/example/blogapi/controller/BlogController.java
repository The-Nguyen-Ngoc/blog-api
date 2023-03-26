package com.example.blogapi.controller;

import com.example.blogapi.dto.request.EmailDetails;
import com.example.blogapi.dto.response.DetailPostResponse;
import com.example.blogapi.dto.response.ListRecentResponse;
import com.example.blogapi.dto.response.RecentDto;
import com.example.blogapi.entity.BlogEntity;
import com.example.blogapi.entity.EmailNewMember;
import com.example.blogapi.repository.EmailNewMemberRepo;
import com.example.blogapi.service.BlogService;
import jakarta.mail.internet.MimeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/blogs")
public class BlogController {
    private static final Logger logger = LogManager.getLogger(BlogController.class);

    private final BlogService blogService;
    private final JavaMailSender mailSender;
    private final EmailNewMemberRepo emailNewMemberRepo;


    public BlogController(BlogService blogService, JavaMailSender mailSender, EmailNewMemberRepo emailNewMemberRepo) {
        this.blogService = blogService;
        this.mailSender = mailSender;
        this.emailNewMemberRepo = emailNewMemberRepo;
    }

    @PostMapping()
    public ResponseEntity<?> addBlog(@RequestBody BlogEntity blogEntity) {
        BlogEntity blogSaved = blogService.addBlog(blogEntity);
        if (blogSaved == null) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(blogSaved);
        }
    }

    @GetMapping("/recents")
    public ResponseEntity<?> getRecents(@RequestParam Map map) {
        logger.info("---------------------------------GET LIST RECENTS-----------------------------------");

        ListRecentResponse listRecentResponse = blogService.getAllRecent(map);
        if (listRecentResponse.getRecentDtoList().isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(listRecentResponse);
        }
    }

    @GetMapping("/detail-post/{id}")
    public ResponseEntity<?> getDetailPost(@PathVariable int id) {
        logger.info("---------------------------------GET DETAIL POST BY ID-----------------------------------");

        DetailPostResponse detailPostById = blogService.getDetailPostById(id);
        if (detailPostById == null) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(detailPostById);
        }
    }

    @GetMapping("/popular")
    public ResponseEntity<?> getPopular5Post() {
        logger.info("---------------------------------GET POPULAR-----------------------------------");

        List<RecentDto> recentDtoList = blogService.get5LimitRecentOrderByView();
        if (recentDtoList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(recentDtoList);
        }
    }

    @GetMapping("/posts/{id}/{page}")
    public ResponseEntity<?> getListPostByCategoryId(@PathVariable int id, @PathVariable int page) {
        logger.info("---------------------------------GET POSTS BY PAGE-----------------------------------");
        logger.info("REQUEST: " +"PAGE: "+ page +" ID: "+ id);


        ListRecentResponse listDetailPostRes = blogService.getListDetailPostByCategoryId(id, page);
        if (listDetailPostRes.getRecentDtoList().isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(listDetailPostRes);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchAll(@RequestParam String keyword) {
        logger.info("---------------------------------SEARCH -----------------------------------");
        logger.info("REQUEST: " +keyword);

        List<RecentDto> recentDtoList = blogService.searchByKeyword(keyword);
        if (recentDtoList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(recentDtoList);
        }
    }

    @PostMapping("/email")
    public ResponseEntity<?> submitEmail(@RequestBody EmailDetails email) {
        logger.info("---------------------------------SEND EMAIL NEW MEMBER-----------------------------------");
        logger.info("REQUEST: " +email);

        try {
            if (email != null) {
                EmailNewMember emailNewMemberFind = emailNewMemberRepo.findEmailNewMemberByIp(email.getIp());
                if (emailNewMemberFind == null) {
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
            return new ResponseEntity<>(email, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
