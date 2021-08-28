package bgu.spl.mics;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	ConcurrentHashMap<MicroService, LinkedBlockingQueue<Message>> microServicesMap; // a map to manage all the micro services which currently registered
	ConcurrentHashMap<Class<? extends Message>,ConcurrentLinkedQueue<MicroService>> messagesMap; // a map to manage all the messages that can be handled
	HashMap<Event<?>,Future> eventFutureMap; // a map to manage all the Events with their corresponding Futures

	public static class SingletonHolder{
		private static MessageBusImpl messageBusImpl = new MessageBusImpl();
	}

	// private constructor restricted to this class itself
	private MessageBusImpl() {
		microServicesMap = new ConcurrentHashMap<MicroService,LinkedBlockingQueue<Message>>(); // the key is the micro service & the mapped value is a queue of the messages to be handled by this MS
		messagesMap = new ConcurrentHashMap<Class<? extends Message>,ConcurrentLinkedQueue<MicroService>>();  // the key is the message & the mapped value is a queue of the MSs which can handle this type of message
		eventFutureMap = new HashMap<Event<?>,Future>(); // the key is the Event & the mapped value is a Future
	}

	// static method to create instance of Singleton class
	public static MessageBusImpl getInstance()
	{
		if (SingletonHolder.messageBusImpl == null) // if instance from type MessageBusImpl was not created yet
			SingletonHolder.messageBusImpl = new MessageBusImpl(); // crete new instance
		return SingletonHolder.messageBusImpl;
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		subscribeMessage(type,m); //use generic function in order to avoid redundancy
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		subscribeMessage(type,m); //use generic function in order to avoid redundancy
    }

    /* A generic function to use for 'subscribeEvent' & 'subscribeBroadcast' methods
       messagesMap is according to key - messages - both types: Event & Broadcast */
    private void subscribeMessage(Class<? extends Message> type, MicroService m){
    	if(microServicesMap.get(m) == null) // check if 'm' was register
    		throw new IllegalArgumentException("Micro Service 'm' is not register");
		ConcurrentLinkedQueue<MicroService> microServicesQueue;
		if (messagesMap.get(type) == null) { // this map contains no mapping for the key - 'type' message.
				microServicesQueue = new ConcurrentLinkedQueue<MicroService>(); // create new queue of micro services
				messagesMap.putIfAbsent(type, microServicesQueue); // Maps the specified key - 'type' to the specified value - 'microServicesQueue' in this table.
		}
		messagesMap.get(type).add(m);
	}

	@Override @SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
		Future future;
    	synchronized (eventFutureMap){
    		try {
				while(!eventFutureMap.containsKey(e)) // while there is no key with this type of event
					eventFutureMap.wait();
			}
    		catch (InterruptedException interruptedException) {
				interruptedException.printStackTrace();
			}
    		future = eventFutureMap.get(e);
		}
		if(future == null) {
			throw new NullPointerException("No future was associated with event 'e'");
		}
		future.resolve(result);
	}


	@Override
	public void sendBroadcast(Broadcast b) {
		ConcurrentLinkedQueue <MicroService> msQueueCanHandleBroad  = messagesMap.get(b.getClass()); // 'msQueueCanHandleBroad' <-- the queue of MS's which can handle 'b' - specified broadcast message
		for(MicroService ms: msQueueCanHandleBroad){ // iterate each MS that can handle 'b'
			microServicesMap.get(ms).add(b); // add to the queues of the MS's 'b' message
		}
	}

	@Override
	public <T> Future<T> sendEvent(Event<T> e) { // return null - in case no one can handle Event , else - return Future
		MicroService ms;
		Future<T> future = new Future<>();
		ConcurrentLinkedQueue<MicroService> msHandleEvent = messagesMap.get(e.getClass());
		if(msHandleEvent == null) // no micro service can handle the event (No queue for this type of Events)
			 return null;
		synchronized (messagesMap.get(e.getClass())){
			ms = msHandleEvent.poll(); // retrieves and removes the head of this queue
			if(ms == null) //poll returns null if the queue is empty
				return null;
			msHandleEvent.add(ms); // right after ms handle the event, we need to return ms to the end of the queue (to keep round-robin-manner)
			}
		microServicesMap.get(ms).add(e); // add to the messages queue of the specified MS the new Event
		synchronized (eventFutureMap){
			eventFutureMap.put(e,future); // Maps the specified Event to the specified Future in this table.
			eventFutureMap.notifyAll(); // notify to the thread who wait to complete this future
		}
		return future; //future that helps the sending MS retrieve the result of processing the event once it is completed
	}

	@Override
	public void register(MicroService m) {
		LinkedBlockingQueue<Message> messagesQueue = new LinkedBlockingQueue<Message>();
		microServicesMap.putIfAbsent(m,messagesQueue); // If the 'm' is not already associated with a queue, associate it with 'messagesQueue'.
	}

	@Override
	public void unregister(MicroService m) {
		microServicesMap.remove(m); // Removes 'm' (and its corresponding queues(values)) from this map.
		for(ConcurrentLinkedQueue<MicroService> q: messagesMap.values()){
			q.remove(m);
		}
    }

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
    	Message msgToHandle;
		if(microServicesMap.get(m) == null) // check if 'm' was register
			throw new IllegalArgumentException("Micro Service 'm' is not register");
		msgToHandle = microServicesMap.get(m).take(); // Retrieves and removes the message at the head of m's queue, waiting if necessary until an element becomes available.
		return msgToHandle;
	}

}
