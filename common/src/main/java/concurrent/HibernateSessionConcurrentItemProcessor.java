package concurrent;

public interface HibernateSessionConcurrentItemProcessor<T> extends ConcurrentItemProcessor<T> {
	public void releaseResource(Exception ex);
}
