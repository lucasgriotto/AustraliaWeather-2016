# AustraliaWeather-Android-MVP (2016)

This is an exercise that I had to develop for a job interview in Australia.

  
_*Technical Constraints*_
- __NO 3rd party frameworks (Retrofit2, Dagger2, RXJava, RXAndroid,etc). Use ONLY frameworks that ship with the Android SDK.__ 
- Minimum Android support is 4.4

_*Brief*_

Create a single screen Android application that displays the weather for Australia’s major cities using a third-party API.
Cities to show weather for: 
- Sydney
- Melbourne
- Brisbane
- Adelaide 
- Perth
- Hobart
- Darwin

_*Features*_
- The app should display a list, with each item in the list being the weather conditions for each city above.
- The following current weather conditions should be displayed for each city: * Temperature in Celcius
  * Weather Icon
  * Weather description
  * Feels like temperature in Celcius * Precipitation
- The weather conditions should update automatically. They should only update if at least 10mins have passed since the last update. If the app is made inactive then reactivated, this rule still applies.
- The user should be able to favourite any number of cities by tapping on a list item. Favourite cities will have a different coloured background to other cities. These cities will show extended information. The additional information to be shown is below
  - Humidity as a %
  - Windspeedinkm/h 
  - Barometric pressure

Bonus
- Cache the weather conditions in the case where the user kills the app and restarts it, the last retrieved weather conditions are shown
- Pull-to-refresh functionality

Technical Information
The documentation for the API to use is below: 
http://www.worldweatheronline.com/api/docs/local-city-town-weather-api.aspx

The endpoint to use is below: 
http://api.worldweatheronline.com/premium/v1/weather.ashx

### The API key must be generated on https://developer.worldweatheronline.com/ and replaced on values/strings.xml (API_KEY) 