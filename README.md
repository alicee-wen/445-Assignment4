## CS 0445 – Assignment 4 – Snake Hash Table

### 🐍 Objective

In this assignment, you will implement a custom 2D hash table that uses snake-based probing to resolve collisions. The table stores keys in a grid of cells, and when a collision occurs, it follows a custom probing sequence that combines paths defined by snakes with row-wise linear probing, depending on the starting cell and configuration. Your implementation must support efficient insertion, lookup, deletion, and probing, and must strictly adhere to the structural and functional constraints described below.

> **Note:** This assignment was developed with the help of OpenAI ChatGPT to brainstorm and generate parts of the scaffolding and documentation files to speedup prototyping.


---

### 📁 Folder Structure

```
|-- pom.xml                      
|-- src/
    |-- main/
        |-- java/
            |-- SnakeHashTable.java           # Your implementation (you must complete this)
            |-- SnakeHashTableInterface.java  # Provided interface
            |-- Snake.java                    # Provided class representing a snake
            |-- Position.java                 # Provided immutable coordinate class
    |-- test/
        |-- java/
            |-- SnakeHashTableTest.java       # Provided partial JUnit 4 test suite
```

---

## **⚙️ Compilation & Running Tests**

You can use GitHub Codespaces to run, compile, and test this assignment entirely in the cloud — no local setup required.

If you choose to work on your **local machine**, you must have Maven installed. If not:

### **Linux/macOS**

```
sudo apt install maven   # on Ubuntu/Debian
brew install maven       # on macOS

```

### **Windows**

1. Download from: [https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi)
2. Extract to a folder like `C:\\Program Files\\Apache\\maven`
3. Set environment variable `MAVEN_HOME` to that folder
4. Add `%MAVEN_HOME%\\bin` to your system `PATH`
5. Open a new command prompt and type `mvn -v` to verify installation

### **🔨 Compile the Project**

```
mvn compile

```

### **✅ Run Tests**

```
mvn test

```

---

## **🔍 Debugging Test Cases in VS Code with Test Runner**

To debug JUnit test cases in VS Code, follow these steps:

### **Prerequisites:**

* Install the **Java Extension Pack** in VS Code.
* You may need to install version **0.40.0** of the **Test Runner for Java** extension if debugging options do not appear.

#### **Steps:**

1. Open the test file (e.g., `SnakeTest.java`) in the editor.
2. Set breakpoints by clicking on the gutter next to the line numbers.
3. Right-click on the gutter next to the line number of the test method name and select **Debug Test**.
4. Use the debug toolbar to step through code, inspect variables, and view call stacks.

This allows you to easily verify internal state, control flow, and ensure correctness of your implementation.

---

### 🧪 Interface Methods

You must implement the following methods as described in `SnakeHashTableInterface<T>`:

* `Position insert(T value)`
* `boolean deleteKey(T value)`
* `Position lookupKey(T value)`
* `T probe(Position p)`
* `void addSnake(Snake snake)`
* `void removeSnake(Snake snake)`
* `int[] getDimensions()`
* `Position insert(T value, int row, int col)`

---

### 🧠 Implementation Details

* Your only instance variable must be a 2D array of `Cell<T>`

* The provided `hash(T key)` method must be used for insert, lookup, and delete

* Snakes are defined by iterables of `Position` objects. You may only use iteration to get the snake cells.

* Multiple snakes may exist but each cell is in at most one snake.

* If the probe sequence goes back to an already visited cell, the method should throw an exception as specified in the Java interface file.

* **Keys inserted** using the `insert(T value, int row, int col)` method may not be found using `probe()`.

* Care should be given when going over deleted cells. Please consult the lectures for how deleted cells should be handled in open addressing schemes (e.g., linear probing and double hashing).

* Collision resolution:

  1. Depending on the starting position, begin probing either within a snake or row-wise
  2. If a probed position is part of a snake, follow the rest of the snake using its iterator
  3. If no snake path is available or fully exhausted, fall back to row-wise probing
#### 🔍 Probing Sequence Examples

In the following example, a 4x4 2D table is assumed.

**Example 1: Snake-first probing**  
- `hash("key1")` maps to `(1, 1)` which is the head of a snake: `[(1,1), (1,2), (1,3)]`
- `(1,1)` is occupied → continue to `(1,2)` → if empty, insert there
- If entire snake is full, switch to row-wise probing from `(1,3)`

**Example 2: Row-first probing, then follow snake**  
- `hash("key2")` maps to `(0, 2)` which is not part of any snake
- `(0,2)` is full → row-wise probe to `(0,3)` → if `(0,3)` is part of a snake: follow the rest of that snake

**Example 3: Full row, multiple fallback**  
- `hash("key3")` → `(2,2)` → full and not in a snake
- Row-wise to `(2,3)` → also full
- Wraps to `(3,0)` → is in a snake → follow that snake

**Example 4: Isolated snake**  
- Snake: `[(3,0), (3,1), (3,2)]`
- `hash("key4")` lands on `(1,1)` → not a snake → probe `(1,2)` → `(1,3)` → `(2,0)` → `(2,1)` ... → hits `(3,0)` which is part of a snake → continue probing along that snake

---


### 🚫 Restrictions or Constraints

* ✅ **You may only modify one file**: `SnakeHashTable.java` This is the only file you are required to change. All other files are provided for context and testing.
* ❗ You may only define one instance variable: `Cell<T>[][] table`
* ❗ You may not define any other state (e.g., no lists, maps, sets, or counters)
* ❗ You must use the provided `hash()` method in insert, lookup, and delete
* ❗ All probing logic must be computed on demand from the 2D array and active snakes
* ❗ Snakes may only be interacted with through iteration
* ⚠️ Violating these restrictions may result in a **zero** for the implementation portion of the assignment.

---

### 📊 Grading Rubric and Guidelines

| Category                                    | Points  |
| ------------------------------------------- | ------- |
| insert, lookup, delete                      | 40      |
| `addSnake()` and `removeSnake()`            | 25      |
| `probe()` and `getDimensions()`.            | 25      |
| Code style, comments, and modularity        | 10      |
| **Total**                                   | **100** |

Additional hidden tests may be used during grading. Partial credit will be awarded for clean logic that adheres to structure even if it doesn’t pass all tests.

### **💡 Grading Guidelines**

* The assignment will be graded using Gradescope’s autograder.
* Test cases include both visible and hidden scenarios to assess correctness, edge handling, and boundary conditions.
* If your autograder score is below 60%, your code will be manually reviewed for partial credit.

  * However, **manual grading can award no more than 60% of the total autograder points**.
* Code style, comments, and modularity is graded manually and includes:

  * Clear and meaningful variable/method names
  * Proper indentation and formatting
  * Use of helper methods to reduce duplication
  * Inline comments explaining non-obvious logic
  * Adherence to Java naming conventions
---

Good luck, and don’t forget to test early and often! 🎯