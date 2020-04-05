# react-native-notification-permissions-android

## Installation

`$ npm install react-native-notification-permissions-android --save`

If your React Native version is 0.60 or higher, that's all you need to do. If not, you'll need to perform the steps described below.

### Mostly automatic installation

`$ react-native link react-native-notification-permissions-android`

## Usage
```javascript
import NotificationPermissionsAndroid from 'react-native-notification-permissions-android';

```
### Check notification is enable or not 
```
const enabled = await NotificationPermissionsAndroid.isEnabled();
```

### Get all global notification settings
```
try {
  const notificationSettings = await NotificationPermissionsAndroid.getGlobalNotificationSettings();
  console.log(notificationSettings);
  // {
  //   isEnabled: boolean;
  //   isBadgeEnabled: boolean;
  //   isSoundEnabled: boolean;
  //   shownInNotificationCenter: boolean;
  //   shownInLockScreen: boolean;
  //   shownAsHeadsupDisplay: boolean;
  // }
} catch (error) {
  console.log(error);
}
```

### Get notification channels
```
try {
  const channels = await NotificationPermissionsAndroid.getNotificationChannels();
  console.log(channels);
  // {
  //   id: string;
  //   isEnabled: boolean;
  //   isBadgeEnabled: boolean;
  //   isSoundEnabled: boolean;
  //   shownInNotificationCenter: boolean;
  //   shownInLockScreen: boolean;
  //   shownAsHeadsupDisplay: boolean;
  // }
} catch (error) {
  console.log(error);
}
```

### Open notification settings 
```
NotificationPermissionsAndroid.openSettings();
```