package com.atguigu.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.hosp.repository.DepartmentRepository;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.vo.hosp.DepartmentQueryVo;
import com.atguigu.yygh.vo.hosp.DepartmentVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public void save(Map<String, Object> paramMap) {
        Department department = JSONObject.parseObject(JSONObject.toJSONString(paramMap), Department.class);

        Department targetDepartment = departmentRepository.getDepartmentByHoscodeAndDepcode(department.getHoscode(), department.getDepcode());

        //  不为null  则证明查询到了部门信息 只对该信息进行修改   没有查到则进行新建
        if (null != targetDepartment) {
            //  copy不为null的值，该方法为自定义方法
            BeanUtils.copyProperties(department, targetDepartment, Department.class);
            departmentRepository.save(targetDepartment);
        } else {
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            //  不存在则保存部门信息
            departmentRepository.save(department);
        }
    }

    /**
     * 分页查询
     *
     * @param page
     * @param limit
     * @param departmentQueryVo
     * @return
     */
    @Override
    public Page<Department> selectPage(int page, int limit, DepartmentQueryVo departmentQueryVo) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        //  0为第一页
        Pageable pageable = PageRequest.of(page - 1, limit, sort);

        Department department = new Department();
        BeanUtils.copyProperties(departmentQueryVo, department);
        department.setIsDeleted(0);

        //    创建匹配器 即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching()      //  构建对象
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)     //  改变默认字符串匹配方式：模糊查询
                .withIgnoreCase(true);      //  改变默认大小写忽略方式 忽略大小写

        //    创建实例
        Example<Department> example = Example.of(department, matcher);

        Page<Department> pages = departmentRepository.findAll(example, pageable);
        return pages;
    }

    @Override
    public void remove(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if (department != null) {
            departmentRepository.deleteById(department.getId());
        }
    }

    /**
     * 根据医院编号，查询医院所有科室列表
     *
     * @param hoscode
     * @return
     */
    @Override
    public List<DepartmentVo> findDeptTree(String hoscode) {
        //  创建list集合，用于最终数据封装
        List<DepartmentVo> result = new ArrayList<>();

        //  根据医院编号，查询医院所有科室信息
        Department departmentQuery = new Department();
        departmentQuery.setHoscode(hoscode);
        Example example = Example.of(departmentQuery);
        //  所有科室列表 departmentList
        List<Department> departmentList = departmentRepository.findAll(example);

        //  根据大科室编号  bigcode 分组，获取每个大科室里面下级子科室
        Map<String, List<Department>> deparmentMap =
                departmentList.stream().collect(Collectors.groupingBy(Department::getBigcode));
        //  遍历map集合 deparmentMap
        for (Map.Entry<String, List<Department>> entry : deparmentMap.entrySet()) {
            //  大科室编号
            String bigcode = entry.getKey();
            //  大科室编号对应的全局数据
            List<Department> deparment1List = entry.getValue();
            //  封装大科室
            DepartmentVo departmentVo1 = new DepartmentVo();
            //  设置大科室的科室编号、科室名称     获取列表第一项中大科室的名称（BigName）并设置进去
            departmentVo1.setDepcode(bigcode);
            departmentVo1.setDepname(deparment1List.get(0).getBigname());

            //  封装小科室
            List<DepartmentVo> children = new ArrayList<>();
            for (Department department : deparment1List) {
                DepartmentVo departmentVo2 = new DepartmentVo();
                //  遍历列表中的内容    将科室编号和科室名称设置进去
                departmentVo2.setDepcode(department.getDepcode());
                departmentVo2.setDepname(department.getDepname());
                //  封装到list集合
                children.add(departmentVo2);
            }
            //  把小科室list集合放到大科室children里面
            departmentVo1.setChildren(children);
            //  放到最终result里面
            result.add(departmentVo1);
        }
        //  返回
        return result;
    }

    /**
     * 根据医院编号和可是编号获取科室名称
     *
     * @param hoscode
     * @param depcode
     * @return
     */
    @Override
    public Department getDepName(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        return department != null ? department : null;
    }
}
