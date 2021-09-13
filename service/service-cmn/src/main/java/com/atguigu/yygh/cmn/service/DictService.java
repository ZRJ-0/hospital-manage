package com.atguigu.yygh.cmn.service;

import com.atguigu.yygh.model.cmn.Dict;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface DictService extends IService<Dict> {
    List<Dict> findChildData(Long id);

    void exportData(HttpServletResponse response);

    void importDictData(MultipartFile file);

    /**
     * 根据上级编码与值获取数据字典名称
     *
     * @param dictCode
     * @param value
     * @return
     */
    String getDictName(String dictCode, String value);

    /**
     * 根据dictCode获取下级节点
     *
     * @param dictCode
     * @return
     */
    List<Dict> findByDictCode(String dictCode);
}
