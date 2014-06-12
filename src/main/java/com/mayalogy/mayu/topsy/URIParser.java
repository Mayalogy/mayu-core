package com.mayalogy.mayu.topsy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mayalogy.mayu.core.WordGramExtractor;

/**
 * Parses URIs from Tweets.
 */
class URIParser implements Serializable {
    
    private static final long serialVersionUID = 2408863876592282365L;
    
    private static String URI_PATTERN_1="http://[a-zA-Z0-9.?/_-]+[$, ]";
    private static String URI_PATTERN_2="http://[a-zA-Z0-9.?/_-]+$";
    Pattern uriPattern1=Pattern.compile(URI_PATTERN_1);
    Pattern uriPattern2=Pattern.compile(URI_PATTERN_2);

    static String filterURI(String tweet) {
        String afterP1 = tweet.replaceAll(URI_PATTERN_1, "");
        return afterP1.replaceAll(URI_PATTERN_2, "");
    }
    
    List<String> parse(String content) throws Exception {
        Matcher m1 = uriPattern1.matcher(content);
        Matcher m2 = uriPattern2.matcher(content);
        List<String> uris = new ArrayList<String>();
        parse(content, uris, m1);
        parse(content, uris, m2);
        return uris;
    }
    
    private static void parse(String content, List<String> matches, Matcher m) throws Exception {
        if(m.find()) {
            int s = m.start(); int e=m.end();
            matches.add(WordGramExtractor.removeTrailingBlacklistedChars(content.substring(s, e).trim()));
            parse(content.substring(e), matches, m);
        }
    }
}
