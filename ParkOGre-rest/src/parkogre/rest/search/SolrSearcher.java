package parkogre.rest.search;/**
 * Lazooo copyright 2012
 */

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import parkogre.Parco;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author giok57
 * @email gioelemeoni@gmail.com
 * @modifiedBy giok57
 * <p/>
 * Date: 24/05/13
 * Time: 00:01
 */
public class SolrSearcher {

    private  static SolrSearcher searcher = new SolrSearcher();
    HttpSolrServer server;


    public SolrSearcher(){
        server = new HttpSolrServer("http://localhost:8983/solr/coreSaed");
    }



    public static SolrSearcher getSearcher(){
        return searcher;
    }

    public boolean addFile(String id, String nomeParco, String indirizzoParco, String city, Integer numVoti, Double votoAttuale, Double latitudine, Double longitudine, String imageURL ) {

        String coordinate = latitudine.toString()+","+longitudine.toString();

        SolrInputDocument newFile = new SolrInputDocument();
        newFile.addField("id", id);
        newFile.addField("nomeParco", nomeParco);
        newFile.addField("indirizzoParco", indirizzoParco);
        newFile.addField("city", city);
        newFile.addField("imageURL", imageURL);
        newFile.addField("coordinate", coordinate);
        newFile.addField("latitudine", latitudine);
        newFile.addField("longitudine", longitudine);
        newFile.addField("numVoti", numVoti);
        newFile.addField("votoAttuale", votoAttuale);

        try {
            server.add(newFile);
            server.commit();

        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }


    public boolean removeFile(String fileID) {

        try {

            server.deleteById(fileID);
            server.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }




    public List<Parco> getBestParkByVote(String query) {
        QueryResponse res = null;


        SolrQuery parameters = new SolrQuery();
        parameters.setRequestHandler("/dismax");
        parameters.setQuery(query);
        parameters.setRows(25);
        parameters.set("qf","nomeParco^0.8 city^1");
        parameters.set("mm", "2<-25%");
        parameters.set("sort", "votoAttuale desc");

        try {
            res = server.query(parameters);
        } catch (SolrServerException e) {
            return null;
        }
        List<Parco> results = new LinkedList<Parco>();

        Parco parco = null;
        for(SolrDocument doc : res.getResults()){
            parco = new Parco((String) doc.getFieldValue("id"), (String) doc.getFieldValue("nomeParco"), (String) doc.getFieldValue("indirizzoParco"), (String) doc.getFieldValue("city"),(Integer) doc.getFieldValue("numVoti"), (Double)doc.getFieldValue("votoAttuale"),  (Double) doc.getFieldValue("latitudine"), (Double) doc.getFieldValue("longitudine") );
            results.add(parco);
        }
        return results;
    }

    /**
     * ritorna i parchi piu vicini
     * @param latitudine
     * @param longitudine
     * @return
     */
    public List<Parco> getBestParkByGPS(String coordinate) {

        QueryResponse res = null;

        SolrQuery parameters = new SolrQuery();
        parameters.setRequestHandler("/dismax");
        parameters.setRows(25);
        parameters.set("score");
        parameters.set("fl","*");
        parameters.set("sort", "score asc");
        parameters.set("q","{!geofilt score=distance sfield=coordinate pt=" + coordinate + " d=250}");

        try {
            res = server.query(parameters);
        } catch (SolrServerException e) {
            return null;
        }
        List<Parco> results = new LinkedList<Parco>();

        Parco parco = null;
        for(SolrDocument doc : res.getResults()){
            parco = new Parco((String) doc.getFieldValue("id"), (String) doc.getFieldValue("nomeParco"), (String) doc.getFieldValue("indirizzoParco"), (String) doc.getFieldValue("city"),(Integer) doc.getFieldValue("numVoti"), (Double)doc.getFieldValue("votoAttuale"),  (Double) doc.getFieldValue("latitudine"), (Double) doc.getFieldValue("longitudine") );
            results.add(parco);
        }
        return results;
    }


    /**
     * ritorna i migliori parchi nel raggio di 10 km
     * @param latitudine
     * @param longitudine
     * @return
     */
    public List<Parco> getBestParkByGPSandVote(String coordinate) {

        QueryResponse res = null;

        SolrQuery parameters = new SolrQuery();
        parameters.setRequestHandler("/dismax");
        parameters.setRows(25);
        parameters.set("score");
        parameters.set("fl","*");
        parameters.set("sort", "votoAttuale desc");
        parameters.set("q","{!geofilt score=distance sfield=coordinate pt=" + coordinate + " d=10}");

        try {
            res = server.query(parameters);
        } catch (SolrServerException e) {
            return null;
        }
        List<Parco> results = new LinkedList<Parco>();

        Parco parco = null;
        for(SolrDocument doc : res.getResults()){
            parco = new Parco((String) doc.getFieldValue("id"), (String) doc.getFieldValue("nomeParco"), (String) doc.getFieldValue("indirizzoParco"), (String) doc.getFieldValue("city"),(Integer) doc.getFieldValue("numVoti"), (Double)doc.getFieldValue("votoAttuale"),  (Double) doc.getFieldValue("latitudine"), (Double) doc.getFieldValue("longitudine") );
            results.add(parco);
        }
        return results;
    }

    public boolean updateVote(String id, Double newVote){
        QueryResponse res = null;


        SolrQuery parameters = new SolrQuery();
        parameters.setRequestHandler("/dismax");
        parameters.setQuery(id);
        parameters.setRows(1);
        parameters.set("qf","id^1");

        try {
            res = server.query(parameters);
        } catch (SolrServerException e) {
            return false;
        }
        SolrDocument doc = res.getResults().get(0);
        if(doc == null)
            return false;

        Parco parco = new Parco((String) doc.getFieldValue("id"), (String) doc.getFieldValue("nomeParco"), (String) doc.getFieldValue("indirizzoParco"), (String) doc.getFieldValue("city"),(Integer) doc.getFieldValue("numVoti"), (Double)doc.getFieldValue("votoAttuale"),  (Double) doc.getFieldValue("latitudine"), (Double) doc.getFieldValue("longitudine") );

        this.removeFile(id);

        Double d = (parco.getNumVoti()*parco.getVotoAttuale()+newVote)/(double)(parco.getNumVoti()+1);
        d = Math.floor(d * 100.0) / 100.0;

        return this.addFile(parco.getId(), parco.getNomeParco(), parco.getIndirizzoParco(), parco.getCity(), parco.getNumVoti()+1, d, parco.getLatitude(), parco.getLongitude(), parco.getImageURL() );
    }
}
