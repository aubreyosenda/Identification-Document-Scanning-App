## SETUP for SIDAR Development Environment

This guide outlines the steps to set up your development environment for contributing to the SIDAR Android application.

### Prerequisites

Before proceeding, ensure you have the following installed:

* **Java Development Kit (JDK):** Download and install the latest JDK from [https://www.oracle.com/java/technologies/downloads/](https://www.oracle.com/java/technologies/downloads/). Verify the installation by running `javac -version` in your terminal.
* **Android Studio:** Download and install the latest stable version of Android Studio from [https://developer.android.com/studio/intro](https://developer.android.com/studio/intro). 

---
### Setting Up Android Studio

1. **Launch Android Studio.**
2. **Configure SDK Manager:**
   - Go to **Tools > SDK Manager**.
   - Install the following components:
      - **Android SDK Tools**
      - **Android SDK Platform-tools** (usually included with SDK Tools)
      - Select a recent **Android SDK Platform** (API level) that the project targets. You can find this information in the project's documentation.
      - Install any additional libraries or tools specific to the project's needs (consult project documentation for details).

3. **Import Project:**
   - Go to **File > Open**.
   - Navigate to the cloned directory of your forked SIDAR App repository.
   - Click "Open" to import the project.

**Note:** Android Studio might automatically attempt to build the project after import. This is normal.

---
### Verifying Setup

1. **Open a Terminal:**
   - In Android Studio, go to **Tools > Terminal**.

2. **Navigate to Root Project Directory:**
   - In the terminal, use the `cd` command to navigate to your project directory.

     ```cd Identification-Document-Scanning-App```

3. **Build the Project:**
   - Try building the project using `./gradlew build` (or `gradlew build` on Windows) in the terminal.

   - If the build is successful, you've successfully set up your development environment. 


---
> Ensure you have the necessary permissions to clone the repository and push changes to your fork.

This setup guide should get you started with developing for the SIDAR Android app. 

Happy coding! ☺️
