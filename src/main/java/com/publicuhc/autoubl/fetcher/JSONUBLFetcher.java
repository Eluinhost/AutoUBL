package com.publicuhc.autoubl.fetcher;

import com.publicuhc.autoubl.entries.JSONUBLEntryBuilder;
import com.publicuhc.autoubl.entries.UBLEntry;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
        String jsonString = getJSONString();
        List<UBLEntry> entries = new ArrayList<UBLEntry>();
        try {
            Object rootNode = m_jsonParser.parse(jsonString);

            if(!(rootNode instanceof JSONArray)) {
                throw new UBLParseException();
            }
            JSONArray entrylist = (JSONArray) rootNode;

            for(Object entryNode : entrylist) {
                if(!(entryNode instanceof JSONObject)) {
                    throw new UBLParseException();
                }
                JSONObject entry = (JSONObject) entryNode;
                JSONUBLEntryBuilder builder = new JSONUBLEntryBuilder(entry);

                entries.add(builder.build());
            }
        } catch (ParseException e) {
            throw new UBLParseException();
        }
        return entries;
    }
}
