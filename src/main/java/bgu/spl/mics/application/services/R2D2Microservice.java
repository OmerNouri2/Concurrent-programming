package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * R2D2Microservices is in charge of the handling {@link DeactivationEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link DeactivationEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class R2D2Microservice extends MicroService {

    private long duration;
    private Diary diary;

    public R2D2Microservice(long duration) {
        super("R2D2");
        this.duration = duration;
        diary = Diary.getInstance();
    }

    @Override
    protected void initialize() {
        super.subscribeEvent(DeactivationEvent.class,(deactivationEvent)->{ // a callback lambda
            try {
                Thread.sleep(duration);
                diary.setR2D2Deactivate(System.currentTimeMillis()); // a timestamp indicating when R2D2 finished deactivation the shield generator.
                R2D2Microservice.super.complete(deactivationEvent, true); // notify the DeactivationEvent was handled
            } catch (InterruptedException e) {
            }
        });

        super.subscribeBroadcast(TerminateBroadcast.class, c -> { // Create lambda for calling terminate() function
            diary.setR2D2Terminate(System.currentTimeMillis()); // a time stamp that R2D2 puts in right before termination
            R2D2Microservice.super.terminate();
        });
    }
}
