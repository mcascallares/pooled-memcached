package org.matias.memcached;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.MemcachedClient;

/**
 * Memcached connection pool. It improves the performance pre-creating many
 * connection as specified and reusing them. It exposes the same interface used
 * by Spy memcached client.
 * 
 * 
 * @author matias
 * 
 */
public class PooledMemcachedClient {

	/**
	 * Size of the connection pool.
	 */
	private int connectionPoolSize = 0;

	/**
	 * Pool of connections.
	 */
	private MemcachedClient[] connectionPool = null;

	/**
	 * Initializez the connection pool.
	 * 
	 * @param connectionPoolSize
	 *            Number of connection to holds into the pool. This number is
	 *            alocated just once at creation.
	 * @param serverAddresses
	 *            telnet instancees in server:port format separated by
	 *            space(e.g. server1:11211 server2:11211).
	 * 
	 */
	public PooledMemcachedClient(int connectionPoolSize, String serverAddresses) {
		try {
			connectionPool = new MemcachedClient[connectionPoolSize];
			for (int i = 0; i < connectionPoolSize; i++) {
				connectionPool[i] = new MemcachedClient(new BinaryConnectionFactory(),
						AddrUtil.getAddresses(serverAddresses));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		this.connectionPoolSize = connectionPoolSize;
	}

	/**
	 * Returns a connected client to retrieve cache items.
	 * 
	 * @return Connected client ready to be used.
	 */
	public MemcachedClient getCache() {
		return connectionPool[(int) (Math.random() * connectionPoolSize)];
	}

}