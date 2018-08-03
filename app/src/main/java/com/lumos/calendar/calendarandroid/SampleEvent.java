package com.lumos.calendar.calendarandroid;

import com.yuyakaido.android.couplescalendar.model.CouplesCalendarEvent;

import java.util.Date;

/**
 * Created by yuyakaido on 9/12/15.
 */
public class SampleEvent implements CouplesCalendarEvent, Cloneable {

    private int id;
    private String title;
    private String description;
    private Date mStartAt;
    private Date mEndAt;
    private String mRRule;
    private String reminder;
    private int mEventColor;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartAt(Date startAt) {
        mStartAt = startAt;
    }

    @Override
    public Date getStartAt() {
        return mStartAt;
    }

    public void setEndAt(Date endAt) {
        mEndAt = endAt;
    }

    @Override
    public Date getEndAt() {
        return mEndAt;
    }

    public void setRecurrenceRule(String recurrenceRule) {
        mRRule = recurrenceRule;
    }

    @Override
    public String getRecurrenceRule() {
        return mRRule;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public void setEventColor(int eventColor) {
        mEventColor = eventColor;
    }

    @Override
    public int getEventColor() {
        return mEventColor;
    }

    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}