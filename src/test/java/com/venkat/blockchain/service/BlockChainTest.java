package com.venkat.blockchain.service;

import com.google.gson.GsonBuilder;
import org.junit.Test;


public class BlockChainTest {

    @Test
    public void testShowBlocksValid(){
        BlockChain bc = new BlockChain();
        bc.add(new Block("This is my genesis block", "0"));
        bc.add(new Block("This is my second block", bc.blocks().get(bc.blocks().size() - 1).getHash()));
        bc.add(new Block("This is my third block", bc.blocks().get(bc.blocks().size() - 1).getHash()));
        System.out.println(bc.isChainValid());
        String blockChainJson = new GsonBuilder().setPrettyPrinting().create().toJson(bc);
        System.out.println(blockChainJson);
    }
    @Test
    public void testShowBlocksNotValid(){
        BlockChain bc = new BlockChain();
        bc.add(new Block("This is my genesis block", "0"));
        bc.add(new Block("This is my second block", bc.blocks().get(bc.blocks().size() - 1).getHash()));
        bc.add(new Block("This is my third block", bc.blocks().get(bc.blocks().size() - 1).getHash()));
        System.out.println(bc.isChainValid());
        String blockChainJson = new GsonBuilder().setPrettyPrinting().create().toJson(bc);
        System.out.println(blockChainJson);

        Block tryToModify = bc.blocks().get(1);
        tryToModify.setData("This is my second block modified");
        tryToModify.calculateHash();
        System.out.println(bc.isChainValid());
        blockChainJson = new GsonBuilder().setPrettyPrinting().create().toJson(bc);
        System.out.println(blockChainJson);
    }
}
