## CONTRIBUTING to SIDAR

This document outlines the guidelines for submitting `code changes`, `bug reports`, and `feature requests` to the app SIDAR.

> **Note:** This is a Private Repository. To contribute to this repository, you have to be added as a collaborator first.
---
### Getting Started

Before diving in, make sure you have the following:

* A GitHub account
* Git installed on your system
* Android Studio

**Development Environment Setup:**

We recommend following the instructions in our separate `SETUP.md` file to configure your development environment for SIDAR. <br>This will ensure you have the necessary tools and dependencies set up.

---
### Contribution Workflow
Use a Git branching model for development. Here's the typical contribution workflow:

1. **Fork the Repository:**
   - Go to the [App Name] repository on GitHub.
   - Click on "Fork" to create your own copy of the repository.

2. **Clone your Forked Repository:**
   - Open your terminal and navigate to your desired directory.
   - Clone your forked repository:
     
     ```
     git clone https://github.com/Vanatel-tech/Identification-Document-Scanning-App.git
     ```

3. **Create a New Branch:**
   - Navigate to your local copy of the repository using:
     
      ```
     cd Identification-Document-Scanning-App
      ```
   - To see the available branches, on the terminal, run
     ```
     git branch
     ```
     At the moment only the `Production` branch is available

   - Create a new `Development` branch locally. This branch will contain the changes that are ready to be published to the [remote development branch](https://github.com/Vanatel-tech/Identification-Document-Scanning-App/tree/Development)
     ```
     git checkout -b Development
     ```
   - Next, pull the changes from the remote `Development` branch to the local `Development` branch.
     ```
     git pull origin Development
     ```
     
   - Now create a new branch for your specific changes. This is where you will first stage any of your changes. Use a descriptive branch name that reflects your contribution (e.g., `fix-login-issue` or `add-new-feature`).

     ```
     git checkout -b add-new-feature
     ```
   - Finaly, you need to merge the local `Development` branch to your newly created branch (e.g. `add-new-feature`)
     ```
     git merge Development
     ```

      
4. **Make Your Changes:**
   - Make your code changes and write unit tests for your modifications (if applicable).
   - Follow the existing code style and formatting conventions.

5. **Commit Your Changes:**
   - Stage your changes using `git add <filename>`. (or `git add .` for all changes)
   - Commit your changes with a clear and concise message using `git commit -m "<commit message>"`.

6. **Push Your Changes to your Fork:**
   - Push your committed changes to your forked repository using `git push origin <branch-name>`.

7. **Create a Pull Request (PR):**
   - Go to your forked repository on GitHub and navigate to the "Pull requests" tab.
   - Click on "New pull request" and select the branch containing your changes from the "head" branch dropdown.
   - Choose the `Development` branch of the main repository as the "base" branch.
   - Write a clear and detailed description of your changes in the PR description.
   - Click on "Create pull request" to submit your contribution for review.

**Important Note:**

* **Never push code directly to the `Production` branch.** All code changes must go through the `Development` branch first and be reviewed before merging into `Production`.

---
### Code Style and Formatting

We maintain a consistent code style throughout the project. Please adhere to the following:

* Use consistent indentation (4 spaces).
* Follow proper naming conventions for variables, classes, and methods.
* Add comments to explain complex logic or non-obvious code sections.

---
### Testing Your Changes

**Unit Tests:**

We encourage writing unit tests for your code changes to ensure their functionality. We may use existing unit test frameworks within the project.

**Manual Testing:**

Thoroughly test your changes manually to verify they don't introduce any regressions or unexpected behavior.

---
### Pull Request Reviews

Once you submit your pull request (PR), we will review your changes. <br>We will provide feedback, suggest improvements, or request additional information if needed. Be prepared to address any comments or concerns raised during the review process.

---
### Merging Your Changes

After successful review and addressing any feedback, your pull request may be merged into the `Development` branch. 

---
### Reporting Bugs and Feature Requests

If you encounter a bug or have an idea for a new feature, you can report it by creating an `Issue` on the project's GitHub repository. <br>Please provide a detailed description of the bug or feature request, including expected behavior and any relevant steps to reproduce the issue (for bugs).

### Thank You! 🙏 😊 

