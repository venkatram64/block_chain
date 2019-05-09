package com.venkat.blockchain.service.tblock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TBlockChain {

    private List<TBlock> bcList;
    int difficulty;

    public static float minimumTransaction = 0.1f;
    public static Map<String, TransactionOutput> UTXOs = new HashMap<>();


    public TBlockChain(int difficulty){
        bcList = new ArrayList<>();
        this.difficulty = difficulty;
    }
    public void add(TBlock block){
        this.bcList.add(block);
    }
    public List<TBlock> blocks(){
        return this.bcList;
    }

    public boolean isChainValid(Transaction genesisTransaction){

        if(this.bcList.size() == 0){
            throw new RuntimeException("No blocks available.");
        }
        Map<String, TransactionOutput> tempUTXOs = new HashMap<>();
        tempUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));
        TBlock current, previous;
        String hashTarget = new String(new char[difficulty]).replace('\0','0');
        for(int i = 1; i < this.bcList.size(); i++){
            current = this.bcList.get(i);
            previous = this.bcList.get(i - 1);
            if(!current.getHash().equals(current.calculateHash())){
                System.out.println("Current hashes not equal");
                return false;
            }
            if(!previous.getHash().equals(previous.calculateHash())){
                System.out.println("Previous hashes not equal");
                return false;
            }
            if(!current.getHash().substring(0, difficulty).equals(hashTarget)){
                System.out.println("This block has not been mined.");
                return false;
            }

            TransactionOutput tempOut;
            for(int t = 0; t < current.getTransactions().size(); t++){
                Transaction currentTrx = current.getTransactions().get(t);
                if(!currentTrx.verifySignature()){
                    System.out.println("Signature on Transaction(" + t + ") is invalid.");
                    return false;
                }
                if(currentTrx.getInputValue() != currentTrx.getOutputValue()){
                    System.out.println("Inputs are note equal to outputs on Transaction(" + t +")");
                    return false;
                }
                for(TransactionInput input: currentTrx.inputs){
                    tempOut = tempUTXOs.get(input.transactionOutputId);

                    if(tempOut == null){
                        System.out.println("Received input on Transaction(" + t + ") is Missing.");
                    }
                }
            }
        }
        System.out.println("Blockchain is valid.");
        return true;
    }

    public int getDifficulty() {
        return difficulty;
    }
}
