import GLOOP.*;
public class Rennwagen
{
    GLQuader karosserie,lampeHR,lampeHL,lampeVR,lampeVL,spL,spR,spoiler,karosserie2,karosserie3,karosserie4,karosserie5,rueckleuchteL,rueckleuchteR,blinkerL,blinkerR;
    GLPrismoid dach1,dach2;
    GLZylinder reifenHR,reifenHL,reifenVR,reifenVL;
    GLVektor yAchse,richtung,ortsvektor,startRichtung,reifenAchse,reifenAchseHintenVek,ovHR,ovHL,ovVR,ovVL,reifenRichtung,ovFlamme;
    GLZylinder auspuff1,auspuff2,reifenAchseHinten,reifenAchseVorne;
    int aufbauzaehler =0, verringerzaehler=99,faecherung,blinkintervall=0;
    static GLQuader[] flammen;

    int geschwindigkeit=0,reifenDrehung=0;
    boolean stehtStill,rueckwaerts,vorwaerts,flammeDa;
    double x,y,z,lenkgeschwindigkeit=0.08;
    public Rennwagen(double pX,double pY,double pZ)
    { 
        x = pX;
        y = pY;
        z = pZ;

        //Vektoren
        yAchse = new GLVektor(0,1,0);               
        richtung = new GLVektor(0,0,0);
        startRichtung = new GLVektor(0,0,-1);
        reifenAchse = new GLVektor(1,0,0);
        reifenRichtung = new GLVektor(0,0,-1);
        reifenAchseHintenVek = new GLVektor(1,0,0);

        //Wagenteile
        karosserie = new GLQuader(x,y+7.5,z,50,15,120);
        karosserie.setzeFarbe(0,0,1);
        dach1 = new GLPrismoid(x,y+23,z+10,30,20,4,16);
        dach1.drehe(90,0,0);      
        dach1.setzeFarbe(0.2,0.2,0.2);
        dach2 = new GLPrismoid(x,y+23,z-10,30,20,4,16);
        dach2.drehe(90,0,0);
        dach2.setzeFarbe(0.2,0.2,0.2);

        lampeHR = new GLQuader(x-15,y,z+57,12,8,10);
        lampeHR.setzeFarbe(0.7,0,0);
        lampeHL = new GLQuader(x+15,y,z+57,12,8,10);
        lampeHL.setzeFarbe(0.7,0,0);

        lampeVR = new GLQuader(x-15,y,z-57,12,8,10);
        lampeVR.setzeFarbe(1,1,0);
        lampeVR.setzeSelbstleuchten(1,1,0);
        lampeVL = new GLQuader(x+15,y,z-57,12,8,10);
        lampeVL.setzeFarbe(1,0,0);
        lampeVL.setzeSelbstleuchten(1,1,0);

        rueckleuchteL = new GLQuader(x-11,y-2.5,z+57.1,4,3,10.1);
        rueckleuchteR = new GLQuader(x+11,y-2.5,z+57.1,4,3,10.1);
        rueckleuchteL.setzeFarbe(0.7,0.7,0.7);
        rueckleuchteR.setzeFarbe(0.7,0.7,0.7);

        blinkerL = new GLQuader(x-17,y-2.5,z+57.1,8,3,10.1);
        blinkerR = new GLQuader(x+17,y-2.5,z+57.1,8,3,10.1);
        blinkerL.setzeFarbe(1,0.2,0);
        blinkerR.setzeFarbe(1,0.2,0);

        spL = new GLQuader(x-15,y+18,z+52,5,8,5);
        spR = new GLQuader(x+15,y+18,z+52,5,8,5);
        spoiler = new GLQuader(x,y+22,z+52,40,2,8);
        spoiler.setzeFarbe(0,1,1);
        spL.setzeFarbe(0,1,1);
        spR.setzeFarbe(0,1,1);

        reifenHL = new GLZylinder(x-25,y-10,z+30,12,5);
        reifenHL.drehe(0,90,0);
        // reifenHL.setzeFarbe(0,0,0);
        reifenHL.setzeTextur("reifen.jpg");
        reifenHR = new GLZylinder(x+25,y-10,z+30,12,5);
        reifenHR.drehe(0,90,0);
        reifenHR.setzeTextur("reifen.jpg");
        reifenVL = new GLZylinder(x-25,y-10,z-30,12,5);
        reifenVL.drehe(0,90,0);
        reifenVL.setzeTextur("reifen.jpg");
        reifenVR = new GLZylinder(x+25,y-10,z-30,12,5);
        reifenVR.drehe(0,90,0);
        reifenVR.setzeTextur("reifen.jpg");   
        karosserie2 = new GLQuader(x,y-5,z,30,15,120); //untere laengere Stueck
        karosserie2.setzeFarbe(0,0,1);       
        karosserie3 = new GLQuader(x,y-5,z+54,50,15,15); //hintere untere Stueck
        karosserie3.setzeFarbe(0,0,1); 
        karosserie4 = new GLQuader(x,y-5,z,50,15,30); //hintere mittlere breite Stueck
        karosserie4.setzeFarbe(0,0,1);      
        karosserie5 = new GLQuader(x,y-5,z-54,50,15,15); //vordere untere Stueck
        karosserie5.setzeFarbe(0,0,1);       
        auspuff1 = new GLZylinder(x-13,y-13,z+50,2,30);
        auspuff2 = new GLZylinder(x-9,y-13,z+50,2,30);
        auspuff1.setzeFarbe(0.5,0.5,0.5);
        auspuff2.setzeFarbe(0.5,0.5,0.5);

        reifenAchseVorne = new GLZylinder(x,y-10,z-30,2,50);
        reifenAchseVorne.drehe(0,90,0);
        reifenAchseHinten = new GLZylinder(x,y-10,z+30,2,50);
        reifenAchseHinten.drehe(0,90,0);

        flammen = new GLQuader[100];
        for(aufbauzaehler=0;aufbauzaehler<100;aufbauzaehler++){ //Erzeugt die Flammen ausm Aufpuff
            faecherung = aufbauzaehler;
            if(aufbauzaehler<=10){faecherung = 10/2; }
            if(aufbauzaehler<=20&&aufbauzaehler>10){faecherung = 9/2; }
            if(aufbauzaehler<=30&&aufbauzaehler>20){faecherung=8/2;}
            if(aufbauzaehler<=40&&aufbauzaehler>30){faecherung=7/2;}
            if(aufbauzaehler<=50&&aufbauzaehler>40){faecherung=6/2;}
            if(aufbauzaehler<=60&&aufbauzaehler>50){faecherung=5/2;}
            if(aufbauzaehler<=70&&aufbauzaehler>60){faecherung=4/2;}
            if(aufbauzaehler<=80&&aufbauzaehler>70){faecherung=3/2;}
            if(aufbauzaehler<=90&&aufbauzaehler>80){faecherung=2/2;}
            if(aufbauzaehler<=100&&aufbauzaehler>90){faecherung=1/2;}
            flammen[aufbauzaehler] = new GLQuader(-13+x+Math.random()*faecherung,-15+y+Math.random()*faecherung,70+z+Math.random()*aufbauzaehler/2,3,3,3);                   
            flammen[aufbauzaehler].setzeFarbe(1,1,0);
            //  flammen[aufbauzaehler].setzeSelbstleuchten(1,1,0);
            flammen[aufbauzaehler].setzeSichtbarkeit(false);
        } 
        aufbauzaehler=0;
    }

    public void fahre(GLVektor pRichtung){        
        //Verschiebt in die Richtung
        if(richtung!=null){
            pRichtung.normiere();
            if(geschwindigkeit>=0){
                pRichtung.multipliziere(geschwindigkeit);
            }else{
                pRichtung.multipliziere(-geschwindigkeit); // Durch die negative Geschwindigkeit würde minus*minus +ergeben,
                //wodurch der Vektor alle zwei Geschwindigkeit++ positiv wird und das Auto in die Falsche Richtung fährt
            }
            pRichtung.multipliziere(0.03); //Soll nicht ganz so schnell fahren
            karosserie.verschiebe(pRichtung);
            dach1.verschiebe(pRichtung);
            dach2.verschiebe(pRichtung);
            lampeHR.verschiebe(pRichtung);
            lampeHL.verschiebe(pRichtung);
            lampeVR.verschiebe(pRichtung);
            lampeVL.verschiebe(pRichtung);
            spL.verschiebe(pRichtung);
            spR.verschiebe(pRichtung);
            spoiler.verschiebe(pRichtung);
            reifenHR.verschiebe(pRichtung);
            reifenHL.verschiebe(pRichtung);
            reifenVR.verschiebe(pRichtung);
            reifenVL.verschiebe(pRichtung);
            karosserie2.verschiebe(pRichtung);
            karosserie3.verschiebe(pRichtung);
            karosserie4.verschiebe(pRichtung);
            karosserie5.verschiebe(pRichtung);
            auspuff1.verschiebe(pRichtung);
            auspuff2.verschiebe(pRichtung);
            reifenAchseVorne.verschiebe(pRichtung);
            reifenAchseHinten.verschiebe(pRichtung);
            rueckleuchteL.verschiebe(pRichtung);
            rueckleuchteR.verschiebe(pRichtung);
            blinkerL.verschiebe(pRichtung);
            blinkerR.verschiebe(pRichtung);
            for(int i=0;i<100;i++){
                flammen[i].verschiebe(pRichtung);
            }
        }
    }

    private void rotiere(double pGrad){ //Um den Wagen zu drehen
        ortsvektor= new GLVektor(karosserie.gibX(),0,karosserie.gibZ());

        karosserie.rotiere(pGrad,yAchse,ortsvektor);
        karosserie2.rotiere(pGrad,yAchse,ortsvektor);
        karosserie3.rotiere(pGrad,yAchse,ortsvektor);
        karosserie4.rotiere(pGrad,yAchse,ortsvektor);
        karosserie5.rotiere(pGrad,yAchse,ortsvektor);
        dach1.rotiere(pGrad,yAchse,ortsvektor);
        dach2.rotiere(pGrad,yAchse,ortsvektor);
        lampeHR.rotiere(pGrad,yAchse,ortsvektor);
        lampeHL.rotiere(pGrad,yAchse,ortsvektor);
        lampeVR.rotiere(pGrad,yAchse,ortsvektor);
        lampeVL.rotiere(pGrad,yAchse,ortsvektor);
        spL.rotiere(pGrad,yAchse,ortsvektor);
        spR.rotiere(pGrad,yAchse,ortsvektor);
        spoiler.rotiere(pGrad,yAchse,ortsvektor);
        reifenHR.rotiere(pGrad,yAchse,ortsvektor);
        reifenHL.rotiere(pGrad,yAchse,ortsvektor);
        reifenVR.rotiere(pGrad,yAchse,ortsvektor);
        reifenVL.rotiere(pGrad,yAchse,ortsvektor);
        auspuff1.rotiere(pGrad,yAchse,ortsvektor);
        auspuff2.rotiere(pGrad,yAchse,ortsvektor);
        reifenAchseVorne.rotiere(pGrad,yAchse,ortsvektor);
        reifenAchseHinten.rotiere(pGrad,yAchse,ortsvektor);
        rueckleuchteL.rotiere(pGrad,yAchse,ortsvektor);
        rueckleuchteR.rotiere(pGrad,yAchse,ortsvektor);
        blinkerR.rotiere(pGrad,yAchse,ortsvektor);
        blinkerL.rotiere(pGrad,yAchse,ortsvektor);
        for(int i=0;i<100;i++){
            flammen[i].rotiere(pGrad,yAchse,ortsvektor);
        }
        //Vektoren
        startRichtung.rotiere(pGrad,yAchse);
        reifenAchse.rotiere(pGrad,yAchse); 
        reifenAchseHintenVek.rotiere(pGrad,yAchse);
    }

    public GLVektor links(){//Nach links fahren
        if(geschwindigkeit!=0){
            richtung.rotiere(lenkgeschwindigkeit,yAchse); 
            this.rotiere(lenkgeschwindigkeit);
        }
        //reifen Einschlagen
        if(reifenDrehung>-450){
            reifenVR.drehe(0,0.1,0);
            reifenVL.drehe(0,0.1,0);
            reifenAchse.drehe(0,0.1,0);
            reifenDrehung--;
        }
        return richtung;
    }

    public GLVektor rechts(){//Nach rechts fahren
        if(geschwindigkeit!=0){
            richtung.rotiere(-lenkgeschwindigkeit,yAchse);
            this.rotiere(-lenkgeschwindigkeit);
        }
        //reifen Einschlagen
        if(reifenDrehung<450){
            reifenVR.drehe(0,-0.1,0);
            reifenVL.drehe(0,-0.1,0);
            reifenAchse.drehe(0,-0.1,0);
            reifenDrehung++;
        }
        return richtung;
    }

    public float gibX(){//Gibt Koordinaten der Karosserie
        return karosserie.gibX();
    }

    public float gibZ(){//Gibt Koordinaten der Karosserie
        return karosserie.gibZ();
    }

    public boolean stehtStill(){ //wird oft in der Rennszene abgefragt
        if(geschwindigkeit==0){stehtStill=true;}
        else{
            stehtStill=false;
        }        

        return stehtStill;
    }

    public boolean faehrtRueckwaerts(){
        if(geschwindigkeit<0){rueckwaerts=true;}
        else{
            rueckwaerts=false;
        }  

        return rueckwaerts;
    }

    public void fahrLos(){ //Lässt den Wagen vorwärts losfahren
        geschwindigkeit=1;
        richtung.addiere(startRichtung);
        rueckleuchteL.setzeSelbstleuchten(0,0,0);
        rueckleuchteR.setzeSelbstleuchten(0,0,0);
    }

    public void fahrRueckwaertsLos(){//Lässt den Wagen rückwärts losfahren
        geschwindigkeit=-1;
        this.bremsLichtAus();
        richtung.subtrahiere(startRichtung);
        rueckleuchteL.setzeSelbstleuchten(1,1,1);
        rueckleuchteR.setzeSelbstleuchten(1,1,1);
    }

    public GLVektor gibRichtung(){  //Liefert die Fahrtrichtung als Vektor 
        return richtung;
    }

    public int gibGeschwindigkeit(){
        return geschwindigkeit;
    }

    public void beschleunige(){
        geschwindigkeit++;   
        if(geschwindigkeit>210){ //210 Topspeed
            geschwindigkeit=210;
        }
        if(geschwindigkeit<0){
            lampeHR.setzeSelbstleuchten(1,0,0);
            lampeHL.setzeSelbstleuchten(1,0,0);
        }else{
            this.bremsLichtAus();
        }
    }

    public void bremse(){
        geschwindigkeit--;  
        lampeHR.setzeSelbstleuchten(1,0,0);
        lampeHL.setzeSelbstleuchten(1,0,0);        
    }

    public void beschleunigeNachHinten(){
        geschwindigkeit--;
    }

    public void bremsLichtAus(){
        lampeHR.setzeSelbstleuchten(0,0,0);
        lampeHL.setzeSelbstleuchten(0,0,0);
    }

    public void ausrollen(){
        if(geschwindigkeit>0){
            geschwindigkeit--;
        }
        if(geschwindigkeit<0){
            geschwindigkeit++;
        }
    }

    public void dreheReifen(){
        ovHR = new GLVektor(reifenHR.gibX(),reifenHR.gibY(),reifenHR.gibZ());
        ovHL = new GLVektor(reifenHL.gibX(),reifenHL.gibY(),reifenHL.gibZ());
        ovVR = new GLVektor(reifenVR.gibX(),reifenVR.gibY(),reifenVR.gibZ());
        ovVL = new GLVektor(reifenVL.gibX(),reifenVL.gibY(),reifenVL.gibZ());

        reifenHR.rotiere(-0.1*geschwindigkeit,reifenAchseHintenVek,ovHR);
        reifenHL.rotiere(-0.1*geschwindigkeit,reifenAchseHintenVek,ovHL);
        reifenVR.rotiere(-0.1*geschwindigkeit,reifenAchse,ovVR);
        reifenVL.rotiere(-0.1*geschwindigkeit,reifenAchse,ovVL); 
    }

    public void geradeReifen(){
        //von rechts eingeschlagen zu gerade
        if(reifenDrehung>0){
            reifenVR.drehe(0,0.1,0);
            reifenVL.drehe(0,0.1,0);
            reifenAchse.drehe(0,0.1,0);
            reifenDrehung--;
        }
        //von links eingeschlagen zu gerade
        if(reifenDrehung<0){
            reifenVR.drehe(0,-0.1,0);
            reifenVL.drehe(0,-0.1,0);
            reifenAchse.drehe(0,-0.1,0);
            reifenDrehung++;
        }
    }

    public void aktualisiere(){
        this.dreheReifen();
        x=karosserie.gibX();
        y=karosserie.gibY();
        z=karosserie.gibZ();
        lenkgeschwindigkeit = -0.00025*geschwindigkeit+0.08; //Der Wert + ist die Wahre lenkgeschwindigkeit! nähert sich also 0.08. Je schneller desto geringer das lenken
        if(lenkgeschwindigkeit<0){
            lenkgeschwindigkeit=0;
        }
        this.flammen();
    }

    public void flammen(){  
        //Sorgt für flammen aus dem Auspuff
        if(geschwindigkeit==30||geschwindigkeit==70||geschwindigkeit==120||geschwindigkeit==180||flammeDa==true){ //laesst die Flammen ausm auspuff erscheinen
            if(aufbauzaehler<99){
                flammeDa=true;
                flammen[aufbauzaehler].setzeSichtbarkeit(true);
            }
            if(aufbauzaehler>99){ //loescht die Flammen ausm Auspuff            
                flammen[verringerzaehler].setzeSichtbarkeit(false);
                verringerzaehler--;
                if(verringerzaehler==-1){
                    aufbauzaehler=0;
                    verringerzaehler=99; 
                    flammeDa=false;
                }           
            }
            aufbauzaehler++;
        }
    }

    public void blinkeLinks(){
        if(blinkintervall>500){
            blinkerL.setzeSelbstleuchten(1,0.2,0);
        }
        if(blinkintervall>1000){
            blinkerL.setzeSelbstleuchten(0,0,0);
            blinkintervall=0;
        }
        blinkintervall++;
    }

    public void blinkeRechts(){
        if(blinkintervall>500){
            blinkerR.setzeSelbstleuchten(1,0.2,0);
        }
        if(blinkintervall>1000){
            blinkerR.setzeSelbstleuchten(0,0,0);
            blinkintervall=0;
        }
        blinkintervall++;
    }

    public double gibLenkgeschwinigkeit(){
        return lenkgeschwindigkeit;
    }
    //Wenn man doch einfach nur die größen von GLQuader abfragen könnte :/

    public boolean pruefeAufStrecke(Gerade[] geradenArray){
        for(int i=0;i<6;i++){
            if(karosserie.gibX()>geradenArray[i].gibX()-geradenArray[i].gibXLaenge()/2&&karosserie.gibX()<geradenArray[i].gibX()+geradenArray[i].gibXLaenge()/2&&karosserie.gibZ()>geradenArray[i].gibZ()-geradenArray[i].gibZLaenge()/2&&karosserie.gibZ()<geradenArray[i].gibZ()+geradenArray[i].gibZLaenge()/2)
            {
                return true;
            }
        }
        return false;  
        //  System.out.println("KarossierieX:"+karosserie.gibX()+" Muss gößer sein als:"+kleinerAls );
        // System.out.println("x Länge" +geradenArray[0].gibXLaenge());
        // if(karosserie.gibX()>geradenArray[0].gibX()-geradenArray[0].gibXLaenge()/2&&karosserie.gibX()<geradenArray[0].gibX()+geradenArray[0].gibXLaenge()/2){
        // return true;
        // }
        // else{return false;}

    }

    public boolean pruefeAufZiellinie(Gerade pGerade){
        if(karosserie.gibX()>pGerade.gibX()-pGerade.gibXLaenge()/2&&karosserie.gibX()<pGerade.gibX()+pGerade.gibXLaenge()/2&&karosserie.gibZ()>pGerade.gibZ()-pGerade.gibZLaenge()/2&&karosserie.gibZ()<pGerade.gibZ()+pGerade.gibZLaenge()/2)
        {return true;}
        else{return false;}
    }

    public boolean pruefeRundenAnforderung(Gerade pGerade){
        if(karosserie.gibX()>pGerade.gibX()-pGerade.gibXLaenge()/2&&karosserie.gibX()<pGerade.gibX()+pGerade.gibXLaenge()/2&&karosserie.gibZ()>pGerade.gibZ()-pGerade.gibZLaenge()/2&&karosserie.gibZ()<pGerade.gibZ()+pGerade.gibZLaenge()/2)
        {return true;}
        else{return false;}
    }
}
