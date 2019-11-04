package com.azazar;

import com.alibaba.fastjson.JSON;
import com.azazar.bitcoin.jsonrpcclient.Bitcoin;
import com.azazar.bitcoin.jsonrpcclient.BitcoinException;
import com.azazar.bitcoin.jsonrpcclient.BitcoinJSONRPCClient;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.List;

public class BtcTest {

    private String host = "127.0.0.1";
    private String port = "19001";
    private String account = "admin1";
    private String pwd = "123";

//    curl -H "Content-Type:application/json" -X POST --data '{"method":"listtransactions","params":["*",160]}' http://admin1:123@127.0.0.1:19001

    public static void main(String[] args) throws Exception{
        BtcTest test = new BtcTest();
        test.listTransactions();
    }

    public void getBalance() throws BitcoinException {
        Bitcoin bitcoin = null;
        try {
            bitcoin = new BitcoinJSONRPCClient(host, port, account, pwd);
            BigDecimal balance = new BigDecimal(String.valueOf(bitcoin.getBalance()));
            System.out.println(balance.toPlainString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void getNewAddress() throws BitcoinException {
        Bitcoin bitcoin = null;
        try {
            bitcoin = new BitcoinJSONRPCClient(host, port, account, pwd);
            String addr = bitcoin.getNewAddress();
            System.out.println(addr);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void validateAddress() throws MalformedURLException, BitcoinException {
        Bitcoin bitcoin = new BitcoinJSONRPCClient(host, port, account, pwd);
        boolean rst = bitcoin.validateAddress("t1X9ZpWnDkZk8yfoDFXZDqePitGjQYoXjZf").isValid();
        System.out.println(rst);
    }

    public void listTransactions() throws MalformedURLException, BitcoinException {
        Bitcoin bitcoin = new BitcoinJSONRPCClient(host, port, account, pwd);
        List<Bitcoin.Transaction> list = bitcoin.listTransactions("*",160);
        System.out.println(list);
    }

    public void gettransaction() throws MalformedURLException, BitcoinException {
        Bitcoin bitcoin = new BitcoinJSONRPCClient(host, port, account, pwd);
        Bitcoin.RawTransaction transaction = bitcoin.getTransaction("6c588fe651bacef7da9f5f8b86e0222f6aaf61fc79f36bd26f4fae06b5ff0dce");
        System.out.println(JSON.toJSONString(transaction));
    }

    public void transfer() throws MalformedURLException, BitcoinException {
        Bitcoin bitcoin = new BitcoinJSONRPCClient(host, port, account, pwd);
        String txId = bitcoin.sendToAddress("t1dCxetNiBGCD8cirvAHPZoPbu5ZhLZLaD5", 0.01);
        System.out.println(txId);
    }
}
