import GLOOP.*;
import java.text.DecimalFormat;
import java.io.*;
//import java.util.Scanner;
//import java.io.FileOutputStream;
public class Spielverwalter
{
    static GLKamera cam;
    static GLLicht licht;
    static Rennwagen wagen;
    static GLTastatur tas;
    static GLVektor ortsvektor,yAchse;
    static GLBoden boden;
    static GLHimmel himmel;
    static GLTafel geschwTafel,rundenzeitTafel,menueTafel1,menueTafel2,menueTafel3,menueTafel4,letzteRundenzeitTafel,besteRundenzeitTafel,besteRundenzeitTafelPlatz1,besteRundenzeitTafelPlatz2,besteRundenzeitTafelPlatz3,bestenlisteTafel,menueTafel5,namenTafel,menueTafel6,fahrerTafel;
    static double rotierGeschwindigkeit,vergangeneZeit,zeitP1=0,zeitP2=0,zeitP3=0,kameraDrehung,kameraDrehgeschwindigkeit;//p1 = Platz1
    static long startZeit,aktuelleZeit,besteRundenzeit=0,besteRundenzeitPlatz1=0,besteRundenzeitPlatz2=0,besteRundenzeitPlatz3=0; 
    static int zaehler=0,reifenZaehler=0,wartezahl=0;
    static Map map;
    static boolean gueltigeRunde,rundenAnforderungErfuellt;
    private static final DecimalFormat dfZero = new DecimalFormat("0.000");
    static String fahrer = "",fahrerP1="",fahrerP2="",fahrerP3="";
    static GLTerrain terrain;

    public static void main(String[] args){
        new Spielverwalter();
        hauptschleife();
    }

    public Spielverwalter(){
        cam = new GLKamera();
        licht = new GLLicht();
        himmel = new GLHimmel("himmel.jpg");    
        wagen= new Rennwagen(0,25,0);
        tas = new GLTastatur();
        //   richtung = new GLVektor(0,0,0); //Fahrgeschwindigkeit
        boden = new GLBoden("wiese.jpg");
        yAchse = new GLVektor (0,1,0);
        cam.verschiebe(0,200,0);
        rotierGeschwindigkeit = 0.1;
        geschwTafel = new GLTafel(350,25,125,30,15);
        geschwTafel.setzeAutodrehung(true);
        rundenzeitTafel = new GLTafel(-320,25,140,90,15);
        rundenzeitTafel.setzeAutodrehung(true);
        map = new Map(0);
       
        menueTafel6 = new GLTafel(0,250,200,300,50);
        menueTafel6.setzeText("Enter drücken zum bestätigen",10);
        letzteRundenzeitTafel = new GLTafel(-310,380,140,90,15);
        letzteRundenzeitTafel.setzeAutodrehung(true);
        letzteRundenzeitTafel.setzeText("Letzte Rundenzeit: /",7);
        besteRundenzeitTafel = new GLTafel(-310,370,140,90,15);
        besteRundenzeitTafel.setzeAutodrehung(true);
        besteRundenzeitTafel.setzeText("Beste Rundenzeit: /",7);
        besteRundenzeitTafelPlatz1 = new GLTafel(290,380,140,120,15);
        besteRundenzeitTafelPlatz1.setzeText("Platz 1:",7);
        besteRundenzeitTafelPlatz2 = new GLTafel(290,370,140,120,15);
        besteRundenzeitTafelPlatz2.setzeText("Platz 2:",7);
        besteRundenzeitTafelPlatz3 = new GLTafel(290,360,140,120,15);
        besteRundenzeitTafelPlatz3.setzeText("Platz 3:",7);
        bestenlisteTafel = new GLTafel(290,390,140,100,15);
        bestenlisteTafel.setzeText("Bestenliste",10);
        bestenlisteTafel.setzeAutodrehung(true);
        menueTafel5 = new GLTafel(0,300,200,300,50);
        menueTafel5.setzeText("Namen Eingeben",10);
        namenTafel = new GLTafel(0,280,200,300,50);
        namenTafel.setzeText("Eingabe",10);
        fahrerTafel = new GLTafel(-310,390,140,90,15);
        startZeit = System.nanoTime(); // Speichert die Zeit der Programmöffnung    
        ortsvektor = new GLVektor(wagen.gibX(),0,wagen.gibZ());
        fahrerTafel.setzeAutodrehung(true);
        // Scanner sc = new Scanner(System.in);
        // System.out.println("bitte um Eingabe");
        // String test = sc.nextLine();
        // System.out.println("Output:"+test);
        //  sc.close();      
        try{
            // Öffnen der Datei
            FileInputStream saveFile = new FileInputStream("besteZeitenSpeicher.sav");        
            // Objekte aus der Datei entnehmen
            ObjectInputStream save = new ObjectInputStream(saveFile);
            //Werte auslesen
            fahrerP1 = (String) save.readObject();
            besteRundenzeitPlatz1 = (long) save.readObject();
            zeitP1 = (double) save.readObject();

            fahrerP2 = (String) save.readObject();
            besteRundenzeitPlatz2 = (long) save.readObject();
            zeitP2 = (double) save.readObject();  

            fahrerP3= (String) save.readObject();
            besteRundenzeitPlatz3 = (long) save.readObject();
            zeitP3 = (double) save.readObject();  

            save.close(); // Datei schließen
        }
        catch(Exception exc){
            exc.printStackTrace(); //Fehler werden angezeigt
        }        
        //Nachdem die Daten ausgelesen wurden werden die Tafeln aktualisiert
        besteRundenzeitTafelPlatz1.setzeText("Platz 1: "+fahrerP1+" "+dfZero.format(zeitP1)+" sek",7);
        besteRundenzeitTafelPlatz2.setzeText("Platz 2: "+fahrerP2+" "+dfZero.format(zeitP2)+" sek",7);
        besteRundenzeitTafelPlatz3.setzeText("Platz 3: "+fahrerP3+" "+dfZero.format(zeitP3)+" sek",7);
    }

    public static void hauptschleife(){
        aktuelleZeit = (System.nanoTime()-startZeit)/1000000000; //Zeit wird reseted 
        while(!tas.enter()){ //Vormenue1 zum Namen eingeben
            namenEingabe(); //Eine lange Methode die im Endeffekt nur überprüft ob es einen Input gibt und diese auf die Tafel bringt
            namenTafel.setzeText(fahrer,10);
            geschwTafel.setzeText(Integer.toString(wagen.gibGeschwindigkeit())+" "+"km/h", 10); //Zeit wird aktualisiert auf der Tafel
            double kameraDrehgeschwindigkeit = 0.05;
            //Kamera rotation für einen schönen Rundumblick,während man den Namen eingibt
            cam.rotiere(kameraDrehgeschwindigkeit, yAchse, ortsvektor);
            menueTafel5.rotiere(kameraDrehgeschwindigkeit, yAchse, ortsvektor);
            menueTafel6.rotiere(kameraDrehgeschwindigkeit, yAchse, ortsvektor);
            letzteRundenzeitTafel.rotiere(kameraDrehgeschwindigkeit, yAchse, ortsvektor);
            bestenlisteTafel.rotiere(kameraDrehgeschwindigkeit, yAchse, ortsvektor);
            besteRundenzeitTafel.rotiere(kameraDrehgeschwindigkeit, yAchse, ortsvektor);
            besteRundenzeitTafelPlatz1.rotiere(kameraDrehgeschwindigkeit, yAchse, ortsvektor);
            besteRundenzeitTafelPlatz2.rotiere(kameraDrehgeschwindigkeit, yAchse, ortsvektor);
            besteRundenzeitTafelPlatz3.rotiere(kameraDrehgeschwindigkeit, yAchse, ortsvektor);
            namenTafel.rotiere(kameraDrehgeschwindigkeit, yAchse, ortsvektor);
            geschwTafel.rotiere(kameraDrehgeschwindigkeit, yAchse, ortsvektor);
            rundenzeitTafel.rotiere(kameraDrehgeschwindigkeit, yAchse, ortsvektor);
            fahrerTafel.rotiere(kameraDrehgeschwindigkeit, yAchse, ortsvektor);
            kameraDrehung= kameraDrehung+0.05;
            Sys.warte();
        }  
        //Kamera wird zurückgedreht zum Ursprungspunkt
        kameraDrehung = kameraDrehung*(-1);
        cam.rotiere(kameraDrehung, yAchse, ortsvektor);
        menueTafel5.rotiere(kameraDrehung, yAchse, ortsvektor);
        menueTafel6.rotiere(kameraDrehung, yAchse, ortsvektor);
        letzteRundenzeitTafel.rotiere(kameraDrehung, yAchse, ortsvektor);
        bestenlisteTafel.rotiere(kameraDrehung, yAchse, ortsvektor);
        besteRundenzeitTafel.rotiere(kameraDrehung, yAchse, ortsvektor);
        besteRundenzeitTafelPlatz1.rotiere(kameraDrehung, yAchse, ortsvektor);
        besteRundenzeitTafelPlatz2.rotiere(kameraDrehung, yAchse, ortsvektor);
        besteRundenzeitTafelPlatz3.rotiere(kameraDrehung, yAchse, ortsvektor);
        namenTafel.rotiere(kameraDrehung, yAchse, ortsvektor);
        geschwTafel.rotiere(kameraDrehung, yAchse, ortsvektor);
        rundenzeitTafel.rotiere(kameraDrehung, yAchse, ortsvektor);
        fahrerTafel.rotiere(kameraDrehung, yAchse, ortsvektor);
        //Nicht mehr benötigte Tafeln werden gelöscht
        namenTafel.loesche();
        menueTafel5.loesche();
        menueTafel6.loesche();
        fahrerTafel.setzeText(fahrer,8);
        menueTafel1 = new GLTafel(0,300,200,300,50);
        menueTafel1.setzeText("W zum beschleunigen",10);
        menueTafel2 = new GLTafel(0,250,200,300,50);
        menueTafel2.setzeText("S zum bremsen",10);
        menueTafel3 = new GLTafel(0,200,200,300,50);
        menueTafel3.setzeText("A und D zum lenken",10);
        menueTafel4 = new GLTafel(0,150,200,300,50);
        menueTafel4.setzeText("Zum starten Leertaste drücken",15);
        Sys.warte(200);
        while(!tas.istGedrueckt(' ')){ //Vormenue2 Steuerung. Es geht weiter sobald Leertaste gedrückt wird
            startZeit = System.nanoTime(); //Startzeit wird auf 0 gehalten
            geschwTafel.setzeText(Integer.toString(wagen.gibGeschwindigkeit())+" "+"km/h", 10); //Zeit wird aktualisiert auf der Tafel
        }         
        //Erklärungstafeln werden gelöscht
        menueTafel1.loesche();
        menueTafel2.loesche();
        menueTafel3.loesche();
        menueTafel4.loesche();   
        while(true){ //Hauptschleife
            zaehler++;
            zaehler++; //Zaehlt die Schleifendurchgänge zur verlangsamung von zb Beschleunigung ohne Lag
            reifenZaehler++; //zählt extra für die Reifen, da sich die zaehler sonst ueberschneiden
            geschwTafel.setzeText(Integer.toString(wagen.gibGeschwindigkeit())+" "+"km/h", 10); // Aktualisiert die Geschwindigkeit auf der Tafel
            aktuelleZeit = (System.nanoTime()-startZeit)/1000000; //aktualisiert die aktuelle Zeit
            /** Für Sekunden /1000000000 !!! */
          rotierGeschwindigkeit = wagen.gibLenkgeschwinigkeit(); // kamera an lenkgeschwindigkeit anpassen
            wagen.aktualisiere(); //aktualisiert bestimmte Parameter des wagens und achtet auf zb die Auspuffflammen
            if(wagen.gibRichtung()!=null){ //Alle nötigen Objekte und der Wagen werden gefahren
                cam.verschiebe(wagen.gibRichtung());
                geschwTafel.verschiebe(wagen.gibRichtung());
                rundenzeitTafel.verschiebe(wagen.gibRichtung());
                wagen.fahre(wagen.gibRichtung());
                letzteRundenzeitTafel.verschiebe(wagen.gibRichtung());
                besteRundenzeitTafel.verschiebe(wagen.gibRichtung());
                besteRundenzeitTafelPlatz1.verschiebe(wagen.gibRichtung());
                besteRundenzeitTafelPlatz2.verschiebe(wagen.gibRichtung());
                besteRundenzeitTafelPlatz3.verschiebe(wagen.gibRichtung());
                bestenlisteTafel.verschiebe(wagen.gibRichtung());
                fahrerTafel.verschiebe(wagen.gibRichtung());
            }    

            if(tas.istGedrueckt('w')){ //Vorwaehrts fahren
                if(wagen.stehtStill()==true&&zaehler>=100){ //Sorgt für das losfahren
                    wagen.fahrLos();
                    zaehler=0;
                }
                //Vorwaerts beschleunigen
                if(wagen.stehtStill()==false&&zaehler>=50){
                    wagen.beschleunige();
                    zaehler=0;
                }  
            }

            if(tas.istGedrueckt('s')){ //Rueckwaerts fahren   
                //Rueckwaerts losfahren
                if(wagen.stehtStill()==true&&zaehler>=100){
                    wagen.fahrRueckwaertsLos();
                    zaehler=0;
                }   
                //bremsen 
                if(wagen.stehtStill()==false&&wagen.faehrtRueckwaerts()==false&&zaehler>=40){
                    wagen.bremse();
                    zaehler=0;
                }   
                //nach hinten beschleunigen
                if(wagen.faehrtRueckwaerts()==true&&zaehler>=100){
                    wagen.beschleunigeNachHinten();
                    zaehler=0;
                }
            }
            //Wagen nach rechts drehen
            if(tas.istGedrueckt('d')){
                wagen.rechts();
                ortsvektor = new GLVektor(wagen.gibX(),0,wagen.gibZ());
                if(wagen.gibGeschwindigkeit()!=0){ //Wagen darf sich nur drehen wenn die Geschwindigkeit nicht 0 ist
                    cam.rotiere(-rotierGeschwindigkeit,yAchse,ortsvektor);
                    geschwTafel.rotiere(-rotierGeschwindigkeit,yAchse,ortsvektor);
                    rundenzeitTafel.rotiere(-rotierGeschwindigkeit,yAchse,ortsvektor);
                    letzteRundenzeitTafel.rotiere(-rotierGeschwindigkeit,yAchse,ortsvektor);
                    besteRundenzeitTafel.rotiere(-rotierGeschwindigkeit,yAchse,ortsvektor);
                    besteRundenzeitTafelPlatz1.rotiere(-rotierGeschwindigkeit,yAchse,ortsvektor);
                    besteRundenzeitTafelPlatz2.rotiere(-rotierGeschwindigkeit,yAchse,ortsvektor);
                    besteRundenzeitTafelPlatz3.rotiere(-rotierGeschwindigkeit,yAchse,ortsvektor);
                    bestenlisteTafel.rotiere(-rotierGeschwindigkeit,yAchse,ortsvektor);
                    fahrerTafel.rotiere(-rotierGeschwindigkeit,yAchse,ortsvektor);
                }
            }
            //Wagen nach links drehen
            if(tas.istGedrueckt('a')){
                wagen.links();
                ortsvektor = new GLVektor(wagen.gibX(),0,wagen.gibZ());
                if(wagen.gibGeschwindigkeit()!=0){ //Wagen darf sich nur drehen wenn die Geschwindigkeit nicht 0 ist
                    cam.rotiere(rotierGeschwindigkeit,yAchse,ortsvektor);
                    geschwTafel.rotiere(rotierGeschwindigkeit,yAchse,ortsvektor);
                    rundenzeitTafel.rotiere(rotierGeschwindigkeit,yAchse,ortsvektor);
                    letzteRundenzeitTafel.rotiere(rotierGeschwindigkeit,yAchse,ortsvektor);
                    besteRundenzeitTafel.rotiere(rotierGeschwindigkeit,yAchse,ortsvektor);
                    besteRundenzeitTafelPlatz1.rotiere(rotierGeschwindigkeit,yAchse,ortsvektor);
                    besteRundenzeitTafelPlatz2.rotiere(rotierGeschwindigkeit,yAchse,ortsvektor);
                    besteRundenzeitTafelPlatz3.rotiere(rotierGeschwindigkeit,yAchse,ortsvektor);
                    bestenlisteTafel.rotiere(rotierGeschwindigkeit,yAchse,ortsvektor);
                    fahrerTafel.rotiere(rotierGeschwindigkeit,yAchse,ortsvektor);
                }
            } 
            if(!tas.istGedrueckt('s')&&!tas.istGedrueckt('w')){ //reguliert die Bremslichter
                wagen.bremsLichtAus();
            }
            if(!tas.istGedrueckt('w')&&!tas.istGedrueckt('s')&&zaehler>=300){ //Lässt den Wagen bei nichtstun ausrollen, die Geschwindigkeit nimmt also ab
                wagen.ausrollen();
                zaehler=0;
            }
            if(!tas.istGedrueckt('d')&&!tas.istGedrueckt('a')&&reifenZaehler>=1){//Macht die Reifen gerade,wenn nicht gelenkt wird          
                wagen.geradeReifen();
                reifenZaehler=0;
            }     
            if(tas.links()){
                wagen.blinkeLinks();
            }
            if(tas.rechts()){
                wagen.blinkeRechts();
            }
            if(wagen.pruefeRundenAnforderung(map.gibGerade4())){ //Wenn die Anforderung eine gültige Runde zu setzten erfüllt wurde
                rundenAnforderungErfuellt=true;
            }

            if(wagen.pruefeAufZiellinie(map.gibZiellinie())){ // Wenn der Wagen über das Ziel fährt
                if(rundenAnforderungErfuellt&&gueltigeRunde){ 
                    //Rundenzeit formatieren und die Tafel aktualisieren
                    vergangeneZeit= (double)aktuelleZeit;
                    vergangeneZeit= vergangeneZeit/1000;
                    letzteRundenzeitTafel.setzeText("Letze Rundenzeit:"+dfZero.format(vergangeneZeit)+" "+" sek",7);
                    if(besteRundenzeit==0||aktuelleZeit<besteRundenzeit){ //Beste Rundenzeit der Session
                        besteRundenzeit=aktuelleZeit;
                        besteRundenzeitTafel.setzeText("beste Rundenzeit:"+dfZero.format(vergangeneZeit)+" "+" sek",7);
                    }
                    //Beste Rundenzeit allgemein
                    if(aktuelleZeit<besteRundenzeitPlatz1||besteRundenzeitPlatz1==0){ //Bestenliste Platz1
                        //Alter P2 muss zu neuen P3 werden
                        besteRundenzeitPlatz3 = besteRundenzeitPlatz2;
                        zeitP3 = zeitP2;
                        fahrerP3 = fahrerP2;
                        besteRundenzeitTafelPlatz3.setzeText("Platz 3: "+fahrerP3+" "+dfZero.format(zeitP3)+" sek",7);

                        //Alter P1 muss zu neuen P2 werden
                        besteRundenzeitPlatz2 = besteRundenzeitPlatz1;
                        zeitP2 = zeitP1;
                        fahrerP2 = fahrerP1;
                        besteRundenzeitTafelPlatz2.setzeText("Platz 2: "+fahrerP2+" "+dfZero.format(zeitP2)+" sek",7);

                        //Neue P1 Rundenzeit anpassen
                        besteRundenzeitPlatz1 = aktuelleZeit;
                        zeitP1 = (double)besteRundenzeitPlatz1;
                        zeitP1 = zeitP1/1000;
                        fahrerP1 = fahrer;   
                        //Auf die Tafel aktualisieren
                        besteRundenzeitTafelPlatz1.setzeText("Platz 1: "+fahrerP1+" "+dfZero.format(zeitP1)+" sek",7);
                    }   else{
                        if(aktuelleZeit<besteRundenzeitPlatz2||besteRundenzeitPlatz2==0){ //Bestenliste Platz2
                            //Alter P2 muss zu neuen P3 werden
                            besteRundenzeitPlatz3 = besteRundenzeitPlatz2;
                            zeitP3 = zeitP2;
                            fahrerP3 = fahrerP2;
                            besteRundenzeitTafelPlatz3.setzeText("Platz 3: "+fahrerP3+" "+dfZero.format(zeitP3)+" sek",7);
                            //Neue P2 Zeit anpassen
                            besteRundenzeitPlatz2 = aktuelleZeit;
                            zeitP2 = (double)besteRundenzeitPlatz2;
                            zeitP2 = zeitP2/1000;
                            fahrerP2 = fahrer;   
                            //Auf die Tafel aktualisieren
                            besteRundenzeitTafelPlatz2.setzeText("Platz 2: "+fahrerP2+" "+dfZero.format(zeitP2)+" sek",7);
                        }    else{      
                            if(aktuelleZeit<besteRundenzeitPlatz3||besteRundenzeitPlatz3==0){ //Bestenliste Platz3
                                besteRundenzeitPlatz3 = aktuelleZeit;
                                zeitP3 = (double)besteRundenzeitPlatz3;
                                zeitP3 = zeitP3/1000;
                                fahrerP3 = fahrer;   
                                //Auf die Tafel aktualisieren
                                besteRundenzeitTafelPlatz3.setzeText("Platz 3: "+fahrerP3+" "+dfZero.format(zeitP3)+"sek",7);
                            }

                        }   
                    }
                    //Bei einer gültigen Runde wird der Speicher aufgerufen, um die Daten zu speichern
                    try{ //Die besten Zeiten werden als Dokument gespeichert
                        FileOutputStream saveFile=new FileOutputStream("besteZeitenSpeicher.sav");
                        ObjectOutputStream save = new ObjectOutputStream(saveFile);
                        //Speichern                                     
                        save.writeObject(fahrerP1);
                        save.writeObject(besteRundenzeitPlatz1);
                        save.writeObject(zeitP1);
                        save.writeObject(fahrerP2);
                        save.writeObject(besteRundenzeitPlatz2);
                        save.writeObject(zeitP2);
                        save.writeObject(fahrerP3);
                        save.writeObject(besteRundenzeitPlatz3);
                        save.writeObject(zeitP3);
                        //Datei schließen
                        save.close();
                    }catch(Exception exc){
                        exc.printStackTrace(); // Bei einem Fehler, die Ursache nennen.
                    }
                }
                startZeit = System.nanoTime();
                gueltigeRunde=true;
                rundenAnforderungErfuellt=false;
            }
            if(!wagen.pruefeAufStrecke(map.gibGeradenArray())){ //Wenn der Wagen die Strecke verlässt,wird die Runde ungültig
                gueltigeRunde= false;
            }                      
            if(gueltigeRunde){ //Wenn die Runde zählt, wird auf der Tafel die Zeit angezeigt, sonst "ungültige Rundenzeit"
                //Formatierung der Rundenzeit
                vergangeneZeit= (double)aktuelleZeit;
                vergangeneZeit= vergangeneZeit/1000;
                rundenzeitTafel.setzeText(dfZero.format(vergangeneZeit)+" "+"sek",7); //Zeit wird auf Tafel geschrieben
            }
            else{rundenzeitTafel.setzeText("ungültige Rundenzeit",7);}        
            Sys.warte();
        }

    }

    public static void namenEingabe(){ //Methode um die Eingabe des Namens auszulesen
        int t = 0;
        wartezahl++;
        if(wartezahl>100){
            wartezahl=0;
            if(tas.istGedrueckt('q')){
                String q = "q";
                fahrer = fahrer+q;
                Sys.warte(t);
            }
            if(tas.istGedrueckt('w')){
                String w = "w";
                fahrer = fahrer+w;
                Sys.warte(t);
            }
            if(tas.istGedrueckt('e')){
                String e = "e";
                fahrer = fahrer+e;
                Sys.warte(t);
            }
            if(tas.istGedrueckt('r')){
                String r= "r";
                fahrer = fahrer+r;
                Sys.warte(t);
            }
            if(tas.istGedrueckt('r')){
                String r = "r";
                fahrer = fahrer+r;
                Sys.warte(t);
            }
            if(tas.istGedrueckt('t')){
                String ts = "t";
                fahrer = fahrer+ts;
                Sys.warte(t);
            }
            if(tas.istGedrueckt('z')){
                String z = "z";
                fahrer = fahrer+z;
                Sys.warte(t);
            }
            if(tas.istGedrueckt('u')){
                String u = "u";
                fahrer = fahrer+u;
                Sys.warte(t);
            }
            if(tas.istGedrueckt('i')){
                String i = "i";
                fahrer = fahrer+i;
                Sys.warte(t);
            }
            if(tas.istGedrueckt('o')){
                String o = "o";
                fahrer = fahrer+o;
                Sys.warte(t);
            }
            if(tas.istGedrueckt('p')){
                String p = "p";
                fahrer = fahrer+p;
                Sys.warte(t);
            }
            if(tas.istGedrueckt('ü')){
                String ü = "ü";
                fahrer = fahrer+ü;
                Sys.warte(t);
            }
            if(tas.istGedrueckt('a')){
                String a = "a";
                fahrer = fahrer+a;
                Sys.warte(t);
            }
            if(tas.istGedrueckt('s')){
                String s = "s";
                fahrer = fahrer+s;
                Sys.warte(t);
            }
            if(tas.istGedrueckt('d')){
                String d = "d";
                fahrer = fahrer+d;
                Sys.warte(t);
            }
            if(tas.istGedrueckt('f')){
                String f = "f";
                fahrer = fahrer+f;
                Sys.warte(t);
            }
            if(tas.istGedrueckt('g')){
                String g= "g";
                fahrer = fahrer+g;
                Sys.warte(t);
            }
            if(tas.istGedrueckt('h')){
                String h = "h";
                fahrer = fahrer+h;
                Sys.warte(t);
            }
            if(tas.istGedrueckt('j')){
                String j = "j";
                fahrer = fahrer+j;
            }
            if(tas.istGedrueckt('k')){
                String k = "k";
                fahrer = fahrer+k;
                Sys.warte(t);
            }
            if(tas.istGedrueckt('l')){
                String l = "l";
                fahrer = fahrer+l;
                Sys.warte(t);
            }
            if(tas.istGedrueckt('ö')){
                String ö = "f";
                fahrer = fahrer+ö;
                Sys.warte(t);
            }
            if(tas.istGedrueckt('ä')){
                String ä= "ä";
                fahrer = fahrer+ä;
                Sys.warte(t);
            }
            if(tas.istGedrueckt('y')){
                String y = "y";
                fahrer = fahrer+y;
                Sys.warte(t);
            }
            if(tas.istGedrueckt('x')){
                String x = "x";
                fahrer = fahrer+x;
                Sys.warte(t);
            }
            if(tas.istGedrueckt('c')){
                String c = "c";
                fahrer = fahrer+c;
                Sys.warte(t);
            }
            if(tas.istGedrueckt('v')){
                String v = "v";
                fahrer = fahrer+v;
                Sys.warte(t);
            }
            if(tas.istGedrueckt('b')){
                String b = "b";
                fahrer = fahrer+b;
                Sys.warte(t);
            }
            if(tas.istGedrueckt('n')){
                String n = "n";
                fahrer = fahrer+n;
                Sys.warte(t);
            }
            if(tas.istGedrueckt('m')){
                String m = "m";
                fahrer = fahrer+m;
                Sys.warte(t);
            }
            if(tas.istGedrueckt('1')){ //Number1
                String n1 = "1";
                fahrer = fahrer+n1;
                Sys.warte(t);
            }
            if(tas.istGedrueckt('2')){
                String n2 = "2";
                fahrer = fahrer+n2;
                Sys.warte(t);
            }
            if(tas.istGedrueckt('3')){
                String n3 = "3";
                fahrer = fahrer+n3;
                Sys.warte(t);
            }
            if(tas.istGedrueckt('4')){
                String n4 = "4";
                fahrer = fahrer+n4;
                Sys.warte(t);
            }
            if(tas.istGedrueckt('5')){
                String n5 = "5";
                fahrer = fahrer+n5;
                Sys.warte(t);
            }
            if(tas.istGedrueckt('6')){
                String n6 = "6";
                fahrer = fahrer+n6;
                Sys.warte(t);
            }
            if(tas.istGedrueckt('7')){
                String n7 = "7";
                fahrer = fahrer+n7;
                Sys.warte(t);
            }
            if(tas.istGedrueckt('8')){
                String n8 = "8";
                fahrer = fahrer+n8;
                Sys.warte(t);
            }
            if(tas.istGedrueckt('9')){
                String n9 = "9";
                fahrer = fahrer+n9;
                Sys.warte(t);
            }
            if(tas.istGedrueckt('0')){
                String n0 = "0";
                fahrer = fahrer+n0;
                Sys.warte(t);
            }
            if(tas.backspace()){
                if(fahrer!=null&&fahrer.length()>0){
                    fahrer = fahrer.substring(0,fahrer.length()-1);
                }
                Sys.warte(t);
            }
            if(tas.istGedrueckt(' ')){
                String space = " ";
                fahrer = fahrer+space;
                Sys.warte(t);
            }
        }                
    }
}

