package lab1;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;


public class Zad3 {
	public static void main(String[] args) throws FileNotFoundException {
		task3();
	}
	
	public static void task3() throws FileNotFoundException {
		PrintWriter pw = new PrintWriter("task3.csv");
		pw.println("length,time");
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
				for (int jj = 0; jj < ii; jj++) {
					sum += Math.log(table[jj]);
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
}
