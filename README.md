# LinkedinMessageAnalyzer  projet

### Goals

- Import the linkedin inmail box from a targeted linkedin user
- Extract information from those messages about the content sent
- Store key info on position offered in messages
- Provide a satisfying response automatically to those messages

### About Linkedin API

In it's current state, linkedin does not provide a easy way to get access to a personal inbox. 
Getting inbox data get be done using a link in your  settings, using this method to get the data at first.
The API still permits sending message and getting profile info tough, so it can be usefull to get data on the messages senders .

### Notes

- To ensure compatibility with spring-data, elastic version should be <= 7.17.4

### Features - Versions

- 0.0.1 : Spring/Maven project setup
- 0.0.2 : adding linkedin OAuth2 and few queries on linkedin profile data
- 0.0.3 : Setting a manual import of linkedin data via recovered csv file ([using this](https://www.linkedin.com/help/linkedin/answer/50191/downloading-your-account-data?lang=en)) + installing and persisting in elasticsearch index


