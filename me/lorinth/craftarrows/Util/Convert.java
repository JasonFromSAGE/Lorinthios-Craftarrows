package me.lorinth.craftarrows.Util;

public class Convert {

    public static <T> T Convert(Class<T> classType, Object value){
        if(classType == Integer.class)
            return (T) ConvertToInteger(value);
        else if(classType == Double.class)
            return (T) ConvertToDouble(value);
        else if(classType == Float.class)
            return (T)ConvertToFloat(value);
        return null;
    }

    public static Double ConvertToDouble(Object value){
        if(value instanceof Integer)
            return (double) (int) value;
        else if(value instanceof Double)
            return (double) value;
        else if(value instanceof Float)
            return (double) (float) value;
        else if(TryParse.parseDouble(value.toString()))
            return Double.parseDouble(value.toString());

        return null;
    }

    public static Float ConvertToFloat(Object value){
        if(value instanceof Integer)
            return (float) (int) value;
        else if(value instanceof Double)
            return (float) (double) value;
        else if(value instanceof Float)
            return (float) value;
        else if(TryParse.parseFloat(value.toString()))
            return Float.parseFloat(value.toString());

        return null;
    }

    public static Integer ConvertToInteger(Object value){
        if(value instanceof Integer)
            return (int) value;
        else if(value instanceof Double)
            return (int) (double) value;
        else if(value instanceof Float)
            return (int) (float) value;
        else if(TryParse.parseInt(value.toString()))
            return Integer.parseInt(value.toString());

        return null;
    }

}
