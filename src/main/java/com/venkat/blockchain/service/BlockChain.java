package com.venkat.blockchain.service;

import java.util.ArrayList;
import java.util.List;

public class BlockChain {

    private List<Block> bcList;
    public BlockChain(){
        bcList = new ArrayList<>();
    }
    public void add(Block block){
        this.bcList.add(block);
    }
    public List<Block> blocks(){
        return this.bcList;
    }

    public boolean isChainValid(){

        if(this.bcList.size() == 0){
            throw new RuntimeException("No blocks available.");
        }
        Block current, previous;
        for(int i = 1; i < this.bcList.size(); i++){
            current = this.bcList.get(i);
            previous = this.bcList.get(i - 1);
            if(!current.getHash().equals(current.calculateHash())){
                return false;
            }
            if(!previous.getHash().equals(previous.calculateHash())){
                return false;
            }
        }
        return true;
    }
}
