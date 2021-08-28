package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {

    private long duration;
    private Diary diary;

    public LandoMicroservice(long duration) {
        super("Lando");
        this.duration = duration;
        diary = Diary.getInstance();
    }

    @Override
    protected void initialize() {
        super.subscribeEvent(BombDestroyerEvent.class,(bombDestroyerEvent)->{
            try {
                Thread.sleep(duration);
                LandoMicroservice.super.complete(bombDestroyerEvent, true); // notify the BombDestroyerEvent was handled
            }
            catch (InterruptedException e) {
            }
        });

        super.subscribeBroadcast(TerminateBroadcast.class, c -> { // Create lambda for calling terminate() function
            diary.setLandoTerminate(System.currentTimeMillis()); // a time stamp that Lando puts in right before termination
            LandoMicroservice.super.terminate();
        });
    }
}
