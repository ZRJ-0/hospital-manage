package com.atguigu.yygh.hosp.controller;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.common.utils.MD5;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.atguigu.yygh.vo.hosp.HospitalSetQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@Api(tags = "医院设置管理")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
// @CrossOrigin
public class HospitalSetController {
    @Autowired
    private HospitalSetService hospitalSetService;

    /**
     * 获取所有医院的信息   封装到Result对象中    返回码和返回消息进行返回
     *
     * @return
     */
    @ApiOperation("获取所有医院设置")
    @GetMapping("findAll")
    public Result findAllHospitalSet() {
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
    }

    /**
     * 对应表中的is_delete  设置了@TableLogic  逻辑删除
     *
     * @param id
     * @return
     */
    @ApiOperation("逻辑删除医院设置")
    @DeleteMapping("{id}")
    public Result removeHospSet(@PathVariable("id") Long id) {
        boolean flag = hospitalSetService.removeById(id);
        return flag == true ? Result.ok() : Result.fail();
    }

    @ApiOperation("条件查询带分页")
    @PostMapping("findPageHospSet/{current}/{limit}")
    public Result findPageHospSet(
            @PathVariable("current") Long current,
            @PathVariable("limit") Long limit,
            @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo) {
        //  创建page对象    传递当前页   每页记录数
        Page<HospitalSet> page = new Page<>(current, limit);
        //  构建条件
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        //  获取的两个对象不为空才进行查询
        if (!StringUtils.isEmpty(hospitalSetQueryVo.getHosname())) {
            wrapper.like("hosname", hospitalSetQueryVo.getHosname());
        }
        if (!StringUtils.isEmpty(hospitalSetQueryVo.getHoscode())) {
            wrapper.eq("hoscode", hospitalSetQueryVo.getHoscode());
        }
        //  调用方法实现分页    属于条件分页查询
        Page<HospitalSet> page1 = hospitalSetService.page(page, wrapper);

        return Result.ok(page1);
    }

    @ApiOperation("添加医院设置")
    @PostMapping("saveHospitalSet")
    public Result saveHospitalSet(@RequestBody HospitalSet hospitalSet) {
        //  设置初始状态为1
        hospitalSet.setStatus(1);
        //  签名密钥
        Random random = new Random();
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis() + "" + random.nextInt(1000)));
        boolean save = hospitalSetService.save(hospitalSet);
        return save == true ? Result.ok() : Result.fail();
    }

    @ApiOperation("根据id获取医院设置")
    @GetMapping("getHospSet/{id}")
    public Result getHospSet(@PathVariable("id") Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        // try {
        //     int i = 1 / 0;
        // } catch (Exception e) {
        //     throw new YyghException("失败", 201);
        // }
        return Result.ok(hospitalSet);
    }

    @ApiOperation("修改医院设置")
    @PostMapping("updateHospSet")
    public Result updateHospSet(@RequestBody HospitalSet hospitalSet) {
        boolean flag = hospitalSetService.updateById(hospitalSet);
        return flag == true ? Result.ok() : Result.fail();
    }

    @ApiOperation("批量修改医院设置")
    @DeleteMapping("batchRemove")
    public Result BatchRemove(@RequestBody List<Long> idList) {
        boolean flag = hospitalSetService.removeByIds(idList);
        return flag == true ? Result.ok() : Result.fail();
    }

    @ApiOperation("医院设置与解锁")
    @PutMapping("lockHosptialSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable("id") Long id,
                                  @PathVariable("status") Integer status) {
        //  根据id查询医院设置的信息
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        //  设置状态
        hospitalSet.setStatus(status);
        //  调用方法    更新该id设置的数据
        boolean flag = hospitalSetService.updateById(hospitalSet);
        return flag == true ? Result.ok() : Result.fail();
    }

    @ApiOperation("发送签名密钥")
    @PostMapping("sendKey/{id}")
    public Result sendKey(@PathVariable Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String signKey = hospitalSet.getSignKey();
        String hoscode = hospitalSet.getHoscode();
        //  TODO    发送签名信息
        return Result.ok();
    }
}
