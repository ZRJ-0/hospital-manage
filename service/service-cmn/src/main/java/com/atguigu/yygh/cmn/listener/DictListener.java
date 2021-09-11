package com.atguigu.yygh.cmn.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.yygh.model.cmn.Dict;
import com.atguigu.yygh.vo.cmn.DictEeVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class DictListener extends AnalysisEventListener<DictEeVo> {
    @Autowired
    private BaseMapper baseMapper;

    public DictListener(BaseMapper baseMapper) {
        this.baseMapper = baseMapper;
    }

    /**
     * 一行一行的读取
     *
     * @param dictEeVo
     * @param analysisContext
     */
    @Override
    public void invoke(DictEeVo dictEeVo, AnalysisContext analysisContext) {
        //  调用方法添加到数据库
        Dict dict = new Dict();
        BeanUtils.copyProperties(dictEeVo, dict);
        // dict.setIsDeleted(0);
        baseMapper.insert(dict);
    }

    /**
     * 读取数据之后执行的相关操作
     *
     * @param analysisContext
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
