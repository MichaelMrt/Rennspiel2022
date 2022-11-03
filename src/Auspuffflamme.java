import GLOOP.*;
public class Auspuffflamme
{
    

    GLEntwicklerkamera cam;
    GLLicht licht;
    GLPrismoid p;
    static GLQuader[] flammen;
    static int i =0, k=99,faecherung;
    static GLTastatur tas;
    static GLTorus torus;

    public Auspuffflamme()
    {   cam = new GLEntwicklerkamera();
        licht = new GLLicht();
        new GLLicht(100,100,0);
        tas = new GLTastatur();
       // cam.verschiebe(0,100,600);
     //  p= new GLPrismoid(0,0,0,30,1,2,30); // DREIECK
       flammen = new GLQuader[100];
 
    //   torus = new GLTorus(0,0,0,10,1);
    }
   
    public static void main(String[] args){
        new Auspuffflamme();

        while(true){
              if(i<100){
                  faecherung = i;
                  if(i<=10){faecherung = 10/2; }
                    if(i<=20&&i>10){faecherung = 9/2; }
                    if(i<=30&&i>20){faecherung=8/2;}
                    if(i<=40&&i>30){faecherung=7/2;}
                    if(i<=50&&i>40){faecherung=6/2;}
                    if(i<=60&&i>50){faecherung=5/2;}
                    if(i<=70&&i>60){faecherung=4/2;}
                    if(i<=80&&i>70){faecherung=3/2;}
                    if(i<=90&&i>80){faecherung=2/2;}
                    if(i<=100&&i>90){faecherung=1/2;}
                    flammen[i] = new GLQuader(Math.random()*faecherung,Math.random()*faecherung,Math.random()*i/2,3,3,3);
                    flammen[i].setzeFarbe(1,1,0);
                }
                             
                if(i==99){
                 Sys.warte(1000);}
            if(i>99){
               
                flammen[k].loesche();
            
                k--;
                if(k==-1){
                    i=0;
                    k=99;   
                }
            }
            
            if(tas.istGedrueckt(' ')){
                flammen[99].verschiebe(0,1,0);
            }
                   i++;
            Sys.warte();
        }
    }
}
