package parkogre;
/**
 The MIT License (MIT)

 Copyright (c) 2013 Gioele Meoni

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 */

import com.google.gson.Gson;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * @author giok57
 * @email gioelemeoni@gmail.com
 * @modifiedBy giok57
 * <p/>
 * Date: 23/05/13
 * Time: 22:59
 */
public class Parco {
    private String id;
    private int numVoti;
    private double votoAttuale;
    private String city;
    private String nomeParco;
    private String indirizzoParco;
    private double latitude;
    private double longitude;
    private String coordinate;
    private String imageURL;


    public Parco(String id, String nP, String iP, String city, Integer numVoti, Double votoAttuale, Double latitude, Double longitude) {
        this.id = id;
        this.numVoti = numVoti;
        this.votoAttuale = votoAttuale;
        this.city = city;
        this.nomeParco = nP;
        this.indirizzoParco = iP;
        this.longitude = longitude;
        this.latitude = latitude;
        this.coordinate = latitude.toString() + "," + longitude.toString();
        this.imageURL = "http://maps.googleapis.com/maps/api/streetview?size=400x400&location="+latitude+",%20"+longitude+"&fov=90&heading=235&pitch=10&sensor=false";

    }
    public int getNumVoti() {
        return numVoti;
    }

    public void setNumVoti(int numVoti) {
        this.numVoti = numVoti;
    }

    public double getVotoAttuale() {
        return votoAttuale;
    }

    public void setVotoAttuale(double votoAttuale) {
        this.votoAttuale = votoAttuale;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNomeParco() {
        return nomeParco;
    }

    public void setNomeParco(String nomeParco) {
        this.nomeParco = nomeParco;
    }

    public String getIndirizzoParco() {
        return indirizzoParco;
    }

    public void setIndirizzoParco(String indirizzoParco) {
        this.indirizzoParco = indirizzoParco;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    @SuppressWarnings("finally")
    public static double getLatitude(String request) {
        Gson jsonobject = new Gson();
        Map map = jsonobject.fromJson(getJson(request), Map.class);
        String status = (String) map.get("status");
        if(status.equals("OK")==false) {
            try {
                Thread.sleep(2000);

            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            finally {
                return 0.0;
            }
        }

        List<Map> list = (List<Map>) map.get("results");
        map = list.get(0);
        map = (Map) map.get("geometry");
        map = (Map) map.get("location");
        return (Double) map.get("lat");
    }


    public static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static String getJson(String request) {


        InputStream is = null;

        try {
            is = new URL(request).openStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            is.close();
            return jsonText;
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }
        // TODO Auto-generated method stub

    }

    @SuppressWarnings("finally")
    public static double getLongitude(String request) {
        Gson jsonobject = new Gson();
        Map map = jsonobject.fromJson(getJson(request), Map.class);
        String status = (String) map.get("status");
        if(status.equals("OK")==false) {
            try {
                Thread.sleep(2000);

            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            finally {
                return 0.0;
            }
        }

        List<Map>list = (List<Map>) map.get("results");
        map = list.get(0);
        map = (Map) map.get("geometry");
        map = (Map) map.get("location");
        return (Double) map.get("lng");
    }
}
