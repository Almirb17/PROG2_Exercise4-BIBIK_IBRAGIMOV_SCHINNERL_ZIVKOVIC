package at.ac.fhcampuswien.fhmdb.observer_pattern;

public interface Subject {
    void registerObserver(Observer o);
    void removeOberserver(Observer o);
    void notifyObservers(String message);
}
