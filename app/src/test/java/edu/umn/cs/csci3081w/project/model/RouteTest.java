package edu.umn.cs.csci3081w.project.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class RouteTest {
  private Route original;
  private List<Stop> testStops;
  private List<Double> testDistance;
  private PassengerGenerator testPassenger;
  private Stop stop1;
  private Stop stop2;


  /**
   * Setup operations before each test runs.
   */
  @BeforeEach
  public void setUp() {
    List<Double> probabilitiesIn = new ArrayList<>();
    probabilitiesIn.add(.025);
    probabilitiesIn.add(0.3);
    probabilitiesIn.add(.0);

    stop1 = new Stop(0, "test stop 1", new Position(-93.243774, 44.972392));
    stop2 = new Stop(1, "test stop 2", new Position(-93.235071, 44.973580));
    testStops = new ArrayList<>();
    testStops.add(stop1);
    testStops.add(stop2);

    testDistance = new ArrayList<>();
    testDistance.add(3.0);
    testDistance.add(4.0);

    testPassenger = new RandomPassengerGenerator(testStops, probabilitiesIn);

    original = new Route(1, "line name", "BUS", "test route",
        testStops, testDistance, testPassenger);
  }

  /**
   * Tests that all parameters values and state are correct
   * immediately after constructing a Route object.
   */
  @Test
  public void testRouteConstructor() {
    assertEquals(1, original.getId());
    assertEquals("line name", original.getLineName());
    assertEquals("test route", original.getName());
    assertEquals("BUS", original.getLineType());
    assertEquals(testStops, original.getStops());
    assertEquals(stop1, original.getNextStop());
    original.nextStop();
    assertEquals(3.0, original.getNextStopDistance());
    assertEquals(stop2, original.getNextStop());
    assertEquals(1, original.getNextStopIndex());

  }

  /**
   * Tests that shallowCopy creates a distinct Route object.
   * With identical field values and stop references, and
   * verifies destination stop behavior after advancing
   */
  @Test
  public void testRouteShallowCopy() {

    Route copy = original.shallowCopy();

    assertNotSame(original, copy);

    assertEquals(1, copy.getId());
    assertEquals("line name", copy.getLineName());
    assertEquals("test route", copy.getName());
    assertEquals("BUS", copy.getLineType());
    assertEquals(testStops, copy.getStops());
    assertEquals(stop1, copy.getNextStop());

    copy.nextStop();

    assertEquals(3.0, copy.getNextStopDistance());
    assertEquals(stop2, copy.getNextStop());
  }


  /**
   * Tests that the update method does not alter the
   * destination stop and that nextStop still functions
   * as expected after calling update.
   */
  @Test
  public void testRouteUpdate() {
    assertEquals(stop1, original.getNextStop());
    original.update();
    assertEquals(stop1, original.getNextStop());
    original.nextStop();
    assertEquals(stop2, original.getNextStop());
  }

  /**
   * Tests the full output of report for a route.
   * with two stops and no passengers, including both
   * route information and stop report details.
   */
  @Test
  public void testRouteReport() {
    final Charset charset = StandardCharsets.UTF_8;
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream testStream = new PrintStream(outputStream, true, charset);
    original.report(testStream);

    String data = outputStream.toString(charset);

    String strToCompare =
        "####Route Info Start####" + System.lineSeparator()
            + "ID: 1" + System.lineSeparator()
            + "Line name: line name" + System.lineSeparator()
            + "Line type: BUS" + System.lineSeparator()
            + "Name: test route" + System.lineSeparator()
            + "Num stops: 2" + System.lineSeparator()
            + "****Stops Info Start****" + System.lineSeparator()
            + "++++Next Stop Info Start++++" + System.lineSeparator()
            + "####Stop Info Start####" + System.lineSeparator()
            + "ID: 0" + System.lineSeparator()
            + "Name: test stop 1" + System.lineSeparator()
            + "Position: 44.972392,-93.243774" + System.lineSeparator()
            + "****Passengers Info Start****" + System.lineSeparator()
            + "Num passengers waiting: 0" + System.lineSeparator()
            + "****Passengers Info End****" + System.lineSeparator()
            + "####Stop Info End####" + System.lineSeparator()
            + "++++Next Stop Info End++++" + System.lineSeparator()
            + "####Stop Info Start####" + System.lineSeparator()
            + "ID: 1" + System.lineSeparator()
            + "Name: test stop 2" + System.lineSeparator()
            + "Position: 44.97358,-93.235071" + System.lineSeparator()
            + "****Passengers Info Start****" + System.lineSeparator()
            + "Num passengers waiting: 0" + System.lineSeparator()
            + "****Passengers Info End****" + System.lineSeparator()
            + "####Stop Info End####" + System.lineSeparator()
            + "****Stops Info End****" + System.lineSeparator()
            + "####Route Info End####" + System.lineSeparator();

    assertEquals(strToCompare, data);
  }

  /**
   * Tests that isAtEnd correctly reports false.
   * at the beginning of the route.
   */
  @Test
  public void testRouteIsAtEnd() {
    boolean isAtEndTest = original.isAtEnd();
    assertFalse(isAtEndTest);
  }

  /**
   * Tests prevStop behavior at the beginning of the route.
   * after advancing once, and after advancing past the end.
   */
  @Test
  public void testRoutePrevStop() {
    Stop prevStopTest = original.prevStop();
    assertEquals(stop1, prevStopTest);

    original.nextStop();
    Stop prevStopTest2 = original.prevStop();
    assertEquals(stop1, prevStopTest2);

    original.nextStop();
    Stop prevStopTest3 = original.prevStop();
    assertEquals(stop2, prevStopTest3);
  }

  /**
   * Tests nextStop() behavior at the beginning of the route.
   * after moving to the next stop, and after moving beyond
   * the last stop.
   */
  @Test
  public void testRouteNextStop() {
    assertEquals(stop1, original.getNextStop());
    assertEquals(0, original.getNextStopIndex());
    original.nextStop();
    assertEquals(stop2, original.getNextStop());
    assertEquals(1, original.getNextStopIndex());
    original.nextStop();
    assertEquals(stop2, original.getNextStop());
    assertEquals(2, original.getNextStopIndex());
  }


}
