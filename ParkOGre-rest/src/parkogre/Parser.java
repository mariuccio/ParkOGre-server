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

import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;


import parkogre.rest.search.SolrSearcher;

public class Parser {


	

	
	public static void insertFromJson(ParcoAddr parco) {
		List<Parco>Locations;
		Locations = new LinkedList<Parco>();
		String indirizzo = parco.getAddress();
		String city = parco.getCity();
		String nomeParco = parco.getNomeParco();
		
		URI uri = null;
		try {
			uri = new URI(
				        "http", 
				        "maps.google.com", 
				        "/maps/api/geocode/json",
				        "address="+city+",+"+indirizzo+"&sensor=false",
//				        &key=AIzaSyDgVVe9CYCFxhOpEFrQC0cte-ZR2JJxv0o
				        null);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		String request = uri.toASCIIString();
		
		
		double latitude = Parco.getLatitude(request);
		double longitude = Parco.getLongitude(request);
		//Locations.add(new Parco(parco.getNomeParco(), parco.getAddress(), parco.getCity(), latitude, longitude));
        String id = nomeParco + city + indirizzo;
        Integer i = id.hashCode();
        id = i.toString();
        String image  = "http://maps.googleapis.com/maps/api/streetview?size=400x400&location="+latitude+",%20"+longitude+"&fov=90&heading=235&pitch=10&sensor=false";
        SolrSearcher.getSearcher().addFile(id, nomeParco, indirizzo, city, 1, 3.0, latitude, longitude, image);
	}


	public static void insertFromJSon(ParcoCoord parco) {
		List<Parco>Locations;
		Locations = new LinkedList<Parco>();
		String indirizzo = getParco(parco.getLat(),parco.getLongit(), "add");
		String city = getParco(parco.getLat(),parco.getLongit(), "city");

//		System.out.println(city);
//		System.out.println(indirizzo);
		//Locations.add(new Parco(parco.getNomeParco(),indirizzo, city, parco.getLat(), parco.getLongit()));
        String id = parco.getNomeParco() + city + indirizzo;
        Integer i = id.hashCode();
        id = i.toString();
        String image = "http://maps.googleapis.com/maps/api/streetview?size=400x400&location="+parco.getLat()+",%20"+parco.getLongit()+"&fov=90&heading=235&pitch=10&sensor=false";
		SolrSearcher.getSearcher().addFile(id, parco.getNomeParco(), indirizzo, city, 1, 3.0, parco.getLat(), parco.getLongit(), image);
	}
	
	
	@SuppressWarnings("finally")
	private static String getParco(double lat, double longit, String what) {
		URI uri = null;
		try {
			uri = new URI(
				        "http", 
				        "maps.googleapis.com", 
				        "/maps/api/geocode/json",
				        "latlng="+lat+","+longit+"&sensor=false",
//				        &key=AIzaSyDgVVe9CYCFxhOpEFrQC0cte-ZR2JJxv0o
				        null);
			String request = uri.toASCIIString();
			
			Gson jsonobject = new Gson();
			Map map = jsonobject.fromJson(Parco.getJson(request), Map.class);
			
			List<Map>list = (List<Map>) map.get("results");
			
			map =  (Map) list.get(0);
			list = (List<Map>) map.get("address_components");
			List<String>list2 = null;
			for(int i=0; i<list.size(); i++) {
				map = (Map) list.get(i);
				list2 = (List<String>) map.get("types");
				String stringa = list2.get(0);
				if(what.equals("city")) {
					if(stringa.equals("administrative_area_level_2")) {	
						return (String) map.get("long_name");
					}
				}
				if(what.equals("add")) {
					if(stringa.equals("route")) {	
						return (String) map.get("long_name");
					}
				}
				
			}

			return null;
			
			
			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String getIndirizzoParco(double lat, double longit) {
		// TODO Auto-generated method stub
		System.out.println("Ciao");
		return "ciao";
	}



}
