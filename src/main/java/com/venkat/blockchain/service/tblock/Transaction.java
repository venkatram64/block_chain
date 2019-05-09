package com.venkat.blockchain.service.tblock;

import com.venkat.blockchain.common.StringUtil;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class Transaction {

    public String transactionId;
    public PublicKey sender;
    public PublicKey recipient;
    public float value;
    public byte[] signature;



    public List<TransactionInput> inputs = new ArrayList<>();
    public List<TransactionOutput> outputs = new ArrayList<>();
    private static int sequence = 0;

    public Transaction(PublicKey from, PublicKey to, float value, List<TransactionInput> inputs){
        this.sender = from;
        this.recipient = to;
        this.value = value;
        this.inputs = inputs;
    }

    private String calculateHash(){
        sequence++;
        return StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient)
                + Float.toString(value) + sequence;
    }
    public void generateSignature(PrivateKey privateKey){
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient)
                            + Float.toString(value);
        signature = StringUtil.applyECDSASig(privateKey, data);
    }

    public boolean verifySignature(){
        String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient)
                                + Float.toString(value);
        return StringUtil.verifyECDSASig(sender, data, signature);
    }

    public boolean processTransaction(){

        if(verifySignature() == false){
            System.out.println("Transaction Signature failed to verify.");
            return false;
        }

        for(TransactionInput input : inputs){
            input.UTXO = TBlockChain.UTXOs.get(input.transactionOutputId);
        }

        if(getInputValue() < TBlockChain.minimumTransaction){
            System.out.println("Transaction Inputs too small: " + getInputValue());
            System.out.println("Please enter the amount greater than " + TBlockChain.minimumTransaction);
            return false;
        }

        float leftOver = getInputValue() - value;
        transactionId = calculateHash();
        //send value to recipient
        outputs.add(new TransactionOutput(this.recipient, value, transactionId));
        //send the left over 'change' back to sender
        outputs.add(new TransactionOutput(this.sender, leftOver, transactionId));

        //add outputs to Unspent list
        for(TransactionOutput output : outputs){
            TBlockChain.UTXOs.put(output.id, output);
        }

        //Remove transaction inputs from UTXO lists as spent:
        for(TransactionInput input : inputs){
            if(input.UTXO == null){
                continue;
            }
            TBlockChain.UTXOs.remove(input.UTXO.id);
        }

        return true;
    }

    public float getInputValue(){
        float total = 0;
        for(TransactionInput input : inputs){
            if(input.UTXO == null){
                continue;
            }
            total += input.UTXO.value;
        }
        return total;
    }

    public float getOutputValue(){
        float total = 0;
        for(TransactionOutput output: outputs){
            total += output.value;
        }
        return total;
    }
}
