package com.drk.tools.gplannercore.core.streams;

import com.drk.tools.gplannercore.core.Plan;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class PlanBuffer {

    private final static Item FINAL_ITEM = new Item(null);
    private final BlockingQueue<Item> queue;
    private boolean isClosed;

    public PlanBuffer() {
        this.queue = new LinkedBlockingQueue<>();
        this.isClosed = false;
    }

    public GOutputStream outputStream() {
        return new OutputStream();
    }

    public GInputStream inputStream() {
        return new InputStream();
    }

    private void internalClose() {
        if (isClosed) {
            return;
        }
        isClosed = true;
        queue.offer(FINAL_ITEM);
    }

    private class InputStream implements GInputStream {

        @Override
        public Plan read() throws GStreamException {
            if (isClosed) {
                throw new GStreamException("Stream is Closed");
            }
            try {
                Item item = queue.poll(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
                return item == null ? null : item.plan;
            } catch (InterruptedException e) {
                throw new GStreamException(e);
            }
        }

        @Override
        public void close() {
            internalClose();
        }
    }

    private class OutputStream implements GOutputStream {

        @Override
        public void write(Plan plan) throws GStreamException {
            if (isClosed) {
                throw new GStreamException("Stream is Closed");
            }
            queue.offer(new Item(plan));
        }

        @Override
        public void write(Plan plan, long timeout) throws GStreamException {
            if (isClosed) {
                throw new GStreamException("Stream is Closed");
            }
            try {
                queue.offer(new Item(plan), timeout, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                throw new GStreamException(e);
            }
        }

        @Override
        public synchronized void close() throws GStreamException {
            internalClose();
        }

        @Override
        public boolean isClosed() {
            return isClosed;
        }
    }

    private static class Item {

        final Plan plan;

        Item(Plan plan) {
            this.plan = plan;
        }
    }
}
