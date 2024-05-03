package Utils;

public class WeekDayName {
    private final static String[] shortNames= {
            "ПН",
            "ВТ",
            "СР",
            "ЧТ",
            "ПТ",
            "СБ",
            "ВС",
    };
    private final static String[] fullNames= {
            "Понедельник",
            "Вторник",
            "Среда",
            "Четверг",
            "Пятница",
            "Суббота",
            "Воскресенье",
    };
    public static String getFullName(int index){
        return fullNames[index];
    }
    public static String getShortName(int index){
        return shortNames[index];
    }
}
