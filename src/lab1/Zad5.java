package lab1;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Zad5 {
	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
		task5();
	}
	
	
	public static void task5() throws FileNotFoundException, InterruptedException {
		PrintWriter pw = new PrintWriter("task5.csv");
		pw.println("length,time");
		
		// this time we got fixed number of thread
		int noThreads = Runtime.getRuntime().availableProcessors();
		ExecutorService executor = Executors.newFixedThreadPool(noThreads);

		
		int sizeMax = (int) Math.pow(2, 20);
		for (int ii = 16; ii < sizeMax + 1; ii*=2) {
			System.out.println("doing table of length " + Integer.toString(ii));
			
			int repeats = 1000;
			long timeAverage = 0;
			for (int tt = 0; tt < repeats; tt++) {
				@SuppressWarnings("unused")
				double sum = 0.;
				double[] table = new double[ii];
				Arrays.fill(table, Math.E);

				long timeStart = System.nanoTime();


				CustomThread[] threads = new CustomThread[noThreads];
                CountDownLatch cdl = new CountDownLatch(noThreads);

                for (int jj = 0; jj < noThreads; jj++) {
                	int sizeOfPart = (ii/noThreads);
    				// creating custom threads
					threads[jj] = new CustomThread(jj*sizeOfPart, (jj+1)*sizeOfPart, table, cdl);
				}
                
            	// starting new threads inside our pool of threads using executor
            	for (int jj = 0; jj < noThreads; jj++) {
            		executor.execute(threads[jj]);
				}
            	cdl.await();
                
                
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
		int startPoint;
		int endPoint;
		double sum;
		double [] table;
	    CountDownLatch cdl;
		
		public CustomThread(int start, int end, double[] table, CountDownLatch cdl ) {
			this.startPoint = start;
			this.endPoint = end;
			this.table = table;
			this.sum = 0.;
	    	this.cdl = cdl;
		}
		
		public double getSum() {
			return this.sum;
		}
		
	    public void run() {
            for (int ii = startPoint; ii < endPoint; ++ii) {
                sum = sum + Math.log(table[ii]);
            }
            cdl.countDown();
	    }

	}


}
