import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


public class ZcMain extends AbstractPrimeFinder{
	
	private final int poolsize;
	private final int numberOfparts;
	
	public ZcMain(final int poolsize, final int theNumberOfParts){
		this.poolsize = poolsize;
		this.numberOfparts = theNumberOfParts;
	}
	
	public static void main(String[] args) {
//		new ZcMain().timeAndCompute(1000000000);
		new ZcMain(4, 4).timeAndCompute(100000);;
	}

	@Override
	public int countPrimes(int number) {
		int count = 0;
		final List<Callable<Integer>> partitions = new ArrayList<Callable<Integer>>();
		final int chunkPerPartition = number / numberOfparts;
		
		for(int i = 0; i < chunkPerPartition; i++){
			final int lower = (i*chunkPerPartition)+ 1;
			final int upper = (i == chunkPerPartition - 1)?number:lower + chunkPerPartition - 1;
			partitions.add(new Callable<Integer>() {
				public Integer call(){
					return countPrimesRange(lower, upper);
				}
			});
		}
		
		final ExecutorService executorPool = Executors.newFixedThreadPool(poolsize);
		try {
			final List<Future<Integer>>  resultFromParts = executorPool.invokeAll(partitions,10000,TimeUnit.SECONDS);
			executorPool.shutdown();
			
			for(final Future<Integer> result : resultFromParts){
				try {
					count += result.get();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return count;
	}
	
}
