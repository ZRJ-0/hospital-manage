package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.vo.hosp.DepartmentQueryVo;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface DepartmentService {
    /**
     * 上传科室信息
     *
     * @param paramMap
     */
    void save(Map<String, Object> paramMap);

    /**
     * 分页查询
     *
     * @param page
     * @param limit
     * @param departmentQueryVo
     * @return
     */
    Page<Department> selectPage(int page, int limit, DepartmentQueryVo departmentQueryVo);

    /**
     * 删除科室
     *
     * @param hoscode
     * @param depcode
     */
    void remove(String hoscode, String depcode);
}
