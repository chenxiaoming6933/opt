package job;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import util.LogUtil;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName 文件定时任务
 * @Author XiaoMing
 * @Date
 * @Vsersion V1.0
 */
public class FileScanningJob {

    /**
     * 开启任务
     */
    public static void startJob(long runTime, String filePath, String fileSuffix, FileAlterationListener jobService) {
        try {
            // 创建过滤器
            IOFileFilter directories = FileFilterUtils.and(FileFilterUtils.directoryFileFilter(), HiddenFileFilter.VISIBLE);
            IOFileFilter files = FileFilterUtils.and(FileFilterUtils.fileFileFilter(), FileFilterUtils.suffixFileFilter(fileSuffix));
            IOFileFilter filter = FileFilterUtils.or(directories, files);

            // 使用过滤器 绑定任务
            FileAlterationObserver fileAlterationObserver = new FileAlterationObserver(new File(filePath), filter);
            fileAlterationObserver.addListener(jobService);

            //创建文件变化监听器
            FileAlterationMonitor fileAlterationMonitor = new FileAlterationMonitor(runTime, fileAlterationObserver);

            // 开始监控
            fileAlterationMonitor.start();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error(e.getMessage());
        }
    }
}
