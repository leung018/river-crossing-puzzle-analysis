# River Crossing Puzzle Analysis

This project aims to provide an automated solution for analyzing and finding the steps and moves in a given river crossing puzzle problem.

## What is a River Crossing Puzzle?

A river crossing puzzle is a type of logic puzzle that involves transporting people, animals, or objects across a river from one riverside to another. The puzzle typically includes a set of constraints and rules that govern how the individuals or items can be moved. The challenge lies in finding a sequence of moves that fulfills all the constraints and successfully transfers everyone or everything to the other riverside.

## How to Run

To compute the solution based on pre-configured rules and characters and print it in the console, use the following command:

```
./gradlew runReleaseExecutableNative
```

## Automated Testing

To ensure the quality of the codebase, all modules are thoroughly covered by unit tests.

## Modularity and Future Directions

The project is designed with a separation of concerns between the solution finding module and the rules module. This modular structure allows for easier maintenance and future enhancements. Specifically, it enables the possibility of allowing users to customize the rules and characters in future iterations without significant modifications to the core solution finding algorithm.

In future iterations, the project plans to incorporate user customization for the rules and characters involved in the puzzle. This enhancement will provide users with greater flexibility to solve river crossing puzzles according to their specific scenarios and constraints.