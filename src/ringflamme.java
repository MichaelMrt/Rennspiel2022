import GLOOP.*;
public class ringflamme
{
    

    GLEntwicklerkamera cam;
    GLLicht licht;
    GLPrismoid p;
    static GLZylinder[] flammen;
    static int i =0, k=99,faecherung;
    static double d = 4;
    static GLTastatur tas;
    static GLTorus torus;
    public ringflamme()
    {   cam = new GLEntwicklerkamera();
        licht = new GLLicht();
        new GLLicht(100,100,0);
        tas = new GLTastatur();
       // cam.verschiebe(0,100,600);
     //  p= new GLPrismoid(0,0,0,30,1,2,30); // DREIECK
       flammen = new GLZylinder[100];
       torus = new GLTorus(0,0,0,10,1);
    }
   
    public static void main(String[] args){
        new ringflamme();
        while(true){
              if(i<100){
                  faecherung = i;
                 
                    flammen[i] = new GLZylinder(0,0,+i,d,1);
                    flammen[i].setzeFarbe(1,1,0);
                   d= d-0.05;
                   if(d<0){
                    d=0;}
                }
                             
                if(i==99){
                 Sys.warte(1000);}
            if(i>99){
               
                flammen[k].loesche();
            
                k--;
                if(k==-1){
                    i=0;
                    k=99;  
                    d=4;
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
