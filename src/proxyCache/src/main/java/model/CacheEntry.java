package model;

import java.util.concurrent.TimeUnit;

public class CacheEntry {
  private final String value;
  private final long timestamp;
  private final long ttlMillis;

  public CacheEntry(String value, long ttlDuration, TimeUnit timeUnit) {
    this.value = value;
    this.timestamp = System.currentTimeMillis();
    this.ttlMillis = timeUnit.toMillis(ttlDuration);
  }

  public String getValue() {
    return value;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public boolean isExpired() {
    return System.currentTimeMillis() - timestamp > ttlMillis;
  }
}
