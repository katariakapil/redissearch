import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.redisearch.*;
import io.redisearch.client.Client;
import io.redisearch.client.SuggestionOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RedisSearchClient {

    //docker pull redislabs/redisearch

    public static void main(String[] args) {

        //Client client = createData();

       fetchData();




    }

    private static void fetchData() {
        Client client = new Client("redisSearchIndex", "localhost", 6379);


        List<Suggestion> payloads = client.getSuggestion("pain",
                SuggestionOptions.builder().with(SuggestionOptions.With.PAYLOAD_AND_SCORES).build());


        System.out.println("Data "+payloads);

        ObjectMapper json = new ObjectMapper();
        String searchKey = "";
        try {
            SearchData searchData = json.readValue(payloads.get(0).getPayload(),SearchData.class);
            System.out.println("Found in Category "+searchData.getCategory());
            searchKey = payloads.get(0).getString();

            System.out.println("Search for "+ searchKey);

            // Creating a complex query
            Query q = new Query(searchKey)
                  //  .addFilter(new Query.NumericFilter("weight", 0, 20))
                    .limit(0,5);

            // actual search

            SearchResult res = client.search(q);

            System.out.println("Final result "+res.docs);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Client createData() {
        Client client = new Client("redisSearchIndex", "localhost", 6379);
        try {

         Schema sc = new Schema()
                 .addTextField("keyword", 5.0)
                 .addTextField("body", 1.0);
                // .addNumericField("weight");

         client.createIndex(sc, Client.IndexOptions.Default());

            Map<String, Object> fields = new HashMap<>();
            fields.put("keyword", "pain head");
            fields.put("body", "problem in head");
         //   fields.put("weight", 15);

            client.addDocument("doc2", fields);

        } catch(Exception e) {
            System.out.println("Error"+e);
        }


        ObjectMapper mapper = new ObjectMapper();


        String payload = "";
        try {
            SearchData data = new SearchData("1","pain head" , "Problems");
            payload = mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println("payload is "+payload);
        Suggestion suggestion = Suggestion.builder().str("pain head").payload(payload).score(0.2).build();

        client.addSuggestion(suggestion, false);

        client.addSuggestion(suggestion.toBuilder().str("pain body").payload("").build(), false) ;
        //client.addSuggestion(suggestion.toBuilder().str("COUNT_ANOTHER").score(1).payload(null).build(), false);
        //Suggestion noScoreOrPayload = Suggestion.builder().str("COUNT NO PAYLOAD OR COUNT").build();
        //client.addSuggestion(noScoreOrPayload, true);




        return client;
    }

}
