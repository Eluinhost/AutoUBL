package com.publicuhc.autoubl.entries;

import java.util.Date;
import java.util.UUID;

public class UBLEntry {

    private UUID m_uuid;
    private String m_reason;
    private Date m_expiryDate;
    private String m_courtroomPost;

    public UUID getUUID() {
        return m_uuid;
    }

    public UBLEntry setUUID(UUID uuid) {
        m_uuid = uuid;
        return this;
    }

    public String getReason() {
        return m_reason;
    }

    public UBLEntry setReason(String reason) {
        m_reason = reason;
        return this;
    }

    public Date getExpiryDate() {
        return m_expiryDate;
    }

    public UBLEntry setExpiryDate(Date expiryDate) {
        m_expiryDate = expiryDate;
        return this;
    }

    public String getCourtroomPost() {
        return m_courtroomPost;
    }

    public UBLEntry setCourtroomPost(String courtroomPost) {
        m_courtroomPost = courtroomPost;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("UUID: ")
                .append(m_uuid.toString())
                .append(", Reason: ")
                .append(m_reason)
                .append(", Date Unbanned: ")
                .append(m_expiryDate)
                .append(", Courtroom Post: ")
                .append(m_courtroomPost);

        return builder.toString();
    }
}
