package com.mayalogy.mayu.topsy;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.log4j.Logger;

import com.mayalogy.mayu.io.URIUtility;
import com.mayalogy.mayu.perf.MRUCache;

public class TopsySearcher {
 
    MRUCache updateCache = new MRUCache("StatusUpdate Cache", 10000);
    private static Logger logger = Logger.getLogger(TopsySearcher.class.getName());
    private String apiKey = null;
    
    public TopsySearcher(String apiKey) {
        this.apiKey=apiKey;
    }
    
    public List<StatusUpdate> search(QueryContext context, List<String> queryList) throws Exception {
        List<StatusUpdate> updates = new ArrayList<StatusUpdate>();
        processStatusUpdates(updates, queryList, context, context.getTimeSliceParameters());
        return updates;
    }
    
    private void processStatusUpdates(List<StatusUpdate> updates, List<String> queryList, QueryContext context, TimeSliceParameters wArgs) throws Exception {        
        String baseURI = buildURI(wArgs, queryList, context);
        List<StatusUpdate> temp = getStatusUpdates(baseURI, context);
        updates.addAll(temp);
    }
        
    private List<StatusUpdate> getStatusUpdates(String baseURI, QueryContext context) throws Exception {
        List<StatusUpdate> updates = new ArrayList<StatusUpdate>();
        int currentPage=0;
        int lastOffset=0;
        while(currentPage++<context.getNumberOfPages()) {
            String searchURI=baseURI+"&offset="+lastOffset;
            System.out.println("STRIP Retrieving page: " + currentPage + " results from Topsy: " + searchURI);
            logger.info("Retrieving page: " + currentPage + " results from Topsy: " + searchURI);
            String rawContents = null;
            if(updateCache.containsKey(searchURI)) {
                rawContents = (String)updateCache.get(searchURI);
            } else {
                rawContents = URIUtility.getUrlContents(new URL(searchURI));
                updateCache.put(searchURI, rawContents);
            }

            JSONObject json = (JSONObject) JSONSerializer.toJSON(rawContents);
            JSONObject response = json.getJSONObject("response");
            JSONArray list = response.getJSONArray("list");
            if(lastOffset==response.getInt("last_offset"))
                break;
            else
                lastOffset = response.getInt("last_offset");

            int total = response.getInt("total");
            
            for(int i=0; i<list.size(); i++) {
                StatusUpdate u = new StatusUpdate();
                u.setContent(list.getJSONObject(i).getString("content"));
                u.setFirstPostDate(new Date(1000*list.getJSONObject(i).getLong("firstpost_date")));
                u.setHighlight(list.getJSONObject(i).getString("highlight"));
                u.setMyType(list.getJSONObject(i).getString("mytype"));
                u.setTopsyScore(list.getJSONObject(i).getInt("score"));
                u.setAuthor(list.getJSONObject(i).getString("trackback_author_url"));
                u.setTrackbackDate(new Date(1000*list.getJSONObject(i).getLong("trackback_date")));
                u.setTrackbackPermalink(list.getJSONObject(i).getString("trackback_permalink"));
                u.setTrackbackTotal(list.getJSONObject(i).getString("trackback_total"));
                u.setURL(list.getJSONObject(i).getString("url").toLowerCase());
                u.setTitle(list.getJSONObject(i).getString("title").trim(), u.getURL());
                updates.add(u);
            }
            if(lastOffset<context.getResultsPerPage())
                break;
        }
        return updates;
    }
    
    private String buildURI(TimeSliceParameters window, List<String> queryList, QueryContext context) throws Exception {
        String baseURI = "http://otter.topsy.com/search.json?q=" + buildQuery(queryList);
        logger.info("All results should be between: " + window.getDisplayableTopsyMinimumTime() + " AND " + window.getDisplayableTopsyMaximumTime());
        baseURI=baseURI+"&mintime="+window.getTopsyMinTime()+"&maxtime="+window.getTopsyMaxTime();
        return baseURI=baseURI+"&perpage="+context.getResultsPerPage()+"&apikey="+apiKey;
    }
    
    private static String buildQuery(List<String> queryList) throws Exception {
        StringBuilder buf = new StringBuilder();
        boolean isFirst = true;
        for(String q:queryList) {
            if(!isFirst) {
                buf.append("+OR+"); 
            }
            buf.append(URLEncoder.encode(q, "UTF8"));
            isFirst=false;
        }
        return buf.toString();
    }
}
