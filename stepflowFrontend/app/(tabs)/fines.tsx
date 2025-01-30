import { Text, View, StyleSheet } from 'react-native';

export default function FinesScreen() {
  return (
    <View style={styles.container}>
      <Text style={styles.text}>Fines screen</Text>
      <Text style={styles.description}>
        
      </Text>
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
