package com.christiankula.seamktimetablereader.enums;

public enum EnumDay {
    MONDAY(0),

    TUESDAY(1),

    WEDNESDAY(2),

    THURSDAY(3),

    FRIDAY(4),

    SATURDAY(5);
    

    private int index;

    private EnumDay(int index) {
	this.index = index;
    }

    public int getIndex() {
	return this.index;
    }
    
    public static EnumDay getDayByIndex(int index){
	EnumDay found = null;

	for (EnumDay eeh : EnumDay.values()) {
	    if (eeh.getIndex() == index) {
		found = eeh;
	    }
	}

	return found;
    }
}
