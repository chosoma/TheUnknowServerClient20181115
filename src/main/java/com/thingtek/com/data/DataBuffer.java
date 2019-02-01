package com.thingtek.com.data;

import com.thingtek.com.data.entity.RawData;
import com.thingtek.config.agreement.AgreementConfig;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class DataBuffer {
    @Resource
    private DataFactory factory;
    @Resource
    private AgreementConfig agreementConfig;
    // 字节数据缓冲区
    private List<RawData> buffer;
    // 缓冲区添加数据和移除数据的线程锁
    private Object dataLock = new Object();
    // 数据处理线程
    private Thread dataThread;

    private Lock lock;
    private Condition con;

    public DataBuffer() {
        buffer = new ArrayList<RawData>();
        lock = new ReentrantLock();
        con = lock.newCondition();
        start();
    }

    /**
     * 将有效长度为length的数据添加到数据缓冲区
     *
     * @param //数据
     */
    public void receDatas(RawData rawData) {
        synchronized (dataLock) {
            buffer.add(rawData);
        }
        // 如果数据处理线程waiting中
        if (dataThread.getState() == Thread.State.WAITING) {
            lock.lock();
            con.signal();
            lock.unlock();
        }
    }

    // 数据处理线程是否等待
    // public boolean isRunnable() {
    // return dataThread.getState() == Thread.State.RUNNABLE;
    // }

    /**
     * 数据处理 ：另起一个线程对数据缓冲区的数据进行处理
     */
    private void start() {
        if (dataThread != null) {
            if (dataThread.isAlive()) {
                return;
            }
        }
        dataThread = new Thread(new DataRunnable());
        dataThread.start();
    }

    private boolean alive = true;

    class DataRunnable implements Runnable {
        @Override
        public void run() {// 如果flag标志为true，则继续循环

            lock.lock();
            try {

                while (alive) {
                    // 判断缓冲区数据是否达到最小数据长度
                    if (buffer.size() == 0) {
                        Thread.sleep(50);// 等待50毫秒
                        // 判断缓冲区数据是否达到最小数据长度,如果没有，则保存数据
                        if (buffer.size() == 0) {
                            Thread.sleep(50);// 等待50毫秒
                            // 判断缓冲区数据是否达到最小数据长度,如果没有则线程休眠
                            if (buffer.size() == 0) {
                                factory.saveData();// 数据存储
                                con.await();//很关键
                            }
                            continue;
                        }
                    }
                    RawData rawdata = buffer.remove(0);
                    byte[] data = rawdata.getData();
                    Date time = rawdata.getTime();

                    try {
                        int datalength = agreementConfig.getDataLength(0, data);
                        int order = agreementConfig.getOrderType(0, data);
                        int cmd = agreementConfig.getCmd(0, data);

                        switch (cmd) {

                        }


                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                // 数据线程出错
            } finally {
                lock.unlock();
            }
        }

    }

    public void close() {
        alive = false;
        // 如果数据处理线程waiting中
        if (dataThread.getState() == Thread.State.WAITING) {
            lock.lock();
            con.signal();
            lock.unlock();
        }
    }

}
