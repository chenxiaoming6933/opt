package service;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author ChenXiaoMing
 * @title: DomainResolveService
 * @projectName resolve
 * @description: 解析数据 并录入es 接口
 * @date 2019/9/6/13:56
 */
public interface DomainResolveService {

    /**
     * 录入数据
     * @param file
     */
    void  insert(File file);

}
