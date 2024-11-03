package com.be.controllerTest;

import com.be.controller.TestController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestControllerTest {

    TestController testController = new TestController();
    @Test
    public void testWelcome()
    {
        Assertions.assertEquals("welcome",testController.Welcome());
    }
}
