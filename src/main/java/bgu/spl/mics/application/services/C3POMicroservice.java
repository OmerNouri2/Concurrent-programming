package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;

import java.util.List;

/**
 * C3POMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class C3POMicroservice extends MicroService {

    private List<Integer> serialsForEwoks;
    private int duration;
    private Ewoks ewoks;
    private Diary diary;

    // Constructor
    public C3POMicroservice() {
        super("C3PO");
        ewoks = Ewoks.getInstance();
        diary = Diary.getInstance();
    }

    @Override
    protected void initialize() {
        super.subscribeEvent(AttackEvent.class,(attackEvent)->{ // a callback lambda
            serialsForEwoks = attackEvent.getSerials();
            duration = attackEvent.getDuration();
            boolean allEwoksAvailable = ewoks.allEwoksAvailable(serialsForEwoks); // only if all Ewoks available
            try {
                if (allEwoksAvailable) {
                    Thread.sleep(duration); // execution of the attack will simulate by sleeping for that duration.
                    diary.setC3POFinish(System.currentTimeMillis()); // a timestamp indicating when C3PO finished the execution of all last attack.
                    ewoks.releaseEwoks(serialsForEwoks); // after attack was handled - ewoks can noe be released
                    diary.increaseTotalAttacks(); // After attack was finished , increase totalAttack
                    C3POMicroservice.super.complete(attackEvent, true); // notify the attack was handled
                }
            } catch (InterruptedException e) {
            }
        });

        super.subscribeBroadcast(TerminateBroadcast.class, c -> { // Create lambda for calling terminate() function
            diary.setC3POTerminate(System.currentTimeMillis()); // a time stamp that C3PO puts in right before termination
            C3POMicroservice.super.terminate();
        });
    }
}
