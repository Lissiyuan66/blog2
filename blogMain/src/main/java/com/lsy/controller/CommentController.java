package com.lsy.controller;


import com.lsy.po.Comment;
import com.lsy.po.User;
import com.lsy.service.BlogService;
import com.lsy.service.CommentService;
import com.lsy.util.CodeUtil;
import com.lsy.util.SensitiveFilter;
import com.lsy.util.mqEmailUtil.MQSendMail;
import com.lsy.vo.QQusert;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller
public class CommentController {

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private MQSendMail producer;

    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model) {
        model.addAttribute("comments", commentService.listCommentByBlogId(blogId));
        return "blog :: commentList";
    }


    @PostMapping("/comments")
    public String post(Comment comment, HttpSession session, HttpServletRequest request,
                       RedirectAttributes attributes) {
        System.out.println("邮箱地址为:" + comment.getEmail());
        Long blogId = comment.getBlog().getId();
        comment.setBlog(blogService.getBlog(blogId));
        User user = (User) session.getAttribute("user");
        UserInfoBean qquser = (UserInfoBean) session.getAttribute("qquser");
        QQusert qqusert = (QQusert) session.getAttribute("qqusert");
        if (qquser == null && user == null) {
            attributes.addFlashAttribute("message", "请登陆后评论");
            return "redirect:/comments/" + blogId;
        } else if (qquser != null) {
            comment.setNickname(qquser.getNickname());
            comment.setAvatar(qqusert.getQqtouxiang());
            //comment.setAvatar(qquser.getAvatar());
        } else {
            comment.setNickname(user.getNickname());
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
        }
        //CodeController controller = new CodeController();
        if (!sensitiveFilter.filter(comment.getContent())) {
            attributes.addFlashAttribute("message", "请文明留言哦");
            return "redirect:/comments/" + blogId;
        }/*else if (!sensitiveFilter.filter(comment.getNickname())){
            attributes.addFlashAttribute("message", "昵称不合规，换一个吧");
            return "redirect:/comments/" + blogId;
        }*/
        if (!CodeUtil.checkVerifyCode(comment.getYzm(), request)) {
            attributes.addFlashAttribute("message", "验证码打错了");
            return "redirect:/comments/" + blogId;
        }
        /*if (user != null) {
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
        }*/
        //查询父评论邮箱
        String email = commentService.selectCommentMailById(comment.getParentComment().getId());
        //查询博客的标题
        String blogTitle = blogService.getBlog(blogId).getTitle();
        if (email != null) {
            //查询父评论昵称
            String fname = commentService.selectNameById(comment.getParentComment().getId());
            //System.out.println("父评论名称："+fname);
            //System.out.println("博客标题："+blogTitle);
            String title = "Hi, " + fname + " 有人在小源的个人博客评论了你，快去看看吧！";
            String text = "Hi," + fname + ",你好呀!" + "有小伙伴#" + comment.getNickname() + "#在小源的博客:[ " + blogTitle + " ]评论了你，评论内容是：**" + comment.getContent() + "**快去回复他吧： " + comment.getPinglunurl();
            //System.out.println("评论url为"+comment.getPinglunurl());
            //System.out.println("父评论id为"+comment.getParentComment().getId());
            producer.send(title, text, email);
        }
        //通知管理员
        producer.send("有人在博客评论了，快去看看吧", comment.getNickname() + "在" + blogTitle + "评论了你，去看看吧:" + comment.getPinglunurl(), "17645135742@163.com");
        commentService.saveComment(comment);
        return "redirect:/comments/" + blogId;
    }
}
