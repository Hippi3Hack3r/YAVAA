![YAVAA](readmeimages/yavaabanner.png)
## About ##
This is another intentionally vulnerable application designed to teach Android exploitation.
YAVAA is a banking application that deals in the currency of "HackerPoints".
Hackerpoints are a limited resource which are distributed during account creation.

Upon install, an account with 42 Hackerpoints will be created for you.
When you start exploring the app, you will find that some of the Hackerpoints distributed to you have been stolen by an unknown hacker. 
Your goal is to find a way to deposit the hackerpoints back into your account.

## Architecture ##
Authentication and database operations are handled by a remote end point, so the app requires
an internet connection to run.
At install time, you are provisioned with a 5 digit user name, as seen on the login screen. The
password is the same as the user name.

#### Anticipated FAQ's ####
* "The app throws a can't connect to server error"
The backend server may not be running. If you're using a proxy, check your proxy settings.
* "I think I broke everything, what should I do?"
Uninstall and reinstall. This will you up with a new account.

## Attack Surface ##
This application is designed to demonstrate the following vulnerabilities:
* Insecure Logging
* Insecure Data Storage
* Intent manipulation
* Vulnerable API endpoints
* Ability to inject malware
* Authentication bypass

This app also does both Root detection and Certificate Pinning

## Flags ##
There are flags. Flags everywhere. Don't bother running strings. You won't find them that way.

## Sponsors ##
![BHIS](readmeimages/bhisbanner.webp)
