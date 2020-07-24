/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author drg42
 */
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MySign {
    public static void main(String args[]) throws NoSuchAlgorithmException, IOException{

        if(args[0].charAt(0) == 's'){
            try {
                Path path1 = Paths.get(args[1]);
                byte[] data = Files.readAllBytes(path1);
                int size = data.length;
                
                MessageDigest md = MessageDigest.getInstance("SHA-256");

                // process the file
                md.update(data);
                // generate a hash of the file
                byte[] digest = md.digest();
                //System.out.println(size);

                BigInteger M = new BigInteger(1, digest);

                //System.out.println(M);
                
                Path path2 = Paths.get("privkey.rsa");
                
                File f = path2.toFile();
                if(!f.exists()){
                    System.out.println("Private key file not found in this directory.");
                    System.exit(0);
                }
                
                FileInputStream fis = new FileInputStream(f);
                ObjectInputStream ois = new ObjectInputStream(fis);

                BigInteger D = (BigInteger) ois.readObject();
                BigInteger N = (BigInteger) ois.readObject();
                
                ois.close();
                fis.close();
                
                BigInteger decrypt = M.modPow(D, N);
                
                String filename = path1.getFileName()+".signed";

                File yourFile = new File(filename);
                if(!yourFile.exists()) {
                    yourFile.createNewFile();
                } 
                
                FileOutputStream fos = new FileOutputStream(yourFile);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                
                oos.writeObject(decrypt);
                oos.writeInt(size);    
                oos.write(data);
                
                oos.close();
            }
            catch(Exception e) {
                System.out.println(e.toString());
            }
        }
        else{
            try {
                byte[] data; // = Files.readAllBytes(path1);

                FileInputStream fis = new FileInputStream(args[1]);
                ObjectInputStream ois = new ObjectInputStream(fis);
                
                BigInteger hash = (BigInteger)ois.readObject();
                int size = ois.readInt();
                
                data = new byte[size];
                //System.out.println(size);
                
                for(int i =0; i<size; i++){
                    data[i] = ois.readByte();
                    //System.out.println(data[i]);
                }
                
                ois.close();
                fis.close();
                
                //System.out.println("done for loop");
                
                // create class instance to create SHA-256 hash
                MessageDigest md = MessageDigest.getInstance("SHA-256");

                // process the file
                md.update(data);
                // generate a hash of the file
                byte[] digest = md.digest();
                
                // note that conversion to biginteger will remove any leading 0s in the bytes of the array!
                BigInteger M = new BigInteger(1, digest);

                
                Path path2 = Paths.get("pubkey.rsa");
                
                File f = path2.toFile();
                if(!f.exists()){
                    System.out.println("Public key file not found in this directory.");
                    System.exit(0);
                }
                
                FileInputStream fisV = new FileInputStream(f);
                
                ObjectInputStream oisV = new ObjectInputStream(fisV);
                
                BigInteger E = (BigInteger) oisV.readObject();
                BigInteger N = (BigInteger) oisV.readObject();
                
                oisV.close();
                fisV.close();
                
                BigInteger encryptedM = hash.modPow(E, N);
                
                //System.out.println(M);
                //System.out.println(encryptedM);
                
                if(M.compareTo(encryptedM) == 0){
                    System.out.println("Signature is VALID");
                }
                else{
                    System.out.println("Signature is NOT VALID");
                }
            }
            catch(Exception e) {
                System.out.println(e.toString());
            }
        }
    }
}
