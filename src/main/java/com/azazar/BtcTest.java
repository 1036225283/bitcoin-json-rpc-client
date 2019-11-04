package com.azazar;

import com.alibaba.fastjson.JSON;
import com.azazar.bitcoin.jsonrpcclient.Bitcoin;
import com.azazar.bitcoin.jsonrpcclient.BitcoinException;
import com.azazar.bitcoin.jsonrpcclient.BitcoinJSONRPCClient;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class BtcTest {

    private String host = "127.0.0.1";
    private String port = "19001";
    private String account = "admin1";
    private String pwd = "123";

//    curl -H "Content-Type:application/json" -X POST --data '{"method":"listtransactions","params":["*",160]}' http://admin1:123@127.0.0.1:19001

    public static void main(String[] args) throws Exception {
        BtcTest test = new BtcTest();
//        test.listTransactions();

//        String strSign = test.createMultiSignRawTransaction("2N72Ec5o3t8CKWq9Zb8cLCCZo7tKo1QaT2m", "2N2jimNewE5T5zFcPrXvuLG5DjmVMrknyVu", 1);
//        System.out.println("strSign = " + strSign);
        //        020000000101ba55ba5d8c14ff663fecaeb57c52260d112d48b6cc8ac33725934f696d169e0000000000ffffffff0100e1f5050000000017a914681ccd18fac30d2b6fa93b0e9cdb4ea3a12ab6d68700000000


        String strSign = "020000000101ba55ba5d8c14ff663fecaeb57c52260d112d48b6cc8ac33725934f696d169e0000000000ffffffff0100e1f5050000000017a914681ccd18fac30d2b6fa93b0e9cdb4ea3a12ab6d68700000000";
        // 添加找零地址,并返回新的签名
        strSign = test.addChangeAddress(strSign, "2N72Ec5o3t8CKWq9Zb8cLCCZo7tKo1QaT2m");
        System.out.println("strSign = " + strSign);

        // 开始签名


    }

    public String createMultiSignRawTransaction(String multiSignAddress, String toAddress, double amount) throws Exception {
        String host = "127.0.0.1";
        String port = "19011";
        String user = "admin2";
        String password = "123";

        Bitcoin bitcoin = new BitcoinJSONRPCClient(host, port, user, password);

        List<Bitcoin.Unspent> unspents = bitcoin.listUnspent(6, 100000, multiSignAddress);

        String ChangeAddress = "2N72Ec5o3t8CKWq9Zb8cLCCZo7tKo1QaT2m";

        double totalAmount = 0;

        List<Bitcoin.TxInput> inputs = new ArrayList<Bitcoin.TxInput>();

        for (int i = 0; i < unspents.size(); i++) {
            Bitcoin.Unspent unspent = unspents.get(i);
            Bitcoin.TxInput input = new Bitcoin.BasicTxInput(unspent.txid(), unspent.vout());
            inputs.add(input);
            totalAmount = totalAmount + unspent.amount();
            if (totalAmount > amount) {
                System.out.println("金额已经足够了 totalAmount = " + totalAmount + " amount = " + amount);
                break;
            }
            System.out.println("totalAmount = " + totalAmount + " amount = " + amount);
        }

        List<Bitcoin.TxOutput> outputs = new ArrayList<Bitcoin.TxOutput>();
        Bitcoin.TxOutput output = new Bitcoin.BasicTxOutput(toAddress, amount);
        outputs.add(output);
        return bitcoin.createRawTransaction(inputs, outputs);
    }

    public String addChangeAddress(String strSign, String changeAddress) throws Exception {

        String host = "127.0.0.1";
        String port = "19011";
        String user = "admin2";
        String password = "123";
        Bitcoin bitcoin = new BitcoinJSONRPCClient(host, port, user, password);
        return bitcoin.fundrawTransaction(strSign, changeAddress, null);
    }

    public String sign(String strSign) throws Exception {
        String host = "127.0.0.1";
        String port = "19011";
        String user = "admin2";
        String password = "123";
        Bitcoin bitcoin = new BitcoinJSONRPCClient(host, port, user, password);
        bitcoin.signRawTransaction(strSign);

        return null;
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
        List<Bitcoin.Transaction> list = bitcoin.listTransactions("*", 160);
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
