package com.libertas.boatlang;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.stream.Collectors;

public class BoatUpdater {

    private static final String GITHUB_API_URL = "https://api.github.com/repos/Libertas007/BoatLang/releases/latest";
    private static final String BOAT_DIR = System.getProperty("user.home") + (isWindows() ? "\\.boat\\" : "/.boat/");
    private static final String CURRENT_VERSION_FILE = BOAT_DIR + "version.txt";

    public static void update() {
        try {
            String latestVersion = getLatestVersion();
            Date latestVersionUpdate = getLatestVersionUpdate();
            String currentVersion = getCurrentVersion();
            Date currentVersionUpdate = getCurrentVersionUpdate();

            if (latestVersionUpdate.after(currentVersionUpdate)) {
                System.out.println("\uD83C\uDD95 New version available: " + currentVersion + " -> " + latestVersion);
                downloadAndRunScript();
                saveCurrentVersion(latestVersion, latestVersionUpdate);
            } else {
                System.out.println("\uD83D\uDC4D Boat is up to date.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getLatestVersion() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(GITHUB_API_URL).openConnection();
        connection.setRequestProperty("User-Agent", "Java");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String response = reader.lines().collect(Collectors.joining());
            return response.split("\"tag_name\":\"")[1].split("\"")[0];
        }
    }

    public static Date getLatestVersionUpdate() throws IOException, ParseException {
        HttpURLConnection connection = (HttpURLConnection) new URL(GITHUB_API_URL).openConnection();
        connection.setRequestProperty("User-Agent", "Java");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String response = reader.lines().collect(Collectors.joining());
            String iso = response.split("\"updated_at\":\"")[1].split("\"")[0];
            LocalDateTime local = LocalDateTime.parse(iso.replace("Z", ""));
            Instant instant = local.toInstant(ZoneOffset.UTC);
            return Date.from(instant);
        }
    }

    public static String getCurrentVersion() {
        try {
            return new String(Files.readAllBytes(Paths.get(CURRENT_VERSION_FILE))).trim().split(";;")[0];
        } catch (IOException e) {
            return "";
        }
    }

    public static Date getCurrentVersionUpdate() {
        try {
            return new Date(Long.parseLong(new String(Files.readAllBytes(Paths.get(CURRENT_VERSION_FILE))).trim().split(";;")[1]));
        } catch (IOException e) {
            return new Date();
        }
    }

    private static void downloadAndRunScript() throws IOException {
        String installScriptUrl = isWindows() ? "https://raw.githubusercontent.com/Libertas007/BoatLang/refs/heads/main/scripts/install.ps1" : "https://raw.githubusercontent.com/Libertas007/BoatLang/refs/heads/main/scripts/install.sh";
        String scriptPath = BOAT_DIR + (isWindows() ? "install.ps1" : "install.sh");

        HttpURLConnection connection = (HttpURLConnection) new URL(installScriptUrl).openConnection();
        connection.setRequestProperty("User-Agent", "Java");

        try (InputStream in = connection.getInputStream();
             FileOutputStream out = new FileOutputStream(scriptPath)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }

        ProcessBuilder processBuilder;
        if (isWindows()) {
            processBuilder = new ProcessBuilder("powershell", "-ExecutionPolicy", "Bypass", "-File", scriptPath);
        } else {
            processBuilder = new ProcessBuilder("bash", scriptPath);
        }

        processBuilder.inheritIO();
        Process process = processBuilder.start();
        System.out.println("The script will run while this process will exit. Although this may cause some unwanted visuals, it should be functional. If necessary, press CTRL+C or ENTER after installing.");
        System.exit(0);
    }

    private static void saveCurrentVersion(String version, Date update) throws IOException {
        Files.write(Paths.get(CURRENT_VERSION_FILE), (version + ";;" + update.getTime()).getBytes());
    }

    private static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }
}
