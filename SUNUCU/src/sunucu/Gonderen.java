package sunucu;

import java.util.*;
import java.net.*;
import java.io.*;

public class Gonderen {
    
    public static void main(String[] args) throws Exception{
        
        InetAddress adres=InetAddress.getByName(null);
        
        Socket soket=new Socket(adres,8080);
        
        String mesaj=null,cevap=null;
        
        Scanner klavye=new Scanner(System.in);
        
        try {
            
            BufferedReader in=new BufferedReader(new InputStreamReader(soket.getInputStream()));
            PrintWriter out=new PrintWriter(new BufferedWriter(new 
                        OutputStreamWriter(soket.getOutputStream())),true);
            
           mesaj=klavye.nextLine();
            
            while(true){
                
                if(mesaj == null){
                    System.out.println("------------------------");
                    System.out.println("Mesajlaşma bitti...");
                    System.out.println("------------------------");
                    System.exit(1);
                }
                
                 out.println(mesaj);
                
                cevap=in.readLine();
                
                System.out.println("Yunus : "+cevap);
                
                mesaj=klavye.nextLine();
                
                if(mesaj.equals("0")) mesaj=null;
                
            }
            
        } catch (Exception e) {
            System.out.println("Mesajlaşmada hata oluştu...");
            System.exit(1);
        }finally{
            soket.close();
        }
        
    }
    
}
