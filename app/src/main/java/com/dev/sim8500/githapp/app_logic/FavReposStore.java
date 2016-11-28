package com.dev.sim8500.githapp.app_logic;
import android.content.Context;

import com.dev.sim8500.githapp.GitHappApp;
import com.dev.sim8500.githapp.models.RepoModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbernad on 27.11.2016.
 */

final public class FavReposStore {

    private List<RepoModel> favRepos = new ArrayList<RepoModel>();
    private boolean fileIsRead = false;
    private Gson gson = new Gson();
    private static final String FAV_REPOS_FILE_NAME = "favrepos";
    private static final FavReposStore instance = new FavReposStore();

    private FavReposStore() {}

    public static FavReposStore getInstance() { return instance; }

    public List<RepoModel> getFavRepos() {

        FileInputStream fInStream = getFavReposFile();
        if(fInStream != null)
        {
            favRepos.clear();
            //read file to string
            StringBuilder strBuilder = new StringBuilder();
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(fInStream));
            String tmp = null;
            try {
                while((tmp = bufReader.readLine()) != null)
                {
                    strBuilder.append(tmp).append("\n");
                }
            }
            catch(IOException ex) {
              // nothing to do here
            }
            tmp = strBuilder.toString();

            JsonParser jsonParser = new JsonParser();
            JsonArray jarr = jsonParser.parse(tmp).getAsJsonObject().get("favRepos").getAsJsonArray();

            for (int i = 0; i < jarr.size(); ++i) {
                favRepos.add(gson.fromJson(jarr.get(i), RepoModel.class));
            }
            try {
                fInStream.close();
            }
            catch(IOException ex) {
                // nothing to do here
            }
        }

        return favRepos;
    }

    public List<RepoModel> addFavRepo(RepoModel repo) {

        if(repo != null) {
            // do the initial checks
            if(!fileIsRead) {
                getFavRepos();
                fileIsRead = true;
            }
            for(RepoModel rm : favRepos)
            {
                // don't allow duplicates
                if(rm.url.equals(repo.url)) {
                    return favRepos;
                }
            }

            favRepos.add(repo);

            saveFavRepos();
        }
        return favRepos;
    }

    protected FileInputStream getFavReposFile() {

        FileInputStream fInStream = null;
        try {
            fInStream = GitHappApp.getInstance().openFileInput(FAV_REPOS_FILE_NAME);
        }
        catch(FileNotFoundException fnfEx)
        {
            // file not found - do nothing...
        }
        return fInStream;
    }

    protected void saveFavRepos() {

        FileOutputStream fOut = openFavReposFile();

        if(fOut != null) {
            BufferedWriter bufWriter = new BufferedWriter(new OutputStreamWriter(fOut));
            JsonObject resObj = new JsonObject();
            JsonArray jarr = new JsonArray();

            for (RepoModel fr: favRepos) {
                jarr.add(gson.toJsonTree(fr));
            }
            resObj.add("favRepos", jarr);
            try {
                bufWriter.write(resObj.toString());
                bufWriter.close();
            }
            catch(IOException ex) {
                // nothing to do here
            }
        }

    }

    protected FileOutputStream openFavReposFile() {
        FileOutputStream fInStream = null;

        try {
            fInStream = GitHappApp.getInstance().openFileOutput(FAV_REPOS_FILE_NAME, Context.MODE_PRIVATE);
        }
        catch(FileNotFoundException ex) {
            //nothing to do here
        }

        return fInStream;
    }


}
