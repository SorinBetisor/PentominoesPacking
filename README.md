# PROJECT TITLE: *Pentominoes*

## Purpose of Project:

The exact cover problem is a classic computational problem in which you need to find a combination of subsets from a given set that exactly covers the entire set, with each subset contributing only once to the cover. In the context of Pentominoes, this problem is solved by arranging a set of 12 distinct pentomino shapes, each consisting of five squares, into a larger grid.

The challenge is to find the right combination of these pentominoes to exactly cover the grid without any overlaps or gaps. This seemingly simple task hides intricate spatial and combinatorial complexities, making it a compelling problem for enthusiasts of recreational mathematics and computer science.

The Pentominoes project provides an interactive platform for solving these puzzles, enhancing your spatial reasoning skills, and offering a fun and engaging experience. It allows you to experiment with different algorithms, including random search, pruned brute force, Dancing Links Algorithm X, and Flood Fill, to tackle the exact cover problem in the world of pentomino puzzles.

## Used Algorithms:

* Random Search
* Pruned Brute Force (with special case testing)
* Flood Fill
* Algorithm X with Dancing Links data structure

## Version or Date:

Version 2.4
Date: [11.12.2023]

## How to Start This Project:

**1. Create a New Java Project:**

* Open your Java Integrated Development Environment (IDE) like Eclipse, IntelliJ IDEA, NetBeans, or Visual Studio Code.
* Create a new Java project.

**2. Copy Project Files:**

* Copy all the project files into the project directory.

**3. Generate Pentominoes Database:**

* After copying the project files, run `PentominoesBuilder.java` in your IDE to generate the pentominoes database (`pentominoes.csv`).# Phase 1:

**Run AlgorithmSelectorUI.java:**

* Run `AlgorithmSelectorUI.java` in your IDE.
* Input the grid sizes and desired pentomino letters.
* This will open the algorithm selection window where you can choose the desired algorithm.

**Alternatively, run Search.java and input the desired Algorithm number.**

**Select the Desired Algorithm:**

* In the UI, select the algorithm you want to run (e.g., "FloodFill").

**Follow the Application Flow:**

* Follow the UI interactions in your program to execute the selected algorithm.

## Screenshots and Usage Examples

### User Interface

![Algorithm Selector](https://ibb.co/xsbdJ9F)

*Figure 1: Algorithm Selector* - The project's user interface allows you to choose from various algorithms to solve pentomino puzzles.

### Running the Algorithm

1. Start the application.
2. Select your desired algorithm (e.g., "Dancing Links Algorithm X") from the dropdown.
3. Click the "Start" button to initiate the algorithm.

![Running Dancing Links](https://ibb.co/CJ6r1Md)

*Figure 2: Running Dancing Links Algorithm* - This screenshot shows the application running the Dancing Links Algorithm X to solve a pentomino puzzle.

### Viewing the Results

Once the algorithm has completed its execution, the results will be displayed. Here's an example of the solutions found on a 12x5 grid using the Flood Fill Algorithm:

![Solutions Found](https://ibb.co/Cm0s8M5)

*Figure 3: Viewing the Solutions* - The application provides a visual representation of the solutions found, with pentomino shapes placed on the grid.

# *Phase 2*

### **Run the Application:**

- Run `Menu.java`.
- Select the game mode (Play, Bot, Sequence), click the corresponding button.

# Phase 3

* Make sure JavaFX is installed and the path properly linked to the project's configurations.
* Run FXVisualizer.java
* Select algorithm and types of parcels.
* Click on compute, and visualize the answer
* Additional modifications to the solution can be made using the provided ComboBoxes and TextFields in the GUI.

## Authors:

* [Sorin Betisor](https://www.linkedin.com/in/sorin-be%C8%9Bi%C8%99or-722188244/)
* Rares Huci
* Adrian Rusu
* Klara Suchan
* Armanto Tsollakou
* Akhmed Mukhtar
