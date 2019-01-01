package venus.frames.mainframe.ncache;

public final class CacheExpireException extends Exception {

    private Object cacheContent = null;

    public CacheExpireException(String message, Object cacheContent) {
	super(message);
	this.cacheContent = cacheContent;
    }

    public CacheExpireException(Object cacheContent) {
	super();
	this.cacheContent = cacheContent;
    }

    public Object getCacheContent() {
	return cacheContent;
    }

}
