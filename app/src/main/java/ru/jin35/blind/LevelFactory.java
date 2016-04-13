package ru.jin35.blind;

import android.content.Context;
import android.content.SharedPreferences;
import ru.jin35.blind.level.Big;
import ru.jin35.blind.level.FirstBlindSpots;
import ru.jin35.blind.level.Intro;
import ru.jin35.blind.level.Level;
import ru.jin35.blind.level.Snail;
import ru.jin35.blind.level.Snake;

import java.util.HashMap;

public class LevelFactory {

    private static final String PREFS_FILE = "blind";
    private static final String PREF_LEVEL_KEY = "level";

    private static java.util.Map<Integer, Level> levels = new HashMap<>();

    static {
        addLevel(new Intro(0, 1));
        addLevel(new FirstBlindSpots(1, 2));
        addLevel(new Snail(2, 3));
        addLevel(new Snake(3, 4));
        addLevel(new Big(4, 5));
    }

    private static void addLevel(Level level) {
        levels.put(level.getId(), level);
    }

    public static Level getLevel(Context context) {
        SharedPreferences prefs = getPrefs(context);
        int level = prefs.getInt(PREF_LEVEL_KEY, 0);
        Level result = levels.get(level);
        if (result == null) {//game complete
            result = levels.get(level - 1);
        }
        return result;
    }

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
    }

    public static boolean onLevelComplete(Context context, Level level) {
        boolean result = true;
        Integer nextId = level.getNextId();
        SharedPreferences prefs = getPrefs(context);
        SharedPreferences.Editor editor = prefs.edit();
        if (nextId == null) {
            result = false;
            editor.remove(PREF_LEVEL_KEY);
        } else {
            editor.putInt(PREF_LEVEL_KEY, nextId);
        }
        editor.apply();
        return result;
    }

    public static void resetProgress(Context context){
        getPrefs(context).edit().remove(PREF_LEVEL_KEY).apply();
    }
}
