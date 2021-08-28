package bgu.spl.mics.application;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;

/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */

public class Main {

	public static void toJson(Diary diary, String path){
		try{
			Gson gson = new GsonBuilder().setPrettyPrinting().create(); // create new gson for output
			FileWriter outputFile = new FileWriter(path); // create new file
			Output outputObject = new Output(diary); // create new output object according to diary
			gson.toJson(outputObject,outputFile); // write outputObject to gson object
			outputFile.close(); // close file after finish writing
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Gson gson = new Gson();
		Diary diary = Diary.getInstance();
		Ewoks ewoks;
		Attack[] attacks;
		long durationR2D2;
		long durationLando;
		try {
			FileReader fileReader = new FileReader(args[0]); // create file with the input
			Input input = gson.fromJson(fileReader, Input.class); // read the content of the gson as an object

			// initialized fields from input
			ewoks = Ewoks.getInstance(input.getEwoks());
			attacks= input.getAttacks();
			durationR2D2 = input.getR2D2();
			durationLando = input.getR2D2();

			// Creation of all Micro-Services
			LeiaMicroservice leiaMicroservice = new LeiaMicroservice(attacks);
			C3POMicroservice c3POMicroservice = new C3POMicroservice();
			HanSoloMicroservice hanSoloMicroservice = new HanSoloMicroservice();
			LandoMicroservice landoMicroservice = new LandoMicroservice(durationLando);
			R2D2Microservice r2D2Microservice = new R2D2Microservice(durationR2D2);

			// Creation of all threads
			Thread leiaThread =new Thread(leiaMicroservice);
			Thread c3poThread =new Thread(c3POMicroservice);
			Thread hanSoloThread =new Thread(hanSoloMicroservice);
			Thread landoThread =new Thread(landoMicroservice);
			Thread r2d2Thread =new Thread(r2D2Microservice);

			// Start all threads
			c3poThread.start();
			hanSoloThread.start();
			landoThread.start();
			r2d2Thread.start();
			leiaThread.start();

			// make the Main thread to wait until all the other threads are done
			try{
				c3poThread.join();
				hanSoloThread.join();
				landoThread.join();
				r2d2Thread.join();
				leiaThread.join();
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}String path= args[1];

		toJson(diary,path);
	}
}

