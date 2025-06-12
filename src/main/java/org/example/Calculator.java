package org.example;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.Arrays;
//imports for Graphics Library
import javax.sound.sampled.Line;
import javax.swing.*;
import javax.swing.border.LineBorder;
//Importing for the Graphics of the Buttons


public class Calculator {

    //Defining the Window of the calculator with a Jframe(fundamental building block for creating graphical user interfaces)
    int boardwidth = 360;
    int boardheight = 540;

    //Colors for the Calculator UI
    Color NewColorBG = new Color(250, 243, 221); //Background color
    Color NewColorNBR = new Color(181, 234, 215); //Number Button color
    Color NewColorEQBT = new Color(255, 183, 178); //Operator Button color
    Color NewColorBrdr = new Color(214, 209, 196); //Borderline Color
    Font NewFont = new Font("Arial", Font.PLAIN, 80); //Font and Size

    //Array with the layout of the Strinngs ButtonValues
    String[] buttonValues = {
            "AC", "+/-", "%", "÷",
            "7", "8", "9", "×",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "0", ".", "√", "="
    };

    //categorized buttons for colors and styling
    String[] rightSymbols = {"÷", "×", "-", "+", "="}; //operators
    String[] topSymbols = {"AC", "+/-", "%"}; //functions

    //newly generated Window with Calculator Text at the Top
    JFrame frame = new JFrame("Calculator");
    JLabel displayLabel = new JLabel();
    JPanel displayPanel = new JPanel();
    JPanel buttonsPanel = new JPanel();

    //keeping track of the two numbers and operator(A+B, A-B, A*B, A/B)
    String A = "0"; //First operand
    String operator = null; //Current operand
    String B = null; //second operand

    // Constructor: sets up the calculator UI and event handling
    Calculator() {
        //Modifying the Window Visibility & Style
        //frame.setVisible(true);
        frame.setSize(boardwidth, boardheight);
        frame.setLocationRelativeTo(null); //used for centering
        frame.setResizable(false); //user cant resize the window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //user clicks x closes the window
        frame.setLayout(new BorderLayout());

        //Modifying the Display of the Label
        // Set background color of the display label
        displayLabel.setBackground(NewColorBG);
        displayLabel.setForeground(NewColorNBR);
        displayLabel.setFont(NewFont); //sets Font
        displayLabel.setHorizontalAlignment(JLabel.RIGHT); //aligns the number 0 to the right
        displayLabel.setText("0"); //default text
        displayLabel.setOpaque(true); //setting the Text as opaque

        //Modifying the Display of the Panel
        displayPanel.setLayout(new BorderLayout()); //Layout Definition
        displayPanel.add(displayLabel); //putting the Text Label in the panel
        frame.add(displayPanel, BorderLayout.NORTH); //putting the panel in the Window

        //Buttons add Layout
        buttonsPanel.setLayout(new GridLayout(5, 4));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonsPanel.setBackground(NewColorBG);
        frame.add(buttonsPanel);

        //created Buttons
        for (int i = 0; i < buttonValues.length; i++) {
            JButton button = new JButton();
            String buttonValue = buttonValues[i];
            button.setFont(new Font("Arial", Font.PLAIN, 30));
            button.setText(buttonValue);
            button.setFocusable(false);
            button.setBorder(new LineBorder(NewColorBrdr));

            //Assign Button colors based on state
            if (Arrays.asList(topSymbols).contains(buttonValue)) {
                button.setBackground(NewColorNBR);
                button.setForeground(NewColorBG);
            } else if (Arrays.asList(rightSymbols).contains(buttonValue)) {
                button.setBackground(NewColorEQBT);
                button.setForeground(Color.WHITE);
            } else {
                button.setBackground(NewColorNBR);
                button.setForeground(Color.WHITE);
            }
            buttonsPanel.add(button);

            //Action Listener that parses and calculates with Buttons logic
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton button = (JButton) e.getSource();
                    String buttonValue = button.getText();
                    //handle right side operators "+-=, etc"
                    if (Arrays.asList(rightSymbols).contains(buttonValue)) {
                        if (buttonValue == "=") {
                            //evaluate expressions
                            if (A != null) {
                                B = displayLabel.getText();
                                double numA = Double.parseDouble(A);
                                double numB = Double.parseDouble(B);

                                //perform operation
                                if (operator == "+") {
                                    displayLabel.setText(removeZeroDecimal(numA + numB));
                                } else if (operator == "-") {
                                    displayLabel.setText(removeZeroDecimal(numA - numB));
                                } else if (operator == "×") {
                                    displayLabel.setText(removeZeroDecimal(numA * numB));
                                } else if (operator == "÷") {
                                    displayLabel.setText(removeZeroDecimal(numA / numB));
                                }
                                clearALl(); //reset to base state ("0")
                            }

                        } else if ("+-÷×".contains(buttonValue)) {
                            //store first number and operator
                            if (operator == null) {
                                A = displayLabel.getText();
                                displayLabel.setText(("0"));
                                B = "0";
                            }
                            operator = buttonValue;
                        }

                        // Handle top function buttons
                        } else if (Arrays.asList(topSymbols).contains(buttonValue)) {
                            if (buttonValue == "AC") {
                                clearALl();
                                displayLabel.setText("0");
                            } else if (buttonValue.equals("+/-")) {
                                double numDisplay = Double.parseDouble((displayLabel.getText()));
                                numDisplay *= -1;
                                displayLabel.setText(removeZeroDecimal(numDisplay));
                            } else if (buttonValue.equals("%")) {
                                double numDisplay = Double.parseDouble((displayLabel.getText()));
                                numDisplay /= 100;
                                displayLabel.setText(removeZeroDecimal(numDisplay));
                            }
                            // Handle numbers and decimal point
                            } else {
                            if (buttonValue == ".") {
                                if (!displayLabel.getText().contains(buttonValue)) {
                                    displayLabel.setText(displayLabel.getText() + buttonValue);
                                }

                            } else if ("0123456789".contains(buttonValue)) {
                                if (displayLabel.getText().equals("0")) {
                                    displayLabel.setText(buttonValue);
                                } else {
                                    displayLabel.setText(displayLabel.getText() + buttonValue);
                                }
                            }
                        }
                    }
                });
                frame.setVisible(true); //show UI
            }
        }
    //reset Calculator stage
    void clearALl() {
        A = "0";
        operator = null;
        B = null;
    }
    //removes decimal ".0" from integers
    String removeZeroDecimal(double numDisplay) {
        if (numDisplay % 1 == 0) {
            return Integer.toString((int) numDisplay);
        }
        return Double.toString(numDisplay);
    }
}
