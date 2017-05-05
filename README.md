# slack-lti-commands
Slack commands for managing LTI provided apps

Uses the great LTI Provider library from Stephen Vickers (http://www.spvsoftwareproducts.com/java/lti_tool_provider/)

# Available commands

## Help
Displays help

    /lti help
    
## List
List installed consumers
    
    /lti list

## Add
Add a new consumer

    /lti add alias key secret launch_url description
    
## Launch

Launches a consumer

    /lti launch alias
    
    
# Install
 
## Database
 
 1. Create Database
 
        CREATE DATABASE slackLti DEFAULT CHARACTER SET utf8;
        GRANT ALL ON slackLti.* TO 'slackLti'@'localhost' IDENTIFIED BY 'slackLti';
        GRANT ALL ON slackLti.* TO 'slackLti'@'127.0.0.1' IDENTIFIED BY 'slackLti';
    
2. Execute LTI SQL script
    
    See http://www.spvsoftwareproducts.com/java/lti_tool_provider/ for the SQL script
    
## LTI library

1. Download library from http://www.spvsoftwareproducts.com/java/lti_tool_provider/
2. Add to your maven repo. Assuming the library is in `/Users/xavi/Downloads/LtiToolProvider-1.1.01/LtiToolProvider-1.1.01.jar`:

        mvn install:install-file -Dfile=/Users/xavi/Downloads/LtiToolProvider-1.1.01/LtiToolProvider-1.1.01.jar \
        -DgroupId=org.oscelot \ 
        -DartifactId=lti-tool-provider \ 
        -Dversion=1.1.01 \
        -Dpackaging=jar
    
## Configuration
 
Define current profile in `src/main/resources/application.properties`
 
    # Active Profile
    spring.profiles.active=devel

Set database properties in `src/main/resources/application-{active_profile}.yml`

    # Database configuration
    spring.datasource:
        url: jdbc:mysql://localhost/slackLti
        username: slackLti
        password: slackLti
        driverClassName: com.mysql.jdbc.Driver
