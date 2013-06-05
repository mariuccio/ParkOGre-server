package parkogre.rest;
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

import parkogre.Parco;
import parkogre.ParcoCoord;
import parkogre.Parser;
import parkogre.rest.search.SolrSearcher;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author giok57
 * @email gioelemeoni@gmail.com
 * @modifiedBy giok57
 * <p/>
 * Date: 23/05/13
 * Time: 14:09
 */
@Path("/park")
public class OGServerRest {



    @GET
    @Path("/search/query/{query}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Parco> searchByQuery(@PathParam("query") String query){

        return SolrSearcher.getSearcher().getBestParkByVote(query);
    }

    @GET
    @Path("/search/gps/{coordinate}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Parco> searchByGPS(@PathParam("coordinate") String coordinate){

        return SolrSearcher.getSearcher().getBestParkByGPS(coordinate);
    }

    @GET
    @Path("/search/gps/best/{coordinate}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Parco> searchByGPSandVote(@PathParam("coordinate") String coordinate){

        return SolrSearcher.getSearcher().getBestParkByGPSandVote(coordinate);
    }

    @GET
    @Path("/feed/{id}/{voto}")
    @Produces({MediaType.APPLICATION_JSON})
    public OGServerResponse feedVote(@PathParam("id") String id, @PathParam("voto") String voto){

        Double d = null;
        try{
            d = Double.parseDouble(voto);
            if (d < 0 || d > 6)
                throw new NumberFormatException();
        }catch (NumberFormatException ex){
            return new OGServerResponse(false, "il voto non é nel giusto intervallo.");
        }
        Boolean ret = SolrSearcher.getSearcher().updateVote(id, d);
        if (ret)
            return new OGServerResponse(true, "OK");
        else
            return new OGServerResponse(false, "internal server error.");
    }

    @POST
    @Path("/add")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public OGServerResponse addPark(ClientParkResponse parkResponse){

        Parser.insertFromJSon(new ParcoCoord(parkResponse.nomeParco, parkResponse.latitudine, parkResponse.longitudine));
        return new OGServerResponse(true, "OK");

    }

}
