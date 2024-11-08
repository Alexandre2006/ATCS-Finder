import java.io.BufferedReader;
import java.io.IOException;

/**
 * Finder
 * A puzzle written by Zach Blick
 * for Adventures in Algorithms
 * At Menlo School in Atherton, CA
 *
 * Completed by: Alexandre Haddad-Delaveau
 **/

public class Finder {
    private static final String INVALID = "INVALID KEY";

    // Configuration (for testing)
    private static final double MAX_LOAD = 0.50;
    private static final int STARTING_SIZE = 8388608; // 2^23 - Seems to timeout with anything lower :(

    private Pair[] hashMap;
    int currentSize;
    int slotsUsed;

    public Finder() {
        currentSize = STARTING_SIZE;
        hashMap = new Pair[currentSize];
        slotsUsed = 0;
    }

    public void buildTable(BufferedReader br, int keyCol, int valCol) throws IOException {
        // Read the file, line by line
        String line;

        while ((line = br.readLine()) != null) {
            // Split the line by commas (CSV)
            String[] parts = line.split(",");

            // Insert into the hash table
            insert(parts[keyCol], parts[valCol]);
        }
        br.close();
    }

    public String query(String key){
        // Calculate the hash of the key
        int hash = calculateHash(key);
        int index = hash;

        // Linear Probing
        for (int i = 0; i < currentSize; i++) {
            // Calculate index (and prevent overflow)
            index = (hash + i) % currentSize;

            // Check for empty slot
            if (hashMap[index] == null) {
                return INVALID;
            }

            // Check for matching key
            if (hashMap[index].key.equals(key)) {
                return hashMap[index].value;
            }
        }

        // Return invalid key if not found
        return INVALID;
    }

    private void rebuildTable() {
        currentSize = currentSize << 1;
        Pair[] oldHashMap = hashMap;
        hashMap = new Pair[currentSize];

        // Save start time
        long startTime = System.currentTimeMillis();

        // Log rebuild
        System.out.println("Rebuilding table to size: " + currentSize);

        for (Pair pair : oldHashMap) {
            if (pair != null) {
                insert(pair);
            }
        }

        // Log time taken
        System.out.println("Rebuild took: " + (System.currentTimeMillis() - startTime) + "ms");
    }

    private void insert(String key, String value) {
        // Rebuild if necessary (only needs to be run in this method, not the main insert as we don't need to check when rebuilding)
        if ((double) slotsUsed / currentSize >= MAX_LOAD) {
            rebuildTable();
        }

        // Calculate the hash of the key
        int hash = calculateHash(key);

        // Insert the key and value
        insert(new Pair(key, value));

        // Increase slots used
        slotsUsed++;
    }

    private void insert(Pair pair) {
        // Linear Probing
        for (int i = 0; i < currentSize; i++) {
            // Calculate index (and prevent overflow)
            int index = (calculateHash(pair.key) + i) % currentSize;

            // Check for empty slot
            if (hashMap[index] == null) {
                hashMap[index] = pair;
                return;
            }
        }
    }

    private int calculateHash(String input) {
        long hashCode = 0;
        byte[] bytes = input.getBytes();

        // Loop over bytes
        for (byte b : bytes) {
            hashCode = (hashCode * 128 + b) % currentSize;
        }

        if (hashCode < 0) {
            hashCode *= -1;
        }

        return (int) hashCode;
    }

    private record Pair(String key, String value) {} // Had 0 clue this existed, but IntelliJ suggested it!
}