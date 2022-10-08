package org.auto.id.generation;

import java.time.Duration;

import com.couchbase.client.core.env.SecurityConfig;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.ClusterOptions;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.env.ClusterEnvironment;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.kv.CounterResult;
import com.couchbase.client.java.kv.GetResult;
import com.couchbase.client.java.kv.IncrementOptions;
import com.couchbase.client.java.kv.MutationResult;
import com.couchbase.client.java.query.QueryResult;

public class Main {
    // Update these variables to point to your Couchbase Capella instance and
    // credentials.
    static String connectionString = "localhost";
    static String username = "Administrator";
    static String password = "password";
    static String bucketName = "Users";

    public static void main(String... args) {
        Cluster cluster = Cluster.connect(connectionString,"Administrator","password");
        Bucket bucket = cluster.bucket(bucketName);

//        Cluster cluster = Cluster.connect(connectionString, username, password);
//
//
//        // get a bucket reference
//        Bucket bucket = cluster.bucket(bucketName);
//        bucket.waitUntilReady(Duration.parse("PT10S"));

        // get a user defined collection reference
        //    Scope scope = bucket.scope("_default");
        //    Collection collection = scope.collection("_default");

        // Upsert Document

       /* // Get Document
        GetResult getResult = bucket.defaultCollection().get("21st_amendment_brewery_cafe");

        String name = getResult.contentAsObject().getString("name");
        System.out.println(name); // name == "mike"

        // Call the query() method on the cluster object and store the result.
        QueryResult result = cluster.query("select \"Hello World\" as greeting");

        // Return the result rows with the rowsAsObject() method and print to the
        // terminal.
        System.out.println(result.rowsAsObject());*/


//        Collection users = bucket.collection("Users");
        Collection users = bucket.collection("users");

        int i=0;

        while (i < 9) {
            String id= "0";
            CounterResult myID = users.binary().increment(id, IncrementOptions.incrementOptions().initial(123456789));
            String generatedID = String.valueOf(myID.content());
            System.out.println("************ myID.content()************** " + generatedID);
//            bucket.defaultCollection().binary().increment(id, IncrementOptions.incrementOptions().delta(1));
            System.out.println("************ myID.content()************** " + generatedID);

            MutationResult upsertResult = users.upsert(
                    id + myID.content(),
                    JsonObject.create().put("id", generatedID)
                    .put("name", "Tyler's AirBnB"+generatedID)
                            .put("country", "Canada"+generatedID)
                            .put("type", "hotel"+generatedID)
                            .put("state", "California"+generatedID)
                            .put("phone", "612-280-1303")
            );
            System.out.println("i= "+ i +"************ documents inserted  myID.content()************** " + generatedID);
         i++;
        }
    }
}
