package com.chaoip.ipproxy;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

/**
 * BaseTest
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/18 11:14
 */
public class BaseTest {
    // 定义3个mock对象
    private static final MongodStarter starter = MongodStarter.getDefaultInstance();
    private static MongodExecutable mongodExecutable;
    private static MongodProcess mongod;

    /**
     * 启动mock的mongo
     * 注意：首次启动测试，会在宿主机下载相应版本的Mongo，所以会比较慢。
     *
     * @throws Exception 可能的异常
     */
    @BeforeAll
    static void startMongo() throws Exception {
        // 超过3.4的以上版本，会报错： Could not start process: <EOF>
        // yml里的spring.data.mongodb.host配置也会引发这个问题
        mongodExecutable = starter.prepare(new MongodConfigBuilder()
                .version(Version.Main.V3_4)
                .net(new Net(12345, Network.localhostIsIPv6())).build());
        mongod = mongodExecutable.start();
    }

    /**
     * 停止mock的mongo
     */
    @AfterAll
    static void stopMongo() {
        mongod.stop();
        mongodExecutable.stop();
    }

}
