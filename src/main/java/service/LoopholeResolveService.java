package service;

import java.io.File;

/**
 * @author ChenXiaoMing
 * @title: DnsResolveService
 * @projectName resolve
 * @description: 漏洞扫描  解析数据 并录入es 接口
 * @date 2019/9/6/13:56
 */
public interface LoopholeResolveService {

    /**
     * 录入数据
     * @param file
     */
    void  insert(File file);
}
