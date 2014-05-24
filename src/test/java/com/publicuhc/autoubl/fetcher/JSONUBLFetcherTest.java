package com.publicuhc.autoubl.fetcher;

import com.publicuhc.autoubl.entries.UBLEntry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(PowerMockRunner.class)
public class JSONUBLFetcherTest {

    @Test
    public void testValidJSON() throws IOException, UBLFetchException, UBLParseException, ParseException {
        URL fileURL = getClass().getResource("/validJSON.json");
        JSONUBLFetcher fetcher = new JSONUBLFetcher(fileURL);

        List<UBLEntry> entryList = fetcher.fetch();

        assertThat(entryList).hasSize(2);

        UBLEntry firstentry = entryList.get(0);
        UBLEntry secondentry = entryList.get(1);

        assertThat(firstentry.getUUID()).isEqualTo(UUID.fromString("048fa310-30de-44fe-9f5e-c7443e91ad46"));
        assertThat(secondentry.getUUID()).isEqualTo(UUID.fromString("6ac803fd-132f-4540-a741-cb18ffeed8ce"));

        assertThat(firstentry.getReason()).isEqualTo("Test Reason");
        assertThat(secondentry.getReason()).isEqualTo("Test Reason 2");

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
        assertThat(firstentry.getExpiryDate()).isEqualTo(dateFormat.parse("Mon, 3 Dec 2007 12:00:22 +0100"));
        assertThat(secondentry.getExpiryDate()).isEqualTo(dateFormat.parse("Mon, 10 Dec 2007 12:00:22 +0100"));

        assertThat(firstentry.getCourtroomPost()).isEqualTo("http://google.com");
        assertThat(secondentry.getCourtroomPost()).isEqualTo("http://publicuhc.com");
    }

    @Test(expected = UBLParseException.class)
    public void invalidJSON() throws UBLFetchException, UBLParseException {
        URL fileURL = getClass().getResource("/invalidJSON.json");
        JSONUBLFetcher fetcher = new JSONUBLFetcher(fileURL);

        fetcher.fetch();
    }

    @Test(expected = UBLParseException.class)
    public void invalidStructureJSON() throws UBLFetchException, UBLParseException {
        URL fileURL = getClass().getResource("/invalidStructureJSON.json");
        JSONUBLFetcher fetcher = new JSONUBLFetcher(fileURL);

        fetcher.fetch();
    }

    @Test(expected = UBLFetchException.class)
    public void invalidLocationJSON() throws UBLFetchException, UBLParseException, MalformedURLException {
        URL fileURL = new URL("http://www.sdjfh93893fhdhf398hfdshfjkshfefh.com"); //if someone registers this I will kill you
        JSONUBLFetcher fetcher = new JSONUBLFetcher(fileURL);

        fetcher.fetch();
    }
}
