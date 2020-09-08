package com.fole_Studios.sup.models;

public class NewTimeline
{
    private String _time;
    private String _date;
    private String _venue;
    private String _moduleCode;
    private String _moduleName;

    public NewTimeline(String time, String date, String venue, String moduleCode, String moduleName)
    {
        _time = time;
        _date = date;
        _venue = venue;
        _moduleCode = moduleCode;
        _moduleName = moduleName;
    }

    public NewTimeline(String time, String venue, String moduleCode, String moduleName)
    {
        _time = time;
        _venue = venue;
        _moduleCode = moduleCode;
        _moduleName = moduleName;
    }

    public String getTime()
    {
        return _time;
    }

    public void setTime(String time)
    {
        _time = time;
    }

    public String getDate()
    {
        return _date;
    }

    public void setDate(String date)
    {
        _date = date;
    }

    public String getVenue()
    {
        return _venue;
    }

    public void setVenue(String venue)
    {
        _venue = venue;
    }

    public String getModuleCode()
    {
        return _moduleCode;
    }

    public void setModuleCode(String moduleCode)
    {
        _moduleCode = moduleCode;
    }

    public String getModuleName()
    {
        return _moduleName;
    }

    public void setModuleName(String moduleName)
    {
        _moduleName = moduleName;
    }
}
