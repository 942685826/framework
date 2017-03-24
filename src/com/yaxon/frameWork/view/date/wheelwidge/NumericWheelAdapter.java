package com.yaxon.frameWork.view.date.wheelwidge;

/**
 * Numeric Wheel adapter.
 */
public class NumericWheelAdapter implements WheelAdapter {

    /**
     * The default min value
     */
    public static final int DEFAULT_MAX_VALUE = 9;

    /**
     * The default max value
     */
    private static final int DEFAULT_MIN_VALUE = 0;

    // Values
    private int minValue;
    private int maxValue;

    // Values
    private double minValue1;
    private double maxValue1;
    // format
    private String format;

    private String values = null;

    private String datevalues = null;

    private String data[] = new String[215];

    private boolean ismin = false;

    private boolean ishour = false;
    private boolean isday = false;
    private boolean ismon = false;
    private boolean isOld = false;

    private int week;
    private String string = null;
    private int dateposition = 0;

    /**
     * Constructor
     *
     * @param minValue the wheel min value
     * @param maxValue the wheel max value
     */
    public NumericWheelAdapter(int minValue, int maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    /**
     * Constructor
     *
     * @param minValue the wheel min value
     * @param maxValue the wheel max value
     * @param format   the format string
     */
    public NumericWheelAdapter(int minValue, int maxValue, String format) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.format = format;
    }

    public NumericWheelAdapter(String tomo, int week, int type, boolean isOld) {
        this.string = "今天";
        this.week = week;
        if (type == 3) {
            this.isday = true;
        }
        this.isOld = isOld;
    }

    public NumericWheelAdapter(int minValue, int maxValue, String format, int type) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.format = format;
        if (type == 1) {
            this.ismin = true;
        } else if (type == 2) {
            this.ishour = true;
        } else if (type == 3) {
            this.isday = true;
        } else if (type == 4) {
            this.ismon = true;
        }

    }

    public String getItem(int index) {

        if (index >= 0 && index < getItemsCount()) {
            if (ismin) {
                if (index == 0) {
                    int value = minValue + index;
                    values = (format != null ? String.format(format, value) : Integer
                            .toString(value));
                    setValue(values);
                    setdateValue(values);
                    return values + "分";
                } else if (index == 1) {
                    int value = 30;
                    values = (format != null ? String.format(format, value) : Integer
                            .toString(value));
                    setValue(values);
                    setdateValue(values);
                    return values + "分";
                }

            } else if (isday) {
                dateposition = index;
                if (isOld) {
                    if (index == 0) {
                        string = "后天";
                        setValue(string);
                        return string;
                    } else {
                        if ((week + index) % 7 == 6) {
                            string = "周日";
                        } else if ((week + index) % 7 == 0) {
                            string = "周一";
                        } else if ((week + index) % 7 == 1) {
                            string = "周二";
                        } else if ((week + index) % 7 == 2) {
                            string = "周三";
                        } else if ((week + index) % 7 == 3) {
                            string = "周四";
                        } else if ((week + index) % 7 == 4) {
                            string = "周五";
                        } else if ((week + index) % 7 == 5) {
                            string = "周六";
                        }
                        setValue(string);
                        return string;
                    }
                } else {
                    if (index == 0) {
                        string = "明天";
                        setValue(string);
                        return string;
                    } else if (index == 1) {
                        string = "后天";
                        setValue(string);
                        return string;
                    } else {
                        if ((week + index) % 7 == 0) {
                            string = "周日";
                        } else if ((week + index) % 7 == 1) {
                            string = "周一";
                        } else if ((week + index) % 7 == 2) {
                            string = "周二";
                        } else if ((week + index) % 7 == 3) {
                            string = "周三";
                        } else if ((week + index) % 7 == 4) {
                            string = "周四";
                        } else if ((week + index) % 7 == 5) {
                            string = "周五";
                        } else if ((week + index) % 7 == 6) {
                            string = "周六";
                        }
                        setValue(string);
                        return string;
                    }
                }


            } else {
                int value = minValue + index;
                values = (format != null ? String.format(format, value) : Integer
                        .toString(value));
                setValue(values);
                if (ishour) {
                    return values + "时";
                } else if (ismon) {
                    return values + "月";
                } else {
                    return values;
                }
            }

        }
        return null;
    }


    public String getValues() {
        return values;
    }

    public int getDate() {
        return dateposition;
    }

    public void setValue(String value) {
        this.values = value;
    }

    public String getdateValues() {
        return datevalues;
    }

    public void setdateValue(String value) {
        this.datevalues = value;
    }

    public int getItemsCount() {
        if (ismin) {
            return 2;
        } else if (isday) {
            return 7;
        } else {
            return maxValue - minValue + 1;
        }

    }


    public int getMaximumLength() {
//		int max = Math.max(Math.abs(maxValue), Math.abs(minValue));
//		int maxLen = Integer.toString(max).length();
//		if (minValue < 0) {
//			maxLen++;
//		}
        return maxValue - minValue + 1;
    }

}
