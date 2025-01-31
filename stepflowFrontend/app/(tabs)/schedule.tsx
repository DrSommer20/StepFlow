import React, { useEffect, useState } from 'react';
import { Text, View, StyleSheet, FlatList } from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';

export default function EventScreen() {
  const [events, setEvents] = useState([]);
  const [token, setToken] = useState<string | null>(null);

  useEffect(() => {
    const fetchToken = async () => {
      const storedToken = await AsyncStorage.getItem('token');
      setToken(storedToken);
    };
    fetchToken();
  }, []);

  useEffect(() => {
    if (token) {
      fetch('http://192.168.2.100:8080/api/events', {
        headers: {
          'Authorization': `Bearer ${token}`,
        },
      })
        .then(response => response.json())
        .then(data => setEvents(data.events))
        .catch(error => console.error('Error fetching events:', error));
    }
  }, [token]);

  const renderItem = ({ item }: { item: { title: string; description: string; date: string; location: string; eventId: number } }) => (
    <View style={styles.eventContainer}>
      <Text style={styles.eventTitle}>{item.title}</Text>
      <Text style={styles.eventDescription}>{item.description}</Text>
      <Text style={styles.eventDate}>{item.date}</Text>
      <Text style={styles.eventLocation}>{item.location}</Text>
    </View>
  );

  return (
    <View style={styles.container}>
      <Text style={styles.text}>Carpool screen</Text>
      <FlatList
        data={events}
        renderItem={renderItem}
        keyExtractor={item => item.eventId.toString()}
      />
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
  eventContainer: {
    backgroundColor: '#333',
    padding: 20,
    marginVertical: 10,
    borderRadius: 10,
  },
  eventTitle: {
    color: '#fff',
    fontSize: 18,
    fontWeight: 'bold',
  },
  eventDescription: {
    color: '#fff',
    marginTop: 5,
  },
  eventDate: {
    color: '#fff',
    marginTop: 5,
  },
  eventLocation: {
    color: '#fff',
    marginTop: 5,
  },
});