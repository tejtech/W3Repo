package com.wakkir.designpattern.creational.abstractFactory;

/**
 * User: wakkir
 * Date: 01/11/13
 * Time: 03:00
 */
public class Blue implements IColour
{
    @Override
    public void fill()
    {
        System.out.println("Inside Blue::fill() method.");
    }
}