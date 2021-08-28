package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import java.util.List;

public class AttackEvent implements Event<Boolean> {

    final List<Integer> serials;
    final int duration;

    public AttackEvent(int duration, List<Integer> serials) {

        this.duration = duration;
        this.serials = serials;
    }

    public int getDuration() {
        return duration;
    }

    public List<Integer> getSerials() {
        return serials;
    }

}
