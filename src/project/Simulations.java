package project;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Simulations {
	public static void serial() throws InterruptedException {
		if (Constants.MAKECHART) {
			RealTimeChart chart;
			WavePacket wavePacket;
			
			wavePacket= new WavePacket(1);
			chart = new RealTimeChart(wavePacket.getAmplitudeData());

	    	long startTime = System.currentTimeMillis();
	    	
			for (double ii = 0.; ii < 1e-2; ii+=Constants.dt) {
				wavePacket.updateWavePacket();
				chart.updateChart(wavePacket.getAmplitudeData());
			}
	        long endTime = System.currentTimeMillis();
	        double time = (double) (endTime - startTime)/1000.;
	        System.out.println("symulacja trwala " + Double.toString(time) + " s\n");
		}
		else {
			for (int nn = 1; nn < 8*8*128+1; nn*=2) {
				long meanTime = 0;
				int repeats = 10;
				for (int tt = 0; tt < repeats; tt++) {
					WavePacket wavePacket= new WavePacket(nn);
			    	long startTime = System.currentTimeMillis();
			    	
			    	for (int ii = 0; ii < 7500; ii++) {
							wavePacket.updateWavePacket();
					}
					
			        long endTime = System.currentTimeMillis();
			        meanTime += (endTime - startTime);
				}
				meanTime/=repeats;
				System.out.println("symulacja dla " + Integer.toString(nn*Constants.TABLE_LEN) + " trwala srednio "+ Double.toString(meanTime) + " ms");
				
			}
		}

	}
	
	public static class ComputingThread implements Runnable{
		int startPoint;
		int endPoint;
		int type;
	    CountDownLatch cdl;
		WavePacket wave;
		
		public ComputingThread(int start, int end, CountDownLatch cdl, WavePacket wave) {
			this.startPoint = start;
			this.endPoint = end;
			this.type = 0;
			this.cdl = cdl;
			this.wave = wave;
		} 
		
		public void setCDL(CountDownLatch cdl) {
			this.cdl = cdl;
		}
		
		@Override
		public void run() {
			wave.updateWavePacketParallel(type, startPoint, endPoint);
//			System.out.println("pracujê od " + Integer.toString(startPoint) + " do " +  Integer.toString(endPoint) + " - type " + Integer.toString(type) + "cdl:" + Long.toString(cdl.getCount()));
			type = (type + 1)%5;
			cdl.countDown();
		}
	}
	
	
	public static void parallel() throws InterruptedException {
		if (Constants.MAKECHART) {
			return;
		}
		int nn=128*16;
		WavePacket wave = new WavePacket(nn);
		int noThreads = Runtime.getRuntime().availableProcessors();
		ExecutorService executor = Executors.newFixedThreadPool(noThreads);
		
		ComputingThread[] computingThreads = new ComputingThread[noThreads];

		CountDownLatch cdl = new CountDownLatch(noThreads);
        for (int jj = 0; jj < noThreads; jj++) {
        	int start = (int) ((double) (jj*(Constants.TABLE_LEN*nn+1)/noThreads));
        	int end = (int) ((double) ((jj+1)*(Constants.TABLE_LEN*nn+1)/noThreads));
			// creating custom threads
        	computingThreads[jj] = new ComputingThread(start, end ,cdl ,wave);
		}
		
        long start ;
        long end ;
        
        for (int jj = 0; jj < 20; jj++) {
            start = System.currentTimeMillis();
            for (int dt = 0; dt < 7500; dt++) {
            	for (int tt = 0; tt < 5; tt++) {
            		for (int ii = 0; ii < noThreads; ii++) {
            			computingThreads[ii].setCDL(cdl);
            			executor.execute(computingThreads[ii]);
            		}
        			cdl.await();
        			cdl = new CountDownLatch(noThreads);
        		}
    		}
    		end = System.currentTimeMillis();
    		System.out.println("rownolegle trwalo " + Long.toString(end - start));
		}
        for (int jj = 0; jj < 5; jj++) {
            start = System.currentTimeMillis();
            for (int dt = 0; dt < 7500; dt++) {
                wave.updateWavePacket();
			}
    		end = System.currentTimeMillis();
    		System.out.println("sekwencyjnie trwalo " + Long.toString(end - start));
		}
        
        
        executor.shutdownNow();
	}
	
}
