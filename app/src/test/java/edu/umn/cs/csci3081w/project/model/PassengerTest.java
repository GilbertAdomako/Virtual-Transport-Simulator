package edu.umn.cs.csci3081w.project.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PassengerTest {
  private Passenger passenger;

  /**
   * Setup operations before each test runs.
   */
  @BeforeEach
  public void setUp() {
    passenger = new Passenger(2, "Goldy");
  }

  /**
   * Tests that all parameters values and state are correct
   * immediately after constructing a passenger object.
   */
  @Test
  public void testConstructorNormal() {
    assertEquals(2, passenger.getDestination());
    assertFalse(passenger.isOnVehicle());
  }

  /**
   * Test setting a passenger on a vehicle.
   */
  @Test
  public void testSetOnVehicle() {
    passenger.setOnVehicle();
    assertTrue(passenger.isOnVehicle());
  }

  /**
   * Test update if passenger is on vehicle.
   */
  @Test
  public void testUpdateOnVehicle() {
    try {
      passenger.setOnVehicle();
      passenger.pasUpdate();
      passenger.pasUpdate();
      final Charset charset = StandardCharsets.UTF_8;
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      PrintStream testStream = new PrintStream(outputStream, true, charset.name());
      passenger.report(testStream);
      outputStream.flush();
      String data = new String(outputStream.toByteArray(), charset);
      testStream.close();
      outputStream.close();
      assertTrue(data.contains("Time on vehicle: 3"));
    } catch (IOException ioe) {
      fail();
    }
  }

  /**
   * Test update if passenger is at a stop.
   */
  @Test
  public void testUpdateAtStop() {
    try {
      passenger.pasUpdate();
      passenger.pasUpdate();
      final Charset charset = StandardCharsets.UTF_8;
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      PrintStream testStream = new PrintStream(outputStream, true, charset.name());
      passenger.report(testStream);
      outputStream.flush();
      String data = new String(outputStream.toByteArray(), charset);
      testStream.close();
      outputStream.close();
      assertTrue(data.contains("Wait at stop: 2"));
    } catch (IOException ioe) {
      fail();
    }
  }

  /**
   * Test reporting functionality for passenger.
   */
  @Test
  public void testStopReportWithPassenger() {
    try {
      final Charset charset = StandardCharsets.UTF_8;
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      PrintStream testStream = new PrintStream(outputStream, true, charset.name());
      passenger.report(testStream);
      outputStream.flush();
      String data = new String(outputStream.toByteArray(), charset);
      testStream.close();
      outputStream.close();
      String strToCompare =
          "####Passenger Info Start####" + System.lineSeparator()
              + "Name: Goldy" + System.lineSeparator()
              + "Destination: 2" + System.lineSeparator()
              + "Wait at stop: 0" + System.lineSeparator()
              + "Time on vehicle: 0" + System.lineSeparator()
              + "####Passenger Info End####" + System.lineSeparator();
      assertEquals(data, strToCompare);
    } catch (IOException ioe) {
      fail();
    }
  }
}

