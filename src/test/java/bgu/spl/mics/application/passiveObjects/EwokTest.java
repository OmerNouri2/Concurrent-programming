package bgu.spl.mics.application.passiveObjects;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EwokTest {

    private Ewok ewok;

    @BeforeEach
    void setUp() {
        ewok = new Ewok(1); // we use the default constructor
        ewok.acquire();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void acquire() {
        assertFalse(ewok.available); // After acquire() - the available flag should be false
        try{
            ewok.acquire();
            fail("should not do acquire when resource is acquired");
        }
        catch (Exception e){
            assertTrue(true);
        }
    }

    @Test
    void release() {
        ewok.release();
        assertTrue(ewok.available); // After release() - the available flag should be true
        try{
            ewok.acquire();
            assertTrue(true); // After release() ewok should be available
        }
        catch (Exception e){
            fail(); // fail - if after release() ewok in not available
        }
    }
}