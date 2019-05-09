package com.venkat.blockchain.service.tblock;

import com.venkat.blockchain.common.StringUtil;
import org.junit.Test;

import java.security.Security;

public class TBlockChainTest {

    @Test
    public void transactionTest(){
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Wallet walletA = new Wallet();
        Wallet walletB = new Wallet();

        System.out.println("Private and Public keys: ");
        System.out.println(StringUtil.getStringFromKey(walletA.privateKey));
        System.out.println(StringUtil.getStringFromKey(walletA.publicKey));

        Transaction transaction = new Transaction(walletA.publicKey, walletB.publicKey, 5, null);
        transaction.generateSignature(walletA.privateKey);
        System.out.println("Is signature verified.");
        System.out.println(transaction.verifySignature());
    }

    @Test
    public void multipleTrxTest(){

        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Wallet walletA = new Wallet();
        Wallet walletB = new Wallet();

        Wallet coinBase = new Wallet();

        TBlockChain bc = new TBlockChain(5);
        //create genesis transaction, which sends 100 Blockchain coins to walletA.

        Transaction genesisTrx = new Transaction(coinBase.publicKey, walletA.publicKey, 100f, null);
        genesisTrx.generateSignature(coinBase.privateKey); //manually sign the genesis transaction
        genesisTrx.transactionId = "0";//manually set the transaction id
        genesisTrx.outputs.add(new TransactionOutput(genesisTrx.recipient, genesisTrx.value, genesisTrx.transactionId));
        //manually add the transactions output.
        TBlockChain.UTXOs.put(genesisTrx.outputs.get(0).id, genesisTrx.outputs.get(0));
        //its important to store our first transaction the UTXOs list.
        System.out.println("Creating and Mining Genesis block.");
        TBlock genesis = new TBlock("0");
        genesis.addTransaction(genesisTrx);
        bc.add(genesis);

        //testing
        TBlock block = new TBlock(genesis.getHash());
        System.out.println("WalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletA's attempting to send funds (40) to WalletB.");
        block.addTransaction(walletA.sendFunds(walletB.publicKey, 40f));
        block.mineBlock(bc.getDifficulty());
        bc.add(block);
        System.out.println("WalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());

        TBlock block2 = new TBlock(block.getHash());
        System.out.println("WalletA's attempting to send funds (1000) to WalletB.");
        block2.addTransaction(walletA.sendFunds(walletB.publicKey, 1000f));
        block2.mineBlock(bc.getDifficulty());
        bc.add(block2);
        System.out.println("WalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());

        TBlock block3 = new TBlock(block2.getHash());
        System.out.println("WalletB's attempting to send funds (20) to WalletA.");
        block3.addTransaction(walletB.sendFunds(walletA.publicKey, 20f));
        block3.mineBlock(bc.getDifficulty());
        bc.add(block3);
        System.out.println("WalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());

        bc.isChainValid(genesisTrx);

    }
}
