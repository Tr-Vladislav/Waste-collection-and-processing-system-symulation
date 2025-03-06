package pl.edu.pwr.student.uladzislauTryb;

public class Adresses {
    private static String officeHost = "192.168.116.123";
    private static int officePort = 1001;

    private static String sewagePlantHost = "192.168.116.156";
    private static int sewagePlantPort = 1002;

    public static String getOfficeHost(){
        return officeHost;
    }
    public static int getOfficePort(){
        return officePort;
    }
    public static String getSewagePlantHost(){
        return sewagePlantHost;
    }
    public static int getSewagePlantPort(){
        return sewagePlantPort;
    }
}
