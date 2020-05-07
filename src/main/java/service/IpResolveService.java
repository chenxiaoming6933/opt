package service;

import com.alibaba.fastjson.JSONObject;

import java.io.File;

/**
 * @author ChenXiaoMing
 * @title: DnsResolveService
 * @projectName resolve
 * @description: dns服务器扫描(ip) 解析数据 并录入es 接口
 * @date 2019/9/6/13:56
 */
public interface IpResolveService {

    /**
     * 录入数据
     * @param file
     */
    void  insert(File file);
}
