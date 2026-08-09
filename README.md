# Suitable: Personal Outfit Advisor

[![Java 17](https://img.shields.io/badge/Java-17-orange.svg?style=flat-square)](https://adoptium.net/temurin/releases/?version=17)
[![Maven 3.6.3+](https://img.shields.io/badge/Maven-3.6.3%2B-red.svg?style=flat-square)](https://maven.apache.org/install.html)
[![MIT License](https://img.shields.io/badge/License-MIT-blue.svg?style=flat-square)](LICENSE.txt)

Suitable is a Java desktop application that manages a user's real-life wardrobe and recommends outfits based on weather,
events, colour and style preferences, or user-created tags. It was created as a CSC207 team project to make everyday
outfit planning easier while applying Clean Architecture and SOLID design principles.

![Sceenshot](assets/screenshot-wardrobe-normal.png)

## Table of Contents

- [Contributors](#contributors)
- [Features](#features)
- [Installation](#installation)
    - [Requirements](#requirements)
    - [Clone the Repository](#clone-the-repository)
    - [Configure Optional API Integrations](#configure-optional-api-integrations)
    - [Build and Run](#build-and-run)
- [Usage Guide](#usage-guide)
- [Architecture and Local Data](#architecture-and-local-data)
- [Testing](#testing)
- [Current Scope](#current-scope)
- [Troubleshooting](#troubleshooting)
- [Feedback](#feedback)
- [Contributing](#contributing)
- [License](#license)

## Contributors

Suitable was created and maintained by the following contributors:

- [aimangit-hub (Aiman Suhail)](https://github.com/aimangit-hub)
- [mach1044 (Max Chen)](https://github.com/mach1044)
- [Kircerta (Zixiang Zhang)](https://github.com/Kircerta)
- [wangedison92-sketch (Guanxi Wang)](https://github.com/wangedison92-sketch)
- [Fei-Sheng-Wu (Jet Wang)](https://github.com/Fei-Sheng-Wu)

The complete commit-based contribution history is available on
the [GitHub Contributors](https://github.com/Fei-Sheng-Wu/CSC207-Project/graphs/contributors) page.

## Features

- **Wardrobe management:** Add, edit, and remove inner topwear, outer topwear, bottomwear, footwear, headwear, and
  accessories.
- **Detailed item records:** Store each item's name, brand, colour, style, condition, purchase date, fondness, and
  custom tags.
- **Search and organization:** Filter by name, category, condition, age, or tag, and sort by type, name, or brand.
- **Wardrobe analysis:** Review totals, average fondness, donation candidates, item ages, and category and condition
  distributions.
- **Context-based recommendations:** Generate outfits using current weather, supported events, location settings, and
  optional colour and style preferences.
- **Tag-based recommendations:** Generate outfits locally from custom tags and optional colour and style preferences.
- **Outfit inspiration:** Search an external inspiration service for ideas related to a selected wardrobe item.
- **Local persistence:** Keep wardrobe items and settings between sessions in the user's home directory.
- **High-contrast mode:** Switch between the normal theme and a persistent high-contrast interface.

## Installation

### Requirements

Suitable is a Java Swing application intended for Windows, macOS, and Linux systems with a graphical desktop
environment.

| Software     | Required version | Purpose                                      |
|--------------|------------------|----------------------------------------------|
| JDK          | 17 or newer      | Compile and run the application              |
| Apache Maven | 3.6.3 or newer   | Resolve dependencies, compile, run, and test |
| Git          | 2.x              | Clone the repository and contribute changes  |

Maven downloads the project's pinned libraries automatically.

| Dependency                                                                                                                                             | Scope  | Version  | Purpose                                         |
|--------------------------------------------------------------------------------------------------------------------------------------------------------|--------|----------|-------------------------------------------------|
| [**JSON In Java** (org.json/json)](https://mvnrepository.com/artifact/org.json/json/20231013)                                                          |        | 20231013 | Read and write JSON data                        |
| [**OkHttp** (com.squareup.okhttp3/okhttp)](https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp/4.12.0)                                      |        | 4.12.0   | Call weather, holiday, and inspiration services |
| [**FlatLaf** (com.formdev/flatlaf)](https://mvnrepository.com/artifact/com.formdev/flatlaf/3.7.2)                                                      |        | 3.7.2    | Render the Swing interface themes               |
| [**FlatLaf IntelliJ Themes Pack** (com.formdev/flatlaf-intellij-themes)](https://mvnrepository.com/artifact/com.formdev/flatlaf-intellij-themes/3.7.2) |        | 3.7.2    | Render the Swing interface themes               |
| [**JDatePicker** (org.jdatepicker/jdatepicker)](https://mvnrepository.com/artifact/org.jdatepicker/jdatepicker/2.0.1)                                  |        | 2.0.1    | Select clothing purchase dates                  |
| [**JUnit Jupiter (Aggregator)** (org.junit.jupiter/junit-jupiter)](https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter/5.11.4)          | `test` | 5.11.4   | Run automated tests                             |
| [**Mockito Core** (org.mockito/mockito-core)](https://mvnrepository.com/artifact/org.mockito/mockito-core/5.13.0)                                      | `test` | 5.13.0   | Create test doubles                             |

### Clone the Repository

```bash
git clone https://github.com/Fei-Sheng-Wu/CSC207-Project.git
cd CSC207-Project
```

Confirm that the required tools are available:

```bash
java -version
mvn -version
```

The Java output must report version 17 or newer, and Maven must report version 3.6.3 or newer.

### Configure Optional API Integrations

The wardrobe, tag-based recommendation, statistics, settings, and high-contrast features operate locally. Context-based
recommendations and outfit inspiration use the integrations below.

| Variable               | Value                             | Used by                       |
|------------------------|-----------------------------------|-------------------------------|
| `API_BASE_URL_WEATHER` | `https://api.weatherapi.com/v1`   | Context-based recommendations |
| `API_KEY_WEATHER`      | A WeatherAPI API key              | Context-based recommendations |
| `API_BASE_URL_HOLIDAY` | `https://calendarific.com/api/v2` | Context-based recommendations |
| `API_KEY_HOLIDAY`      | A Calendarific API key            | Context-based recommendations |
| `API_BASE_URL_SOCIAL`  | `https://www.socialcrawl.dev/v1`  | Outfit inspiration            |
| `API_KEY_SOCIAL`       | A Socialcrawl API key             | Outfit inspiration            |

Keep API keys outside version control and export them in the terminal that launches Suitable.

On macOS or Linux:

```bash
export API_BASE_URL_WEATHER="https://api.weatherapi.com/v1"
export API_KEY_WEATHER="weatherapi-api-key"
export API_BASE_URL_HOLIDAY="https://calendarific.com/api/v2"
export API_KEY_HOLIDAY="calendarific-api-key"
export API_BASE_URL_SOCIAL="https://www.socialcrawl.dev/v1"
export API_KEY_SOCIAL="socialcrawl-api-key"
```

On Windows PowerShell:

```powershell
$env:API_BASE_URL_WEATHER = "https://api.weatherapi.com/v1"
$env:API_KEY_WEATHER = "weatherapi-api-key"
$env:API_BASE_URL_HOLIDAY = "https://calendarific.com/api/v2"
$env:API_KEY_HOLIDAY = "calendarific-api-key"
$env:API_BASE_URL_SOCIAL = "https://www.socialcrawl.dev/v1"
$env:API_KEY_SOCIAL = "socialcrawl-api-key"
```

### Build and Run

From the repository root, compile and start Suitable:

```bash
mvn clean compile exec:java
```

The first build can take longer while Maven downloads dependencies. Close the Suitable window to stop the process.

To run from an IDE instead, import `pom.xml` as a Maven project, select JDK 17 or newer, and run the `main` method in
`app.Main`.

## Usage Guide

### 1. Configure Location and Contrast

Open **Settings**, enter a city and a two-letter ISO country code such as `CA`, optionally enable high-contrast mode,
and select **Save Settings**. Context-based recommendations use this location.

![Suitable settings view in high-contrast mode with Toronto and CA selected](assets/screenshot-settings-high-contrast.png)

### 2. Add and Edit Clothing

Open **My Wardrobe** and select **Add Item**. Choose the clothing type, complete the fields that describe the item, and
select **Update Item** to save it. Use comma-separated tags such as `rain, hiking` when the item should participate in
tag-based recommendations.

![Suitable item editor showing a raincoat and its clothing attributes](assets/screenshot-item-normal.png)

Use **Edit Item** to revise an existing record or **Remove Item** to delete it. Suitable writes changes to local JSON
immediately.

### 3. Filter, Sort, and Analyze the Wardrobe

The wardrobe toolbar provides three organization tools:

1. **Filter** narrows items by name, category, condition, months since purchase, or tag.
2. **Sort By** orders items by type, name, or brand.
3. **Report Statistics** shows wardrobe totals, fondness, age, donation candidates, and category and condition
   distributions.

![Suitable wardrobe view with clothing cards and organization controls](assets/screenshot-wardrobe-normal.png)

### 4. Generate an Outfit Recommendation

Open **Recommendation**, optionally select a preferred colour and style, and choose one of the following modes:

- **Use Current Weather & Events** uses the saved city and country together with the configured weather and holiday
  services.
- **Use My Custom Tags** ranks wardrobe items locally using the entered tags.

Select **Get Recommendation** to see the chosen clothing slots and an explanation. A complete recommendation requires at
least one inner topwear, one bottomwear, and one footwear item in the wardrobe.

![Suitable recommendation view with preference controls and outfit slots](assets/screenshot-recommendation-normal.png)

### 5. Find Outfit Inspiration

Edit an item and select **Get Inspired**. Suitable searches the configured SocialScrawl service using the item's colour,
name, and brand. Select **Open** on a result to view its source in the system browser.

## Architecture and Local Data

Suitable follows Clean Architecture. Dependencies point inward toward application-independent entities and use cases.

| Package             | Responsibility                                                                          |
|---------------------|-----------------------------------------------------------------------------------------|
| `entity`            | Wardrobe, clothing, outfit, weather, event, and settings domain models                  |
| `use_case`          | Application rules for wardrobe operations, recommendations, inspiration, and settings   |
| `interface_adapter` | Controllers, presenters, state, and view models that translate between UI and use cases |
| `database`          | Local JSON/properties persistence and external HTTP service adapters                    |
| `views`             | Java Swing screens and reusable UI components                                           |
| `app`               | Dependency registration, application composition, and startup                           |

Suitable creates these files automatically:

- `~/suitable/wardrobe.json` stores wardrobe records.
- `~/suitable/user.properties` stores the city, country code, and high-contrast preference.

API keys are read from environment variables. Weather requests send the configured city, holiday requests send the
configured country code, and inspiration requests use the selected item's colour, name, and brand.

## Testing

Run the automated suite from the repository root:

```bash
mvn test
```

The suite covers entities, wardrobe use cases, recommendation logic, presenters, controllers, persistence, and
background requests. Live WeatherAPI and SocialScrawl integration tests activate when their corresponding environment
variables are present; JUnit skips them when credentials are absent.

## Current Scope

- Suitable currently runs from source; a packaged desktop installer is future work.
- Context-based recommendations and inspiration depend on third-party services and valid credentials.
- The Wardrobe Details screen currently presents summary statistics and distributions; all-items and donation-candidate
  lists remain planned.
- Holiday recommendations use the events represented in the bundled event-to-clothing mapping.

## Troubleshooting

| Problem                                                              | Resolution                                                                                                                                                 |
|----------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `java Main.java` reports `ClassNotFoundException`                    | Run `mvn clean compile exec:java` from the repository root; the packaged entry point is `app.Main`.                                                        |
| `java` or `mvn` is not found                                         | Install the required software above, open a new terminal, and repeat `java -version` and `mvn -version`.                                                   |
| Maven reports an unsupported Java release                            | Ensure Maven is using JDK 17 or newer; `mvn -version` displays the active Java runtime.                                                                    |
| Weather or event recommendations report that a service is unavailable | Export all four weather and holiday variables in the same terminal before starting Suitable, then verify the city and two-letter country code in Settings. |
| Inspiration curation fails                                           | Configure both SocialScrawl variables.                                                                                                                     |
| The first build appears slow                                         | Allow Maven to finish downloading dependencies. Later builds reuse the local Maven cache.                                                                  |
| Local wardrobe or settings data is invalid                           | Close Suitable, back up `~/suitable`, and move the directory aside. Suitable creates fresh files on the next launch.                                       |

If the problem remains, gather the operating system, JDK and Maven versions, error text, and reproduction steps before
opening an issue.

## Feedback

Submit feedback through [GitHub Discussions](https://github.com/Fei-Sheng-Wu/CSC207-Project/discussions). Search
existing issues first, then open a new issue when the topic has not already been reported.

Valid feedback includes reproducible defects, accessibility problems, documentation corrections, and focused feature
proposals. Include:

- A clear summary and the affected feature.
- Steps to reproduce the problem.
- Expected and actual behaviour.
- Operating system, JDK version, Maven version, and relevant commit.
- Logs or screenshots when useful, after redacting API keys and personal information.

Maintainers prioritize unique, respectful, on-topic, reproducible reports with secrets redacted. They triage submissions
when available, may request more information, and may accept, defer, or decline a proposal.

## Contributing

Protect `main` by submitting every contribution through a reviewed pull request.

1. [Fork the repository](https://docs.github.com/en/pull-requests/how-tos/work-with-forks/fork-a-repo) on GitHub.
2. Clone your fork and configure the project repository as `upstream`:

   ```bash
   git clone https://github.com/YOUR-USERNAME/CSC207-Project.git
   cd CSC207-Project
   git remote add upstream https://github.com/Fei-Sheng-Wu/CSC207-Project.git
   ```

3. Synchronize and branch from the latest `main`:

   ```bash
   git fetch upstream
   git switch main
   git merge --ff-only upstream/main
   git switch -c feat/short-description
   ```

4. Make a focused change that preserves the project's Clean Architecture boundaries and existing style.
5. Run `mvn test` and resolve relevant failures.
6. Commit the change, push the branch to your fork,
   and [open a pull request](https://docs.github.com/en/pull-requests/how-tos/create-pull-requests/creating-a-pull-request-from-a-fork)
   targeting `Fei-Sheng-Wu/CSC207-Project:main`.
7. Complete every section of the [pull request template](PULL_REQUEST_TEMPLATE.md), link related issues, explain
   testing, and keep the branch current with `main`.

Use the branch pattern `<category>/<short-description>`, where the category is normally `feat`, `fix`, `test`,
`refactor`, or `chore`.

A pull request must receive at least one approving review before merge. Reviewers check scope, correctness,
architecture, tests, and documentation; they may request changes. A maintainer merges the branch after required feedback
is addressed and the branch is ready.

## License

Suitable is available under the [MIT License](LICENSE.txt).
