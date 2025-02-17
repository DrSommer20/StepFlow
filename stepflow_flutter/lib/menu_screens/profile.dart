import 'package:flutter/material.dart';
import 'package:intl/date_symbol_data_local.dart';
import 'package:shared_preferences/shared_preferences.dart';

class ProfileScreen extends StatelessWidget {
  const ProfileScreen({super.key});

  Future<Map<String, dynamic>> _getUserData() async {
    final prefs = await SharedPreferences.getInstance();
    final userId = prefs.getInt('userId');
    final name = prefs.getString('name');
    final firstName = prefs.getString('firstName');
    final email = prefs.getString('email');
    final password = prefs.getString('password');
    final active = prefs.getBool('active');
    final teamIds = prefs.getStringList('teamIds');

    return {
      'userId': userId,
      'name': name,
      'firstName': firstName,
      'email': email,
      'password': password,
      'active': active,
      'teamIds': teamIds,
    };
  }

  @override
  Widget build(BuildContext context) {
    return FutureBuilder(
      future: Future.wait([
        initializeDateFormatting('de_DE', null), // Initialize for German locale
        _getUserData(),
      ]),
      builder: (context, snapshot) {
        if (snapshot.connectionState == ConnectionState.waiting) {
          return const Center(child: CircularProgressIndicator()); // Show loading indicator
        } else if (snapshot.hasError) {
          return Center(child: Text('Error: ${snapshot.error}')); // Handle errors
        } else {
          final userData = snapshot.data![1] as Map<String, dynamic>;
          final name = userData['name'] ?? '';
          final firstName = userData['firstName'] ?? '';
          final email = userData['email'] ?? '';
          final bio = 'This is a placeholder bio'; // Replace with actual bio if available

          return Scaffold(
            appBar: AppBar(
              title: const Text('Profile'),
              centerTitle: true, // Center the title
            ),
            body: SingleChildScrollView(
              // For scrollability if content overflows
              padding: const EdgeInsets.all(16.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Center(
                    // Center the profile picture
                    child: CircleAvatar(
                      radius: 60,
                      backgroundImage: const NetworkImage(
                          'https://www.pngitem.com/middle/ixJxmw_profile-pic-dummy-png-transparent-png/'), // Replace with actual image URL
                    ),
                  ),
                  const SizedBox(height: 20),
                  Center(
                    // Center the name
                    child: Text(
                      '$firstName $name',
                      style: const TextStyle(
                        fontSize: 24,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ),
                  const SizedBox(height: 10),
                  Center(
                    // Center the email
                    child: Text(
                      email,
                      style: const TextStyle(fontSize: 16),
                    ),
                  ),
                  const SizedBox(height: 20),
                  const Text(
                    'Bio:',
                    style: TextStyle(
                      fontSize: 18,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  Text(
                    bio,
                    style: const TextStyle(fontSize: 16),
                  ),
                ],
              ),
            ),
          );
        }
      },
    );
  }
}