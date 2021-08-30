# Project name:  Concurrent-programming

# Description: 
Building a Microservice framework and Implementing a system on top of this framework.

# The Framework: 
  The framework consists of two main parts: first is a Message-Bus, and second - Microservices. 
  Each Microservice is a thread that can exchange messages with other Microservices, using a shared object - the Message-Bus. 
  There are two different types of messages: 
  1 - Event:
      An Event defines an action that needs to be processed. Each Microservice specializes in processing one or more types of events. 
      Upon receiving an event, the Message-Bus assigns it to the messages queue of a certain Microservice - one that registered to handle events of this type. 
      It is possible that there are several Microservices that can handle the event that was just received. 
      In that case, the Message-Bus assigns the event to one of them, in a round-robin manner.
      Each Event expects a result of type Future<T>. This Future<T> object should be resolved once the Microservice that handles the Event completes processing it
  2- Broadcast:
     Broadcast messages represent a global announcement in the system. Each Microservice can register to the type of broadcast messages it is interested in. 
     The Message-Bus sends the broadcast messages that are passed to it to all the registered Microservices 
     (unlike Events - those are sent to only one of the registered Microservices).
     
# The end-to-end description of the full system ("simulate stars wars"):
  Leia is initialized with Attack objects, which she sends to HanSolo and C3PO as AttackEvents. 
  In order to process those events, HanSolo and C3PO use the Ewoks - the resources in our program (in reality, though, Ewoks are little forest creatures that can fight fairly well). 
  Whenever HanSolo or C3PO hold the required resources for an attack - they execute it (this is simulated by “sleeping”). 
  When all attacks are finished, R2D2 is informed and deactivates the shield generator (simulated by “sleeping”).
  Finally, Lando can now bomb the star destroyer of the Empire (simulated by “sleeping”). 
  Only when this is done - all threads terminate at once.
  
  Active Objects (Microservices):
  The Microservices communicate indirectly, via the Message-Bus.
  ● Leia - Princess of the planet Alderaan, a member of the Imperial Senate and an agent of the Rebel Alliance. 
           Initialized with Attack objects, and sends them as AttackEvents
  ● HanSolo - A pilot. Receives AttackEvents, and uses the Ewoks (those resources are also accessed by C3PO) to execute them.
  ● C3PO - A droid programmed for etiquette and protocol.  
    Receives AttackEvents, and uses the Ewoks (those resources are also accessed by HanSolo) to execute them.
  ● R2D2 - A droid. After Han and C3PO finish their coordinated attacks on the Empire’s troops, he can deactivate the shield generator - 
    so Lando can safely approach the star destroyer. The deactivation of the shield generator is simulated by sleeping.
  ● Lando - - an old friend of HanSolo, and a decent pilot. When informed about the deactivation of the shield generator (via BombDestroyerEvent) - 
    he proceeds and bombs the star destroyer. This act is simulated by sleeping.
    
  Passive Objects (non-runnable classes):
  ● Ewok - a forest creature.
  ● Ewoks - wraps a collection of Ewok objects.
  ● Attack - information about an attack - List<Integer> serialNumbers.
  ● Diary - in which the flow of the battle is recorded. We are going to compare your recordings with the expected recordings, and make sure that your output makes sense.

  Messages:
  ● AttackEvent
  ● DeactivationEvent
  ● BombDestroyerEvent - sent to Lando when all is set for his final action
  
  # Input / Output files:
  The input file holds a JSON object which contains the following fields:
  ● Attacks: An array that represents the Attack objects. Each element in this array stands for a single attack, 
  and holds the information about the duration of the attack and the requested Ewok objects. 
  Leia is initialized with the Attack objects that are parsed from this part of the JSON.
  ● long R2D2: R2D2 is initialized with this parameter, which indicates the time it takes for him (“it”, actually) to deactivate the shield generator.
  ● long Lando: Lando is initialized with this parameter, which indicates the time it takes for him to eliminate the star destroyer.
  ● int Ewoks: number of Ewok objects in the program. Each of the created Ewok objects will have a serial number. 
    E.g. in case int Ewoks = 4, there will be 4 Ewok objects with the serial number 1,2,3,4.
    
    
# Program Execution :
  The Main class will run the simulation. When started, it should accept as command line argument the names of the input/output files - 
  the first argument will be the input file and the second is the output file.
  The Main class will read the input file, and construct the passive objects and Microservices accordingly. 
  Only after all threads terminate, the Main class will generate the output files and exit.

 
Usage & Installation :
  maven דhould be installed.
  Once installed you can use ~/bin/mvn compile to compile the code.
  For execution the code -  mvn exec: java -Dexec.mainClass="bgu.spl.mics.application.Main" -Dexec.args="path/to/input.json path/to/output.json"



