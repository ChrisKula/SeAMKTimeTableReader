package com.christiankula.seamktimetablereader.enums;

public enum EnumEndingHour {

    EIGHT_FORTY_FIVE(8, 45, 0),

    NINE_FORTY(9, 40, 1),

    TEN_THIRTY_FIVE(10, 35, 2),

    ELEVEN_THIRTY(11, 30, 3),

    THIRTEEN_OCLOCK(13, 0, 4),

    FOURTEEN_OCLOCK(14, 0, 5),

    FIFTEEN_OCLOCK(15, 0, 6),

    SIXTEEN(16, 0, 7),

    SEVENTEEN(17, 0, 8),

    SEVENTEEN_FIFTY_FIVE(17, 55, 9),

    EIGHTEEN_FORTY_FIVE(18, 45, 10),

    NINETEEN_THIRTY_FIVE(19, 35, 11),

    TWENTY_TWENTY_FIVE(20, 25, 12),

    TWENTY_ONE_FIFTEEN(21, 15, 13);

    private int hour;

    private int minute;

    private int index;

    EnumEndingHour(int hour, int minute, int index) {
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

    public static EnumEndingHour getEndingHourByIndex(int index) {
	EnumEndingHour found = null;

	for (EnumEndingHour eeh : EnumEndingHour.values()) {
	    if (eeh.getIndex() == index) {
		found = eeh;
	    }
	}

	return found;
    }

}
