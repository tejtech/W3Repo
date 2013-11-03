package com.wakkir.designpattern.creational.abstractFactory;

/**
 * User: wakkir
 * Date: 01/11/13
 * Time: 03:08
 */
public class AbstractFactoryPatternDemo
{
    public static void main(String[] args)
    {
        //get shape factory
        IAbstractFactory shapeFactory = FactoryProducer.getFactory("SHAPE");

        //get an object of IShape Circle
        IShape shape1 = shapeFactory.getShape("CIRCLE");

        //call draw method of IShape Circle
        shape1.draw();

        //get an object of IShape Rectangle
        IShape shape2 = shapeFactory.getShape("RECTANGLE");

        //call draw method of IShape Rectangle
        shape2.draw();

        //get an object of IShape Square
        IShape shape3 = shapeFactory.getShape("SQUARE");

        //call draw method of IShape Square
        shape3.draw();

        //get color factory
        IAbstractFactory colorFactory = FactoryProducer.getFactory("COLOR");

        //get an object of Color Red
        IColour color1 = colorFactory.getColour("RED");

        //call fill method of Red
        color1.fill();

        //get an object of Color Green
        IColour color2 = colorFactory.getColour("Green");

        //call fill method of Green
        color2.fill();

        //get an object of Color Blue
        IColour color3 = colorFactory.getColour("BLUE");

        //call fill method of Color Blue
        color3.fill();
    }
}