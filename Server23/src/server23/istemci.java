package server23;

import java.io.*;
import java.net.*;
import java.util.*;

public class istemci {
    
    public static void main(String[] args) throws IOException{
        Scanner input=new Scanner(System.in);
        int saldirilimiti = input.nextInt();
        for(int i=0 ; i< saldirilimiti ; i++ ){
            Musteri m= new Musteri();
            m.start();
        }
    }
    
}
class Musteri extends Thread{
    Scanner input=new Scanner(System.in);
    public void run(){
        try {
            InetAddress adres=InetAddress.getByName(null);      // Bulunduğumuz sistemin Ip adresini bize verir.
            Socket soket=new Socket(adres,8080);        // 8080 portuna Adres yani bizim sistemimizden mesaj bırakır.
            boolean hata=false;
            try {
                System.out.println("Soket = " + soket);
                BufferedReader in =new BufferedReader(new InputStreamReader(soket.getInputStream()));
                PrintWriter out=new PrintWriter(new OutputStreamWriter(soket.getOutputStream()),true);
                try {
                    String message = input.nextLine();
                    out.println(message);
                    String str=in.readLine();
                }
                catch (Exception e) {
                    hata=true;
                }
                if(!hata){
                    System.out.println(this.getName()+"Ben sunucuya bağlandım...");
                }
            }
            finally{
                System.out.println("Bağlantı kapatılıyor...");
                soket.close();
            }
        }
        catch (Exception e) {
            System.err.println(this.getName()+" Hata oluştu...");
        }
    }
}