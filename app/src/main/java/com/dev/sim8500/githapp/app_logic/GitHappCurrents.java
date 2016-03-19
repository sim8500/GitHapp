package com.dev.sim8500.githapp.app_logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sbernad on 18.03.16.
 */
public class GitHappCurrents {

    public static GitHappCurrents getInstance() { return instance; }


    public <T> T getCurrent(String name) {

        Object resObj = currents.containsKey(name) ? currents.get(name) : null;
        return (T)resObj;
    }

    public void setCurrent(String name, Object current) {
        currents.put(name, current);
    }

    private Map<String, Object> currents = new HashMap<>();

    private static final GitHappCurrents instance = new GitHappCurrents();

    private GitHappCurrents() {}
}
