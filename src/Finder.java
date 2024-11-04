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

    private static final String INVALID = "INVALID KEY";
    private final Bucket[] hashMap = new Bucket[9999991]; // Largest 7-digit prime number, I found that this was faster than the largest 6/8 digit prime number

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
            Bucket bucket = hashMap[hash];

            // If the bucket is null, create a new bucket
            if (bucket == null) {
                bucket = new Bucket();
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
        Bucket bucket = hashMap[hash];

        // Verify bucket is not null
        if (bucket == null) {
            return INVALID;
        }

        // If there is only one pair in the bucket, return the value
        return bucket.get(key);
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

    private static class Bucket {
        public ArrayList<Pair> pairs;

        public Bucket() {
            pairs = new ArrayList<>();
        }

        public void add(Pair pair) {
            pairs.add(pair);
        }

        public String get(String key) {
            if (pairs.size() == 1) {
                return pairs.getFirst().value;
            } else {
                for (Pair pair : pairs) {
                    if (pair.key.equals(key)) {
                        return pair.value;
                    }
                }
                return INVALID;
            }
        }
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