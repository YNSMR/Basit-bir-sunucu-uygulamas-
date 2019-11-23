package sunucu;

import java.util.*;
import java.io.*;
import java.net.*;

public class Alici {
    
    public static final int PORT=8080;
    
    public static void main(String[] args) throws Exception{
        
        ServerSocket alicisoketi=new ServerSocket(PORT);
        
        String mesaj=null,cevap=null;
        
        Scanner klavye=new Scanner(System.in);
        
        try {
            
            Socket soket=alicisoketi.accept();
            
            BufferedReader in=new BufferedReader(new InputStreamReader(soket.getInputStream()));
            PrintWriter out=new PrintWriter(new BufferedWriter(new 
                                OutputStreamWriter(soket.getOutputStream())),true);
            
            try {
                
                while( true ){
                    
                    mesaj=in.readLine();
                    
                    if(mesaj == null){
                        System.exit(1);
                    }
                    
                    System.out.println("Ali : " +mesaj);
                    
                    cevap=klavye.nextLine();
                    
                    if(cevap.equals("0")){
                        cevap="Mesajlaşma bitti...";
                        mesaj=null;
                    }
                    
                    out.println(cevap);
                    
                }
                
            } finally{
                soket.close();
            }
            
        } finally{
            alicisoketi.close();
        }
        
    }
    
}
