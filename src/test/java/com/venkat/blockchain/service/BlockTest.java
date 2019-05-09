package com.venkat.blockchain.service;

import com.google.gson.GsonBuilder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class BlockTest {

    @Test
    public void createBlocks(){
        Block genesisBlock = new Block("This is my genesis block", "0");
        System.out.println("Hash value genesis is " + genesisBlock.getHash());

        Block secondBlock = new Block("This is my second block", genesisBlock.getHash());
        System.out.println("Hash value of second is " + genesisBlock.getHash());

        Block thirdBlock = new Block("This is my third block", secondBlock.getHash());
        System.out.println("Hash value of third is " + thirdBlock.getHash());
    }

    @Test
    public void showJsonAsBlocksInfo(){
        List<Block> bcList = new ArrayList<>();
        bcList.add(new Block("This is my genesis block", "0"));
        bcList.add(new Block("This is my second block", bcList.get(bcList.size() - 1).getHash()));
        bcList.add(new Block("This is my third block", bcList.get(bcList.size() - 1).getHash()));

        String blockChainJson = new GsonBuilder().setPrettyPrinting().create().toJson(bcList);
        System.out.println(blockChainJson);
    }
}
