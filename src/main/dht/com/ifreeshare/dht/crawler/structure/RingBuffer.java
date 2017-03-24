package com.ifreeshare.dht.crawler.structure;


public class RingBuffer {

    private int bufferSize;
    private int head = 0;
    private int tail = 0;
    
    public RingBuffer(int i) {
    	this.bufferSize = i;
	}
	private Boolean empty() {
        return head == tail;
    }
    private Boolean full() {
        return (tail + 1) % bufferSize == head;
    }
//    public Boolean put(Record v) {
//        /*if (full()) {
//            return false;
//        }*/
//        buffer[tail] = v;
//        tail = (tail + 1) % bufferSize;
//        return true;
//    }
//    public Record get() {
//        if (empty()) {
//            return null;
//        }
//        Record result = buffer[head];
//        head = (head + 1) % bufferSize;
//        return result;
//    }
//	public Record[] getAll() {
//        return buffer;
//    }
}
