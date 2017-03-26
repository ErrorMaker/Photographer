package com.hypermine.habbo.photographer.threading;

import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Scott Stamp <scott@hypermine.com> on 3/25/2017.
 */

public class ThreadPooling
{
    public final int threads;
    private final ScheduledExecutorService scheduledPool;
    private volatile boolean canAdd;

    public ThreadPooling(Integer threads)
    {
        this.threads = threads;
        this.scheduledPool = Executors.newScheduledThreadPool(this.threads, new DefaultThreadFactory("ArcturusThreadFactory"));
        this.canAdd = true;
//        Emulator.getLogging().logStart("Thread Pool -> Loaded!");
    }

    public void run(Runnable run)
    {
        try
        {
            if (this.canAdd)
            {
                this.run(run, 0);
            }
        }
        catch (Exception e)
        {
//            Emulator.getLogging().logErrorLine(e);
        }
    }

    public void run(Runnable run, long delay)
    {
        try
        {
            if (this.canAdd)
            {
                this.scheduledPool.schedule(run, delay, TimeUnit.MILLISECONDS);
            }
        }
        catch (Exception e)
        {
//            Emulator.getLogging().logErrorLine(e);
//            Emulator.getLogging().logErrorLine(e.getCause());
        }
    }

    public void shutDown()
    {
        this.canAdd = false;

        this.scheduledPool.shutdownNow();
        while(!this.scheduledPool.isTerminated()) {
        }
    }

    public ScheduledExecutorService getService()
    {
        return this.scheduledPool;
    }
}

