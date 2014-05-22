package com.publicuhc.autoubl.entries;

import java.util.Date;
import java.util.UUID;

public class DefaultUBLEntry implements UBLEntry {

    private UUID m_uuid;
    private String m_reason;
    private Date m_dateBanned;
    private String m_lengthOfBan;
    private Date m_dateUnbanned;
    private String m_courtroomPost;

    @Override
    public UUID getUUID() {
        return m_uuid;
    }

    @Override
    public UBLEntry setUUID(UUID uuid) {
        m_uuid = uuid;
        return this;
    }

    @Override
    public String getReason() {
        return m_reason;
    }

    @Override
    public UBLEntry setReason(String reason) {
        m_reason = reason;
        return this;
    }

    @Override
    public Date getDateBanned() {
        return m_dateBanned;
    }

    @Override
    public UBLEntry setDateBanned(Date banned) {
        m_dateBanned = banned;
        return this;
    }

    @Override
    public String lengthOfBan() {
        return m_lengthOfBan;
    }

    @Override
    public UBLEntry setLengthOfBan(String lengthOfBan) {
        m_lengthOfBan = lengthOfBan;
        return this;
    }

    @Override
    public Date getExpiryDate() {
        return m_dateUnbanned;
    }

    @Override
    public UBLEntry setExpiryDate(Date expiryDate) {
        m_dateUnbanned = expiryDate;
        return this;
    }

    @Override
    public String getCourtroomPost() {
        return m_courtroomPost;
    }

    @Override
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
                .append(", Date Banned: ")
                .append(m_dateBanned.toString())
                .append(", Date Unbanned: ")
                .append(m_dateUnbanned.toString())
                .append(", Courtroom Post: ")
                .append(m_courtroomPost)
                .append(", Length: ")
                .append(m_lengthOfBan);

        return builder.toString();
    }
}
