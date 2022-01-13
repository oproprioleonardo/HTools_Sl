package com.leon.htools.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.FileWriter;
import java.io.IOException;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Config {

    private transient final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private long adminRole;
    private long teamGuildId;
    private long recruitmentGuildId;
    private long referenceGuildId;
    private long verifiedRoleId;

    private RecruitmentConfig recruitmentSystem;

    public Gamemode getGamemode(String name) {
        return recruitmentSystem.getGamemodes()
                                .stream()
                                .filter(gamemode -> gamemode.getName().equalsIgnoreCase(name))
                                .findFirst()
                                .get();
    }

    public void saveRemoteChanges() throws IOException {
        final FileWriter writer = new FileWriter("config.json");
        final String json = gson.toJson(this);
        writer.write(json);
        writer.flush();
        writer.close();
    }

}
