package com.example.blogapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {
  @Value("${spring.mail.host}")
  private String host;
  @Value("${spring.mail.port}")
  private int port;
  @Value("${spring.mail.properties.mail.smtp.auth:true}")
  private String auth;
  @Value("${spring.mail.username}")
  private String user_name;
  @Value("${spring.mail.password}")
  private String password;

  @Value("${spring.mail.properties.mail.smtp.starttls.enable:true}")
  private String starttls_enable;
  @Value("${spring.mail.properties.mail.smtp.starttls.required:true}")
  private String starttls_required;

  @Bean
  public JavaMailSender getMailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost(this.host);
    mailSender.setPort(this.port);
    mailSender.setUsername(this.user_name);
    mailSender.setPassword(this.password);

    Properties props = mailSender.getJavaMailProperties();
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.auth", this.auth);
    props.put("mail.smtp.starttls.enable", this.starttls_enable);
    props.put("mail.smtp.starttls.required", this.starttls_required);

    return mailSender;
  }
}