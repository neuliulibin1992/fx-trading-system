package com.enginemobi.fx.cucumber.stepdefs;

import com.enginemobi.fx.FxgatewayApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = FxgatewayApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
