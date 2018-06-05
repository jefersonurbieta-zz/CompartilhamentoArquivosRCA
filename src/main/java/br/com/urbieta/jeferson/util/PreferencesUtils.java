package br.com.urbieta.jeferson.util;

import com.google.gson.Gson;
import br.com.urbieta.jeferson.data.Preferences;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PreferencesUtils {

    public static final String CONFIG_FILE = "/config.json";

    public static void initConfig() {
        Writer writer = null;
        try {
            Preferences preference = new Preferences();
            Gson gson = new Gson();
            writer = new FileWriter(PreferencesUtils.class.getResource(CONFIG_FILE).getPath());
            gson.toJson(preference, writer);
        } catch (IOException ex) {
            Logger.getLogger(PreferencesUtils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(PreferencesUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static Preferences getPreferences() {
        try {
            Gson gson = new Gson();
            Preferences preferences = gson.fromJson(new FileReader(PreferencesUtils.class.getResource(CONFIG_FILE).getPath()), Preferences.class);
            return preferences;
        } catch (FileNotFoundException ex) {
            initConfig();
            Logger.getLogger(PreferencesUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Preferences();
    }

    public static void writePreferenceToFile(Preferences preference) {
        Writer writer = null;
        try {
            Gson gson = new Gson();
            writer = new FileWriter(PreferencesUtils.class.getResource(CONFIG_FILE).getPath());
            gson.toJson(preference, writer);
        } catch (IOException ex) {
            Logger.getLogger(PreferencesUtils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(PreferencesUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
