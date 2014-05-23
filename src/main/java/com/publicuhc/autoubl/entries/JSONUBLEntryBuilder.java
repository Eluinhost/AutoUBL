package com.publicuhc.autoubl.entries;

import com.publicuhc.autoubl.fetcher.UBLParseException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class JSONUBLEntryBuilder implements UBLEntryBuilder {

    private final Map m_entryNode;

    //Parse dates as RFC 2822 in the english locale
    private final SimpleDateFormat m_dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH);

    public JSONUBLEntryBuilder(Map entryNode) {
        m_entryNode = Collections.unmodifiableMap(entryNode);
    }

    @Override
    public UBLEntry build() throws UBLParseException {
        UBLEntry entry = new UBLEntry();

        Object uuidNode = m_entryNode.get("uuid");
        if(!(uuidNode instanceof String)) {
            throw new UBLParseException();
        }
        String uuidString = (String) uuidNode;
        try {
            UUID uuid = UUID.fromString(uuidString);
            entry.setUUID(uuid);
        } catch (IllegalArgumentException ex) {
            throw new UBLParseException();
        }

        Object reasonNode = m_entryNode.get("reason");
        if(!(reasonNode instanceof String)) {
            throw new UBLParseException();
        }
        entry.setReason((String) reasonNode);

        Object dateExpiresNode = m_entryNode.get("expiryDate");
        if(!(dateExpiresNode instanceof String)) {
            throw new UBLParseException();
        }
        try {
            entry.setExpiryDate(m_dateFormat.parse((String) dateExpiresNode));
        } catch (ParseException e) {
            throw new UBLParseException();
        }

        Object courtroomPost = m_entryNode.get("courtroom");
        if(!(courtroomPost instanceof String)) {
            throw new UBLParseException();
        }
        entry.setCourtroomPost((String) courtroomPost);
        return entry;
    }
}
