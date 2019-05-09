package com.venkat.blockchain.service;

import com.venkat.blockchain.common.StringUtil;

import java.util.Date;

public class MBlock {

    private String hash;
    private String previousHash;
    private String data;
    private long timeStamp;
    private int nonce;

    public MBlock(String data, String previousHash){
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
                        + Long.toString(this.timeStamp)
                        + Integer.toString(this.nonce)
                        + this.data);
        return calculateHash;
    }

    public void mineBlock(int difficulty){
        String target = new String(new char[difficulty]).replace('\0','0');
        while(!this.hash.substring(0, difficulty).equals(target)){
            nonce++;
            this.hash = this.calculateHash();
        }
        System.out.println("Block Mined!!! " + this.hash);
    }
}
