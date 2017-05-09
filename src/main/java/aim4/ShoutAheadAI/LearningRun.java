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
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.locks.Lock;

import aim4.Main;
import aim4.config.Debug;
import aim4.config.DebugPoint;
import aim4.gui.Viewer;
import aim4.sim.setup.SimSetup;
import aim4.vehicle.VinRegistry;

/**
 * @author Chris Hawk
 *
 */
public class LearningRun {
	public static Lock lock;
	public static final String DESKTOP_PATH = "/Users/chrishawk_MacBookAir/Desktop/";
	public static final String LEARNING_RECORDS_PATH_MAC = "/Users/chrishawk_MacBookAir/Google Drive/UofC/F16/502/Simulation2/AIM3-Shout-Ahead/LearningRecords/";
	private static final String LEARNING_RECORDS_PATH_UOFC = "todo";// TODO
	private static final String PARAM_FILE_NAME = "paramaters";
	private static final String LOG_COL_HEADINGS = "Generation,Strategy,Net Dist. Moved Towards Goal,Building Collisions,Car Collisions,Ave. Acceleration,Completed Vehicles,Total Fitness";
	private static final String LOG_SUMMARY_COL_HEADINGS = "Generation, Ave Net Dist. Moved Towards Goal,Ave Building Collisions,Ave Car Collisions,Ave. Ave. Acceleration,Ave. Completed Vehicles,Ave. Total Fitness";
	static final String NEW_LINE = "\n";
	private static String learningRunRootPath;
	private static int numGenerations = ShoutAheadSimSetup.getNumGenerations();
	private static int numStrategiesPerGen = ShoutAheadSimSetup.getNumRoundsPerGeneration();
	private Viewer simViewer;
	private ShoutAheadSimulator simulator;
	private ShoutAheadSimSetup simSetup;
	
	private static ArrayList<Generation> genList = new ArrayList<Generation>();
	private static Generation currentGen;
//	private Generation nextGen = new Generation();
	private static int currentGenerationIndex = 0;
	private static Strategy currentStrategy;
	private static int currentStrategyIndex = 0;
	private static File learningLog;

	/** Allows sim to notify Learning Harness when sim is complete */
	private Object simSyncObject = new Object();
	private Random rand = new Random();
	private File learningSummary;

	/**
	 * @param simViewer
	 * @param simSetup
	 * @param args
	 * 
	 */
	public LearningRun(ShoutAheadSimSetup setup, Viewer viewer) {
		simSetup = setup;
		simViewer = viewer;
		
		setupRecordsDirectory(simSetup);

		// MAIN LEARNING LOOP:
		
		//Outer Loop: Evolutionary Learning
		currentGen = getGen1();
		for (currentGenerationIndex = 0; currentGenerationIndex < numGenerations; currentGen = evolve(), currentGenerationIndex++) {
			log(NEW_LINE);
			genList.add(currentGen);
			setupGenDirectory();
			
			//Inner Loop: Reinforcement Learning
			for (currentStrategyIndex = 0; currentStrategyIndex < numStrategiesPerGen; currentStrategyIndex++) {
				currentStrategy = currentGen.get(currentStrategyIndex);
				setupStratDirectory();
				initializeSimulation();
				simViewer.startShoutAheadProcess();
				waitForSimToComplete();
			}
			
		}
		writeLearningRunSummary();
	}

	private void writeLearningRunSummary() {
		learningSummary = new File(learningRunRootPath + "learningRunSummary");
		try {
			learningSummary.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logSummary(LOG_SUMMARY_COL_HEADINGS);
		for(Generation gen: genList){
			logSummary(gen.getGenSummaryCSV());
			logSummary(NEW_LINE);
		}
		
	}

	private void initializeSimulation() {
		VinRegistry.reset();  
		System.gc();
		simulator = (ShoutAheadSimulator) simSetup.getSimulator(currentStrategy, simSyncObject);
		simViewer.setSimulator(simulator);
	}

	/**
	 * Wait for simulation to complete.
	 */
	private void waitForSimToComplete() {
		synchronized(simSyncObject) {
		    try {
		    	simSyncObject.wait();
		    } catch (InterruptedException e) {
		    	e.printStackTrace();
		    }
		}
		simViewer.pauseSimProcess();
		simViewer.resetSimProcess();
	}

	private Generation evolve() {
		//Sort the current generation in descending order of fitness
		Collections.sort(currentGen, Collections.reverseOrder());
		
		System.out.println("\ncurrent\n");
		System.out.println(currentGen);
		Generation nextGen = new Generation();
		copy(nextGen);
		System.out.println(nextGen);
		crossover(nextGen);
		System.out.println(nextGen);
		mutate(nextGen);
		System.out.println(nextGen);
		
		return nextGen;
	}

	private void mutate(Generation nextGen) {
		while(nextGen.size() < numStrategiesPerGen)
			nextGen.add(Strategy.getRandomStrategy());
	}

	private void crossover(Generation nextGen) {
		for(int i = 0; i <  getNumOffspring(); i++)
			nextGen.add(getOffspring(i));
	}

	private void copy(Generation nextGen) {
		for(int i = 0; i < getNumStrategiesToCarryForward(); i++)
			nextGen.add(currentGen.get(i));
	}
	
    //TODO: breed more parents
	private Strategy getOffspring(int i) {
		Strategy parent1 = currentGen.get(i);
		Strategy parent2 = currentGen.get(rand .nextInt(ShoutAheadSimSetup.getNumRoundsPerGeneration()));//2nd best
		return new Strategy(parent1, parent2);	
	}

	private int getNumOffspring() {
		return (int) Math.rint( ((double) numStrategiesPerGen) * ShoutAheadSimSetup.getOffspringFraction());
	}

	private int getNumStrategiesToCarryForward() {
		return (int) Math.rint( ((double) numStrategiesPerGen) * ShoutAheadSimSetup.getFractionStratsToCarryForward() );
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
	public static void createLearningLog(){
		learningLog = new File(learningRunRootPath + "learningLog");
		try {
			learningLog.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void log(int number){
		log(Integer.toString(number));
	}
	public static void log(String content) {
		BufferedWriter bw = null;
		FileWriter fw = null;
		
		try {
			fw = new FileWriter(learningLog,true);
			bw = new BufferedWriter(fw);
			bw.write(content);
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
	
	public void logSummary(String content) {
		BufferedWriter bw = null;
		FileWriter fw = null;
		
		try {
			fw = new FileWriter(learningSummary, true);
			bw = new BufferedWriter(fw);
			bw.write(content);
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
			if(Debug.SHOW_FILE_SYS_INFO)
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

	private Generation getGen1() {
		Generation gen1 = new Generation();
		for (int i = 0; i < numStrategiesPerGen; i++) {
			gen1.add(Strategy.getRandomStrategy());
		}
		return gen1;
	}

	private void setupRecordsDirectory(SimSetup simSetup) {
		learningRunRootPath = LEARNING_RECORDS_PATH_MAC + getTimeStamp() + "/";
		// TODO: write hardware info file
		makeDirectory("");// make root directory for this run
		writeToNewFile(PARAM_FILE_NAME, simSetup.toString());
		createLearningLog();
		log(LOG_COL_HEADINGS + NEW_LINE);
	}

	private static String getTimeStamp() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMMyyyy(HH.mm.ss)");
		LocalDateTime now = LocalDateTime.now();
		return dtf.format(now);
	}

	private void setupGenDirectory() {
		if (Debug.SHOW_LEARNING_STAGE) {
			if (currentGenerationIndex == 0) 
				System.out.println("\nBEGINNIG LEARNING RUN\n");
			System.out.println("Beginning Generation " + (currentGenerationIndex + 1) + " of " + numGenerations + "\n");
		}
		if(Debug.MAKE_STRAT_DIRECTORIES)
			makeDirectory(generationPath());
	}

	private void setupStratDirectory() {
		if (Debug.SHOW_LEARNING_STAGE) {
			System.out.println("Beginning Simulation " + (currentStrategyIndex + 1) + " of "
					+ numStrategiesPerGen + " (Gen " + (currentGenerationIndex + 1) + ")\n");
		}
		log((currentGenerationIndex + 1) + ",");
		log((currentStrategyIndex + 1) + ",");
		if(Debug.MAKE_STRAT_DIRECTORIES) {
			makeDirectory(generationPath() + currentStratPath());
			writeToNewFile(generationPath() + currentStratPath() + "rulesBefore", currentStrategy.toString());
		}
	}

	private String currentStratPath() {
		return "strategy" + ( (currentStrategyIndex <= 10) ? "0" : "")
						  + (currentStrategyIndex + 1) + "/";
	}

	private String generationPath() {
		return "gen" + ((currentGenerationIndex <= 10) ? "0" : "") + (currentGenerationIndex + 1) + "/";
	}

	public static String getLearningStage() {
		return "Gen " + (currentGenerationIndex + 1) + "/" + numGenerations + "\nStrategy " 
		+ (currentStrategyIndex + 1) + "/" + numStrategiesPerGen;
	}
	
	public static Strategy getCurrentStrategy() {
		return currentStrategy;
	}

	public static Generation getCurrentGen() {
		return currentGen;
	}
}
