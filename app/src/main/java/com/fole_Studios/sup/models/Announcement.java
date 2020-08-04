package com.fole_Studios.sup.models;

import com.google.firebase.firestore.ServerTimestamp;

public class Announcement
{
    private String _annType, _annText, _annTime;
    @ServerTimestamp
    private String _time;

    public Announcement(String annType, String annText, String annTime)
    {
        _annType = annType;
        _annText = annText;
        _annTime = annTime;
    }

    public String getAnnType()
    {
        return _annType;
    }

    public String getTime()
    {
        return _time;
    }

    public void setTime(String time)
    {
        _time = time;
    }

    public void setAnnType(String annType)
    {
        _annType = annType;
    }

    public String getAnnText()
    {
        return _annText;
    }

    public void setAnnText(String annText)
    {
        _annText = annText;
    }

    public String getAnnTime()
    {
        return _annTime;
    }

    public void setAnnTime(String annTime)
    {
        _annTime = annTime;
    }
}
