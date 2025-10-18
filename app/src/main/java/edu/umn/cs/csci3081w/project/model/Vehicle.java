package edu.umn.cs.csci3081w.project.model;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract representation of a vehicle in the transit simulation.
 */
public abstract class Vehicle {
  private int id;
  private int capacity;
  //the speed is in distance over a time unit
  private double speed;
  private PassengerLoader loader;
  private PassengerUnloader unloader;
  private List<Passenger> passengers;
  private String name;
  private Position position;


  /**
   * Constructor for a vehicle.
   *
   * @param id       vehicle identifier
   * @param capacity vehicle capacity
   * @param speed    vehicle speed
   * @param loader   passenger loader for vehicle
   * @param unloader passenger unloader for vehicle
   */
  public Vehicle(int id, int capacity, double speed, PassengerLoader loader,
                 PassengerUnloader unloader) {
    this.id = id;
    this.capacity = capacity;
    this.speed = speed;
    this.loader = loader;
    this.unloader = unloader;
    this.passengers = new ArrayList<Passenger>();
  }

  /**
   * Report function for object.
   *
   * @param out printstream object
   */
  public abstract void report(PrintStream out);

  /**
   * To check whether the trip is complete.
   *
   * @return boolean value whether trip is complete
   */
  public abstract boolean isTripComplete();

  /**
   * Load a  passenger on this object.
   *
   * @param newPassenger to be added
   * @return a passenger loaded on the object.
   */
  public abstract int loadPassenger(Passenger newPassenger);

  /**
   * Move this object.
   */
  public abstract void move();

  /**
   * Update the state of this object.
   */
  public abstract void update();

  /**
   * To get the vehicle id.
   *
   * @return vehicle id
   */
  public int getId() {
    return id;
  }

  /**
   * To get the capacity of the vehicle.
   *
   * @return the vehicle capacity
   */
  public int getCapacity() {
    return capacity;
  }

  /**
   * to get the speed of the vehicle.
   *
   * @return the speed of the vehicle
   */
  public double getSpeed() {
    return speed;
  }

  /**
   * To load passengers onto vehicle.
   *
   * @return passengers that have been loaded
   */
  public PassengerLoader getPassengerLoader() {
    return loader;
  }

  /**
   * To unload passengers from vehicle.
   *
   * @return passengers to be unloaded
   */
  public PassengerUnloader getPassengerUnloader() {
    return unloader;
  }

  /**
   * To get a list of passengers.
   *
   * @return a list of passengers
   */
  public List<Passenger> getPassengers() {
    return passengers;
  }

  /**
   * To get the name of the vehicle.
   *
   * @return vehicle name
   */
  public String getName() {
    return name;
  }

  /**
   * To set the name of the vehicle.
   *
   * @param name be set/changed
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * To get the position of the vehicle.
   *
   * @return position of the vehicle
   */
  public Position getPosition() {
    return position;
  }

  /**
   * To set the position of the vehicle.
   *
   * @param position to be set for vehicle
   */
  public void setPosition(Position position) {
    this.position = position;
  }
}
