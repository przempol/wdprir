package lab2;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicIntegerArray;

import javax.imageio.ImageIO;


public class PanKrawiecki{
    public static void main(String[] args) throws IOException, InterruptedException {
    	String site = "http://www.if.pw.edu.pl/~mrow/dyd/wdprir/";
    	
    	downloadPNGs(site);
        downloadParallelPNGs(site);
    	
    }
	
    
    public static void downloadPNGs(String website) throws IOException {
    	String site = website;
    	
    	Document doc = Jsoup.connect(site).get();
    	Elements links = doc.select("a[href$=.png]");	// looking for pngs
    	
    	long startTime = System.currentTimeMillis();
    	
        for(Element el : links ){
			URL url = new URL(site + el.attr("href"));
			String destiny = el.attr("href");

			// downloading image
			BufferedImage img = ImageIO.read(url);
			for (int ii = 0; ii < 50; ii++) {
				img = addGaussianBlur(img);
			}
			
			//saving image
			File file = new File(destiny);
			ImageIO.write(img, "png", file);
            System.out.println("pobrano i zapisano z szumem obrazek " + el.attr("href"));
        }

        long endTime = System.currentTimeMillis();
        double time = (double) (endTime - startTime)/1000.;
        System.out.println("pobieranie sekwencyjne trwalo " + Double.toString(time) + " s\n");
    }
    
    
    public static void downloadParallelPNGs(String website) throws IOException, InterruptedException {
    	String site = website;
    	
    	Document doc = Jsoup.connect(site).get();
    	Elements links = doc.select("a[href$=.png]");	// looking for pngs
    	
    	AtomicIntegerArray flags = new AtomicIntegerArray(links.size());
    	
    	long startTime = System.currentTimeMillis();
    	
		int noThreads = Runtime.getRuntime().availableProcessors();
		CustomThread[] threads = new CustomThread[noThreads];
		Thread[] threadsPure = new Thread[noThreads];
    	
        for (int jj = 0; jj < noThreads; jj++) {
			// creating custom threads
			threads[jj] = new CustomThread(flags, links, site);
			// creating pure threads, which will be working with customized method "run"
			threadsPure[jj] = new Thread(threads[jj]);
			threadsPure[jj].start();
		}
        
        // joining threads, so they will wait until the last one end 
        for (int jj = 0; jj < noThreads; jj++) {
			threadsPure[jj].join();
		}
        
        long endTime = System.currentTimeMillis();
        double time = (double) (endTime - startTime)/1000.;
        System.out.println("pobieranie rownolegle trwalo " + Double.toString(time) + " s\n");
    }
    
    
    public static class CustomThread implements Runnable{
	    AtomicIntegerArray flags;
	    Elements links;
	    String site;
	    
		public CustomThread(AtomicIntegerArray flags, Elements links, String site) {
	    	this.flags = flags;
	    	this.links = links;
	    	this.site = site;
		}
	    
		@Override
		public void run() {
			for (int ii = 0; ii < flags.length(); ii++) {
				if(flags.getAndSet(ii, 1) == 0) {
					try {
						URL url = new URL(site + links.get(ii).attr("href"));
						String destiny = links.get(ii).attr("href");

						// downloading image
						BufferedImage img = ImageIO.read(url);
						for (int jj = 0; jj < 50; jj++) {
							img = addGaussianBlur(img);
						}
						//saving image
						File file = new File(destiny);
						ImageIO.write(img, "png", file);
						
						System.out.println("Pobrano i zapisano z szumem obrazek " + destiny);
					} catch (IOException e) {}
				}
			}
		}
    	
    }
    
    
    public static BufferedImage addGaussianBlur(BufferedImage a) {
    	float[] matrix = { 1 / 16f, 1 / 8f, 1 / 16f, 
    			1 / 8f, 1 / 4f, 1 / 8f, 
    			1 / 16f, 1 / 8f, 1 / 16f, };
    	BufferedImageOp op = new ConvolveOp(new Kernel(3, 3, matrix));
    	BufferedImage blurredImage = op.filter(a, null);
    	blurredImage = op.filter(blurredImage, null);
		blurredImage = op.filter(blurredImage, null);
		return blurredImage;
    }
    
}
