# Watermelon - Front Android

## Design Constraints :

1. Ergonomic interface (use fragments, menus like toolbars and navigation drawers, ...)
2. Locate the ATMs (latitude, longitude and geocoding - transform these into a street name)
3. Save payment list on a real external database (via REST API)
4. Be able to display information about any previous payment (read the information from the local database or the distant one)

## Installation 

1. To run the App you need the Watermelon Backend available [here](https://github.com/Francoois/back_WaterMelon "Watermelon Backend git repository"). 
2. Once the backend is up and running, change the ip address and port in `BASE_URL` in `fr/android/watermelon/controller/retrofit/RetrofitClient.java` to match your configuration. 
If you are using an emulated device, the address to join your computer is probably `http://10.0.2.2:8000/`
- You can now launch the  Android App from Android Studio and create your user.
