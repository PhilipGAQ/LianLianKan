package LLK;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SystemParameters {
    private Properties properties;
    private String propertiesFilePath;

    public SystemParameters(String propertiesFilePath) {
        this.propertiesFilePath = propertiesFilePath;
        properties = new Properties();
        loadProperties();
    }

    private void loadProperties() {
        try (FileInputStream in = new FileInputStream(propertiesFilePath)) {
            properties.load(in);
        } catch (IOException e) {
            System.out.println("Error loading properties file: " + e.getMessage());
        }
    }

    private void saveProperties() {
        try (FileOutputStream out = new FileOutputStream(propertiesFilePath)) {
            properties.store(out, null);
        } catch (IOException e) {
            System.out.println("Error saving properties file: " + e.getMessage());
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
        saveProperties();
    }

    public void removeProperty(String key) {
        properties.remove(key);
        saveProperties();
    }

    public void listProperties() {
        properties.list(System.out);
    }

    
}
