/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telemarketing.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author HCD
 */
public class ThreadPoolExecuter {
    
    private final static ExecutorService POOLS_EXECUTOR_SERVICE = Executors.newCachedThreadPool();
    
    public static void execute(Runnable runable) {
        POOLS_EXECUTOR_SERVICE.execute(runable);
    }
}
