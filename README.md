# Draft Tap API
Controls Draft Tap Equipment.
Provides functions for manage equipment functionality and logs.

## Installation

Add the dependency in the application build.gradle file

```bash
implementation 'com.github.fluidobjects:draft_tap_sdk:0.6'
```

Add it in your root build.gradle at the end of repositories:

```bash
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

## Interface

```python
/**
* DraftTapController
* @param ip      String of ip address of equipment for open connection.
* @param context Context of the running Activity.
*/
public DraftTapController(Context context, String ip){}

/**
* Use the readVolume and expected volume to recalculate pulseFactor.
* Should be used when the measurement of volume served is wrong.
*
* @param measuredVolume Number in ml. Volume expected in calibration tests.
* @param servedVolume Number in ml. Volume served in calibration tests.
*/
public void calibratePulseFactor(int measuredVolume, int servedVolume) {}

/**
* Read volume
* Read the served volume
*
* @return false in case of error, true otherwise
*/
public int readVolume() {}

/**
* @return List of all saved log objects.
*/
public ArrayList<LogObj> getLogs() {}

/**
* @return List of log objects between startDate and endDate
*/
public ArrayList<LogObj> getLogs(Date startDate, Date endDate) {}

/**
* @param context Context of the running Activity
* @return List of all saved log objects.
*/
public static ArrayList<LogObj> getLogs(Context context) {}

/**
* @param context Context of the running Activity
* @return List of log objects between startDate and endDate
*/
public static ArrayList<LogObj> getLogs(Context context, Date startDate, Date endDate) {}


/**
* Open Valve
* Open valve so user can start serving before servingTimeout ends.
* The valve will close when user stop serving or maxVolume reached
*
* @param maxVolume Number in ml. The maximum volume user is allowed to serve.
*/
public void openValve(int maxVolume) throws Exception {}


/**
* SetTimeouts
* Open valve so user can start serving before servingTimeout ends.
* The valve will close when user stop serving or maxVolume reached
*
* @param beginTimeout Number in milliseconds. The time waited before user start to serve to close the valve.
* @param servingTimeout Number in milliseconds. The time waited after user start to serve to close the valve.
*/
public void setTimeouts(int beginTimeout, int servingTimeout)throws Exception{}

/**
* Equipment log object
* Object saved in database
*/
class LogObj{
   Date date;
   int servedVolume;
   int pulseFactor;
   int cutVolume;
}
```

## License
[MIT](https://choosealicense.com/licenses/mit/)
