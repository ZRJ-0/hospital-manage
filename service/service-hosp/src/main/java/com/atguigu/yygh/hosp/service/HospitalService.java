package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface HospitalService {
    /**
     * 上传医院信息
     *
     * @param paramMap
     */
    void save(Map<String, Object> paramMap);

    /**
     * 查询医院信息
     *
     * @param hoscode
     * @return
     */
    Hospital getByHoscode(String hoscode);

    /**
     * 分页查询
     *
     * @param page
     * @param limit
     * @param hospitalQueryVo
     * @return
     */
    Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);

    /**
     * 更新医院上线状态
     *
     * @param id
     * @param status
     */
    void updateStatus(String id, Integer status);

    /**
     * 获取医院详情信息
     *
     * @param id
     * @return
     */
    Map<String, Object> getHospById(String id);

    /**
     * 根据医院编号获取医院名称接口
     *
     * @param hoscode
     * @return
     */
    String getHospName(String hoscode);
}
