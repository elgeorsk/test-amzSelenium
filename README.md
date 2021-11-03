# Project Instructions

### Project 1: Automating the Amazon application using Selenium Webdriver

A maven project using Selenium Webdriver, the following requirements should be met:

- Create a Java class for an Amazon application
- Open the browser and locate web elements using Locators.
- Write an automation script using page object design pattern class to store the web elements of a web page.
- Manage Transactions using Selenium and JDBC.

## Getting started

Remember that once you clone, you will still need to:

add `chromedriver.exe` under `resources` folder
- [suitable Chrome version](https://chromedriver.chromium.org/downloads)

Additionally, the following should be in place:

- MySQL

## Setting up the project

### 1. Create MySQL Database using CLI

#### Step 1: SSH into your server as `root`

#### Step 2: Log into MySQL as `root`
```
mysql -u root
```

#### Step 3: Create a new database user
```
GRANT ALL PRIVILEGES ON *.* TO 'sa'@'localhost' IDENTIFIED BY 'sa';
```

#### Step 4: Log out of MySQL by typing: `\q`

#### Step 5: Log in as the new database user you just created
```
mysql -u sa -p
```
#### Step 6: Create a new database
```
CREATE DATABASE amzlist;
```


Note: the project tested with ChromeDriver 95.0.4638.54 and Google Chrome version 95.0.4638.69 (Official Build) (64-bit).
