package com.publicuhc.autoubl.fetcher;

import com.publicuhc.autoubl.entries.DefaultUBLEntry;
import com.publicuhc.autoubl.entries.UBLEntry;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URL;
import java.util.*;

public class JSONUBLFetcher implements UBLFetcher {

    private final URL m_ublLocation;
    private final JSONParser m_jsonParser = new JSONParser();

    public JSONUBLFetcher(URL ublLocation) {
        m_ublLocation = ublLocation;
    }

    private String getJSONString() throws UBLFetchException {
        InputStream inputStream = null;
        try {
            inputStream = m_ublLocation.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder jsonStringBuilder = new StringBuilder();
            String tempLine = null;
            while ((tempLine = reader.readLine()) != null) {
                jsonStringBuilder.append(tempLine);
            }
            return jsonStringBuilder.toString();
        } catch (IOException ioe) {
            throw new UBLFetchException();
        } finally {
            try {
                if(inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ignored) {}
        }
    }

    @Override
    public List<UBLEntry> fetch() throws UBLParseException, UBLFetchException {
        List<UBLEntry> entries = new ArrayList<UBLEntry>();
        try {
            Map mainJSON = (Map) m_jsonParser.parse(getJSONString());
            Map feedMap = (Map) mainJSON.get("feed");
            List entryArray = (List) feedMap.get("entry");

            for(Object entryObject : entryArray) {
                //if one entry fails we don't want all the rest not to be parsed
                try {
                    Map entry = (Map) entryObject;

                    Map ignNode = (Map) entry.get("gsx$ign");
                    String ign = (String) ignNode.get("$t");

                    Map reasonNode = (Map) entry.get("gsx$reason");
                    String reason = (String) reasonNode.get("$t");

                    Map dateBannedNode = (Map) entry.get("gsx$datebanned");
                    String dateBanned = (String) dateBannedNode.get("$t");

                    Map lengthOfBanNode = (Map) entry.get("gsx$lengthofban");
                    String lengthOfBan = (String) lengthOfBanNode.get("$t");

                    Map exiryDateNode = (Map) entry.get("gsx$expirydate");
                    String expiryDate = (String) exiryDateNode.get("$t");

                    Map courtroomPostNode = (Map) entry.get("gsx$courtroompost");
                    String courtroomPost = (String) courtroomPostNode.get("$t");

                    UBLEntry ublEntry = new DefaultUBLEntry();

                    ublEntry.setUUID(UUID.randomUUID()) //TODO get the UUID
                            .setReason(reason)
                            .setDateBanned(new Date()) //TODO parse the dates
                            .setLengthOfBan(lengthOfBan)
                            .setExpiryDate(new Date()) //TODO parse the dates
                            .setCourtroomPost(courtroomPost);

                    entries.add(ublEntry);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception e) {
            throw new UBLParseException();
        }
        return entries;
    }
}
