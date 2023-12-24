import classes.Exercise;
import classes.Practice;
import classes.Student;
import classes.Theme;

import java.util.*;

public class ChartDatasetBuilder {
    public static HashMap<String, Integer> getTenBestStudents(HashMap<String, HashMap<Practice, Integer>> uidPracticeScore, HashMap<String, HashMap<Exercise, Integer>> uidExerciseScore, ArrayList<Student> students) {
        HashMap<String, Integer> pupils = new HashMap<>();
        HashMap<String, Integer> PracticeScore = new HashMap<>();
        HashMap<String, Integer> ExerciseScore = new HashMap<>();

        for (var pr : uidPracticeScore.entrySet()) {
            int sum = 0;
            for (var val : pr.getValue().values()) {
                sum += val;
            }
            PracticeScore.put(pr.getKey(), sum);
        }
        for (var pr : uidExerciseScore.entrySet()) {
            int sum = 0;
            for (var val : pr.getValue().values()) {
                sum += val;
            }
            ExerciseScore.put(pr.getKey(),sum);
        }
        for (var st: PracticeScore.entrySet()) {
            for (var ex: ExerciseScore.entrySet()) {
                if (Objects.equals(st.getKey(), ex.getKey())) {
                    pupils.put(ex.getKey(), ex.getValue() + st.getValue());
                }
            }
        }

        var nupupils = sortByValue(pupils);
        HashMap<String ,Integer> bestpupils = new HashMap<>();
        int count = 0;
        for (var val: nupupils.entrySet()) {
            if (count<10){
                bestpupils.put(val.getKey(),val.getValue());}
            count++;
        }

        HashMap<String,Integer> updateBestPupils = new HashMap<>();
        for (var st: students){
            for (var ulid: bestpupils.entrySet()){
                if (Objects.equals(st.ulearnID, ulid.getKey())){
                    updateBestPupils.put(st.firstname+' '+st.lastname, ulid.getValue());
                }
            }
        }
        return updateBestPupils;
    }

    public static HashMap<String, Integer> getTotalExcersiseScorePerTheme(ArrayList<Theme> dbThemes, HashMap<Exercise, ArrayList<Integer>> exersise) {
        HashMap<String, Integer> ThemesScoresEx = new HashMap<>();
        for (var theme: dbThemes) {
            int sumThemesScoreEx = 0;
            for (Exercise ex: theme.getExercises()){
                int sumScore = 0;
                String id = ex.getId();
                for (var exscore: exersise.entrySet()){
                    if (Objects.equals(id, exscore.getKey().getId())){
                        sumScore += sum(exscore.getValue());
                    }
                }
                sumThemesScoreEx+=sumScore;
            }
            ThemesScoresEx.put(theme.getTitle(),sumThemesScoreEx);
        }
        return ThemesScoresEx;
    }

    public static HashMap<String, Integer> getTotalPracticeScorePerTheme(ArrayList<Theme> themes, HashMap<Practice, ArrayList<Integer>> practice) {
        HashMap<String, Integer> ThemesScores = new HashMap<>();
        for (var theme: themes) {
            int sumThemesScore = 0;
            for (Practice pr: theme.getPractices()){
                int sumScore = 0;
                String id = pr.getId();
                for (var prscore: practice.entrySet()){
                    if (Objects.equals(id, prscore.getKey().getId())){
                        sumScore += sum(prscore.getValue());
                    }
                }
                sumThemesScore+=sumScore;
            }
            ThemesScores.put(theme.getTitle(),sumThemesScore);
        }
        return ThemesScores;
    }

    public static HashMap<String, Integer> getAvgScorePerPractice(HashMap<Practice, ArrayList<Integer>> practice) {
        HashMap<String, Integer> fullScoreByPractice = new HashMap<>();
        for (var e : practice.entrySet()
        ) {
            int sum = 0;
            for (int score : e.getValue()
            ) {
                sum += score;
            }
            if (sum == 0) continue;
            fullScoreByPractice.put(e.getKey().getTitle(), sum);
        }
        return fullScoreByPractice;
    }

    public static HashMap<String, Integer> getTotalScorePerExcersise(HashMap<Exercise, ArrayList<Integer>> exersise) {
        HashMap<String, Integer> fullScoreByExcersize = new HashMap<>();
        for (var e : exersise.entrySet()
        ) {
            int sum = 0;
            for (int score : e.getValue()
            ) {
                sum += score;
            }
            if (sum == 0) continue;
            fullScoreByExcersize.put(e.getKey().getTitle(), sum);
        }
        return fullScoreByExcersize;
    }

    public static HashMap<String, Integer> getAvgScorePerExcersise(HashMap<Exercise, ArrayList<Integer>> exersise) {
        HashMap<String, Integer> avgScoreByExcersize = new HashMap<>();
        for (var e : exersise.entrySet()
        ) {
            int sum = 0;
            for (int score : e.getValue()
            ) {
                sum += score;
            }
            if (sum / e.getValue().size() == 0) continue;
            avgScoreByExcersize.put(e.getKey().getTitle(), sum / e.getValue().size());
        }
        return avgScoreByExcersize;
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list.reversed()) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }
    public static int sum(ArrayList<Integer> values) {
        int result = 0;
        for (var elem: values){
            result+=elem;
        }
        return result;
    }
}
