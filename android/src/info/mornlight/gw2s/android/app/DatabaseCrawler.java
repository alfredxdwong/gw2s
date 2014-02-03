package info.mornlight.gw2s.android.app;

import info.mornlight.gw2s.android.client.ApiClient;
import info.mornlight.gw2s.android.db.Database;
import info.mornlight.gw2s.android.model.item.ItemDetails;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by alfred on 5/28/13.
 */
public class DatabaseCrawler {
    public interface CrawlerEventListener {
        void onStart();

        void onProgress(int percent);

        void onStop(Throwable error);
    }

    private Database db;
    private Throwable error;
    private String lang;
    private CrawlerEventListener listener;

    private ExecutorService pool;
    static final int POOL_SIZE = 5;

    public void setListener(CrawlerEventListener listener) {
        this.listener = listener;
    }

    Thread workingThread;

    public DatabaseCrawler(Database db, String lang) {
        this.db = db;
        this.lang = lang;
    }

    public void start() {
        if(workingThread != null)
            return;

        error = null;
        workingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(listener != null) {
                        listener.onStart();
                    }

                    pool = Executors.newFixedThreadPool(POOL_SIZE);

                    crawl();
                } catch (IOException e) {
                    error = e;
                    return;
                } finally {
                    pool.shutdown();
                    pool = null;

                    if(listener != null) {
                        listener.onStop(error);
                    }
                }
            }
        });
        workingThread.start();
    }

    public void stop() {
        workingThread.interrupt();
        try {
            workingThread.join(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workingThread = null;
        }
    }

    public Throwable getError() {
        return error;
    }

    public boolean isWorking() {
        return workingThread != null;
    }

    private void crawl() throws IOException {
        final ApiClient api = new ApiClient();

        Set<Integer> idsLocal = new HashSet<Integer>(db.getItemIds());
        final Set<Integer> ids = new HashSet<Integer>(api.listItems());

        ids.removeAll(idsLocal);

        if(Thread.interrupted()) {
            return;
        }

        final AtomicInteger updatedNum = new AtomicInteger(0);

        Iterator<Integer> iter = ids.iterator();
        while(iter.hasNext()) {
            List<Callable<Void>> tasks = new ArrayList<Callable<Void>>();
            while(iter.hasNext() && tasks.size() < 20) {
                final int id = iter.next();
                Callable<Void> task = new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        //ApiClient api = new ApiClient();
                        ItemDetails item = api.readItemDetails(id, lang);
                        db.insertItem(item);

                        updatedNum.incrementAndGet();

                        if(listener != null) {
                            listener.onProgress(updatedNum.get() / ids.size());
                        }

                        return null;
                    }
                };

                tasks.add(task);
            }

            try {
                pool.invokeAll(tasks);
            } catch (InterruptedException e) {
                return;
            }

            if(Thread.interrupted()) {
                return;
            }
        }
    }
}
