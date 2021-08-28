package bgu.spl.mics.application.services;

import java.util.concurrent.CountDownLatch;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LeiaMicroservice extends MicroService {
	private Attack[] attacks;
	private Future<Boolean>[] futureAttacks;
	private boolean allDone;
    private DeactivationEvent deactivationEvent;
    private Future<Boolean> deactivatedFuture;
    private BombDestroyerEvent bombDestroyerEvent;
    private Future<Boolean> bombDestroyerFuture;
    private TerminateBroadcast terminateBroadcast;
    private Diary diary;
  //  private static CountDownLatch latch =new CountDownLatch(1);

    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
		this.attacks = attacks;
		this.futureAttacks = new Future[attacks.length];
		this.allDone = true;
        deactivationEvent = new DeactivationEvent();
        bombDestroyerEvent = new BombDestroyerEvent();
        terminateBroadcast = new TerminateBroadcast();
        diary = Diary.getInstance();
    }

    @Override
    protected void initialize(){

        try {
            Thread.sleep(1000); // Lea waits until all other MSs will start
          //  latch.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int i=0; // index of the attack in attackEvent array
        for(Attack attack: attacks){ // go through each attack
            AttackEvent attackEvent = new AttackEvent(attack.getDuration(), attack.getSerials());
            futureAttacks[i] = super.sendEvent(attackEvent); // send to MSGBusImpl the attacks for handling
            i++;
        }
        // go through each future return after handling the attackEvent
        for (Future<Boolean>future: futureAttacks) {
            if (!future.get()) {
                allDone = false;
                break; // enough that one of the futures is false for allDone to be - false
            }
        }
        if(allDone){ // if all attack events are done
            deactivatedFuture = super.sendEvent(deactivationEvent); // now R2D2 can be informed and deactivates the shield generator
            deactivatedFuture.get(); // get function is blocking
            bombDestroyerFuture = super.sendEvent(bombDestroyerEvent); // assuming 'deactivatedFuture.get() = true'
            bombDestroyerFuture.get();// get function is blocking
            super.sendBroadcast(terminateBroadcast); /// each microService should subscribe terminateBroadcast message
            diary.setLeiaTerminate(System.currentTimeMillis()); //a time stamp that Leia puts in right before termination
            super.terminate(); // after all MS were terminate its time for Lea
        }
    }
}
