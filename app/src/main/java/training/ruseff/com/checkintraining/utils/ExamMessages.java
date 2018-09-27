package training.ruseff.com.checkintraining.utils;

import java.util.Calendar;

public class ExamMessages {

    public static String getExamMessage(int trainings, int lastPayment) {
        if(trainings >= 25) {
            if(lastPayment == Calendar.getInstance().get(Calendar.MONTH)) {
                return getOkayMessage(trainings);
            } else {
                return getErrorMessagePayments(trainings);
            }
        } else {
            if(lastPayment == Calendar.getInstance().get(Calendar.MONTH)) {
                return getErrorMessageTrainings(trainings);
            } else {
                return getErrorMessageTrainingsAndPayments(trainings);
            }
        }
    }

    private static String getErrorMessageTrainings(int trainings) {
        StringBuilder sb = new StringBuilder("Не може да бъде допуснат до изпит. Има ");
        sb.append(trainings);
        if(trainings == 1) {
            sb.append(" тренировка");
        } else {
            sb.append(" тренировки");
        }
        sb.append(" за последните 3 месеца и няма неплатени задължения");
        return sb.toString();
    }

    private static String getErrorMessagePayments(int trainings) {
        StringBuilder sb = new StringBuilder("Не може да бъде допуснат до изпит. Има ");
        sb.append(trainings);
        sb.append(" тренировки за последните 3 месеца, но има неплатени задължения");
        return sb.toString();
    }

    private static String getErrorMessageTrainingsAndPayments(int trainings) {
        StringBuilder sb = new StringBuilder("Не може да бъде допуснат до изпит. Има ");
        sb.append(trainings);
        if(trainings == 1) {
            sb.append(" тренировка");
        } else {
            sb.append(" тренировки");
        }
        sb.append(" за последните 3 месеца и има неплатени задължения");
        return sb.toString();
    }

    private static String getOkayMessage(int trainings) {
        StringBuilder sb = new StringBuilder("Може да бъде допуснат до изпит. Има ");
        sb.append(trainings);
        sb.append(" тренировки за последните 3 месеца и няма неплатени задължения");
        return sb.toString();
    }

}
