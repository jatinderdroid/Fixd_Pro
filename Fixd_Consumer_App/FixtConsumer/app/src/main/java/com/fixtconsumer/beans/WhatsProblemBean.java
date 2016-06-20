package com.fixtconsumer.beans;

import java.io.Serializable;

/**
 * Created by sahil on 11-05-2016.
 */
public class WhatsProblemBean implements Serializable{
    String id = "";
    String problem = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }
}
