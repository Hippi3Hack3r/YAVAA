![YAVAA](readmeimages/yavaabanner.png)
## About ##
YAVAA is an intentionally vulnerable android application for pentesting.
The app calls itself "The Hacker Bank" and the premise is as follows:  
Hackerpoints are a limited resource that were distributed to those with an account on the app.

Your goal is to first log into the account as the user `notahacker` and explore the app.
Upon exploring the app, you will find that some hacker points were distributed to you but were then
stolen by a hacker. Your goal is to find a way to deposit the hackerpoints back into your account.

## Architecture ##
Authentication and database operations are handled by a remote end point. Therefore the app requires
an internet connection to run.
At install time, you are provisioned with a 5 digit user name, as can be seen on the login screen. The
password will be the same as the user name.

#### Anticipated FAQ's ####
* "The app throws a can't connect to server error"
The backend server probably isn't currently running. If you're using a proxy, check your proxy settings.
* "I think I broke everything, what should I do?"
Uninstall and reinstall. This should set you up with a new account.

## Attack Surface ##
This application is designed to demonstrate the following vulnerabilities:
* Insecure Logging
* Insecure Data Storage
* Intent manipulation
* Injectable API endpoints
* ability to inject malware
* Authentication bypass

This app also does both Root detection and Certificate Pinning

## Flags ##
There's flags. Flags everywhere. Don't bother running strings. You won't find them that way.

## Sponsors ##
![BHIS](readmeimages/bhisbanner.webp)