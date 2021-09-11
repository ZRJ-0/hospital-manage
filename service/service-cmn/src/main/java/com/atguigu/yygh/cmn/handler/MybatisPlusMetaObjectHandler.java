package com.atguigu.yygh.cmn.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

@Component
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        /**
         * mybatisplus自定义填充公共字段 ,即没有传的字段自动填充
         * 第一个参数是数据库逻辑删除字段名称，第二个参数是你默认添加的逻辑删除字段数据
         * fieldName需要和实体类中的字段名一致   通过实例类中字段名称对应数据库中的字段名称
         */
        setFieldValByName("isDeleted", 0, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {

    }
}
