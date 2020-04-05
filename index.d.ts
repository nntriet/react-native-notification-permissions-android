declare module 'react-native-notification-permissions-android' {
  interface NotificationSettings {
    isEnabled: boolean;
    isBadgeEnabled: boolean;
    isSoundEnabled: boolean;
    shownInNotificationCenter: boolean;
    shownInLockScreen: boolean;
    shownAsHeadsupDisplay: boolean;
  }

  interface NotificationChannel extends NotificationSettings {
    id: string;
  }

  export const NotificationPermissionsAndroid: {
    openSettings: () => void,
    isEnabled: () => Promise<boolean>,
    getGlobalNotificationSettings: () => Promise<NotificationSettings>,
    getNotificationChannels: () => Promise<NotificationChannel[]>,
  }
  export default NotificationPermissionsAndroid
}