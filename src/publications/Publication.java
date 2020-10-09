/** 
 * A Publication is an object that can be used to receive and broadcast messages.
 * It is a thin wrapper built on top of a ConcurrentHashMap.
 * 
 * A Publication is a collection of subscribers. Each subscriber has an ID (type <K>)
 *   and a function (type java.util.function.Consumer<V>).
 *   
 * Whenever a Publication receives a message (type <V>), it calls each subscriber's 
 *   function on that message. 
 *   
 * To add a new subscriber, call subscribe(id, f), providing the subscriber's ID <K>
 *   and function <Consumer<V>> as args.
 *   
 * To remove a subscriber, call unsubscribe(id).
 * 
 * To publish a message to all subscribers, call publish(message), providing a message <V>
 *   that you wish the subscribers to consume.
 *   
 * A Publication can have an expiryCount or an expiryMs, which represent the max number
 *   of messages a subscriber can consume before being unsubcribed, or the max number of
 *   milliseconds a subscriber can be subscribed for respectively. To add expiration to a
 *   Publication, call withExpiryMs or withExpiryCount on your Publication. 
 * 
 * 2020-10-08 : csulpizi
 */

package publications;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.LinkedList;

public class Publication<K,V> {
	// Subscription class holds all of the required data for a Subscription.
	private class Subscription<T> {
		private Date initTs = new Date();
		private Consumer<T> f;
		private int msgCount = 0;
		private int expiryCount = -1;
		private int expiryMs = -1;
		
		Subscription(Consumer<T> f) {
			this.f = f;
		}
		public void setExpiryCount(int expiryCount) {
			this.expiryCount = expiryCount;
		}
		public void setExpiryMs(int expiryMs) {
			this.expiryMs = expiryMs;
		}
		public Consumer<T> getConsumer() {
			return this.f;
		}
		public void inc() {
			this.msgCount++;
		}
		public void touch() {
			this.initTs = new Date();
			this.msgCount = 0;
		}
		public boolean isExpired(int channelExpiryMs, int channelExpiryCount) {
			if(channelExpiryMs >= 0 
					&& (new Date().getTime() - this.initTs.getTime()) > channelExpiryMs) {
				return true;
			} else if (this.expiryMs >= 0 
					&& (new Date().getTime() - this.initTs.getTime()) > this.expiryMs) {
				return true;
			} else if (channelExpiryCount >= 0 && msgCount >= channelExpiryCount){
				return true;
			} else if (this.expiryCount >= 0 && msgCount >= this.expiryCount){
				return true;
			} else {
				return false;	
			}
		}
	}
	
	private Date initTs = new Date();
	private ConcurrentHashMap<K,Subscription<V>> subscriptions = 
			new ConcurrentHashMap<K,Subscription<V>>();
	private int expiryMs = -1;
	private int expiryCount = -1;
	private int totalMessages = 0;
	private int totalExpiredSubscribers = 0;
	private LinkedList<V> history = new LinkedList<V>();
	private int recordSize = 1;
	
	// History
	public void setRecordSize(int recordSize) {
		this.recordSize = recordSize;
	}
	private void recordMessage(V message) {
		if (this.recordSize > 0) {
			history.addLast(message);
			while (this.history.size() > this.recordSize) {
				history.removeFirst();
			}
		}
	}
	
	// Handlers
	public void subscribe(K id, Consumer<V> f) {
		Subscription<V> s = new Subscription<V>(f);
		this.subscriptions.put(id, s);
	}
	public void unsubscribe(K id) {
		this.subscriptions.remove(id);
	}
	public void unsubscribeAll() {
		this.subscriptions.clear();
	}
	public void publish(V message) {
		recordMessage(message);
		this.totalMessages++;
		for(K id : subscriptions.keySet()) {
			Subscription<V> s = this.subscriptions.get(id);
			if (s.isExpired (expiryMs, expiryCount)) {
				this.totalExpiredSubscribers++;
				this.unsubscribe(id);
			}
			else { 
				s.inc();
				s.getConsumer().accept(message);
			}
		}
	}
	
	// Expiry
	public void setExpiryMs(int expiryMs) {
		this.expiryMs = expiryMs;
	}
	public void setExpiryCount(int expiryCount) {
		this.expiryCount = expiryCount;
	}
	public void setSubscriberExpiryCount(K id, int expiryCount) {
		subscriptions.get(id).setExpiryCount(expiryCount);
	}
	public void setSubscriberExpiryMs(K id, int expiryMs) {
		subscriptions.get(id).setExpiryMs(expiryMs);
	}
	public void touchSubscriber(K id) {
		subscriptions.get(id).touch();
	}
	
	// Utilities
	public Date getInitTime() {
		return this.initTs;
	}
	public long uptime() {
		return new Date().getTime() - this.initTs.getTime();
	}
	public long countSubscribers() {
		return this.subscriptions.mappingCount();
	}
	public int countMessages() {
		return this.totalMessages;
	}
	public int countExpiredSubscribers() {
		return this.totalExpiredSubscribers;
	}
	public V getLastMessage() {
		return history.peekLast();
	}
	public LinkedList<V> getAllRecordedMessages() {
		return history;
	}
}