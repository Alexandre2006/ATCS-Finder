import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Finder
 * A puzzle written by Zach Blick
 * for Adventures in Algorithms
 * At Menlo School in Atherton, CA
 *
 * Completed by: Alexandre Haddad-Delaveau
 **/

public class Finder {
    int currentSize = 2;
    int slotsUsed = 0;

    private static final String INVALID = "INVALID KEY";
    private String[] hashMap = new String[currentSize];

    public Finder() {}

    public void buildTable(BufferedReader br, int keyCol, int valCol) throws IOException {

        // Read the file, line by line
        String line;
        while ((line = br.readLine()) != null) {
            // Split the line into columns
            String[] columns = line.split(",");

            // Get the key and the value columns
            String key = columns[keyCol];
            String value = columns[valCol];

            // Calculate the hash of the key
            int hash = calculateHash(key);

            // Add value to hashMap
            boolean foundLocation = false;
            while (!foundLocation) {
                if (hashMap[hash] == null) {
                    hashMap[hash] = value;
                    foundLocation = true;
                    slotsUsed++;
                } else {
                    // Increment hash
                    hash++;

                    // Make sure hash is not greater than length of hashMap
                    if (hash >= currentSize) {
                        hash = 0;
                    }
                }
            }

            // Check usage
            if ((double) slotsUsed / currentSize >= 0.50) {
                rebuildTable();
            }
        }
        br.close();
    }

    public String query(String key){
        // Calculate the hash of the key
        int hash = calculateHash(key);
        int initialHash = hash;

        while (true) {
            if (hashMap[hash] == null) {
                return INVALID;
            } else if (hashMap[hash].equals(key)) {
                return hashMap[hash];
            } else {
                // Increment hash
                hash++;

                // Make sure hash is not greater than length of hashMap
                if (hash >= currentSize) {
                    currentSize = 0;
                }
            }
        }
    }

    private void rebuildTable() {
        currentSize = currentSize << 1;
        String[] newHashMap = new String[currentSize];

        // Loop over existing elements
        for (int i = 0; i < hashMap.length; i++) {
            if (hashMap[i] != null) {
                int hash = calculateHash(hashMap[i]);

                // Add to map
                boolean foundLocation = false;
                while (!foundLocation) {
                    if (newHashMap[hash] == null) {
                        newHashMap[hash] = hashMap[i];
                        foundLocation = true;
                    } else {
                        // Increment hash
                        hash++;

                        // Make sure hash is not greater than length of hashMap
                        if (hash >= currentSize) {
                            hash = 0;
                        }
                    }
                }
            }
        }

        // Apply new HashMap
        hashMap = newHashMap;


    }

    private int calculateHash(String input) {
        long hashCode = 0;
        byte[] bytes = input.getBytes();

        // Loop over bytes
        for (byte b : bytes) {
            hashCode = (hashCode * 128 + b) % (currentSize - 1);
        }

        return (int) hashCode;
    }
}