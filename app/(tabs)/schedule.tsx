import { Text, View, StyleSheet, Button } from 'react-native';
import * as Calendar from 'expo-calendar';
import { useEffect, useState } from 'react';

export default function ScheduleScreen() {
  const [calendarId, setCalendarId] = useState(null);

  useEffect(() => {
    (async () => {
      const { status } = await Calendar.requestCalendarPermissionsAsync();
      if (status === 'granted') {
        const calendars = await Calendar.getCalendarsAsync(Calendar.EntityTypes.EVENT);
        const defaultCalendar = calendars.find(cal => cal.source.name === 'Default');
        setCalendarId(defaultCalendar.id);
      }
    })();
  }, []);

  const createEvent = async () => {
    if (calendarId) {
      const eventId = await Calendar.createEventAsync(calendarId, {
        title: 'Dance Training',
        startDate: new Date(),
        endDate: new Date(new Date().getTime() + 2 * 60 * 60 * 1000),
        timeZone: 'GMT',
        location: 'Dance Studio',
      });
      alert(`Event created with ID: ${eventId}`);
    }
  };

  return (
    <View style={styles.container}>
      <Text style={styles.text}>Schedule screen</Text>
      <Text style={styles.description}>
        Manage your dance training schedule here.
      </Text>
      <Button title="Create Event" onPress={createEvent} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#25292e',
    justifyContent: 'center',
    alignItems: 'center',
  },
  text: {
    color: '#fff',
  },
  description: {
    color: '#fff',
    marginTop: 20,
    textAlign: 'center',
  },
});
