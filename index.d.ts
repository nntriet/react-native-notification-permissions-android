declare module 'react-native-notification-permissions-android' {
  export interface NotificationSettings {
    isEnabled: boolean;
    isBadgeEnabled: boolean;
    isSoundEnabled: boolean;
    shownInNotificationCenter: boolean;
    shownInLockScreen: boolean;
    shownAsHeadsupDisplay: boolean;
  }

  export interface NotificationChannel extends NotificationSettings {
    id: string;
  }

  const NotificationPermissionsAndroid: {
    openSettings: () => void,
    isEnabled: () => Promise<boolean>,
    getGlobalNotificationSettings: () => Promise<NotificationSettings>,
    getNotificationChannels: () => Promise<NotificationChannel[]>,
  }
  export default NotificationPermissionsAndroid
}