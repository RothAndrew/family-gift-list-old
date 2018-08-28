package com.rothandrew.familygiftlist.cucumber.stepdefs;

import com.rothandrew.familygiftlist.FamilygiftlistApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = FamilygiftlistApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
