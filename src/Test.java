import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedTransferQueue;

public class Test {
	public static void main(String[] args) {

		final BlockingQueue<Long> queue = new LinkedTransferQueue<Long>();

		Runnable offerTask = new Runnable() {
			public void run() {
				queue.offer(8L);
				System.out.println("offerTask thread has gone!");
			}
		};

		Runnable takeTask = new Runnable() {
			public void run() {
				try {
					System.out.println(Thread.currentThread().getId() + " "
							+ queue.take());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};

		Runnable takeTaskInterrupted = new Runnable() {
			public void run() {
				Thread.currentThread().interrupt();
				try {
					System.out.println(Thread.currentThread().getId() + " "
							+ queue.take());
				} catch (InterruptedException e) {
					System.out.println(e + " " + Thread.currentThread().getId());
				}
			}
		};

		new Thread(offerTask).start();
		new Thread(takeTask).start();
		new Thread(takeTaskInterrupted).start();
	}
}
