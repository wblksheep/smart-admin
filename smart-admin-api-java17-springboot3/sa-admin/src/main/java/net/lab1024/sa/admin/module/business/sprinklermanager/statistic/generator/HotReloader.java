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

public class HotReloader {
    private static final String FILE_PATH = "./config/hello2.json";
    public static Integer[][] MONTHBEGIN;

    // 初始化时加载并启动监听线程
    public static void init() {
        printResourcePath(); // 新增路径打印
        loadFile();
        startFileWatcher();
    }

    // 修改后的文件加载方法
    private static void loadFile() {
        try {
            Path fullPath = Paths.get(System.getProperty("user.dir"), FILE_PATH);
            System.out.println("正在加载文件: " + fullPath); // 新增加载路径打印

            try (InputStream is = Files.newInputStream(fullPath)) {
                MONTHBEGIN = loadFromJson(is);
                System.out.println("文件已重新加载");
            }
        } catch (Exception e) {
            System.err.println("加载失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 新增方法：打印资源路径信息
    private static void printResourcePath() {
        try {
            // 获取 JAR 运行目录
            String jarDir = System.getProperty("user.dir");
            System.out.println("JAR运行目录: " + jarDir);

            // 构建完整资源路径
            Path fullPath = Paths.get(jarDir, FILE_PATH).toAbsolutePath();
            System.out.println("配置文件完整路径: " + fullPath);

            // 验证文件是否存在
            if (!Files.exists(fullPath)) {
                System.err.println("警告: 配置文件不存在于上述路径");
            }
        } catch (Exception e) {
            System.err.println("路径打印失败: " + e.getMessage());
        }
    }

    private static void startFileWatcher() {
        new Thread(() -> {
            try {
                WatchService watchService = FileSystems.getDefault().newWatchService();
                Paths.get(FILE_PATH).getParent().register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

                while (true) {
                    WatchKey key = watchService.take();
                    for (WatchEvent<?> event : key.pollEvents()) {
                        if (event.context().toString().equals(Paths.get(FILE_PATH).getFileName().toString())) {
                            loadFile(); // 文件修改时触发重新加载
                        }
                    }
                    key.reset();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static Integer[][] loadFromJson(InputStream is) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            Gson gson = new Gson();
            Type arrayType = new TypeToken<Integer[][]>() {}.getType();
            return gson.fromJson(reader, arrayType);
        } catch (JsonSyntaxException | JsonIOException e) {
            throw new IOException("JSON解析失败: " + e.getMessage(), e);
        }
    }
}
