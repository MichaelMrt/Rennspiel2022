import GLOOP.*;
public class Gerade
{   
    //Muss diese Klasse einführen, damit ich bei der Prüfung, ob das Auto auf der Strecke ist die jeweiligen größen der Geraden abfragen kann.
    //GLQuader kann keine breite, höhe, weite übergeben
    GLQuader quader;
    int xLaenge1,zLaenge1;
    public Gerade(int pX,int pY, int pZ,int xLaenge, int yLaenge, int zLaenge)
    {
        xLaenge1 = xLaenge;
        zLaenge1 = zLaenge;
        quader = new GLQuader(pX,pY,pZ,xLaenge,yLaenge,zLaenge);
        quader.setzeFarbe(0,0,0);
    }

    public double gibX(){
        return  quader.gibX();
    }

    public double gibZ(){
        return  quader.gibZ();
    }

    public int gibXLaenge(){
        return xLaenge1;
    }

    public int gibZLaenge(){
        return zLaenge1;
    }

    public void setzeTextur(String pDateiname){
        quader.setzeTextur(pDateiname);
    }

    public void setzeFarbe(int r,int g, int b){
        quader.setzeFarbe(r,g,b);
    }

}
