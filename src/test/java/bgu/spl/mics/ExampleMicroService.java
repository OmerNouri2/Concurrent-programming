package bgu.spl.mics;

public class ExampleMicroService extends MicroService{

    /**
     * @param name the micro-service name (used mainly for debugging purposes -
     *             does not have to be unique)
     */
    public ExampleMicroService() {
        super("exampleMS");
    }

    @Override
    protected void initialize() {

    }
}
