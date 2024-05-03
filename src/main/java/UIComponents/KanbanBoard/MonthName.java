package UIComponents.KanbanBoard;

public class MonthName {
    private final static String[] shortNames= {
            "ЯНВ",
            "ФЕВ",
            "МАРТ",
            "АПР",
            "МАЙ",
            "ИЮНЬ",
            "ИЮЛЬ",
            "АВГ",
            "СЕНТ",
            "ОКТ",
            "НОЯБ",
            "ДЕК",
    };
    private final static String[] fullNames= {
            "Январь",
            "Февраль",
            "Март",
            "Апрель",
            "Мая",
            "Июнь",
            "Июль",
            "Август",
            "Сентябрь",
            "Октябрь",
            "Ноябрь",
            "Декабрь",
    };
    public static String getFullName(int index){
        return fullNames[index];
    }
    public static String getShortName(int index){
        return shortNames[index];
    }


}
