package com.example.beeline;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootApplication
public class BeelineApplication {

    private static final String GET_URL = "https://procodeday-01.herokuapp.com/meet-up/get-country-list";
    private static final String POST_URL = "https://procodeday-01.herokuapp.com/meet-up/post-request";

    private static OkHttpClient client = new OkHttpClient();
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        SpringApplication.run(BeelineApplication.class, args);

        try {
            String responseJson = sendGetRequest(GET_URL);

            List<City> cities = objectMapper.readValue(responseJson, objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, City.class));

            Map<String, List<String>> citiesByCountry = cities.stream()
                    .collect(Collectors.groupingBy(City::getCountry,
                            Collectors.mapping(City::getCity, Collectors.toList())));

            citiesByCountry.values().forEach(Collections::sort);

            List<Result> results = citiesByCountry.entrySet().stream()
                    .map(entry -> new Result(entry.getKey(), entry.getValue(), String.valueOf(entry.getValue().size())))
                    .collect(Collectors.toList());

            Student student = new Student("Kyzzhibek Orozbekova", "0703517540", "https://github.com/jifon");

            PostData postData = new PostData(student, results);

            String postJson = objectMapper.writeValueAsString(postData);

            System.out.println(sendPostRequest(POST_URL, postJson));
            System.out.println("Send");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String sendGetRequest(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.body().string();
            }
        }
        throw new IOException("GET request failed: " + url);
    }

    private static String sendPostRequest(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, okhttp3.MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();


        Response response = client.newCall(request).execute();
        return response.body().toString();

    }

}
