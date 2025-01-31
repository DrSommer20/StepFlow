import { Slot, Stack } from 'expo-router';
import { AuthProvider, useAuth } from '../context/AuthContext';
import React from 'react';

export default function RootLayout() {
  const auth = useAuth();
  const user = auth ? auth.user : null;

  return (
    <AuthProvider>
      {user ? (
        <Stack initialRouteName="(tabs)/home">
          <Stack.Screen name="(tabs)" options={{ headerShown: false }} />
          <Stack.Screen name="(tabs)/home" options={{ headerShown: false }} />
          <Stack.Screen name="(tabs)/carpool" options={{ headerShown: false }} />
          <Stack.Screen name="(tabs)/schedule" options={{ headerShown: false }} />
          <Stack.Screen name="(tabs)/fines" options={{ headerShown: false }} />
          <Stack.Screen name="+not-found" />
        </Stack>
      ) : (
        <Stack>
          <Stack.Screen name="(auth)" options={{ headerShown: false }} />
        </Stack>
      )}
    </AuthProvider>
  );
}