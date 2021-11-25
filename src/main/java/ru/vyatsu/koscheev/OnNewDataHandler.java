package ru.vyatsu.koscheev;

public interface OnNewDataHandler<T> {
    void OnNewData(Object sender, T e);
}