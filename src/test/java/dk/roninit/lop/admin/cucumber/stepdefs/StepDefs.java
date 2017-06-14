package dk.roninit.lop.admin.cucumber.stepdefs;

import dk.roninit.lop.admin.LopadminappApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = LopadminappApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
