/**
 * 
 */
package aim4.ShoutAheadAI;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;

import aim4.Main;
import aim4.config.Debug;
import aim4.gui.Viewer;
import aim4.sim.ShoutAheadSimulator;
import aim4.sim.setup.ShoutAheadSimSetup;
import aim4.sim.setup.SimSetup;
import aim4.vehicle.VinRegistry;

/**
 * @author Chris Hawk
 *
 */
public class LearningHarness {
	public static Lock lock;
	private static final String LEARNING_RECORDS_PATH_MAC = "/Users/chrishawk_MacBookAir/Google Drive/UofC/F16/502/Simulation2/AIM3-Shout-Ahead/LearningRecords/";
	private static final String LEARNING_RECORDS_PATH_UOFC = "todo";// TODO
	private static final String PARAM_FILE_NAME = "paramaters";

	private static String learningRunRootPath;
	private int numGenerations = ShoutAheadSimSetup.getNumGenerations();
	private int numStrategiesPerGen = ShoutAheadSimSetup.getNumRoundsPerGeneration();
	private ArrayList<ArrayList<Strategy>> genList = new ArrayList<ArrayList<Strategy>>();
	private Viewer simViewer;
	private ShoutAheadSimulator simulator;
	private ShoutAheadSimSetup simSetup;

	/**
	 * @param viewer
	 * @param simSetup
	 * @param args
	 * 
	 */
	public LearningHarness(ShoutAheadSimSetup setup, Viewer viewer) {
		simSetup = setup;
		simViewer = viewer;
		// simulator = (ShoutAheadSimulator) viewer.getSimulator();

		setupRecordsDirectory(simSetup);

		// MAIN LEARNING LOOP:
		ArrayList<Strategy> currentGen = new ArrayList<Strategy>();
		for (currentGen = getGen1(); genList.size() < numGenerations; currentGen = evolve(currentGen)) {
			genList.add(currentGen);
			setupGenDirectory(currentGen);
			for (Strategy strategy : currentGen) {
				setupStratDirectory(strategy, currentGen);
				VinRegistry.reset();   // from sim factory
			    System.gc();
				simulator = (ShoutAheadSimulator) simSetup.getSimulator(strategy);
				// simulator = new ShoutAheadSimulator(simSetup.getMap(),
				// strategy);
				viewer.setSimulator(simulator);
				viewer.startShoutAheadProcess();
				waitForSim();
				
				//terminate?
			}
			// stop sim

		}
	}
	// simViewer.startSimProcess(simSetup);
	// if(simulator != null && simulator.isComplete()) {
	// simViewer.resetSimProcess();
	// if(Debug.SHOW_LEARNING_STAGE) System.out.println("Evaluating strategy
	// X\n");
	// }

	// Generation generation = new Generation();

	// create paramaters.xml
	// for each gen:
	// generate genX from genX-1
	// make genX dir
	// for each strategy
	// make stratX dir
	// write beforeRL.xml
	// do RL run until target num cars completed
	// write afterRL.xml
	// make stratX dir
	// ...
	/**
	 * TODO: busy waiting. should change
	 */
	private void waitForSim() {
		while(true) {
			if(simulator.isComplete())
				break;
		}
	
//		lock.lock();
//		try {
//			simulator.isComplete().await(); // releases lock and waits until
//											// doSomethingElse is called
//		} finally {
//			lock.unlock();
//		}
	}

	private ArrayList<Strategy> evolve(ArrayList<Strategy> currentGen2) {
		// TODO: evolve function
		// dummy evolver
		return getGen1();
	}

	public static void makeDirectory(String relativePath) {
		File dir = new File(learningRunRootPath + relativePath);

		// attempt to create the directory here
		boolean successful = dir.mkdir();
		if (successful) {
			// creating the directory succeeded
			if (Debug.SHOW_FILE_SYS_INFO)
				System.out
						.println("directory + \"" + learningRunRootPath + relativePath + "\" was created successfully");
		} else {
			// creating the directory failed
			if (Debug.SHOW_FILE_SYS_INFO)
				System.out.println("failed to create directory \"" + learningRunRootPath + relativePath + "\"");
		}

	}

	public static void writeToNewFile(String filePath, String content) {
		if (Debug.SHOW_FILE_SYS_INFO)
			System.out.println("file path = " + learningRunRootPath + filePath);

		BufferedWriter bw = null;
		FileWriter fw = null;

		try {
			File newFile = new File(learningRunRootPath + filePath);
			newFile.createNewFile();

			fw = new FileWriter(newFile);
			bw = new BufferedWriter(fw);
			bw.write(content);

			System.out.println("wrote to " + filePath);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}
	}

	private ArrayList<Strategy> getGen1() {
		ArrayList<Strategy> gen1 = new ArrayList<Strategy>();
		for (int i = 0; i < numStrategiesPerGen; i++) {
			gen1.add(new Strategy());
		}
		return gen1;
	}

	private void setupRecordsDirectory(SimSetup simSetup) {
		learningRunRootPath = LEARNING_RECORDS_PATH_MAC + getTimeStamp() + "/";
		// TODO: write hardware info file
		makeDirectory("");// make root directory for this run
		writeToNewFile(PARAM_FILE_NAME, simSetup.toString());
	}

	private static String getTimeStamp() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMMyyyy(HH.mm.ss)");
		LocalDateTime now = LocalDateTime.now();
		return dtf.format(now);
	}

	private void setupGenDirectory(ArrayList<Strategy> currentGen) {
		if (Debug.SHOW_LEARNING_STAGE) {
			if (genList.size() == 1) {
				System.out.println("\nBEGINNIG LEARNING RUN\n");
				System.out.println("Beginning Generation " + genList.size() + " of " + numGenerations + "\n");
			}
		}
		makeDirectory(generationPath());
	}

	private void setupStratDirectory(Strategy strategy, ArrayList<Strategy> currentGen) {
		if (Debug.SHOW_LEARNING_STAGE) {
			System.out.println("Beginning Simulation " + (currentGen.indexOf(strategy) + 1) + " of "
					+ numStrategiesPerGen + " (Gen " + genList.size() + ")\n");
		}
		makeDirectory(generationPath() + currentStratPath(strategy, currentGen));
		writeToNewFile(generationPath() + currentStratPath(strategy, currentGen) + "rulesBefore", strategy.toString());
	}

	private String currentStratPath(Strategy currentStrat, ArrayList<Strategy> currentGen) {
		return "strategy" + (((currentGen.indexOf(currentStrat) + 1) < 10) ? "0" : "")
				+ (currentGen.indexOf(currentStrat) + 1) + "/";
	}

	private String generationPath() {
		return "gen" + ((genList.size() < 10) ? "0" : "") + genList.size() + "/";
	}

}
