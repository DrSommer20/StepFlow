import { Stack } from 'expo-router';

export default function RootLayout() {
  return (
    <Stack>
      <Stack.Screen name="(tabs)" options={{ headerShown: false }} />
      <Stack.Screen name="+not-found" />
      <Stack.Screen name="schedule" />
      <Stack.Screen name="fahrgemeinschaften" />
      <Stack.Screen name="fines" />
    </Stack>
  );
}
