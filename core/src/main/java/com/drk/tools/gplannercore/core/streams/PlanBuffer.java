package com.drk.tools.gplannercore.core.streams;

import com.drk.tools.gplannercore.core.Plan;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class PlanBuffer {

    private final BlockingQueue<Item> queue;
    private boolean isClosed;

    public PlanBuffer(int bufferSize) {
        this.queue = new ArrayBlockingQueue<>(bufferSize);
        this.isClosed = false;
    }

    public GOutputStream outputStream() {
        return new OutputStream();
    }

    public GInputStream inputStream() {
        return new InputStream();
    }

    private void internalClose() {
        if(isClosed){
            return;
        }
        isClosed = true;
        //Unblock producer
        if(isFull()){
            queue.poll();
        }
        //Unblock consumer
        if(queue.isEmpty()) {
            queue.offer(new Item(null));
        }
    }

    private boolean isFull(){
       return queue.remainingCapacity() == 0;
    }

    private class InputStream implements GInputStream {

        @Override
        public Plan read() throws GStreamException {
            if (isClosed) {
                throw new GStreamException("Stream is Closed");
            }
            Item item = queue.poll();
            return item.plan;
        }

        @Override
        public void close() {
            internalClose();
        }
    }

    private class OutputStream implements GOutputStream {

        @Override
        public void write(Plan plan) throws GStreamException {
            if(isClosed){
                throw new GStreamException("Stream is Closed");
            }
            queue.offer(new Item(plan));
        }

        @Override
        public void write(Plan plan, long timeout) throws GStreamException {
            if(isClosed){
                throw new GStreamException("Stream is Closed");
            }
            try {
                queue.offer(new Item(plan), timeout, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                throw new GStreamException(e);
            }
        }

        @Override
        public void close() throws GStreamException {
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
