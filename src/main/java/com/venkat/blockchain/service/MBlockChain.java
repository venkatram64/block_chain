package com.venkat.blockchain.service;

import java.util.ArrayList;
import java.util.List;

public class MBlockChain {

    private List<MBlock> bcList;
    int difficulty;
    public MBlockChain(int difficulty){
        bcList = new ArrayList<>();
        this.difficulty = difficulty;
    }
    public void add(MBlock block){
        this.bcList.add(block);
    }
    public List<MBlock> blocks(){
        return this.bcList;
    }

    public boolean isChainValid(){

        if(this.bcList.size() == 0){
            throw new RuntimeException("No blocks available.");
        }
        MBlock current, previous;
        String hashTarget = new String(new char[difficulty]).replace('\0','0');
        for(int i = 1; i < this.bcList.size(); i++){
            current = this.bcList.get(i);
            previous = this.bcList.get(i - 1);
            if(!current.getHash().equals(current.calculateHash())){
                return false;
            }
            if(!previous.getHash().equals(previous.calculateHash())){
                return false;
            }
            if(!current.getHash().substring(0, difficulty).equals(hashTarget)){
                return false;
            }
        }
        return true;
    }

    public int getDifficulty() {
        return difficulty;
    }
}
