package communication;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Pattern;

import com.zeroc.Ice.*;
import com.zeroc.Ice.Exception;
import com.zeroc.IceGrid.QueryPrx;
import Contract.*;

import model.CacheEntry;

public class ProxyCacheImpl implements ConsultService {

  private final ConcurrentHashMap<String, CacheEntry> cache = new ConcurrentHashMap<>();
  private static final int MAX_CACHE_SIZE = 10_000;
  private static final long DEFAULT_TTL_MINUTES = 5;
  private static final long VOTER_DATA_TTL_MINUTES = 30;

  private final ScheduledExecutorService cleanupExecutor = Executors.newSingleThreadScheduledExecutor();
  private static final Pattern VOTER_QUERY_PATTERN = Pattern.compile("votantes|cedula", Pattern.CASE_INSENSITIVE);

  private ConsultServicePrx backendService;

  // Thread-local MessageDigest para evitar sincronización costosa
  private static final ThreadLocal<MessageDigest> md5Digest = ThreadLocal.withInitial(() -> {
    try {
      return MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  });

  private static final ThreadLocal<MessageDigest> sha256Digest = ThreadLocal.withInitial(() -> {
    try {
      return MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  });

  public ProxyCacheImpl(Communicator communicator) {
    initBackendService(communicator);
    cleanupExecutor.scheduleAtFixedRate(this::cleanExpiredEntries, 1, 1, TimeUnit.MINUTES);
  }

  private void initBackendService(Communicator communicator) {
    QueryPrx query = QueryPrx.checkedCast(communicator.stringToProxy("IceQuerySystem/Query"));
    ObjectPrx queryFind = query.findObjectById(Util.stringToIdentity("ConsultService"));
    backendService = ConsultServicePrx.checkedCast(queryFind);
  }

  private void cleanExpiredEntries() {
    cache.entrySet().removeIf(entry -> entry.getValue().isExpired());

    if (cache.size() > MAX_CACHE_SIZE) {
      PriorityQueue<Map.Entry<String, CacheEntry>> pq = new PriorityQueue<>(
          Comparator.comparingLong(e -> e.getValue().getTimestamp()));
      pq.addAll(cache.entrySet());

      int entriesToRemove = cache.size() - (int) (MAX_CACHE_SIZE * 0.8);
      for (int i = 0; i < entriesToRemove && !pq.isEmpty(); i++) {
        cache.remove(pq.poll().getKey());
      }
    }
  }

  private long determineTTL(String sqlQuery) {
    return VOTER_QUERY_PATTERN.matcher(sqlQuery).find() ? VOTER_DATA_TTL_MINUTES : DEFAULT_TTL_MINUTES;
  }

  private String generateCacheKey(String sqlQuery, String[] params) {
    StringBuilder sb = new StringBuilder(sqlQuery.trim());
    if (params != null) {
      for (String param : params) {
        sb.append('|').append(param != null ? param.trim() : "null");
      }
    }

    MessageDigest digest = md5Digest.get();
    byte[] hashBytes = digest.digest(sb.toString().getBytes(StandardCharsets.UTF_8));
    StringBuilder hex = new StringBuilder(hashBytes.length * 2);
    for (byte b : hashBytes) {
      hex.append(String.format("%02x", b));
    }
    return hex.toString();
  }

  private void logHashedResponse(String response, String voterId) {
    try {
      MessageDigest digest = sha256Digest.get();
      byte[] hashBytes = digest.digest(response.getBytes(StandardCharsets.UTF_8));
      StringBuilder hex = new StringBuilder(hashBytes.length * 2);
      for (byte b : hashBytes) {
        hex.append(String.format("%02x", b));
      }
      System.out.println("Hash de respuesta para " + voterId + ": " + hex);
    } catch (Exception e) {
      System.err.println("Error generando hash de respuesta para " + voterId);
      e.printStackTrace();
    }
  }

  public void clearCache() {
    cache.clear();
  }

  public void shutdown() {
    cleanupExecutor.shutdown();
    try {
      if (!cleanupExecutor.awaitTermination(3, TimeUnit.SECONDS)) {
        cleanupExecutor.shutdownNow();
      }
    } catch (InterruptedException e) {
      cleanupExecutor.shutdownNow();
    }
  }

  @Override
  public String getVotingLocation(String voterId, Current current) {
    final String sqlKey = "getVotingLocation";
    final String[] params = new String[] { voterId };
    final String cacheKey = generateCacheKey(sqlKey, params);

    CacheEntry cached = cache.get(cacheKey);
    if (cached != null && !cached.isExpired()) {
      return cached.getValue();
    }

    String response = backendService.getVotingLocation(voterId);

    logHashedResponse(response, voterId);
    cache.put(cacheKey, new CacheEntry(response, determineTTL(sqlKey), TimeUnit.MINUTES));

    return response;
  }

  @Override
  public void setCandidates(Candidate[] candidates, Current current) {
    backendService.setCandidates(candidates);
  }

  @Override
  public Candidate[] getCandidates(Current current) {
    return backendService.getCandidates();
  }

  @Override
  public String getResults(Current current) {
    return backendService.getResults();
  }

  @Override
  public String getResumeCSV(Current current) {
    return backendService.getResumeCSV();
  }

  @Override
  public String getPartialCSV(Current current) {
    return backendService.getPartialCSV();
  }
}
