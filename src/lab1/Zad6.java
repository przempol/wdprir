package lab1;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicIntegerArray;


public class Zad6 {
	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
		task6();
	}
	
	
	public static void task6() throws FileNotFoundException, InterruptedException {
		PrintWriter pw = new PrintWriter("task6.csv");
		pw.println("length,time");
		
		int sizeMax = (int) Math.pow(2, 20);
		for (int ii = 16; ii < sizeMax + 1; ii*=2) {
			System.out.println("doing table of length " + Integer.toString(ii));

			int repeats = 1000;
			long timeAverage = 0;
			for (int tt = 0; tt < repeats; tt++) {
				double[] table = new double[ii];
				Arrays.fill(table, Math.E);
				AtomicIntegerArray flags = new AtomicIntegerArray(ii);
//				for (int jj=0; jj < ii; jj++) flags.set(jj, 0);
				
				@SuppressWarnings("unused")
				double sum = 0.;
				
				long timeStart = System.nanoTime();

				int noThreads = Runtime.getRuntime().availableProcessors();
				CustomThread[] threads = new CustomThread[noThreads];
				Thread[] threadsPure = new Thread[noThreads];

                for (int jj = 0; jj < noThreads; jj++) {
    				// creating custom threads
					threads[jj] = new CustomThread(flags, table);
					// creating pure threads, which will be working with customized method "run"
					threadsPure[jj] = new Thread(threads[jj]);
					threadsPure[jj].start();
				}
                
                // joining threads, so they will wait until the last one end 
                for (int jj = 0; jj < noThreads; jj++) {
					threadsPure[jj].join();
				}

                // getting sum
                for (int jj = 0; jj < noThreads; jj++) {
					sum += threads[jj].getSum();
				}
				long timeEnd = System.nanoTime();
				long timeTotal = timeEnd - timeStart;
				
				timeAverage+= timeTotal;
			}
			
			timeAverage /= repeats;
			pw.println(Integer.toString(ii) + "," + Long.toString(timeAverage));
		}
		pw.close();
		System.out.println("Done.");
	}
	
	
	// class for implementing method for multiple thread
	public static class CustomThread implements Runnable{
		double sum;
		double [] table;
	    AtomicIntegerArray flags;
		
		public CustomThread(AtomicIntegerArray flags, double[] table) {
			this.table = table;
			this.flags = flags;
			this.sum = 0.;
		}
		
		public double getSum() {
			return this.sum;
		}
		
	    public void run() {
            for (int ii = 0; ii < table.length; ii++) {
                if(flags.getAndSet(ii, 1) == 0) {
                	sum = sum + Math.log(table[ii]);
                }
            }
	    }

	}

}
