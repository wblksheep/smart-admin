package net.lab1024.sa.admin.module.business.sprinklermanager.statistic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MachineConfigUpdater {

    public static void updateMachineType(String machineType) throws IOException {
        // 1. 构建配置文件路径
        Path configPath = Paths.get("config/config.json");

        // 2. 读取文件内容
        String content = Files.readString(configPath, StandardCharsets.UTF_8);

        // 3. 解析JSON并更新字段
        JsonObject config = JsonParser.parseString(content).getAsJsonObject();
        JsonObject machineTypeObj = config.getAsJsonObject("machinetype");
        machineTypeObj.addProperty("on", machineType);  // 更新目标字段

        // 4. 格式化JSON（保留缩进等格式）
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String updatedJson = gson.toJson(config);

        // 5. 写入文件并强制刷盘
        Files.writeString(configPath, updatedJson, StandardCharsets.UTF_8);
        // 然后强制刷盘
        try (FileOutputStream fos = new FileOutputStream(configPath.toFile(), true)) {
            fos.getFD().sync();
        }
    }

    // 使用示例
    public static void main(String[] args) {
        try {
            // 更新为新的机器类型（如 "samba"）
            updateMachineType("samba");

            System.out.println("配置文件更新成功！");

            // 此处添加后续操作...
        } catch (Exception e) {
            System.err.println("配置文件更新失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}