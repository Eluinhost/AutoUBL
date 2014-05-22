package com.publicuhc.autoubl.fetcher;

import com.publicuhc.autoubl.entries.UBLEntry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@RunWith(PowerMockRunner.class)
public class JSONUBLFetcherTest {

    @Test(expected = UBLFetchException.class)
    public void testInvalidURL() throws MalformedURLException, UBLFetchException, UBLParseException {
        //URL that doesn't point anywhere
        UBLFetcher fetcher = new JSONUBLFetcher(new URL("http://www.publicuhc.com/skdjfs89"));

        fetcher.fetch();
    }

    @Test
    public void testValidURL() throws MalformedURLException, UBLFetchException, UBLParseException {
        UBLFetcher fetcher = new JSONUBLFetcher(new URL("https://spreadsheets.google.com/feeds/list/0AjACyg1Jc3_GdEhqWU5PTEVHZDVLYWphd2JfaEZXd2c/od6/public/values?alt=json"));

        fetcher.fetch();
    }

    @Test
    public void testInvalidJSONStructure() {
        //TODO
    }

    @Test
    public void testValidJSONStructure() {
        //TODO
    }

    @Test
    public void testInvalidJSONFile() {
        //TODO
    }
}
