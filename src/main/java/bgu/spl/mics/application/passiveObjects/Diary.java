package bgu.spl.mics.application.passiveObjects;
import bgu.spl.mics.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive data-object representing a Diary - in which the flow of the battle is recorded.
 * We are going to compare your recordings with the expected recordings, and make sure that your output makes sense.
 * <p>
 * You can add to this class members and methods as you see right. according to specifications!
 */
public class Diary {


    private AtomicInteger totalAttacks; //  the total number of attacks executed by HanSolo and C3PO.
    private long HanSoloFinish; // a timestamp indicating when HanSolo finished the execution of all his attacks.
    private long C3POFinish; // a timestamp indicating when C3PO finished the execution all his attacks.
    private long R2D2Deactivate; // a timestamp indicating when R2D2 finished deactivation the shield generator.
    private long LeiaTerminate; // a time stamp that Leia puts in right before termination
    private long HanSoloTerminate; // a time stamp that HanSolo puts in right before termination
    private long C3POTerminate; // a time stamp that C3PO puts in right before termination.
    private long R2D2Terminate; // a time stamp that R2d2 puts in right before termination.
    private long LandoTerminate; // a time stamp that Lando puts in right before termination
    private static Diary diary = null;


    public static class SingletonHolder{
        private static Diary diaryInstance = new Diary();
    }

    // private constructor restricted to this class itself
    private Diary(){
        totalAttacks = new AtomicInteger(0);
    }

    // static method to create instance of Singleton class
    public static Diary getInstance()
    {
        if (SingletonHolder.diaryInstance == null) // if instance from type Diary was not created yet
            SingletonHolder.diaryInstance = new Diary(); // crete new instance
        return SingletonHolder.diaryInstance;
    }

    public void increaseTotalAttacks(){
        totalAttacks.compareAndSet(totalAttacks.get(), totalAttacks.get()+1);
    }

    public void setHanSoloFinish(long hanSoloFinish) {
        HanSoloFinish = hanSoloFinish;
    }

    public void setC3POFinish(long c3POFinish) {
        C3POFinish = c3POFinish;
    }

    public void setR2D2Deactivate(long r2D2Deactivate) {
        R2D2Deactivate = r2D2Deactivate;
    }

    public void setLeiaTerminate(long leiaTerminate) {
        LeiaTerminate = leiaTerminate;
    }

    public void setHanSoloTerminate(long hanSoloTerminate) {
        HanSoloTerminate = hanSoloTerminate;
    }

    public void setC3POTerminate(long c3POTerminate) {
        C3POTerminate = c3POTerminate;
    }

    public void setR2D2Terminate(long r2D2Terminate) {
        R2D2Terminate = r2D2Terminate;
    }

    public void setLandoTerminate(long landoTerminate) {
        LandoTerminate = landoTerminate;
    }

    public long getHanSoloFinish() {
        return HanSoloFinish;
    }

    public long getC3POFinish() {
        return C3POFinish;
    }

    public long getR2D2Deactivate() {
        return R2D2Deactivate;
    }

    public long getLeiaTerminate() {
        return LeiaTerminate;
    }

    public long getHanSoloTerminate() {
        return HanSoloTerminate;
    }

    public long getC3POTerminate() {
        return C3POTerminate;
    }

    public long getR2D2Terminate() {
        return R2D2Terminate;
    }

    public long getLandoTerminate() {
        return LandoTerminate;
    }

    public long getTotalAttacks(){
        return totalAttacks.get();
    }

    public void resetNumberAttacks() {
        totalAttacks.set(0);
    }
}
