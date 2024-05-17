package com.example.weatherkostya.Controller;

import com.example.weatherkostya.services.WeatherService;
import com.example.weatherkostya.model.WeatherModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/weatherForm")
@SessionAttributes("weatherDataList")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
    }

    @ModelAttribute("weatherDataList")
    public List<Map<String, Object>> getWeatherDataList() {
        return new ArrayList<>();
    }

    @GetMapping
    public String showWeatherPage(
            Model model,
            @ModelAttribute("weatherDataList") List<Map<String, Object>> weatherDataList
    ) {
        model.addAttribute("weatherModel", new WeatherModel());

        if (!weatherDataList.isEmpty()) {
            Map<String, Object> lastWeatherData = weatherDataList.get(weatherDataList.size() - 1);
            model.addAttribute("lastCity", lastWeatherData.get("city"));
        } else {
            model.addAttribute("lastCity", "");
        }

        // Clear the weatherDataList to force updating the div blocks
        weatherDataList.clear();

        model.addAttribute("weatherDataList", weatherDataList);
        return "weatherForm";
    }

    @PostMapping
    public String getWeather(
            @RequestParam("city") String city,
            @RequestParam("source") String source,
            Model model,
            @ModelAttribute("weatherDataList") List<Map<String, Object>> weatherDataList
    ) {
        try {
            Map<String, Object> weatherData = weatherService.getWeather(city, source);
            if (weatherData == null) {
                model.addAttribute("errorMessage", "City not found. Please enter a valid city.");
            } else {
                weatherData.put("city", city);

                if (weatherDataList.size() >= 3) {
                    if (weatherDataList.size() == 3) {
                        weatherDataList.remove(0); // Remove the oldest weather block
                    } else {
                        weatherDataList.remove(weatherDataList.size() - 2); // Overwrite the first weather block
                    }
                }

                weatherDataList.add(weatherData);
            }
        } catch (JSONException e) {
            // Handle JSON parsing error
            model.addAttribute("errorMessage", "Возникла ошибка при анализе данных о погоде. Пожалуйста, попробуйте ещё раз.");
        }

        model.addAttribute("weatherDataList", weatherDataList);
        model.addAttribute("weatherModel", new WeatherModel());

        return "weatherForm";
    }
}