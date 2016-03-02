package com.christiankula.seamktimetablereader.enums;

public enum EnumStartingHour {

    EIGHT_OCLOCK(8, 0, 0),

    EIGHT_FIFTY_FIVE(8, 55, 1),

    NINE_FIFTY(9, 50, 2),

    TEN_FORFTY_FIVE(10, 45, 3),

    TWELVE_FIFTEEN(12, 15, 4),

    THIRTEEN_FIFTEEN(13, 15, 5),

    FOURTEEN_FIFTEEN(14, 15, 6),

    FIFTEEN_FIFTEEN(15, 15, 7),

    SIXTEEN_FIFTEEN(16, 15, 8),

    SEVENTEEN_TEN(17, 10, 9),

    EIGHTEEN_OCLOCK(18, 0, 10),

    EIGHTEEN_FIFTY(18, 50, 11),

    NINETEEN_FORTY(19, 40, 12),

    TWENTY_THIRTY(20, 30, 13);

    private int hour;

    private int minute;

    private int index;

    EnumStartingHour(int hour, int minute, int index) {
	this.hour = hour;
	this.minute = minute;
	this.index = index;
    }

    public String toString() {
	String minute = this.minute + "";
	if (this.minute == 0)
	    minute = "00";

	return this.hour + ":" + minute;
    }

    public int getIndex() {
	return this.index;
    }

    public static EnumStartingHour getStartingHourByIndex(int index) {
	EnumStartingHour found = null;

	for (EnumStartingHour esh : EnumStartingHour.values()) {
	    if (esh.getIndex() == index) {
		found = esh;
	    }
	}

	return found;
    }
}
