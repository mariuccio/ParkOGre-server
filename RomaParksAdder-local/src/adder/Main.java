
package adder;
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

import au.com.bytecode.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

/**
 * @author giok57
 * @email gioelemeoni@gmail.com
 * @modifiedBy giok57
 * <p/>
 * Date: 24/05/13
 * Time: 10:20
 */
public class Main {


    public static void main(String [] args){

        insertFromCSV("verderoma.csv");

    }


    public static void insertFromCSV(String file) {
        CSVReader reader;
        String [] nextLine;
        List<Parco> Locations;
        Locations = new LinkedList<Parco>();

        try {
            reader = new CSVReader(new FileReader(file), '\t');
            int i=0;
            while ((nextLine = reader.readNext()) != null) {
                // nextLine[] is an array of values from the line
                URI uri = null;

                try {
                    uri = new URI(
                            "http",
                            "maps.google.com",
                            "/maps/api/geocode/json",
                            "address="+"Roma%20"+",+"+nextLine[1]+"&sensor=false",null);//&key=AIzaSyDgVVe9CYCFxhOpEFrQC0cte-ZR2JJxv0o",
//						        &key=AIzaSyDgVVe9CYCFxhOpEFrQC0cte-ZR2JJxv0o
                            //null);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                String request = uri.toASCIIString();
                //Locations.add(new Parco(nextLine[0],nextLine[1], "Roma", Parco.getLatitude(request), Parco.getLongitude(request)));

                Double latitudine = Parco.getLatitude(request);
                Double longitudine = Parco.getLongitude(request);
                String id = nextLine[0] + "Roma" + nextLine[1];
                Integer i2 = id.hashCode();
                id = i2.toString();

                if(latitudine!= 0.0 ){
                    String image  = "http://maps.googleapis.com/maps/api/streetview?size=400x400&location="+latitudine+",%20"+longitudine+"&fov=90&heading=120&pitch=10&sensor=false";
                    SolrSearcher.getSearcher().addFile(id, nextLine[0], nextLine[1], "Roma", 1, 3.0, Parco.getLatitude(request), Parco.getLongitude(request), image );
                    System.out.println(latitudine+","+longitudine);
                }
                i++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
