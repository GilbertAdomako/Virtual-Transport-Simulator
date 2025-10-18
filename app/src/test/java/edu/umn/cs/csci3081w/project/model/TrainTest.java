package edu.umn.cs.csci3081w.project.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TrainTest {


  /**
   * Setup operations before each test runs.
   */
  @BeforeEach
  public void setUp() {
    PassengerFactory.DETERMINISTIC = true;
    PassengerFactory.DETERMINISTIC_NAMES_COUNT = 0;
    PassengerFactory.DETERMINISTIC_DESTINATION_COUNT = 0;
    RandomPassengerGenerator.DETERMINISTIC = true;
  }

  /**
   * Create an outbound and inbound route and return them as a two element list.
   *
   * @return
   */
  public List<Route> createRoutes() {
    Stop stop1 = new Stop(0, "test stop 1", new Position(0, 0.5));
    Stop stop2 = new Stop(1, "test stop 2", new Position(0, 1));
    Stop stop3 = new Stop(2, "test stop 3", new Position(-1, -1));
    List<Stop> stopsOut = new ArrayList<Stop>();
    stopsOut.add(stop1);
    stopsOut.add(stop2);
    stopsOut.add(stop3);
    List<Double> distancesOut = new ArrayList<Double>();
    distancesOut.add(0.9);
    distancesOut.add(0.89);
    List<Double> probabilitiesOut = new ArrayList<Double>();
    probabilitiesOut.add(.15);
    probabilitiesOut.add(0.3);
    probabilitiesOut.add(.0);
    PassengerGenerator generatorOut = new RandomPassengerGenerator(stopsOut, probabilitiesOut);
    Route testRouteOut = new Route(10, "testLine", "TRAIN", "testRouteOut",
        stopsOut, distancesOut, generatorOut);

    List<Stop> stopsIn = new ArrayList<>();
    stopsIn.add(stop3);
    stopsIn.add(stop2);
    stopsIn.add(stop1);
    List<Double> distancesIn = new ArrayList<>();
    distancesIn.add(0.89);
    distancesIn.add(0.9);
    List<Double> probabilitiesIn = new ArrayList<>();
    probabilitiesIn.add(.025);
    probabilitiesIn.add(0.3);
    probabilitiesIn.add(.0);
    PassengerGenerator generatorIn = new RandomPassengerGenerator(stopsIn, probabilitiesIn);
    Route testRouteIn = new Route(11, "testLine", "TRAIN", "testRouteIn",
        stopsIn, distancesIn, generatorIn);

    List<Route> routes = new ArrayList<>();
    routes.add(testRouteOut);
    routes.add(testRouteIn);

    return routes;
  }

  /**
   * Create a train with outgoing and incoming routes and three stops per route.
   */
  public Train createTrain() {
    return new Train(0, createRoutes().get(0), createRoutes().get(1), 5, 1);
  }

  /**
   * Takes a Position object and returns it in the form of a [long, lat] array
   * for more convenient comparisons.
   *
   * @param p The Position object
   * @return A double[] with [0] = long, [1] = lat
   */
  public double[] positionToArray(Position p) {
    return new double[]{p.getLongitude(), p.getLatitude()};
  }

  /**
   * Testing state after using constructor.
   */
  @Test
  public void testConstructorNormal() {
    Train testTrain = createTrain();

    Stop destStop = testTrain.getNextStop();
    assertEquals(0, destStop.getId());
    assertEquals("test stop 1", destStop.getName());
    assertEquals(0.5, destStop.getPosition().getLatitude());
    assertEquals(0, destStop.getPosition().getLongitude());

    assertEquals(0.5, testTrain.getPosition().getLatitude());
    assertEquals(0, testTrain.getPosition().getLongitude());
    assertFalse(testTrain.isTripComplete());
  }

  /**
   * Testing reporting functionality with no passengers.
   */
  @Test
  public void testTrainReportNoPassengers() {
    try {
      Train testTrain = createTrain();
      final Charset charset = StandardCharsets.UTF_8;
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      PrintStream testStream = new PrintStream(outputStream, true, charset.name());
      testTrain.report(testStream);
      outputStream.flush();
      String data = new String(outputStream.toByteArray(), charset);
      testStream.close();
      outputStream.close();
      String strToCompare =
          "####Train Info Start####" + System.lineSeparator()
              + "ID: 0" + System.lineSeparator()
              + "Name: testRouteOut0" + System.lineSeparator()
              + "Speed: 1.0" + System.lineSeparator()
              + "Capacity: 5" + System.lineSeparator()
              + "Position: 0.5,0.0" + System.lineSeparator()
              + "Distance to next stop: 0.0" + System.lineSeparator()
              + "****Passengers Info Start****" + System.lineSeparator()
              + "Num of passengers: 0" + System.lineSeparator()
              + "****Passengers Info End****" + System.lineSeparator()
              + "####Train Info End####" + System.lineSeparator();
      assertEquals(data, strToCompare);
    } catch (IOException ioe) {
      fail();
    }

  }

  /**
   * Testing reporting functionality with passengers.
   */
  @Test
  public void testTrainReportWithPassengers() {
    try {
      Train testTrain = createTrain();
      Passenger testPass = new Passenger(1, "Goldy");
      testTrain.getNextStop().addPassengers(testPass);
      testTrain.getNextStop().loadPassengers(testTrain);
      final Charset charset = StandardCharsets.UTF_8;
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      PrintStream testStream = new PrintStream(outputStream, true, charset.name());
      testTrain.report(testStream);
      outputStream.flush();
      String data = new String(outputStream.toByteArray(), charset);
      testStream.close();
      outputStream.close();
      String strToCompare =
          "####Train Info Start####" + System.lineSeparator()
              + "ID: 0" + System.lineSeparator()
              + "Name: testRouteOut0" + System.lineSeparator()
              + "Speed: 1.0" + System.lineSeparator()
              + "Capacity: 5" + System.lineSeparator()
              + "Position: 0.5,0.0" + System.lineSeparator()
              + "Distance to next stop: 0.0" + System.lineSeparator()
              + "****Passengers Info Start****" + System.lineSeparator()
              + "Num of passengers: 1" + System.lineSeparator()
              + "####Passenger Info Start####" + System.lineSeparator()
              + "Name: Goldy" + System.lineSeparator()
              + "Destination: 1" + System.lineSeparator()
              + "Wait at stop: 0" + System.lineSeparator()
              + "Time on vehicle: 1" + System.lineSeparator()
              + "####Passenger Info End####" + System.lineSeparator()
              + "****Passengers Info End****" + System.lineSeparator()
              + "####Train Info End####" + System.lineSeparator();
      assertEquals(data, strToCompare);
    } catch (IOException ioe) {
      fail();
    }
  }

  /**
   * Testing trip complete functionality in the midst of and when done with a trip.
   */
  @Test
  public void testTripComplete() {
    Train testTrain = createTrain();
    testTrain.move();
    assertFalse(testTrain.isTripComplete());
    testTrain.move();
  }

  /**
   * Testing load passenger when there is no room.
   */
  @Test
  public void testLoadPassengerFull() {
    Train testTrain = new Train(0, createRoutes().get(0), createRoutes().get(1), 0, 1);
    assertEquals(0, testTrain.loadPassenger(new Passenger(1, "Goldy")));
  }

  /**
   * Testing load passenger when there is room.
   */
  @Test
  public void testLoadPassengerAvailable() {
    Train testTrain = new Train(0, createRoutes().get(0), createRoutes().get(1), 1, 1);
    assertEquals(1, testTrain.loadPassenger(new Passenger(1, "Goldy")));
  }

  /**
   * Testing move properly updating position and correctly ending when both routes are completed.
   */
  @Test
  public void testMove() {
    Train testTrain = new Train(0, createRoutes().get(0), createRoutes().get(1), 5, 0.8);
    testTrain.move();
    assertArrayEquals(new double[]{0, 0.5}, positionToArray(testTrain.getPosition()));
    testTrain.move();
    assertArrayEquals(new double[]{0, 0.9444444444444445}, positionToArray(testTrain.getPosition()));

    testTrain.move();
    // Now train is at stop 2
    assertArrayEquals(new double[]{0, 1}, positionToArray(testTrain.getPosition()));
    testTrain.move();
    assertArrayEquals(new double[]{-0.898876404494382, -0.797752808988764}, positionToArray(testTrain.getPosition()));
    // Now train is at stop 3
    testTrain.move();
    assertArrayEquals(new double[]{-1, -1}, positionToArray(testTrain.getPosition()));
    testTrain.move();
    // Now train has ended outbound route
    assertArrayEquals(new double[]{-1, -1}, positionToArray(testTrain.getPosition()));
    testTrain.move();
    assertArrayEquals(new double[]{-0.10112359550561795, 0.797752808988764}, positionToArray(testTrain.getPosition()));
    testTrain.move();
    // Now back at stop 2
    assertArrayEquals(new double[]{0, 1}, positionToArray(testTrain.getPosition()));
    testTrain.move();
    assertArrayEquals(new double[]{0, 0.5555555555555556}, positionToArray(testTrain.getPosition()));
    testTrain.move();
    // The train should not move because it should've triggered the early exit
    // as both routes are at end
    assertArrayEquals(new double[]{0, 0.5555555555555556}, positionToArray(testTrain.getPosition()));
  }

}
