package com.binance.api;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public enum Properties {
    API_KEY("api-key", ""),
    SECRET("secret", "");

    final String name;
    String value;

    Properties(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static void load() throws IOException {
        File directory = new File(".");
        final File file = new File(directory.getCanonicalPath() + File.separator + "src/test/resources/data/config.properties");
        java.util.Properties properties = new java.util.Properties();
        if (file.exists()) {
            try (FileInputStream in = new FileInputStream(file)) {
                properties.load(in);
                Arrays.stream(Properties.values()).forEach(e -> e.value = properties.getProperty(e.name));
            }
        } else {
            Arrays.stream(Properties.values()).forEach(e -> properties.setProperty(e.name, ""));
            try (FileOutputStream out = new FileOutputStream(file)) {
                properties.store(out, null);
            }
        }
    }

    public static BinanceApiRestClient getRestClient() {
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(
                Properties.API_KEY.value, Properties.SECRET.value, true, true
        );
        return factory.newRestClient();
    }
}
