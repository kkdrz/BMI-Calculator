package pl.edu.pwr.drozd.lab1;


public interface ICountBmi {

    boolean isMassValid(float mass);
    boolean isHeightValid(float height);
    float count(float mass, float height);

}
