package com.zy.plugin;


import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class Test implements Plugin<Project> {

    @Override
    void apply(Project project) {

        project.task("testTask") << {
                println("hello world")
        }

    }
}