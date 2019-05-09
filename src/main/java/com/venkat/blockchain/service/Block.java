package com.venkat.blockchain.service;

import com.venkat.blockchain.common.StringUtil;

import java.util.Date;

public class Block {

    private String hash;
    private String previousHash;
    private String data;
    private long timeStamp;

    public Block(String data, String previousHash){
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = this.calculateHash();
    }

    public String getHash() {
        return hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getData() {
        return data;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String calculateHash(){
        String calculateHash = StringUtil.applySha256(this.previousHash
                        + Long.toString(this.timeStamp) + this.data);
        return calculateHash;
    }
}
