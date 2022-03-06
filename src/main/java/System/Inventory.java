package System;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author dongyudeng
 */
public class Inventory {
    final static File SHIPPING = new File("src/main/resources/Shipping.txt");
    final static File ERROR = new File("src/main/resources/Errors.txt");
    final static File INVENTORY = new File("src/main/resources/Inventory.txt");
    final static File TRANSACTIONS = new File("src/main/resources/Transactions.txt");
    final static File NEW_INVENTORY = new File("src/main/resources/NewInventory.txt");
    private static Map<Good,Integer> goodMap= new HashMap<>();
    private static List<Transaction> transactionList=new ArrayList<>();
    static BufferedWriter shippingWriter;
    static BufferedWriter errorWriter;
    private static void writeShipping(String @NotNull [] strings) throws IOException {
        String string=strings[0]+"\t"+strings[1]+"\t"+strings[2];
        shippingWriter.write(string);
        shippingWriter.newLine();
    }
    private static void writeError(String @NotNull [] strings) throws IOException {
        String string=strings[0]+"\t"+strings[1]+"\t"+strings[2];
        errorWriter.write(string);
        errorWriter.newLine();
    }
    private static void send(@NotNull Transaction transaction) throws IOException {
        Good good=new Good(transaction.strings[1],"supplier","description");
        if(goodMap.get(good)>Integer.parseInt(transaction.strings[2])){
            int temp=goodMap.get(good);
            goodMap.replace(good,temp,temp-Integer.parseInt(transaction.strings[2]));
            String[] strings={transaction.strings[3],transaction.strings[1],transaction.strings[2]};
            writeShipping(strings);
        }else {
            String[] strings={transaction.strings[3],transaction.strings[1],transaction.strings[2]};
            writeError(strings);
        }
    }
    private static void add(@NotNull Transaction transaction){
        goodMap.put(new Good(transaction.strings[1],transaction.strings[2],transaction.strings[3]),0);
    }
    private static void increase(@NotNull Transaction transaction){
        Good good=new Good(transaction.strings[1],"supplier","description");
        int temp=goodMap.get(good);
        goodMap.replace(good,temp,temp+Integer.parseInt(transaction.strings[2]));
    }
    private static void remove(@NotNull Transaction transaction){
        Good good=new Good(transaction.strings[1],"supplier","description");
        if(goodMap.get(good)!=0){
            String[] strings={"0",transaction.strings[1], String.valueOf(goodMap.get(good))};
        }
        else{
            goodMap.keySet().removeIf(key->key.equals(good));
        }
    }
    public static void main(String[] args) throws IOException {
        try (BufferedReader inventoryReader = new BufferedReader(new FileReader(INVENTORY, StandardCharsets.UTF_8))) {
            String string = null, regex = "\\t";
            while ((string = inventoryReader.readLine()) != null) {
                String[] strings = string.split(regex);
                Good good = new Good(strings[0], strings[2], strings[3]);
                goodMap.put(good,Integer.parseInt(strings[1]));
            }
        }
        try (BufferedReader transactionsReader = new BufferedReader(new FileReader(TRANSACTIONS, StandardCharsets.UTF_8))) {
            String string = null, regex = "\\t";
            while ((string = transactionsReader.readLine()) != null) {
                String[] strings = string.split(regex);
                transactionList.add(new Transaction(strings));
            }
        }
        Collections.sort(transactionList);
        shippingWriter=new BufferedWriter(new FileWriter(SHIPPING,StandardCharsets.UTF_8));
        errorWriter=new BufferedWriter(new FileWriter(ERROR,StandardCharsets.UTF_8));
        for(Transaction transaction:transactionList){
            switch (transaction.strings[0]) {
                case "O" -> send(transaction);
                case "A" -> add(transaction);
                case "R" -> increase(transaction);
                case "D" -> remove(transaction);
                default -> System.out.println("Transaction Error");
            }
        }
        shippingWriter.close();
        errorWriter.close();
        try(BufferedWriter inventoryWriter=new BufferedWriter(new FileWriter(NEW_INVENTORY,StandardCharsets.UTF_8))){
            for(Good good:goodMap.keySet()){
                String string = good.itemNumber + "\t" + goodMap.get(good) + "\t" +
                        good.supplier + "\t" + good.description;
                inventoryWriter.write(string);
                inventoryWriter.newLine();
            }
        }
    }
}
