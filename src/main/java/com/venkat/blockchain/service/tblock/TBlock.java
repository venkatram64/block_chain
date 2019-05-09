package com.venkat.blockchain.service.tblock;

import com.venkat.blockchain.common.StringUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TBlock {

    private String hash;
    private String previousHash;
    private String data;
    private long timeStamp;
    private int nonce;

    private String merkleRoot;
    private List<Transaction> transactions = new ArrayList<>();

    public TBlock(String previousHash){
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = this.calculateHash();
    }

    public TBlock(String data, String previousHash){
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
        merkleRoot = StringUtil.getMerkleRoot(transactions);
        String target = new String(new char[difficulty]).replace('\0','0');
        while(!this.hash.substring(0, difficulty).equals(target)){
            nonce++;
            this.hash = this.calculateHash();
        }
        System.out.println("Block Mined!!! " + this.hash);
    }

    public boolean addTransaction(Transaction transaction){
        if(transaction == null){
            return false;
        }
        if(previousHash != "0"){
            if(transaction.processTransaction() != true){
                System.out.println("Transaction failed to process. Discarded.");
                return false;
            }
        }
        transactions.add(transaction);
        System.out.println("Transaction successfully added to Block.");
        return true;
    }

    public String getMerkleRoot() {
        return merkleRoot;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}
