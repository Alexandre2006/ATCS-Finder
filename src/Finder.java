import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Finder
 * A puzzle written by Zach Blick
 * for Adventures in Algorithms
 * At Menlo School in Atherton, CA
 *
 * Completed by: [YOUR NAME HERE]
 **/

public class Finder {

    private static final String INVALID = "INVALID KEY";
    private ArrayList<Pair>[] hashMap = new ArrayList[999983];

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

            // Create a pair object from the key and value
            Pair pair = new Pair(key, value);

            // Get the bucket for the hash
            ArrayList<Pair> bucket = hashMap[hash];

            // If the bucket is null, create a new bucket
            if (bucket == null) {
                bucket = new ArrayList<Pair>();
                hashMap[hash] = bucket;
            }

            // Add the pair to the bucket
            bucket.add(pair);
        }
        br.close();
    }

    public String query(String key){
        // Calculate the hash of the key
        int hash = calculateHash(key);

        // Get the bucket for the hash
        ArrayList<Pair> bucket = hashMap[hash];

        // If the bucket is null, return INVALID
        if (bucket == null) {
            return INVALID;
        }

        // If there is only one pair in the bucket, return the value
        if (bucket.size() == 1) {
            return (String) bucket.getFirst().value;
        }

        // Otherwise, find the pair with the matching key
        for (Pair pair : bucket) {
            if (pair.key.equals(key)) {
                return (String) pair.value;
            }
        }

        // If there is no matching key, return INVALID
        return INVALID;
    }

    private int calculateHash(String input) {
        long hashCode = 0;
        int modulus = hashMap.length;
        byte[] bytes = input.getBytes();

        // Loop over bytes
        for (byte b : bytes) {
            hashCode = (hashCode * 256) % modulus;
            hashCode = (hashCode + b) % modulus;
        }

        return (int) hashCode;
    }

    private static class Pair {
        public String key;
        public String value;

        public Pair(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }
}