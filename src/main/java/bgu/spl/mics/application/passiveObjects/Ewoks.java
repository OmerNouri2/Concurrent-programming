package bgu.spl.mics.application.passiveObjects;

import java.util.List;

/**
 * Holds a collection - of your choice - of Ewoks objects.
 * All calls for resources (by HanSolo and C3PO) are done via the Ewoks class.
 * You can add to this class members and methods as you see right.
 */
public class Ewoks {

    Ewok[] ewoks;

    public static class SingletonHolder{
        private static Ewoks ewoksInstance;
    }

    // private constructor restricted to this class itself
    private Ewoks(int size){
        ewoks = new Ewok[size+1]; // an array of given ewoks
        for (int i=1; i<=size; i++){
            ewoks[i] = new Ewok(i);
        }
    }

    // static method to create instance of Singleton class
    public static Ewoks getInstance(int size)
    {
        if (SingletonHolder.ewoksInstance == null) // if instance from type MessageBusImpl was not created yet
            SingletonHolder.ewoksInstance = new Ewoks(size); // crete new instance
        return SingletonHolder.ewoksInstance;
    }

    public static Ewoks getInstance() {
        return Ewoks.SingletonHolder.ewoksInstance;
    }

    public void setEwoks(Ewok[] ewoks){
        this.ewoks=ewoks;
    }


    public boolean allEwoksAvailable(List<Integer> serialsForEwoks){  // checks & acquires the Ewoks which needed to the handle attack
        for(int serial: serialsForEwoks){
            Ewok currEwok = ewoks[serial];
            try {
                synchronized (currEwok) {
                    while (!currEwok.getAvailable())
                        currEwok.wait(); // wait until ewok available
                    currEwok.acquire();
                }
            }
            catch (InterruptedException e) {
                return false;
            }
        }
        return true;
    }

    public void releaseEwoks(List<Integer> serialsForEwoks){
        for(int serial: serialsForEwoks){
            Ewok currEwok = ewoks[serial];
            synchronized (currEwok) {
                currEwok.release();
                currEwok.notifyAll(); // after releasing ewok, notify all thread which are waiting
            }
        }
    }

}


