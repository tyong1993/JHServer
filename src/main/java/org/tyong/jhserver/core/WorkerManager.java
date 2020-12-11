package org.tyong.jhserver.core;

import java.util.ArrayList;

/**
 * 工作线程管理器
 */
public class WorkerManager extends Thread{
    //最小worker个数
    protected int minWorkerNum = 5;
    //最大worker个数
    protected int maxThreadNum = 300;
    //worker池中worker总个数
    protected static volatile int workerPoolNum = 0;
    //工作中的worker个数
    protected static volatile int activeWorkerNum = 0;
    //worker池
    protected volatile static ArrayList<Worker> workerPool = new ArrayList<Worker>();
    //容器
    public static Container container;


    public void init(){
        WorkerManager.container = new Container();
        container.init();
        this.setDaemon(true);
        this.start();
    }
    @Override
    public void run() {
        System.out.println("WorkerManager start work...");
        for (int i=0;i<this.minWorkerNum;i++){
            this.setActiveWorkerNumInc();
            this.workerPoolNum++;
            Worker worker = new Worker();
            workerPool.add(worker);
            worker.start();
        }
        while (true){
            //待处理的连接数量大于了活动worker的3倍并且还存在未唤醒的worker则唤醒一个
            if(Connector.connectPool.size()>this.activeWorkerNum*3 && this.workerPoolNum > this.activeWorkerNum){
                Integer index = this.getAUnActiveWorkerIndex();
                if(index != null){
                    Worker worker = workerPool.get(index);
                    worker.is_active = true;
                    synchronized (worker) {
                        worker.notify();
                    }
                }
            }
            //请求数太多动态添加worker
            if(Connector.connectPool.size()>this.workerPoolNum*3 && this.workerPoolNum < this.maxThreadNum){
                Worker worker = new Worker();
                workerPool.add(worker);
                worker.start();
                this.setActiveWorkerNumInc();
                this.workerPoolNum++;
            }
            //请求数太少释放work
            if(Connector.connectPool.size()<this.workerPoolNum*3 && this.workerPoolNum> this.minWorkerNum){
                Integer index = this.getAUnActiveWorkerIndex();
                if(index != null){
                    Worker worker = workerPool.get(index);
                    workerPool.remove(index.intValue());
                    this.workerPoolNum--;
                    worker.interrupt();
                }
            }

        }

    }
    /**
     * 获取一个睡眠的worker
     */
    private Integer getAUnActiveWorkerIndex(){
        Integer i = 0;
        Worker worker;
        while (true){
            if(i >= workerPool.size()){
                worker = null;
                break;
            }
            worker = workerPool.get(i);
            if(worker.is_active == false){
                break;
            }
            i++;
        }
        i = (null==worker?null:i);
        return  i;
    }

    /**
     * 工作中的worker个数减一
     */
    public synchronized static void setActiveWorkerNumDec(){
        WorkerManager.activeWorkerNum--;
    }
    /**
     * 工作中的worker个数加一
     */
    public synchronized static void setActiveWorkerNumInc(){
        WorkerManager.activeWorkerNum++;
    }
}
