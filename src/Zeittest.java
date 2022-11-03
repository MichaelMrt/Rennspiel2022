import GLOOP.*;
import java.text.DecimalFormat;

public class Zeittest
{
    

    GLEntwicklerkamera cam;
    GLLicht licht;
    static GLTastatur tas;
    static GLTafel tafel1, tafel2,tafel3;
    static long startZeit,startZeit2,aktuelleZeit, aktuelleZeit2;
    static double zeit;
   private static final DecimalFormat dfZero = new DecimalFormat("0.000");
    public Zeittest()
    {   cam = new GLEntwicklerkamera();
        licht = new GLLicht();
        new GLLicht(100,100,0);
        tas = new GLTastatur();
        
        tafel1 = new GLTafel(-250,0,0,50,50);
        tafel2 = new GLTafel(250,0,0,50,50);
        tafel3 = new GLTafel(0,0,0,50,50);
          
    }
   
    public static void main(String[] args){
        new Zeittest();
         startZeit = System.nanoTime();
         startZeit2 = System.currentTimeMillis();
         while(true){
            while(!tas.istGedrueckt(' ')){
                aktuelleZeit = (System.nanoTime()-startZeit);
                aktuelleZeit2 = System.currentTimeMillis()-startZeit2;
                tafel1.setzeText(Long.toString(aktuelleZeit/1000000000)+" "+"sek",7);
                tafel2.setzeText(Long.toString(aktuelleZeit/1000000)+" "+"sek",7);
                
                aktuelleZeit2 = aktuelleZeit/1000000;
                zeit = (double)aktuelleZeit2;
                zeit= zeit/1000;
               //TRENN
              
                  
                tafel3.setzeText(dfZero.format(zeit)+" "+"sek",7);
                
              
                
                Sys.warte();
            }
    }
}
}
