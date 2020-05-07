package service.impl;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.stereotype.Service;
import service.DomainFilelistenerService;
import service.DomainResolveService;
import service.LoopholeFilelistenerService;
import service.LoopholeResolveService;
import util.LogUtil;
import util.SpringContentUtil;

import java.io.File;

/**
 * @ClassName 漏洞扫描接口实现类(domain)
 * @Author XiaoMing
 * @Date
 * @Vsersion V1.0
 */
@Service
public class LoopholeFileListenerServiceImpl extends FileAlterationListenerAdaptor implements LoopholeFilelistenerService {

    private LoopholeResolveService loopholeResolveService;

    /**
     * 文件创建执行
     */
    @Override
    public void onFileCreate(File file) {
        try {
            if (null == loopholeResolveService) {
                loopholeResolveService = new LoopholeResolveServiceImpl();
            }
            loopholeResolveService.insert(file);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.info("启动失败：" + e.getMessage());
        }
    }

    /**
     * 文件创建修改
     */
    @Override
    public void onFileChange(File file) {
        System.out.println("[修改]:" + file.getAbsolutePath());
    }

    /**
     * 文件删除
     */
    @Override
    public void onFileDelete(File file) {
        System.out.println("[漏扫删除]:" + file.getAbsolutePath());
    }


    @Override
    public void onStart(FileAlterationObserver observer) {
        super.onStart(observer);
    }

    @Override
    public void onStop(FileAlterationObserver observer) {
        super.onStop(observer);
    }
}
