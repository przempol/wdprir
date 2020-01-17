package lab1;

public class Zad2 {

	public static void main(String[] args) {
		task2();
	}
	
	public static void task2() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("Hello world");
			}
		});	
		
		t.start();
	}
}
