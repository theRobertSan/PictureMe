# PictureMe :camera:
PictureMe is an Android Application built using Kotlin, which uses the Google Maps API and Firebase and allows users to take and view pictures in fun and interactive ways.

## Functionalities
### Taking a Picme
The user can press a button in the application which will open the camera. They can then
take a picture (PicMe) and a preview of that picture will be loaded onto the screen. There is
also an option to retake the picture. The current location is also associated to the PicMe.
Finally, the PicMe button is embedded in the nav bar, which makes it easily accessible from
almost any screen in the application.

### Adding friends to PicMe
Once a PicMe has been taken and loaded onto the preview screen, the user can select
multiple other users (friends) to associate to the taken PicMe. The user can also deselect
other users.

### Adding feelings to PicMe
Once a PicMe has been taken and loaded onto the preview screen, the user can select one
(and only one) feeling, which can either be a food or a non-food feeling. The selected feeling
will be associated to the taken PicMe.

### Home
Inside the application’s Home screen, which will be the first one displayed to the user once
he’s logged/signed in, there will be multiple different scrollable sections, with each
displaying PicMes according to a certain topic, such as food PicMes or PicMes with friends.
For each displayed PicMe, there will be displayed information such as how long ago it was
taken and how many friends are in the PicMe. Finally, each section will also display the
number of PicMes it is displaying.

### Explore – Gallery Mode
Inside the application’s Explore screen, there is a Gallery mode. Here, the user can view all
of their PicMes, which are displayed in a grid format, adjusted to the user’s phone screen
size. The user can also press the filter button, which will allow him to sort the PicMes by date
(newest and oldest), as well as filter the PicMes by the friends present in them.

### Explore – Map Mode
Inside the application’s Explore screen, there is a Map mode. Here, the user can view all of
their PicMes in an interactive map setting. In the map, PicMes are clustered together
according to the distance between them and the selected zoom, displaying the number of
PicMes inside that cluster. The user can click on a cluster of PicMes, which will display a
scrollable horizontal list of the PicMes in that cluster. The user can also click on individual
markers, which will display the selected PicMe. This mode uses the Google Maps SDK.

### PicMe Details Page
In any scenario where a PicMe is displayed on the screen, the user can press it and he will
then be redirect to that PicMe’s detail page, where the following information will be displayed:

• The feeling associated to that PicMe

• The creator of the PicMe, which can be you or a friend that associated you

• The date it was taken in

• The relative date (e.g.: 5 days ago)

• The location it was taken in, which consist of the city and the country’s code

• The relative location which uses the Google’s Distance Matrix API given the PicMe’s
location and the current location to retrieve the distance between both while
considering infrastructures and roads (e.g.: 20.8 km)

• The friends associated with that PicMe, which are inside a scrollable horizontal list
The user has the option to press the Go There button, which will then open the Google Maps
application with directions towards the PicMe’s location from the current location.

### Let’s Go
Inside the application’s Let’s Go screen there are two tabs which represent the type of activity
the user wants to do (explore or eat). Once a tab is select, the user can proceed to shake
their device to obtain the closest PicMe to them given their current location, respecting the
selected activity. After shaking the device, the user is taken to the details page of that PicMe,
having the option to click on the Go There button. If the user shakes the device again, the next
closest PicMe will be displayed. The user also has the ability to go back to the previously
displayed PicMes.

### Profile
Inside the application’s Profile screen, the user can view their profile picture, name and
username and statistics regarding the number of PicMe’s they have taken and friends they
have. From there, the user can then click on a few options: Friends, Edit Profile, Copy
Username, Settings and Log Out.

### Friends
When clicking on the friends option inside the Profile screen the user is taken to the Friends
screen, where he can navigate through three tabs:

• Friends Tab: Here, the user can view the friends they have and information such as
the friends’ full name, username and the number of PicMe’s they share together

• Requests Tab: Here, the user can view the friend requests they have received from
other users, which they can either accept or decline. For each request, it is displayed
information such as the requester’s full name, username and how long ago the
request was made

• Add Tab: Here, the user can send a friend request to any other user. In order to do so,
they insert the user’s username and presses Send. Proper validation is made and the
user is notified of errors regarding the inserted username (username doesn’t exist,
friendship already exists or request already sent)

### Edit Profile 
When clicking on the edit profile option inside the Profile screen the user is taken to the Edit
Profile screen, where they can edit their profile picture by choosing an existing image from
their gallery, and change their name.

### Copy Username
When clicking on the copy username option inside the Profile screen, the application copies
the username to the user’s clipboard.

### Register
When clicking on the Sign Up button, the user is taken to the register screen. Here, the user
can proceed to insert their information, such as username, name, email and password. For
the password, it is requested twice and hidden by default, for convenience and security
purposes respectively. The proper validation is made for all fields and the Sign Up button is
only enabled when all the inserted data is valid. On the event of invalid input from the user
or the username/email being already in use, the user is properly notified.

### Login
This is the first screen that the user is exposed to when opening the application unless he
has logged in already, where they are taken directly to the Home screen. In the login screen,
the user can insert their email and password, with the latter being hidden by default. Proper
validation occurs regarding both fields and the button is enabled accordingly.

## Technologies
### Firebase - Firestore Database
Firestore Database is used to store documents regarding PicMes, users, friendships,
requests and relations between all of the mentioned.

### Firebase - Storage
Firebase Storage is used to store the images from PicMes and user profile pictures.

### Firebase - Authentication
Firebase Authentication is used to register and authenticate users in the application. It’s
where the emails and passwords are stored.

### Google Maps SDK
Google Maps SDK is used so that the application has an embedded maps display. It is also
used to cluster the PicMes in the map.

### Google Distance Matrix API
Google Distance Matrix API is used to the distances between PicMes and the current location.

### Google Maps URLs
Google Maps URLs are used on the Go There button, in order to open the Google Maps
application with directions from the current location to the PicMe’s location.

### Accelerometer Sensor
The accelerometer sensor is used on the Let’s Go feature to shake the device.

### Location
The location of the user’s device is used in multiple scenarios, such as when taking a PicMe
(so it can be stored), when accessing the map mode, or when opening any PicMe details.

### Camera
The camera is used so that the user can take PicMes.

### Gallery
The gallery is used when a user wants to edit their profile picture.

### Other
Coil, Hilt – Dagger, Dexter, Moshi, Retrofit, MVVM Pattern.
