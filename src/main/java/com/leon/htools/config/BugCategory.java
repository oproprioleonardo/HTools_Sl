package com.leon.htools.config;

import lombok.Data;

import java.util.Set;

@Data
public class BugCategory {

    private String id;
    private String label;
    private Set<Bug> bugs;
}
