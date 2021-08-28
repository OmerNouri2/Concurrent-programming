package bgu.spl.mics.application.passiveObjects;

// A class to handle json output
public class Output {
    long totalAttacks;
    long HanSoloFinish;
    long C3POFinish;
    long R2D2Deactivate;
    long LeiaTerminate;
    long HanSoloTerminate;
    long C3POTerminate;
    long R2D2Terminate;
    long LandoTerminate;
    public Output(Diary diary){
        totalAttacks = diary.getTotalAttacks();
        HanSoloFinish = diary.getHanSoloFinish();
        C3POFinish = diary.getC3POFinish();
        R2D2Deactivate = diary.getR2D2Deactivate();
        LeiaTerminate = diary.getLeiaTerminate();
        HanSoloTerminate = diary.getHanSoloTerminate();
        C3POTerminate = diary.getC3POTerminate();
        R2D2Terminate = diary.getR2D2Terminate();
        LandoTerminate = diary.getLandoTerminate();
    }

    public long getC3POFinish() {
        return C3POFinish;
    }

    public long getC3POTerminate() {
        return C3POTerminate;
    }

    public long getHanSoloFinish() {
        return HanSoloFinish;
    }

    public long getHanSoloTerminate() {
        return HanSoloTerminate;
    }

    public long getLandoTerminate() {
        return LandoTerminate;
    }

    public long getLeiaTerminate() {
        return LeiaTerminate;
    }

    public long getR2D2Deactivate() {
        return R2D2Deactivate;
    }

    public long getR2D2Terminate() {
        return R2D2Terminate;
    }

    public long getTotalAttacks() {
        return totalAttacks;
    }
}


