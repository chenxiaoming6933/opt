package service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.stereotype.Service;
import service.IpFilelistenerService;
import service.IpResolveService;
import util.ElasticsearchUtil;
import util.LogUtil;
import util.SpringContentUtil;

import java.io.File;

/**
 * @ClassName 文件监听接口实现类(IP)
 * @Author XiaoMing
 * @Date
 * @Vsersion V1.0
 */
@Service
public class IpFileListenerServiceImpl extends FileAlterationListenerAdaptor implements IpFilelistenerService {

    private IpResolveService ipResolveService;

    /**
     * 文件创建执行
     */
    @Override
    public void onFileCreate(File file) {
        try {
            if (null == ipResolveService) {
                ipResolveService = new IpResolveServiceImpl();
            }
            ipResolveService.insert(file);
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
        LogUtil.info("ip文件删除成功:" + file.getAbsolutePath());
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
