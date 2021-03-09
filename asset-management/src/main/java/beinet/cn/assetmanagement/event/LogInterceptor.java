package beinet.cn.assetmanagement.event;

import beinet.cn.assetmanagement.logs.model.Operatelog;

/**
 * 日志处理接口
 */
public interface LogInterceptor {
    /**
     * 指定日志是否可以处理
     *
     * @param log 日志
     * @return true false
     */
    boolean isSupported(Operatelog log);

    /**
     * 具体处理方法
     *
     * @param log    日志
     * @param source 日志源
     */
    void filter(Operatelog log, Object source);
}
