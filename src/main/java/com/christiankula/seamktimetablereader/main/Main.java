package com.christiankula.seamktimetablereader.main;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JTextPane;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.christiankula.seamktimetablereader.enums.EnumDay;
import com.christiankula.seamktimetablereader.enums.EnumEndingHour;
import com.christiankula.seamktimetablereader.enums.EnumStartingHour;
import com.christiankula.seamktimetablereader.pojos.Course;

public class Main {
    static String TIMETABLE_URL = "http://lukkarit.epedu.fi/LukujarjestyksetSeAMKLiiketoimintajaKulttuuriFramiF/AMK/2015-2016/vko08/x3028gateway16k6902.htm";
    static final String htmlPageBreak = "<br/>";
    static final int TIME_OUT_IN_MILLIS = 3000;

    public static void main(String[] args) {

	Path notMyCoursesFilePath = Paths.get("not_my_courses.txt");
	List<String> notMyCourses = new ArrayList<String>();
	if (!notMyCoursesFilePath.toFile().isFile() || !notMyCoursesFilePath.toFile().exists()) {
	    // System.err.println("File " + notMyCoursesFilePath.getFileName() +
	    // " not found.");
	    // System.err.println(
	    // "In order to generate timetables with only your courses, you need
	    // to create a plain text file called 'not_my_courses.txt'
	    // containing every courses you DO NOT HAVE (one course per line) in
	    // the same directory as this .jar.");

	    JFrame j = new JFrame();
	    j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    j.setSize(400, 150);
	    j.setBackground(Color.WHITE);
	    j.setLocationRelativeTo(null);
	    JTextPane jtp = new JTextPane();
	    jtp.setText("File '" + notMyCoursesFilePath.getFileName()
		    + "' not found.\n\nIn order to generate timetables with only your courses, you need to create a plain text file called 'not_my_courses.txt' containing every courses you DO NOT HAVE (one course per line) in the same directory as this .jar.");

	    jtp.setAlignmentX(JTextPane.CENTER_ALIGNMENT);
	    j.setContentPane(jtp);
	    j.setTitle("Error");
	    j.setVisible(true);

	} else {
	    final Connection JSOUP_CONNECTION = Jsoup.connect(TIMETABLE_URL).timeout(TIME_OUT_IN_MILLIS)
		    .ignoreContentType(true).parser(Parser.htmlParser());

	    List<String> urls = getAllTimetablesURLs();
	    for (String url : urls) {
		JSOUP_CONNECTION.url(url);
		Element timeTableDocument = null;

		try {
		    timeTableDocument = JSOUP_CONNECTION.get().getElementsByTag("table").get(0);
		} catch (HttpStatusException hse) {
		    System.err.println("The timetable was not found (HTTP 404 NOT FOUND : " + url + ")");
		} catch (IOException ioe) {
		    ioe.printStackTrace();
		}

		if (timeTableDocument != null) {
		    String title = new String();
		    List<Course> FULL_COURSES = new ArrayList<Course>();
		    Map<EnumDay, Integer> timeSlotOverflowMap = new HashMap<EnumDay, Integer>();

		    for (EnumDay ed : EnumDay.values()) {
			timeSlotOverflowMap.put(ed, 0);
		    }

		    Elements timeTableRows = timeTableDocument.getElementsByTag("tr");

		    title = timeTableRows.get(0).text();

		    Elements weekDayElements = timeTableRows.get(1).children();
		    Map<EnumDay, String> weekDates = new HashMap<EnumDay, String>();

		    for (int columnIndex = 1; columnIndex < weekDayElements.size(); columnIndex++) {
			String dayDate = weekDayElements.get(columnIndex).text();

			dayDate = dayDate.replaceAll("[^0-9.]", "").replace(".", "/");
			weekDates.put(EnumDay.getDayByIndex(columnIndex - 1), dayDate);
		    }

		    Elements coursesElements = timeTableRows.get(2).children();
		    for (int columnIndex = 1; columnIndex < coursesElements.size(); columnIndex++) {
			Element courseElement = coursesElements.get(columnIndex);

			String cellContent = courseElement.text();

			if (cellContent.equals("/////")) {
			    timeSlotOverflowMap.put(EnumDay.getDayByIndex(columnIndex - 1), 20);
			}
		    }

		    for (int rowIndex = 2; rowIndex < timeTableRows.size(); rowIndex++) {
			coursesElements = timeTableRows.get(rowIndex).children();

			updateTimeSlotOverflowMap(timeSlotOverflowMap);

			for (int columnIndex = 1; columnIndex < coursesElements.size(); columnIndex++) {

			    Element courseElement = coursesElements.get(columnIndex);

			    String cellContent = courseElement.text();

			    if (!StringUtil.isBlank(cellContent.replaceAll("\u00a0", "").trim())
				    && !cellContent.equals("/////")) {
				Course course = new Course();

				int currentDayIndex = columnIndex - 1;
				EnumDay currentDay = EnumDay.getDayByIndex(currentDayIndex);

				while (timeSlotOverflowMap.get(currentDay).intValue() > 0) {
				    currentDayIndex++;
				    currentDay = EnumDay.getDayByIndex(currentDayIndex);
				}
				course.setDay(currentDay);
				course.setDate(weekDates.get(currentDay));
				course.setStartingHour(EnumStartingHour.getStartingHourByIndex(rowIndex - 2));

				String rowspan = courseElement.attr("rowspan");
				if (!StringUtil.isBlank(rowspan)) {
				    int rowpsanOverflowSize = (Integer.valueOf(rowspan) - 1);

				    course.setEndingHour(
					    EnumEndingHour.getEndingHourByIndex((rowIndex - 2) + rowpsanOverflowSize));

				    timeSlotOverflowMap.put(course.getDay(), rowpsanOverflowSize + 1);
				} else {
				    course.setEndingHour(EnumEndingHour.getEndingHourByIndex(rowIndex + 1 - 2));
				    timeSlotOverflowMap.put(course.getDay(), 0);
				}

				Element cellContentElement = courseElement.getElementsByTag("font").get(0);
				course.setSubject(cellContentElement.getElementsByTag("a").text());

				course.setDescription(cellContentElement.ownText());

				FULL_COURSES.add(course);
			    }
			}
		    }
		    try {
			notMyCourses = Files.readAllLines(notMyCoursesFilePath);
		    } catch (IOException e) {
			e.printStackTrace();
		    }

		    List<Course> FILTERED_COURSES = new ArrayList<Course>(FULL_COURSES);

		    Collections.sort(FULL_COURSES);
		    for (Course c : FULL_COURSES) {
			for (String s : notMyCourses) {
			    if (c.getSubject().contains(s)) {
				FILTERED_COURSES.remove(c);
				break;
			    }
			}
		    }
		    Collections.sort(FILTERED_COURSES);
		    Map<EnumDay, List<Course>> courses = new HashMap<EnumDay, List<Course>>();

		    for (Course c : FILTERED_COURSES) {
			List<Course> coursesOfTheDay = courses.get(c.getDay());

			if (coursesOfTheDay == null) {
			    coursesOfTheDay = new ArrayList<Course>();
			}
			coursesOfTheDay.add(c);
			courses.put(c.getDay(), coursesOfTheDay);
		    }

		    printTimetableToPdf(title, courses);
		}
	    }
	}
    }

    private static void printTimetableToPdf(String title, Map<EnumDay, List<Course>> courses) {
	OutputStream os = null;
	title = title.replace("...", " - ").replace(":", " -");
	String css = "<style>" + "h1 {" + "text-align:center;" + "}" + "p {" + "width:100%; "
		+ "page-break-inside: avoid; " + "border:solid 0px black;} " + "hr{ border-width: 1px 1px 0;"
		+ "border-style: solid;}" + "</style>";

	String head = "<head>";
	head += css;
	head += "</head>";

	String html = "<html>";
	html += head;
	html += "<body>";
	html += "<h1>" + title + "</h1>";

	List<EnumDay> sortedDayKeys = new ArrayList<EnumDay>(courses.keySet());
	Collections.sort(sortedDayKeys);

	Iterator<EnumDay> daysIterator = sortedDayKeys.iterator();

	while (daysIterator.hasNext()) {
	    EnumDay currentDay = daysIterator.next();

	    String date = courses.get(currentDay).get(0).getDate();
	    html += "<p><h2>" + currentDay.toString() + " - " + date + "</h2>";

	    Iterator<Course> coursesIterator = courses.get(currentDay).iterator();

	    while (coursesIterator.hasNext()) {
		Course c = coursesIterator.next();

		html += "<b>" + c.getStartingHour() + " - " + c.getEndingHour() + "</b>";
		html += htmlPageBreak;
		html += c.getSubject();
		html += htmlPageBreak;
		html += c.getDescription();
		html += htmlPageBreak;

		if (coursesIterator.hasNext()) {
		    html += htmlPageBreak;
		}
	    }

	    html += "</p>";
	    if (daysIterator.hasNext()) {
		html += "<hr/>";
	    }
	}
	html += "</body></html>";
	try {
	    final File outputFile = new File(title + ".pdf");
	    os = new FileOutputStream(outputFile);

	    ITextRenderer renderer = new ITextRenderer();
	    renderer.setDocumentFromString(html);
	    renderer.layout();
	    renderer.createPDF(os);
	    renderer.finishPDF();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private static void updateTimeSlotOverflowMap(Map<EnumDay, Integer> previousRowMadness) {
	Iterator<Map.Entry<EnumDay, Integer>> it = previousRowMadness.entrySet().iterator();

	while (it.hasNext()) {
	    Map.Entry<EnumDay, Integer> pair = (Map.Entry<EnumDay, Integer>) it.next();

	    if (pair.getValue() > 0) {
		pair.setValue(pair.getValue() - 1);
	    }
	}
    }

    private static List<String> getAllTimetablesURLs() {
	List<String> urls = new ArrayList<String>();
	int startingWeek = 8;
	final int endingWeek = 21;
	int startingTimetableID = 3028;
	String baseUrl = "http://lukkarit.epedu.fi/LukujarjestyksetSeAMKLiiketoimintajaKulttuuriFramiF/AMK/2015-2016/vko%s/x%sgateway16k6902.htm";

	for (; startingWeek <= endingWeek; startingWeek++) {
	    String s_startingWeek = startingWeek + "";

	    if (startingWeek < 10) {
		s_startingWeek = "0" + startingWeek;
	    }
	    urls.add(String.format(baseUrl, s_startingWeek, startingTimetableID));
	    startingTimetableID++;

	}
	return urls;
    }
}
