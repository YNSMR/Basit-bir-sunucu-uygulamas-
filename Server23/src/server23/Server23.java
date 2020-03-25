package server23;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server23 {
    public static final int PORT = 8080;
    public ArrayList havuz = new ArrayList();
    public void basla(int havuzlimiti) throws IOException{
        ServerSocket sunucusoketi = new ServerSocket(PORT);
        System.out.println("Sunucu Basladi : " + sunucusoketi);
        for(int i=0;i<havuzlimiti ; i++ ){                      // Havuza Koyulacak iş parçacıklarını oluşturuyoruz.
            Garson g=new Garson(havuz); 
            g.start();          // Kullanıcıyı başlat.
            havuz.add(g);
            System.out.println( g + " havuza eklendi...");
        }
        try {
            while(true) {        // Sonsuza kadar dinle.               
                //  Yeni bir bağlantı gelene kadar bekle...
                Socket kullanicisoketi = sunucusoketi.accept();
                synchronized (havuz){
                    if(havuz.isEmpty()){
                        // Bu kullanıcıya cevap verilmeyecek...
                        System.out.println("Yeni istek geldi ama boşta hiç garson yok...");
                        kullanicisoketi.close();
                    }else{
                        System.out.println("Garson bu istekle ilgilenecek");
                        Garson g=(Garson) havuz.get(0);         //  Havuzdan Bir Thread getirilecek...
                        havuz.remove(0);        //  Getirilen Thread havuzdan silinecek...
                        kullanicisoketi.setSoTimeout(5000);         // 5 Saniye zaman aşımı gerçekleşecek...
                        g.soketBelirt(kullanicisoketi);             // havuzdaki boş Thread işlem için belirtilecek...
                    }
                }
            }
        }
        catch (IOException e) {
            System.err.println("Ana yapida hata var : " + e);
        }finally{
            sunucusoketi.close();
        }
    }
    public static void main(String[] args) throws IOException {
        Scanner input=new Scanner(System.in);
        System.out.println("Thread Sayısını belirtiniz...");
        int havuzlimiti = input.nextInt();
        Server23 sunucu=new Server23();
        sunucu.basla(havuzlimiti);
    }
}
class Garson extends Thread{
    Scanner input=new Scanner(System.in);
    public Socket soket=null;      //   bağlantı için kullanılır.
    public ArrayList havuz=null;    //   İş parçacıklarını barındıran Havuz
    public Garson(ArrayList havuz){
        this.havuz=havuz;
    }
    public synchronized void soketBelirt(Socket s){
        this.soket=s;
        notify();       //      Havuzda bulunan boş iş parcacığı varsa onu alır , getirir ve soket e eşitler.
    }
    public String islemyap(String message){
        System.out.println("İstemciden gelen mesaj : " + message);
        System.out.println("Cevap yaz : ");
        String cevap=input.nextLine();
        return cevap;
    }
    @Override
    public synchronized void run(){
        while(true) {            
            try {
                if(soket != null){
                    // Soket yoksa kullanıcı da yoktur ,  Bu nedenle bekle.
                    try {
                        wait();
                    }
                    catch (InterruptedException e) {
                        continue;
                    }
                }
                System.out.println("Baglanti alan is parcacigi : " + this.getName() + 
                        " port numarası = " + soket);
                BufferedReader in=new BufferedReader(new InputStreamReader(soket.getInputStream()));        // Sunucuya gelen mesajları okur.
                PrintWriter out=new PrintWriter(new OutputStreamWriter(soket.getOutputStream()),true);      // istemciye mesajları gönderir.
                
                //Kullanıcıdan gelen mesajları okumaya başla.
                
                String str=in.readLine();   // Kullanıcıdan mesajı oku.
                while(str != null){
                    String ifade = islemyap(str);       //  Mesajı gör ve cevabı yaz.
                    out.println(ifade);                 //  Cevabını istemciye ilet.
                    str=in.readLine();                  //  mesajı tekrar oku.
                }
            }
            catch (Exception e) {
                System.err.println("Hata : " + this.getName() + "  -->  " + e);
            }finally{
                try {
                    soket.close();                  // Açılan soket kapatılmak zorunda.
                    soket=null;
                    havuz.add(this);          // İşi biten Thread ı tekrar havuza koy.
                }
                catch (IOException e) {
                    System.err.println("Socket kapatılamadı : " + this.getName() + "  -->  " + e);
                }
            }
        }
    }
}