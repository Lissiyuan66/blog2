package com.lsy.service.serviceImpl;

import com.lsy.NotFoundException;
import com.lsy.dao.TypeRepository;
import com.lsy.po.Type;
import com.lsy.service.TypeService;
import com.lsy.util.RedisUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    private TypeRepository typeRepository;

    @Autowired
    private RedisUtil redisUtil;

    //保存分类
    @Transactional
    @Override
    public Type saveType(Type type) {
        return typeRepository.save(type);
    }

    //根据id获取分类
    @Transactional
    @Override
    public Type getType(Long id) {
        //查询缓存，存在返回，不存在加入缓存
        String strtypeid = "type-"+ id;
        if (redisUtil.hasKey(strtypeid)) {
            return (Type) redisUtil.get(strtypeid);
        }
        Type type = typeRepository.findOne(id);
        //加入缓存
        redisUtil.set(strtypeid, type, 600);
        return type;
    }

    //根据名字获取分类
    @Override
    public Type getTypeByName(String name) {
        //查询缓存，存在返回，不存在加入缓存
        String strtypename = "typename-"+ name;
        if (redisUtil.hasKey(strtypename)) {
            return (Type) redisUtil.get(strtypename);
        }
        Type type = typeRepository.findByName(name);
        //加入缓存
        redisUtil.set(strtypename, type, 600);
        return type;
    }

    @Transactional
    @Override
    public Page<Type> listType(Pageable pageable) {
        return typeRepository.findAll(pageable);
    }

    //以list返回所有type
    @Override
    public List<Type> listType() {
        //查询缓存
        /*String strlisttype = "strlisttype";
        if (redisUtil.hasKey(strlisttype)) {
            return redisUtil.lGet(strlisttype, 0, -1);
        }*/
        //加入缓存
        //redisUtil.lSet(strlisttype, list, 600);
        return typeRepository.findAll();
    }


    @Override
    public List<Type> listTypeTop(Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC,"blogs.size");
        Pageable pageable = new PageRequest(0,size,sort);
        return typeRepository.findTop(pageable);
    }


    //修改分类
    @Transactional
    @Override
    public Type updateType(Long id, Type type) {
        Type t = typeRepository.findOne(id);
        //查询缓存，存在则删除
        String strtypeid = "type-" + id;
        if (redisUtil.hasKey(strtypeid)) {
            redisUtil.del(strtypeid);
        }
        if (t == null) {
            throw new NotFoundException("不存在该类型");
        }
        BeanUtils.copyProperties(type,t);
        return typeRepository.save(t);
    }



    @Transactional
    @Override
    public void deleteType(Long id) {
        //查询缓存，存在则删除
        String strtypeid = "type-" + id;
        if (redisUtil.hasKey(strtypeid)) {
            redisUtil.del(strtypeid);
        }
        typeRepository.delete(id);
    }
}
