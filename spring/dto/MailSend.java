package com.db.spring.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Data
public class MailSend {
    //发件人邮箱
    private String senderEmail;

    //收件人邮箱
    private String receiverEmail;

    //邮件主题
    private String subject;

    //邮件正文
    private String content;

    //附件列表
    private List<MultipartFile> attachments;
}