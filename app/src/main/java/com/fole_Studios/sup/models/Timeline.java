package com.fole_Studios.sup.models;

public class Timeline
{
    private String _startTime, _venueName, _courseName, _activeLecture;

    public Timeline(String startTime, String venueName, String courseName, String activeLecture)
    {
        _startTime = startTime;
        _venueName = venueName;
        _courseName = courseName;
        _activeLecture = activeLecture;
    }

    public String getStartTime()
    {
        return _startTime;
    }

    public void setStartTime(String startTime)
    {
        _startTime = startTime;
    }

    public String getVenueName()
    {
        return _venueName;
    }

    public void setVenueName(String venueName)
    {
        _venueName = venueName;
    }

    public String getCourseName()
    {
        return _courseName;
    }

    public void setCourseName(String courseName)
    {
        _courseName = courseName;
    }

    public String getActiveLecture()
    {
        return _activeLecture;
    }

    public void setActiveLecture(String activeLecture)
    {
        _activeLecture = activeLecture;
    }
}
