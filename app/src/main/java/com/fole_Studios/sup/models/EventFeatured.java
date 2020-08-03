package com.fole_Studios.sup.models;

public class EventFeatured
{
    private int _eventPhoto;
    private String _eventDate;
    private String _eventMonth;
    private String _eventTitle;

    public EventFeatured(int eventPhoto, String eventTitle, String eventDate, String eventMonth)
    {
        _eventPhoto = eventPhoto;
        _eventDate = eventDate;
        _eventMonth = eventMonth;
        _eventTitle = eventTitle;
    }

    public int getEventPhoto()
    {
        return _eventPhoto;
    }

    public void setEventPhoto(int eventPhoto)
    {
        _eventPhoto = eventPhoto;
    }

    public String getEventDate()
    {
        return _eventDate;
    }

    public void setEventDate(String eventDate)
    {
        _eventDate = eventDate;
    }

    public String getEventMonth()
    {
        return _eventMonth;
    }

    public void setEventMonth(String eventMonth)
    {
        _eventMonth = eventMonth;
    }

    public String getEventTitle()
    {
        return _eventTitle;
    }

    public void setEventTitle(String eventTitle)
    {
        _eventTitle = eventTitle;
    }
}
