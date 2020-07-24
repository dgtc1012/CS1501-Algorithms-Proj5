/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author drg42
 */
import java.math.BigInteger;
import java.util.*;
import java.io.*;

public class MyKeyGen {
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        Random rand = new Random(); // generate a random number
        BigInteger P = new BigInteger(512, 10, rand);
        BigInteger Q = new BigInteger(512, 10, rand);

        BigInteger one = new BigInteger("1");
        BigInteger N = Q.multiply(P); 
        
        byte[] n = N.toByteArray();
        while(n.length != 1024){
            P = new BigInteger(512, 10, rand);
            Q = new BigInteger(512, 10, rand);

            N = Q.multiply(P); 
            n = N.toByteArray();
        }
        
        BigInteger phiN = (P.subtract(one)).multiply(Q.subtract(one));
        
        BigInteger E = new BigInteger(1024, rand);
        while(E.compareTo(phiN) != -1){
            //System.out.println("E: "+E);
            E=new BigInteger(1024, rand);    
        }
        BigInteger i = E.gcd(phiN);
        //System.out.println("enter loop");
        while((i.compareTo(one) != 0)){
            E=new BigInteger(1024, rand);
            while(E.compareTo(phiN) != -1){
                E=new BigInteger(1024, rand);    
            }
            //System.out.println("in loop");
            i = E.gcd(phiN);
            
        }
        /*if((E.compareTo(phiN)!=-1)){
            E=one;
        }*/
        
        BigInteger D = E.modInverse(phiN);
        
        FileOutputStream fosPub = new FileOutputStream("pubkey.rsa");
        ObjectOutputStream oosPub = new ObjectOutputStream(fosPub);
        
        oosPub.writeObject(E);
        oosPub.writeObject(N);
        
        oosPub.close();
        fosPub.close();
        
        FileOutputStream fosPriv = new FileOutputStream("privkey.rsa");
        ObjectOutputStream oosPriv = new ObjectOutputStream(fosPriv);
        
        oosPriv.writeObject(D);
        oosPriv.writeObject(N);
        
        oosPriv.close();
        fosPriv.close();
        /*
        PrintWriter writer = new PrintWriter("pubkey.rsa", "UTF-8");
        writer.println(E + "\n" + N + "\n");
        writer.close();
        
        writer = new PrintWriter("privkey.rsa", "UTF-8");
        writer.println(D + "\n" + N + "\n");
        writer.close();
    */
    }

    /**
     * Checks to see if the requested value is prime.
     */
    /*static boolean isPrime(int inputNum){
        if (inputNum <= 3 || inputNum % 2 == 0) 
            return inputNum == 2 || inputNum == 3; //this returns false if number is <=1 & true if number = 2 or 3
        int divisor = 3;
        while ((divisor <= Math.sqrt(inputNum)) && (inputNum % divisor != 0)) 
            divisor += 2; //iterates through all possible divisors
        return inputNum % divisor != 0; //returns true/false
    }*/
    
    /*static BigInteger gcd(BigInteger a, BigInteger b){
      if(a.equals(new BigInteger("0")) || b.equals(new BigInteger("0"))) return a.add(b); // base case
      return gcd(b,a.mod(b));
    }*/
}
