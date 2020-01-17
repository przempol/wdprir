package lab1;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicIntegerArray;


public class Zad7 {
	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
		task7();
	}

	
	public static void task7() throws FileNotFoundException, InterruptedException {
		PrintWriter pw = new PrintWriter("task7.csv");
		pw.println("length,time");

		int sizeTable = (int) Math.pow(2, 20);
		double []table = new double[sizeTable];
		Arrays.fill(table, Math.E);

		for (int ii = 1; ii < sizeTable + 1; ii*=2) {
			System.out.println("summarize sequences of length equal " + ii);
			
			int repeats = 1000;
			long timeAverage = 0;
			for (int tt = 0; tt < repeats; tt++) {
				@SuppressWarnings("unused")
				double sum = 0.;
				
				int segmentSize = ii;
				int noSegments = sizeTable / segmentSize;

				AtomicIntegerArray flags = new AtomicIntegerArray(noSegments);
				for (int jj=0; jj < noSegments; jj++) flags.set(jj, 0);
				
				long timeStart = System.nanoTime();

				int noThreads = Runtime.getRuntime().availableProcessors();
				CustomThread[] threads = new CustomThread[noThreads];
				Thread[] threadsPure = new Thread[noThreads];

                for (int jj = 0; jj < noThreads; jj++) {
    				// creating custom threads
					threads[jj] = new CustomThread(flags, table, segmentSize, noSegments);
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
	    int segmentSize;
	    int noSegments;

		
		public CustomThread(AtomicIntegerArray flags, double[] table, int segmentSize, int noSegments) {
			this.table = table;
			this.flags = flags;
			this.sum = 0.;
			this.segmentSize = segmentSize;
			this.noSegments = noSegments;
		}
		
		public double getSum() {
			return this.sum;
		}
		
	    public void run() {
            for (int ii = 0; ii < noSegments; ii++) {
                if(flags.getAndIncrement(ii) == 0) {
                	for (int jj = ii * segmentSize; jj < (ii+1) * segmentSize; jj++) {
                    	sum = sum + Math.log(table[jj]);	
					}
                }
            }
	    }
	}


}
