package com.example.weatherkostya.services;

import com.example.weatherkostya.model.WeatherModel;
import org.springframework.stereotype.Service;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

@Service
public class WeatherService {

    private static final String OPENWEATHERMAP_API_KEY = "25244cb4c913eff3b8268aa22e703998";
    private static final String WEATHERBIT_API_KEY = "b1cdc1160ef342fdb452d03747fb767d";

    public Map<String, Object> getWeather(String city, String source) throws JSONException {
        Map<String, Object> weatherData = new HashMap<>();

        if (source.equalsIgnoreCase("openweathermap")) {
            weatherData = getOpenWeatherMap(city);
        } else if (source.equalsIgnoreCase("weatherbit")) {
            weatherData = getWeatherBit(city);
        }

        return weatherData;
    }

    private Map<String, Object> getOpenWeatherMap(String city) throws JSONException {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + OPENWEATHERMAP_API_KEY +"&units=metric";

        //String url = "http://api.openweathermap.org/data/2.5/weather?q={city}&appid={apiKey}&units=metric";
        //Map<String, Object> response = restTemplate.getForObject(url, Map.class, city, OPENWEATHERMAP_API_KEY);

        double temp = 0;
        double hum = 0;
        String output = getUrlContent(url);

        JSONObject obj = new JSONObject(output);
        WeatherModel model = new WeatherModel(temp, hum);


        Map<String, Object> weatherData = new HashMap<>();

        weatherData.put("temperature", obj.getJSONObject("main").getDouble("temp"));
        weatherData.put("humidity", obj.getJSONObject("main").getDouble("humidity"));

        return weatherData;
    }

    private static String getUrlContent(String urlAddress) {
        StringBuffer content = new StringBuffer();

        try {
            URL url = new URL(urlAddress);
            URLConnection urlConn = url.openConnection();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String line;

            while((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch(Exception e) {
            System.out.println("Такой город не найден.");
        }
        return content.toString();

    }


    private Map<String, Object> getWeatherBit(String city) throws JSONException {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.weatherbit.io/v2.0/current?city=" + city + "&key=" + WEATHERBIT_API_KEY + "&units=metric";

        double temp = 0;
        double hum = 0;
        String output = getUrlContent(url);

        WeatherModel model = new WeatherModel(temp, hum);

        JSONObject obj = new JSONObject(output);
        Map<String, Object> weatherData = new HashMap<>();

        //weatherData.put("temperature: ", obj.getJSONObject("data[]").getDouble("temp"));
        //weatherData.put("temperature: " + response.get("data[0].temp") + ", humidity: " + response.get("data[0].rh") + "     ", "");
        //weatherData.put("humidity", obj.getJSONObject("data[0].rh"));

        weatherData.put("temperature", obj.getJSONArray("data").getJSONObject(0).getDouble("temp"));
        weatherData.put("humidity", obj.getJSONArray("data").getJSONObject(0).getDouble("rh"));

        return weatherData;
    }
}

