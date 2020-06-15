package com.aosama.it.utiles;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import com.aosama.it.models.responses.boards.UserBoard;
import com.linkedin.android.spyglass.mentions.Mentionable;
import com.linkedin.android.spyglass.tokenization.QueryToken;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.ref.WeakReference;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public abstract class MentionsLoader<T extends Mentionable> {

    protected List<T> mData;
    private static final String TAG = MentionsLoader.class.getSimpleName();

    public MentionsLoader(List<UserBoard> userBoards) {

        new LoadJSONArray(userBoards).execute();
    }

    public abstract List<T> loadData(List<UserBoard> userBoards);

    // Returns a subset
    public List<T> getSuggestions(QueryToken queryToken) {
        String prefix = queryToken.getKeywords().toLowerCase();
        List<T> suggestions = new ArrayList<>();
        if (mData != null) {
            for (T suggestion : mData) {
                String name = suggestion.getSuggestiblePrimaryText().toLowerCase();
                if (name.startsWith(prefix)) {
                    suggestions.add(suggestion);
                }
            }
        }
        return suggestions;
    }

    private class LoadJSONArray extends AsyncTask<Void, Void, List<UserBoard>> {

        List<UserBoard> userBoards;

        public LoadJSONArray(List<UserBoard> userBoards) {
            this.userBoards = userBoards;
        }


        @Override
        protected void onPostExecute(List<UserBoard> arr) {
            super.onPostExecute(arr);
            mData = loadData(arr);
        }

        @Override
        protected List<UserBoard> doInBackground(Void... voids) {
            return userBoards;
        }
    }

}
