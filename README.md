# slack-lti-commands
Slack commands for managing LTI provided apps

Uses the great LTI Provider library from Stephen Vickers (http://www.spvsoftwareproducts.com/java/lti_tool_provider/)

# Available commands

## Help
Displays help

    /lti help
    
    
# Install
 
## Database
 
 1. Create Database
 
        CREATE DATABASE slackLti DEFAULT CHARACTER SET utf8;
        GRANT ALL ON slackLti.* TO 'slackLti'@'localhost' IDENTIFIED BY 'slackLti';
        GRANT ALL ON slackLti.* TO 'slackLti'@'127.0.0.1' IDENTIFIED BY 'slackLti';
    
    
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
