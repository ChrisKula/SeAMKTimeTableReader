package com.christiankula.seamktimetablereader.pojos;

import com.christiankula.seamktimetablereader.enums.EnumDay;
import com.christiankula.seamktimetablereader.enums.EnumEndingHour;
import com.christiankula.seamktimetablereader.enums.EnumStartingHour;

public class Course implements Comparable<Course> {

    private EnumDay day = EnumDay.MONDAY;
    private EnumStartingHour startingHour = EnumStartingHour.EIGHT_OCLOCK;
    private EnumEndingHour endingHour = EnumEndingHour.EIGHT_FORTY_FIVE;

    private String subject = new String();
    private String description = new String();
    private String date = new String();

    public Course() {

    }

    public Course(EnumDay day, EnumStartingHour startingHour, EnumEndingHour endingHour, String subject,
	    String description) {
	this.day = day;
	this.startingHour = startingHour;
	this.endingHour = endingHour;
	this.subject = subject;
	this.description = description;
    }

    public EnumDay getDay() {
	return day;
    }

    public void setDay(EnumDay day) {
	this.day = day;
    }

    public EnumStartingHour getStartingHour() {
	return startingHour;
    }

    public void setStartingHour(EnumStartingHour startingHour) {
	this.startingHour = startingHour;
    }

    public EnumEndingHour getEndingHour() {
	return endingHour;
    }

    public void setEndingHour(EnumEndingHour endingHour) {
	this.endingHour = endingHour;
    }

    public String getSubject() {
	return subject;
    }

    public void setSubject(String subject) {
	this.subject = subject;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getDate() {
	return date;
    }

    public void setDate(String date) {
	this.date = date;
    }

    @Override
    public String toString() {
	return "Course [day=" + day + ", startingHour=" + startingHour + ", endingHour=" + endingHour + ", subject="
		+ subject + ", description=" + description + ", date=" + date + "]";
    }

    public int compareTo(Course o) {
	int compare = this.getDay().getIndex() - o.getDay().getIndex();
	if (compare == 0) {
	    compare = this.getStartingHour().getIndex() - o.getStartingHour().getIndex();
	}

	return compare;
    }

}
