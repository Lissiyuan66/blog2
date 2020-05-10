package com.lsy.controller;


import com.lsy.po.Comment;
import com.lsy.po.User;
import com.lsy.service.BlogService;
import com.lsy.service.CommentService;
import com.lsy.util.CodeUtil;
import com.lsy.util.SensitiveFilter;
import com.lsy.util.MQSendMail;
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
        Long blogId = comment.getBlog().getId();
        comment.setBlog(blogService.getBlog(blogId));
        User user = (User) session.getAttribute("user");
        UserInfoBean qquser = (UserInfoBean) session.getAttribute("qquser");
        if (qquser == null && user == null) {
            attributes.addFlashAttribute("message", "请登陆后评论");
            return "redirect:/comments/" + blogId;
        } else if (qquser != null) {
            comment.setNickname(qquser.getNickname());
            comment.setAvatar(qquser.getAvatar().getAvatarURL30());
        } else {
            comment.setNickname(user.getNickname());
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
        }
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
        //查询父评论邮箱
        String email = commentService.selectCommentMailById(comment.getParentComment().getId());
        //查询博客的标题
        String title = blogService.getBlog(blogId).getTitle();
        //评论内容
        String text = comment.getContent();
        //评论地址
        String url = comment.getPinglunurl();
        //评论人昵称
        String zname = comment.getNickname();
        if (email != null) {
            //查询父评论内容
            String fText = commentService.selectCommentTextById(comment.getParentComment().getId());
            //查询父评论昵称
            String fname = commentService.selectNameById(comment.getParentComment().getId());
            producer.send(title, text, email, fname, url, zname, fText);
        } else {
            //通知管理员
            producer.send(title, text, "17645135742@163.com", null, url, zname, null);
        }
        commentService.saveComment(comment);
        return "redirect:/comments/" + blogId;
    }
}
