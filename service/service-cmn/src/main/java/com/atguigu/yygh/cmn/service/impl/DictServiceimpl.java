package com.atguigu.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.yygh.cmn.listener.DictListener;
import com.atguigu.yygh.cmn.mapper.DictMapper;
import com.atguigu.yygh.cmn.service.DictService;
import com.atguigu.yygh.model.cmn.Dict;
import com.atguigu.yygh.vo.cmn.DictEeVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class DictServiceimpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    /**
     * 根据上级id查询子数据列表
     *
     * @param id
     * @return
     */
    @Cacheable(value = "dict", keyGenerator = "keyGenerator")
    @Override
    public List<Dict> findChildData(Long id) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("parent_id", id);
        List<Dict> dictList = baseMapper.selectList(wrapper);
        //  向list集合中的每个dict对象中设置hasChildren
        // for (Dict dict : dictList) {
        //     Long dictId = dict.getId();
        //     boolean isChild = this.isChildren(dictId);
        //     dict.setHasChildren(isChild);
        // }
        dictList.stream().forEach(dict -> {
            boolean isHasChildren = this.isChildren(dict.getId());
            dict.setHasChildren(isHasChildren);
        });

        return dictList;
    }

    /**
     * 判断id下面是否有子节点
     *
     * @param id
     * @return
     */
    private boolean isChildren(Long id) {
        QueryWrapper wrapper = new QueryWrapper();
        //  条件查询    parent_id和传入的id进行查询
        wrapper.eq("parent_id", id);
        Integer count = baseMapper.selectCount(wrapper);
        return count > 0;
    }

    /**
     * 导出数据到Excel
     *
     * @param response
     */
    @Override
    public void exportData(HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("数据字典", "utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

            List<Dict> dictList = baseMapper.selectList(null);
            ArrayList<DictEeVo> dictVoList = new ArrayList<>(dictList.size());
            for (Dict dict : dictList) {
                DictEeVo dictVo = new DictEeVo();
                BeanUtils.copyProperties(dict, dictVo, DictEeVo.class);
                dictVoList.add(dictVo);
            }

            EasyExcel.write(response.getOutputStream(), DictEeVo.class)
                    .sheet("数据字典")
                    .doWrite(dictVoList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 导入数据字典   导入数据之后  数据库中的数据就发生了变化   此处需要清空缓存    重新进行注入
     * allEntries = true    方法调用后清空所有缓存
     *
     * @param file
     */
    @CacheEvict(value = "dict", allEntries = true)
    @Override
    public void importDictData(MultipartFile file) {
        try {
            //  读取导入的Excel文件    类型为DictEeVo实体类  并通过监听器将导入的数据同步到数据库中
            EasyExcel.read(file.getInputStream(), DictEeVo.class, new DictListener(baseMapper))
                    .sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
