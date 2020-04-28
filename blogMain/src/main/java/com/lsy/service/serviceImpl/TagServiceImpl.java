package com.lsy.service.serviceImpl;

import com.lsy.NotFoundException;
import com.lsy.dao.TagRepository;
import com.lsy.po.Tag;
import com.lsy.service.TagService;
import com.lsy.util.RedisUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private RedisUtil redisUtil;

    @Transactional
    @Override
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    //根据id获取标签
    @Transactional
    @Override
    public Tag getTag(Long id) {
        //查询缓存，若不存在则加入
        String strtagid = "tag-" + id;
        if (redisUtil.hasKey(strtagid)) {
            return (Tag) redisUtil.get(strtagid);
        }
        Tag tag = tagRepository.findOne(id);
        //加入缓存
        redisUtil.set(strtagid, tag, 600);
        return tag;
    }

    //根据名字获取标签
    @Override
    public Tag getTagByName(String name) {
        //查询缓存
        String strtagname = "strtagname-" + name;
        if (redisUtil.hasKey(strtagname)) {
            return (Tag) redisUtil.get(strtagname);
        }
        Tag tag = tagRepository.findByName(name);
        //不存在则加入缓存
        redisUtil.set(strtagname, tag, 600);
        return tag;
    }

    //获取标签分页
    @Transactional
    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    //获取标签列表
    @Override
    public List<Tag> listTag() {
        //查询缓存
        /*String strlisttag = "strlisttag";
        if (redisUtil.hasKey(strlisttag)) {
            return redisUtil.lGet(strlisttag, 0, -1);
        }*/
        //加入缓存
        //redisUtil.lSet(strlisttag, list, 600);
        return tagRepository.findAll();
    }

    @Override
    public List<Tag> listTagTop(Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "blogs.size");
        Pageable pageable = new PageRequest(0, size, sort);
        return tagRepository.findTop(pageable);
    }


    @Override
    public List<Tag> listTag(String ids) { //1,2,3
        return tagRepository.findAll(convertToList(ids));
    }

    private List<Long> convertToList(String ids) {
        List<Long> list = new ArrayList<>();
        if (!"".equals(ids) && ids != null) {
            String[] idarray = ids.split(",");
            for (int i = 0; i < idarray.length; i++) {
                list.add(new Long(idarray[i]));
            }
        }
        return list;
    }


    //修改标签
    @Transactional
    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag t = tagRepository.findOne(id);
        //查询缓存，存在则删除
        String strtagid = "tag-" + id;
        if (redisUtil.hasKey(strtagid)) {
            redisUtil.del(strtagid);
        }
        if (t == null) {
            throw new NotFoundException("不存在该标签");
        }
        BeanUtils.copyProperties(tag, t);
        return tagRepository.save(t);
    }


    //删除标签
    @Transactional
    @Override
    public void deleteTag(Long id) {
        String strtagid = "tag-" + id;
        if (redisUtil.hasKey(strtagid)) {
            redisUtil.del(strtagid);
        }
        tagRepository.delete(id);
    }


}
