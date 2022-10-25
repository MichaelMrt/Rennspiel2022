import GLOOP.*;

public class Map
{
    //Erstellt die Rennstrecke,könnte auch noch Bäume und weitere Strukturen erstellen und platzieren
    public Gerade meineGerade1,meineGerade2,meineGerade3,meineGerade4,meineGerade5,meineGerade6,meineGerade7;
    int hoehe;
    Gerade geradenArray[];
    public Map(int pHoehe)
    {   hoehe = pHoehe;
        //Geraden der Rennstrecke
        meineGerade1= new Gerade(0,hoehe,-10_000,1300,1,20_000);
        meineGerade2 = new Gerade(4350,hoehe,-20_000,10_000,1,1300);
        meineGerade3 = new Gerade(10_000,hoehe,-15_650,1300,1,10_000);
        meineGerade4 = new Gerade(8157,hoehe,-10000,5000,1,1300);
        meineGerade5 = new Gerade(5015,hoehe,-5348,1300,1,10600);
        meineGerade6 = new Gerade(2508,hoehe,584,6300,1,1300);

        meineGerade7 = new Gerade(0,5,-2600,1300,1,200);//Ziellinie
        meineGerade7.setzeFarbe(1,1,1);
        meineGerade7.setzeTextur("checkeredflag.jpg");
        //Geraden in einen Array packen
        geradenArray = new Gerade[6];
        geradenArray[0] = meineGerade1;
        geradenArray[1] = meineGerade2;
        geradenArray[2] = meineGerade3;
        geradenArray[3] = meineGerade4;
        geradenArray[4] = meineGerade5;
        geradenArray[5] = meineGerade6;

    }

    public Gerade[] gibGeradenArray(){
        return geradenArray;
    }

    public Gerade gibZiellinie(){
        return meineGerade7;
    }

    public Gerade gibGerade4(){
        return meineGerade4;
    }
}
