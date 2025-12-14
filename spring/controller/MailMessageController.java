package com.db.spring.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.db.spring.entity.MailMessage;
import com.db.spring.service.MailMessageService;
import com.db.spring.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Su
 * @since 2025-12-04
 */
@RestController
@RequestMapping("/mail-message")
public class MailMessageController {

    @Autowired
    private MailMessageService mailMessageService;


    @GetMapping
    public MailMessage getMailById(@RequestParam("id") Long id){
        return mailMessageService.getBaseMapper().selectById(id);
    }

    @GetMapping("/getMailById")
    public Object getMailById0(@RequestParam("id") Long id){

        MailMessage mailMessage = mailMessageService.getBaseMapper().selectById(id);
        if(mailMessage == null){
            return ResultUtil.fail("801","返回未查询到邮件");
        }
        return ResultUtil.success(mailMessage);
    }

    @GetMapping("/getMailBycontent")
    public List<MailMessage> getMailBycontent(@RequestParam("content") String content){
        QueryWrapper<MailMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("content",content);
        return mailMessageService.getBaseMapper().selectList(queryWrapper);
    }

    @PostMapping("/insertMail")
    public int insertMail(@RequestBody MailMessage mailMessage){
        return mailMessageService.getBaseMapper().insert(mailMessage);
    }

    @GetMapping("/getAllUid")
    public List<String> getAllUid(){
        List<String> res =  new ArrayList<>();
        List<MailMessage> mailMessages = mailMessageService.getBaseMapper().selectList(null);
        for(MailMessage mailMessage : mailMessages){
            res.add(mailMessage.getMailUid());
        }
        return res;
    }

    @PostMapping("/markAsRead")
    public Object markAsRead(@RequestBody Long id) {
        if (id == null) {
            return ResultUtil.fail("802", "邮件ID不能为空");
        }
        MailMessage mailMessage = mailMessageService.getBaseMapper().selectById(id);
        if (mailMessage == null) {
            return ResultUtil.fail("801", "未查询到邮件");
        }
        // 更新阅读状态为1（已读）
        mailMessage.setReadStatus((byte) 1);
        mailMessage.setUpdateTime(LocalDateTime.now());
        mailMessageService.getBaseMapper().updateById(mailMessage);
        return ResultUtil.success("标记已读成功");
    }


    //传入email,得到email发送的所有邮件
    @GetMapping("/getMailByEmail")
    public Object getMailByEmail(@RequestParam("email") String email){
        QueryWrapper<MailMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("sender_account_email",email).orderByDesc("send_time");
        List<MailMessage> mailMessages = mailMessageService.getBaseMapper().selectList(queryWrapper);
        if(mailMessages!=null && mailMessages.size()>0){
            return ResultUtil.success(mailMessages);
        }
        else {
            return ResultUtil.fail("803","未查询到邮件");
        }
    }

}
