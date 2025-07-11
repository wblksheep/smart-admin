package net.lab1024.sa.admin.module.business.sprinklermanager.statistic.generator;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import lombok.Data;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

public class HotReloader {
    // 配置文件路径
    private static final String CONFIG_FILE_PATH = "./config/config.json";
    // 数据文件路径映射
    private static final Map<String, String> DATA_FILE_MAP = new HashMap<String, String>() {{
        put("samba", "./config/hello2.json");
        put("se", "./config/hello.json");
    }};

    public static Integer[][] MONTHBEGIN;
    private static volatile String currentDataFilePath; // 当前使用的数据文件路径

    // 初始化时加载并启动监听线程
    public static void init() {
        printResourcePath();
        loadConfigAndData();
        startFileWatcher();
    }

    // 加载配置并确定数据文件路径
    private static void loadConfigAndData() {
        try {
            // 1. 加载配置文件
            Path configPath = Paths.get(System.getProperty("user.dir"), CONFIG_FILE_PATH);
            System.out.println("正在加载配置文件: " + configPath);

            MachineTypeConfig config;
            try (InputStream is = Files.newInputStream(configPath)) {
                config = loadConfigFromJson(is);
            }

            // 2. 确定数据文件路径
            String machineType = config.getMachinetype().getOn();
            currentDataFilePath = DATA_FILE_MAP.getOrDefault(machineType, DATA_FILE_MAP.get("se"));
            System.out.println("当前机器类型: " + machineType + ", 使用数据文件: " + currentDataFilePath);

            // 3. 加载数据文件
            Path dataPath = Paths.get(System.getProperty("user.dir"), currentDataFilePath);
            System.out.println("正在加载数据文件: " + dataPath);

            try (InputStream dataIs = Files.newInputStream(dataPath)) {
                MONTHBEGIN = loadFromJson(dataIs);
            }
            System.out.println("数据文件已加载");

        } catch (Exception e) {
            System.err.println("加载失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 配置文件JSON解析
    private static MachineTypeConfig loadConfigFromJson(InputStream is) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            Gson gson = new Gson();
            return gson.fromJson(reader, MachineTypeConfig.class);
        } catch (JsonSyntaxException | JsonIOException e) {
            throw new IOException("JSON解析失败: " + e.getMessage(), e);
        }
    }

    // 数据文件JSON解析
    public static Integer[][] loadFromJson(InputStream is) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            Gson gson = new Gson();
            Type arrayType = new TypeToken<Integer[][]>() {}.getType();
            return gson.fromJson(reader, arrayType);
        } catch (JsonSyntaxException | JsonIOException e) {
            throw new IOException("JSON解析失败: " + e.getMessage(), e);
        }
    }

    // 文件监听逻辑
    private static void startFileWatcher() {
        new Thread(() -> {
            try {
                WatchService watchService = FileSystems.getDefault().newWatchService();
                Path configDir = Paths.get(CONFIG_FILE_PATH).getParent();
                configDir.register(watchService,
                        StandardWatchEventKinds.ENTRY_MODIFY,
                        StandardWatchEventKinds.ENTRY_CREATE);

                while (true) {
                    WatchKey key = watchService.take();
                    for (WatchEvent<?> event : key.pollEvents()) {
                        String changedFile = event.context().toString();

                        // 1. 配置文件修改时重新加载所有配置
                        if (changedFile.equals(Paths.get(CONFIG_FILE_PATH).getFileName().toString())) {
                            System.out.println("配置文件修改，重新加载配置...");
                            loadConfigAndData();
                        }
                        // 2. 当前使用的数据文件修改时重新加载数据
                        else if (currentDataFilePath != null &&
                                changedFile.equals(Paths.get(currentDataFilePath).getFileName().toString())) {
                            System.out.println("数据文件修改，重新加载数据...");
                            Path dataPath = Paths.get(System.getProperty("user.dir"), currentDataFilePath);
                            try (InputStream dataIs = Files.newInputStream(dataPath)) {
                                MONTHBEGIN = loadFromJson(dataIs);
                                System.out.println("数据文件已重新加载");
                            }
                        }
                    }
                    key.reset();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    // 打印路径信息（保留原逻辑）
    private static void printResourcePath() {
        try {
            String jarDir = System.getProperty("user.dir");
            System.out.println("JAR运行目录: " + jarDir);

            Path configPath = Paths.get(jarDir, CONFIG_FILE_PATH).toAbsolutePath();
            System.out.println("配置文件完整路径: " + configPath);

            if (!Files.exists(configPath)) {
                System.err.println("警告: 配置文件不存在于上述路径");
            }
        } catch (Exception e) {
            System.err.println("路径打印失败: " + e.getMessage());
        }
    }

    // 配置类定义
    @Data
    static class MachineTypeConfig {
        private MachineType machinetype;
    }

    @Data
    static class MachineType {
        private String on;
    }
}