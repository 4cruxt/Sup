package com.fole_Studios.sup.models;

public class Announcement
{
    private String _annType, _annText, _annTime;

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
