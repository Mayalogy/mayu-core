package com.mayalogy.mayu.topsy;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A real-time status update.
 */
public class StatusUpdate implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer tweetId = null;
    private Integer entityId = null;
    private String trackbackPermalink;
    private String content="";
    private Date trackbackDate;
    private String title;
    private String highlight;
    private String myType;
    private Date firstPostDate;
    private String url;
    private float topsyScore;
    private String trackbackTotal="-1";
    private float score = 1f;
    private String author = null;
    private String tableName = "";

    private String parsedContent;
    private List<String> hashtags = new ArrayList<String>();
    private List<String> tweeters = new ArrayList<String>();    

    private static final Pattern SOURCE_TWEETER = Pattern.compile("@[a-zA-Z0-9_-]+[.,! a-zA-Z]");
    private static final Pattern REFERENCED_HASHTAGS = Pattern.compile("#[a-zA-Z0-9_-]+[.,! a-zA-Z]");
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");

    private boolean isParsed = false;

    public Integer getTweetId() {
        return this.tweetId;
    }

    public Integer getEntityId() {
        return this.entityId;
    }

    public String getTableName() {
        return this.tableName;
    }


    public String getTrackbackPermalink() {
        return this.trackbackPermalink;
    }

    public void setTrackbackPermalink(String trackbackPermalink) {
        this.trackbackPermalink=trackbackPermalink;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTrackbackDate(Date trackbackDate) {
        this.trackbackDate=trackbackDate;
    }

    public Date getTrackbackDate() {
        return this.trackbackDate;
    }

    public void setFirstPostDate(Date firstPostDate) {
        this.firstPostDate=firstPostDate;
    }

    public Date getFirstPostDate() {
        return this.firstPostDate;
    }

    public void setURL(String url) {
        if(containsDoc(url))
            this.url=url;
    }

    private boolean containsDoc(String url) {
        if(url==null) return false;

        return (!(url.toLowerCase().indexOf("twitter.com")!=-1 && url.toLowerCase().indexOf("status")!=-1));
    }

    public String getURL() {
        return this.url;
    }

    public void setHighlight(String highlight) {
        this.highlight=highlight;
    }

    public String getHighlight() {
        return this.highlight;
    }

    public void setMyType(String myType) {
        this.myType=myType;
    }

    public String getMyType() {
        return this.myType;
    }

    public float getScore() {
        return this.score;
    }

    public void setTopsyScore(float score) {
        this.topsyScore=score;
    }

    public float getTopsyScore() {
        return this.topsyScore;
    }

    public void setTrackbackTotal(String trackbackTotal) {
        this.trackbackTotal=trackbackTotal;
    }

    public int getTrackbackTotal() {
        return Integer.parseInt(this.trackbackTotal);
    }

    public void setTitle(String title, String url) {
        if(containsDoc(url))
            this.title=title;
    }

    public String getTitle() {
        return this.title;
    }

    private void parse() {
        if(!isParsed) {
            parsedContent=content;
            tweeters=genTweeters(parsedContent);
            parsedContent=parsedContent.replaceAll("RT ", "");
            parsedContent=filterTweeters(parsedContent);
            parsedContent=filterHTMLResidue(parsedContent);
            parsedContent=URIParser.filterURI(parsedContent);
            hashtags=genHashtags(parsedContent);

            if(containsDisplayableReferencedDocTitle())
                parsedContent+=" " + this.getTitle();

            isParsed=true;
        }
    }

    private boolean containsDisplayableReferencedDocTitle() {
        if(getURL()==null || parsedContent==null || getTitle()==null)
            return false;
        return this.getURL()!=null && !this.parsedContent.toLowerCase().contains(getTitle().toLowerCase());
    }

    public String getParsedText() {
        parse();
        return this.parsedContent;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content=content;
    }

    public List<String> getHashtags() {
        parse();
        return this.hashtags;
    }

    public List<String> getTweeters() {
        parse();
        return this.tweeters;
    }

    static List<String> genHashtags(String tweet) {
        List<String> tags = new ArrayList<String>();
        Matcher matcher = REFERENCED_HASHTAGS.matcher(tweet);
        while(matcher.find()) {
            String tag = tweet.substring(matcher.start(), matcher.end()).trim();
            if(tag.endsWith(".") || tag.endsWith(",") || tag.endsWith("!"))
                tag=tag.substring(0, tag.length()-1);
            if(tag.indexOf("#fb")==-1)
                tags.add(tag.toLowerCase());
        }
        return tags;
    }

    static List<String> genTweeters(String tweet) {
        if(tweet==null) throw new IllegalArgumentException("Null tweets not accepted");

        Set<String> tweeters = new HashSet<String>();
        Matcher matcher = SOURCE_TWEETER.matcher(tweet);
        while(matcher.find()) {
            String tweeter = tweet.substring(matcher.start(), matcher.end()).trim();
            if(tweeter.endsWith(".") || tweeter.endsWith(",") || tweeter.endsWith("!"))
                tweeter=tweeter.substring(0, tweeter.length()-1);

            tweeters.add(tweeter.toLowerCase());
        }
        return new ArrayList<String>(tweeters);
    }

    static String filterTweeters(String tweet) {
        String pattern = "@[a-zA-Z0-9_-]+[:, ]";
        return tweet.replaceAll(pattern, "").trim();
    }

    static String filterHTMLResidue(String tweet) {
        //Strip aposts
        tweet=tweet.replaceAll("&#39;", "'");

        //String extra HTML
        String pattern = "&[a-zA-Z#]+[;]";
        tweet=tweet.replaceAll(pattern, "");

        return tweet;
    }

    /**
     * Returns readable form of this object.
     * @return The readable form.
     */
    public String toReadableString() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        if(this.getTrackbackDate()!=null)
            sb.append("time: "+sdf.format(this.getTrackbackDate())+"\n");
        sb.append("content: "+content+"\n");
        sb.append("filtered: "+getParsedText()+"\n");
        if(getHashtags().size()>0) {
            sb.append("tags:\n");
            sb.append("\n");
            for(String tag:getHashtags()) {
                sb.append(tag+"\n");
            }
            sb.append("\n");
        }
        if(this.getURL()!=null)
            sb.append("url: "+this.getURL()+"\n\n");


        if(this.getTitle()!=null)
            sb.append("title: "+this.getTitle()+"\n\n");

        sb.append("trackbackTotal: " + this.getTrackbackTotal()+"\n\n");

        return sb.toString();
    }

    public String toCompactReadableForm() {
        StringBuilder sb = new StringBuilder();
        if(this.getFirstPostDate()!=null)
            sb.append(simpleDateFormat.format(this.getFirstPostDate())+" - @");

        sb.append(getAuthor());
        sb.append(": ");
        sb.append(content);

        if(containsDisplayableReferencedDocTitle())
            sb.append(" ["+this.getTitle()+"]");

        return sb.toString();
    }

    static String getCompareString(StatusUpdate u) {
        StringBuilder key1 = new StringBuilder();
        key1.append(u.getContent().toLowerCase());
        if(u.getFirstPostDate()!=null) {
            key1.append(u.getFirstPostDate().getTime());
        }    
        key1.append(u.getAuthor());
        return key1.toString();
    }

    public boolean equals(Object o) {
        StatusUpdate inputUpdate = (StatusUpdate)o;
        return getCompareString(this).equals(getCompareString(inputUpdate));
    }

    public int hashCode() {
        return this.getContent().hashCode();
    }

    public String getAuthor() {
        return author;
    }

    /**
     * Sorts updates by time. 
     * @param updates
     * @param useFirstPostDate If true, uses firstPostDate, else uses trackbackDate.
     */
    public static void sort( List<StatusUpdate> updates, boolean useFirstPostDate ) {
        StatusUpdateTemporalComparator comparator =
                StatusUpdateTemporalComparator.getInstance(useFirstPostDate);
        Collections.sort(updates, comparator);
    }
}

class StatusUpdateTemporalComparator implements Comparator<StatusUpdate> {

    private boolean useFirstPostDate = false;

    StatusUpdateTemporalComparator(boolean useFirstPostDate){
        super();
        this.useFirstPostDate=useFirstPostDate;
    }

    public static StatusUpdateTemporalComparator getInstance(boolean useFirstPostDate){
        return new StatusUpdateTemporalComparator(useFirstPostDate);
    }

    public int compare( StatusUpdate u1, StatusUpdate u2 ) throws IllegalArgumentException {
        if(useFirstPostDate) {
            if(u1.getFirstPostDate()==null || u2.getFirstPostDate()==null) {
                String msg = "No first post date set. u1="+u1.toCompactReadableForm()+"*** u2="+u2.toCompactReadableForm();
                throw new IllegalArgumentException(msg);
            }
            return (int)(u2.getFirstPostDate().getTime()-u1.getFirstPostDate().getTime());
        } else {
            if(u1.getTrackbackDate()==null || u2.getTrackbackDate()==null) {
                String msg = "No trackback date set. u1="+u1.toCompactReadableForm()+"*** u2="+u2.toCompactReadableForm();
                throw new IllegalArgumentException(msg);
            }
            return (int)(u2.getTrackbackDate().getTime()-u1.getTrackbackDate().getTime());
        }
    }
}
