# Android Background Service

A sample Android app demonstrating background work with **WorkManager** and **AlarmManager**.

- Show a notification every 15 minutes using a periodic `WorkRequest`.
- Schedule an alarm at an exact time with `AlarmManager`, falling back to a Doze-friendly inexact alarm when the exact-alarm permission isn't granted on API 31+.
- Reschedule the alarm automatically after a device reboot (`RECEIVE_BOOT_COMPLETED`).
- Handle runtime permissions the right way on modern Android:
  - `POST_NOTIFICATIONS` (Android 13+ / Tiramisu)
  - `SCHEDULE_EXACT_ALARM` / `USE_EXACT_ALARM` (Android 12+ / S)
- Keep alarms alive on aggressive OEMs (Xiaomi, Oppo, Vivo, Huawei, Honor, OnePlus, Samsung, Asus) by routing the user to the **Auto-start** and **Ignore battery optimizations** settings.

#### Screenshot

| ![](https://i.imgur.com/vs7bLAz.jpeg) | ![](https://i.imgur.com/4jgnpKn.jpeg) | ![](https://i.imgur.com/WFwnQX6.jpeg) |
| :-----------------------------------: | :-----------------------------------: | :-----------------------------------: |
| ![](https://i.imgur.com/R0TgW8r.jpeg) | ![](https://i.imgur.com/DnJqIy6.jpeg) | ![](https://i.imgur.com/qC52APu.jpeg) |

#### Tech stack

Kotlin · minSdk 24 / targetSdk 36 · View Binding · WorkManager · AlarmManager · SharedPreferences (PreferenceKtx) · Material Components

#### Reference

- [WorkManager - Define work](https://developer.android.com/develop/background-work/background-tasks/persistent/getting-started/define-work)
- [AlarmManager - Schedule an alarm](https://developer.android.com/develop/background-work/services/alarms/schedule)
- [Notifications](https://developer.android.com/guide/topics/ui/notifiers/notifications)
- [Request runtime permissions](https://developer.android.com/training/permissions/requesting)
- [Shared Preferences](https://developer.android.com/reference/android/content/SharedPreferences)
- [View Binding](https://developer.android.com/topic/libraries/view-binding)
- [Stack Overflow](https://stackoverflow.com/a/7960057)
