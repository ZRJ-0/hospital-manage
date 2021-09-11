package com.atguigu.yygh.hosp.service.impl;

import com.atguigu.yygh.hosp.mapper.HospitalSetMapper;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class HosptialSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {

    // @Autowired
    // private HospitalSetMapper hospitalSetMapper;


    @Override
    public String getSignKey(String hoscode) {
        HospitalSet hospSet = baseMapper.selectOne(new QueryWrapper<HospitalSet>().eq("hoscode", hoscode));
        return hospSet.getSignKey();
    }
}
