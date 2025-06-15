package communication;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Current;
import com.zeroc.IceGrid.QueryPrx;

import Contract.Candidate;
import Contract.ConsultService;
import Contract.ConsultServicePrx;
import model.CacheEntry;

public class ProxyCacheImpl implements ConsultService {

  private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();

  private static final int MAX_CACHE_SIZE = 10000;
  private static final long DEFAULT_TTL_MINUTES = 5;
  private static final long VOTER_DATA_TTL_MINUTES = 30;

  private ConsultServicePrx backendService;
  private final ScheduledExecutorService cleanupExecutor = Executors.newSingleThreadScheduledExecutor();

  public ProxyCacheImpl(Communicator communicator) {
    getbackendService(communicator);
    startCacheCleanupTask();
  }

  private void getbackendService(Communicator communicator) {
    System.out.println("[INFO] Going to fetch a backend service");
    QueryPrx query = QueryPrx.checkedCast(communicator.propertyToProxy("IceQueryGrid/Query"));
    backendService = ConsultServicePrx.checkedCast(query.findObjectByType("::Contract::ConsultService"));
    System.out.println("[INFO] backendService Adquired");
  }

  private void startCacheCleanupTask() {
    cleanupExecutor.scheduleAtFixedRate(this::cleanExpiredEntries, 1, 1, TimeUnit.MINUTES);
  }

  private void cleanExpiredEntries() {
    cache.entrySet().removeIf(entry -> entry.getValue().isExpired());

    if (cache.size() > MAX_CACHE_SIZE) {
      removeOldestEntries();
    }
  }

  private void removeOldestEntries() {
    int targetSize = (int) (MAX_CACHE_SIZE * 0.8);

    List<Map.Entry<String, CacheEntry>> entries = new ArrayList<>(cache.entrySet());
    entries.sort(Comparator.comparing(e -> e.getValue().getTimestamp()));

    for (int i = 0; i < entries.size() - targetSize; i++) {
      cache.remove(entries.get(i).getKey());
    }
  }

  private long determineTTL(String sqlQuery) {
    String query = sqlQuery.toLowerCase();
    if (query.contains("votantes") || query.contains("cedula")) {
      return VOTER_DATA_TTL_MINUTES;
    }
    return DEFAULT_TTL_MINUTES;
  }

  private String generateCacheKey(String sqlQuery, String[] params) {
    try {
      StringBuilder sb = new StringBuilder();
      sb.append(sqlQuery.trim().toLowerCase());

      if (params != null) {
        for (String param : params) {
          sb.append("|").append(param != null ? param.trim() : "null");
        }
      }

      MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] hashBytes = md.digest(sb.toString().getBytes(StandardCharsets.UTF_8));

      StringBuilder hashString = new StringBuilder();
      for (byte b : hashBytes) {
        hashString.append(String.format("%02x", b));
      }

      return hashString.toString();

    } catch (Exception e) {
      return sqlQuery + Arrays.toString(params);
    }
  }

  public void clearCache() {
    cache.clear();
  }

  public void shutdown() {
    cleanupExecutor.shutdown();
    try {
      if (!cleanupExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
        cleanupExecutor.shutdownNow();
      }
    } catch (InterruptedException e) {
      cleanupExecutor.shutdownNow();
    }
  }

  @Override
  public String getVotingLocation(String voterId, Current current) {
    System.out.println("[INFO] GET VOTING LOCATION CALLED FROM CACHE PROXY");
    String cacheKey = generateCacheKey("getVotingLocation", new String[] { voterId });
    CacheEntry cached = cache.get(cacheKey);

    if (cached != null && !cached.isExpired()) {
      return cached.getValue();
    }

    String response = backendService.getVotingLocation(voterId);

    // Hashear la respuesta (sin retornarla)
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hashBytes = digest.digest(response.getBytes(StandardCharsets.UTF_8));

      StringBuilder hexHash = new StringBuilder();
      for (byte b : hashBytes) {
        hexHash.append(String.format("%02x", b));
      }

      String hash = hexHash.toString();

      // Aquí puedes loguear, almacenar o auditar el hash
      System.out.println("Hash de respuesta para " + voterId + ": " + hash);

    } catch (Exception e) {
      System.err.println("Error generando hash de respuesta para " + voterId);
      e.printStackTrace();
    }

    // Cachear el resultado original
    long ttl = determineTTL("getVotingLocation");
    cache.put(cacheKey, new CacheEntry(response, ttl, TimeUnit.MINUTES));

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
