package org.connection;

import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;

/**
 * @author wangzhanwei
 */
public class Chunk {
    public byte[] buffer;
    public int offset;
    public int length;
    public Chunk(){}
    public Chunk(Builder builder){
        this(builder.build());
    }
    public Chunk(Message msg){
        this(msg.toByteArray());
    }
    public Chunk(byte[] bytes){
        this.buffer = bytes;
        this.offset = 0;
        this.length = bytes.length;
    }
     public Chunk(byte[] buf, int off, int len) {
        this.buffer = buf;
        this.offset = off;
        this.length = len;
    }
}
