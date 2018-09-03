package com.rothandrew.familygiftlist.gateway.cucumber.stepdefs;

import com.rothandrew.familygiftlist.gateway.FglgatewayApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = FglgatewayApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
