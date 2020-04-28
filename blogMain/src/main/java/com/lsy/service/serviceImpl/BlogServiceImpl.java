package com.lsy.service.serviceImpl;

import com.lsy.NotFoundException;
import com.lsy.dao.BlogRepository;
import com.lsy.po.Blog;
import com.lsy.po.Type;
import com.lsy.service.BlogService;
import com.lsy.util.MarkdownUtils;
import com.lsy.util.MyBeanUtils;
import com.lsy.util.RedisUtil;
import com.lsy.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;


@Service
public class BlogServiceImpl implements BlogService {


    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private RedisUtil redisUtil;

    //获取博客对象
    @Override
    public Blog getBlog(Long id) {
        //查询缓存，存在返回，不存在加入缓存
        String strblogid = "blog-"+ id;
        if (redisUtil.hasKey(strblogid)) {
            return (Blog) redisUtil.get(strblogid);
        }
        Blog b = blogRepository.findOne(id);
        //加入缓存
        redisUtil.set(strblogid, b, 600);
        return b;
    }

    //获取博客，并增加点击量
    @Transactional
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = null;
        //查询缓存
        String strblogid = "blog-"+ id;
        if (redisUtil.hasKey(strblogid)) {
            blogRepository.updateViews(id);
            blog = (Blog) redisUtil.get(strblogid);
        }
        if (blog==null) {
            blog = blogRepository.findOne(id);
        }
        if (blog == null) {
            throw new NotFoundException("该博客不存在");
        }
        Blog b = new Blog();
        BeanUtils.copyProperties(blog, b);
        String content = b.getContent();
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        //增加查询次数
        blogRepository.updateViews(id);
        //将处理过的博客对象替换缓存
        redisUtil.set(strblogid,b,600);
        return b;
    }


    //获取分页对象
    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (!"".equals(blog.getTitle()) && blog.getTitle() != null) {
                    predicates.add(cb.like(root.<String>get("title"), "%" + blog.getTitle() + "%"));
                }
                if (blog.getTypeId() != null) {
                    predicates.add(cb.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
                }
                if (blog.isRecommend()) {
                    predicates.add(cb.equal(root.<Boolean>get("recommend"), blog.isRecommend()));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Join join = root.join("tags");
                return cb.equal(join.get("id"), tagId);
            }
        }, pageable);
    }

    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return blogRepository.findByQuery(query, pageable);
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        Pageable pageable = new PageRequest(0, size, sort);
        return blogRepository.findTop(pageable);
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogRepository.findGroupYear();
        Map<String, List<Blog>> map = new HashMap<>();
        for (String year : years) {
            map.put(year, blogRepository.findByYear(year));
        }
        return map;
    }

    @Override
    public Long countBlog() {
        return blogRepository.count();
    }


    //保存博客
    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if (blog.getId() == null) {
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        } else {
            blog.setUpdateTime(new Date());
        }
        return blogRepository.save(blog);
    }

    //修改博客
    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = blogRepository.findOne(id);
        //查询缓存，存在则删除
        String strblogid = "blog-"+ id;
        if (redisUtil.hasKey(strblogid)) {
            redisUtil.del(strblogid);
        }
        if (b == null) {
            throw new NotFoundException("该博客不存在");
        }
        BeanUtils.copyProperties(blog, b, MyBeanUtils.getNullPropertyNames(blog));
        b.setUpdateTime(new Date());
        return blogRepository.save(b);
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        //查询缓存，存在则删除
        String strblogid = "blog-"+ id;
        if (redisUtil.hasKey(strblogid)) {
            redisUtil.del(strblogid);
        }
        blogRepository.delete(id);
    }

}
