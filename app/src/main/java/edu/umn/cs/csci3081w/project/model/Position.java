package edu.umn.cs.csci3081w.project.model;

public class Position {

  private double longitude;
  private double latitude;

  /**
   *
   * @param longitude
   * @param latitude
   */
  public Position(double longitude, double latitude) {
    this.longitude = longitude;
    this.latitude = latitude;
  }

  /**
   *
   * @return
   */
  public double getLongitude() {
    return longitude;
  }

  /**
   *
   * @return
   */
  public double getLatitude() {
    return latitude;
  }

}
