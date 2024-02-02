**Dropbox-Equivalent Service**

**Project Description:**

The goal of this project is to implement a simplified Dropbox-like service where users can upload, retrieve, and
manage their files through a set of RESTful APIs. The service should also support the storage of metadata for each
uploaded file, such as the file name, creation timestamp, and more.

**Features API**
POST ```/files/upload```
GET ```/get/{id}```
GETALL ```/files/getall```
PUT ```/files/update/{id}```
DELETE ```/files/delete/{id}```

**Further Considerations**

1. Add logging for better debugging.
2. Handle Exceptions and write unit testing for more stable application.
3. Add security machenism to the API's for authentication.
